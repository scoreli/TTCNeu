package tk.scoreli.liveticker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tk.scoreli.liveticker.bluetooth.BluetoothService;
import tk.scoreli.liveticker.bluetooth.DeviceListActivity;
import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.DatabasehandlerUUID;
import tk.scoreli.liveticker.data.Mitglied;
import tk.scoreli.liveticker.data.Veranstaltung;
import tk.scoreli.liveticker.loginregister.AppConfig;
import tk.scoreli.liveticker.loginregister.AppController;
import tk.scoreli.liveticker.loginregister.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class ImSpielActivity extends Activity implements
		OnCheckedChangeListener {
	private EditText txfSpielstandHeim, txfSpielstandGast, txfStatus;
	private Button btnaktualisieren, btnloeschen;
	private Switch switch_scoreboard;
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	DatabasehandlerUUID dbuuid = new DatabasehandlerUUID(this);
	private boolean freigabe = false;
	private static final String TAG = NeuesSpielActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	private SessionManager session;
	private static final String TAG_VeranstaltungUpdate = "veranstaltungupdate";
	private static final long serialVersionUID = 1L;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	public static final String KEY = "ID_Veranstaltung";
	private final static int REQUEST_ENABLE_BT = 2;
	// Name of the connected device
	private String mConnectedDeviceName = null;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothService mChatService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatespiel);
		init();
		switch_scoreboard.setOnCheckedChangeListener(this);

		// Session manager
		session = new SessionManager(getApplicationContext());
		// Progress dialog
		pDialog = new ProgressDialog(this);
		btnaktualisieren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (session.isLoggedIn()) {
					aktualisieren();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Bitte einloggen um Veranstaltungen zu aktualisieren",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		btnloeschen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (session.isLoggedIn()) {
					loeschen();
				} else {
					Toast.makeText(getApplicationContext(),
							"Bitte einloggen um Veranstaltungen zu löschen",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		/*
		 * switch_scoreboard.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent serverIntent = new
		 * Intent(getApplicationContext(), DeviceListActivity.class);
		 * startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		 * 
		 * } })
		 */
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		Toast.makeText(this, "The Switch is " + (isChecked ? "on" : "off"),
				Toast.LENGTH_SHORT).show();
		if (isChecked) {
			// Get local Bluetooth adapter
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			// If the adapter is null, then Bluetooth is not supported
			if (mBluetoothAdapter == null) {
				Toast.makeText(this, "Bluetooth is not available",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
				// Otherwise, setup the chat session
			} else {
				if (mChatService == null)
					setupUebertragung();
				/**
				 * Falls beim anmachen des Schalters Bluetooth schon an ist wird
				 * die Gerätesuche geöffnet.
				 */
				Intent serverIntent = new Intent(getApplicationContext(),
						DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			}

		} else {
			// do stuff when Switch if OFF
		}
	}

	private void init() {
		txfSpielstandHeim = (EditText) findViewById(R.id.txfHeimmannschaftAktualisiere);
		txfSpielstandGast = (EditText) findViewById(R.id.txfGastmannschaftAktualisiere);
		txfStatus = (EditText) findViewById(R.id.txfAktualisiereStatus);
		btnaktualisieren = (Button) findViewById(R.id.btnaktualisieren);
		btnloeschen = (Button) findViewById(R.id.btnloeschen);
		switch_scoreboard = (Switch) findViewById(R.id.switch_scoreboard);

	}

	private void loeschen() {
		/*
		 * Hier wird die Zahl(id) der Veranstaltung geholt
		 */
		long i = getIntent().getExtras().getLong(SpieleActivity.KEY);
		Veranstaltung updateveranstaltung = db.getVeranstaltung((int) i);
		// db.deleteVeranstaltung(updateveranstaltung); Gleiche wie unten
		Mitglied uebertrag = dbuuid.getMitglied();
		Veranstaltungloeschen("" + updateveranstaltung.getId(),
				uebertrag.getUuid());
		Toast.makeText(getApplicationContext(), "Gelöscht", Toast.LENGTH_SHORT)
				.show();

		 finish();
	}

	private void aktualisieren() {
		/*
		 * Hier wird die Zahl(id) der Veranstaltung geholt
		 */
		long i = getIntent().getExtras().getLong(SpieleActivity.KEY);
		Veranstaltung updateveranstaltung = db.getVeranstaltung((int) i);
		String spielstandheim = txfSpielstandHeim.getText().toString();
		String spielstandgast = txfSpielstandGast.getText().toString();
		String spielstatus = txfStatus.getText().toString();

		if (TextUtils.isEmpty(spielstandheim) == false) {
			updateveranstaltung.setSpielstandHeim(Integer
					.parseInt(spielstandheim));
		}
		if (TextUtils.isEmpty(spielstandgast) == false) {
			updateveranstaltung.setSpielstandGast(Integer
					.parseInt(spielstandgast));
		}
		if (TextUtils.isEmpty(spielstatus) == false) {
			updateveranstaltung.setStatus(spielstatus);
		}
		Mitglied uebertrag = dbuuid.getMitglied();
		try {
			updateVeranstaltung("" + updateveranstaltung.getSpielstandHeim(),
					"" + updateveranstaltung.getSpielstandGast(),
					updateveranstaltung.getStatus(),
					"" + updateveranstaltung.getId(), uebertrag.getUuid());
			// db.updateVeranstaltung(updateveranstaltung);Geht nicht irgendwie
			// ist schon geschlossen keine ahnung
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}
		if (switch_scoreboard.isChecked() && freigabe) {
			sendVeranstaltung(updateveranstaltung);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_spiel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void Veranstaltungloeschen(final String veranstaltungs_id,
			final String user_id) {
		// Tag used to cancel the request
		String tag_string_req = "req_loescheVeranstaltung";

		pDialog.setMessage("Löschen ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VERANSTALTUNG, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG,
								"Veranstaltung Response: "
										+ response.toString());
						hideDialog();

						try {

							/*
							 * Toast.makeText(getApplicationContext(),
							 * response.toString(), Toast.LENGTH_SHORT) .show();
							 */

							JSONObject jObj = new JSONObject(response);
							boolean error = jObj.getBoolean("error");
							if (!error) {

								Toast.makeText(getApplicationContext(),
										"Gelöscht", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"Löschen fehlgeschlagen",
										Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) { // JSON error
							e.printStackTrace();
						}

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Registration Error: " + error.getMessage());

						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();

					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "loescheveranstaltung");
				params.put("veranstaltungs_id", veranstaltungs_id);
				params.put("user", user_id);
				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	/**
	 * Muss noch geändert werden damit es was empfängt
	 * 
	 * @param punkteHeim
	 * @param punkteGast
	 * @param status
	 * @param veranstaltungs_id
	 */
	private void updateVeranstaltung(final String punkteHeim,
			final String punkteGast, final String status,
			final String veranstaltungs_id, final String user_id) {
		// Tag used to cancel the request
		String tag_string_req = "req_updateveranstaltung";

		pDialog.setMessage("Aktualisieren ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VERANSTALTUNG, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Register Response: " + response.toString());
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);
							boolean error = jObj.getBoolean("error");
							if (!error) {
								// User successfully stored in MySQL
								// Now store the user in sqlite
								// String uid = jObj.getString("uid");
								/*
								 * Toast.makeText(getApplicationContext(),
								 * response, Toast.LENGTH_LONG).show();
								 * JSONObject user = jObj
								 * .getJSONObject("veranstaltung"); String idj =
								 * user.getString("veranstaltung_id"); String
								 * sportartj = user.getString("sportart");
								 * String heimmannschaftj = user
								 * .getString("heimmannschaft"); String
								 * gastmannschaftj = user
								 * .getString("gastmannschaft"); String
								 * punkteHeimj = user .getString("punkteHeim");
								 * String punkteGastj = user
								 * .getString("punkteGast"); String spielbeginnj
								 * = user .getString("spielbeginn"); String
								 * statusj = user.getString("status");
								 * 
								 * 
								 * 
								 * int ka = db.updateVeranstaltung(new
								 * Veranstaltung(Long .parseLong(idj),
								 * sportartj, heimmannschaftj, gastmannschaftj,
								 * Integer.parseInt(punkteHeimj), Integer
								 * .parseInt(punkteGastj),
								 * 
								 * spielbeginnj, statusj));
								 */
								// finish();// Hat beendet da man was aufgerufen
								// hat obwohl es beendet worden ist.
								Toast.makeText(getApplicationContext(),
										"Aktualisiert", Toast.LENGTH_SHORT)
										.show();
							} else {

								// Error occurred in registration. Get the error
								// message
								String errorMsg = jObj.getString("error_msg");

								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();

							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									e.toString(), Toast.LENGTH_LONG).show();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Registration Error: " + error.getMessage());

						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();

					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "updateveranstaltung");// Zuerst Tag dann
				// Daten
				params.put("user", user_id);
				params.put("punkteHeim", punkteHeim);
				params.put("punkteGast", punkteGast);
				params.put("veranstaltungs_id", veranstaltungs_id);
				params.put("status", status);
				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		// if(D) Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		// if(D) Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		// if(D) Log.e(TAG, "--- ON DESTROY ---");
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			switch (requestCode) {
			case REQUEST_CONNECT_DEVICE:
				// When DeviceListActivity returns with a device to connect
				if (resultCode == Activity.RESULT_OK) {
					// Get the device MAC address
					String address = data.getExtras().getString(
							DeviceListActivity.EXTRA_DEVICE_ADDRESS);

					// Get the BLuetoothDevice object
					BluetoothDevice device = mBluetoothAdapter
							.getRemoteDevice(address);
					// Attempt to connect to the device

					mChatService.connect(device);
				}
				break;
			case REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (resultCode == Activity.RESULT_OK) {
					// Bluetooth is now enabled, so set up a chat session
					setupUebertragung();
					Intent serverIntent = new Intent(getApplicationContext(),
							DeviceListActivity.class);
					startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				} else {
					// User did not enable Bluetooth or an error occured
					// Toast.makeText(this, R.string.bt_not_enabled_leaving,
					// Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(),
							"Bluetooth muss aktiviert sein", Toast.LENGTH_SHORT)
							.show();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setupUebertragung() {
		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothService(this, mHandlerHier);
		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	private void sendVeranstaltung(Veranstaltung veranstaltung) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
			// Toast.makeText(this, R.string.not_connected,
			// Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (veranstaltung != null) {
			// Get the message bytes and tell the BluetoothChatService to write

			byte[] send = null;
			try {
				send = serialize(veranstaltung);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// send=veranstaltung.getBytes();

			mChatService.write(send);

		}
	}

	private final Handler mHandlerHier = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:

				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					// mTitle.setText(R.string.title_connected_to);
					// mTitle.append(mConnectedDeviceName);
					// mConversationArrayAdapter.clear();
					break;
				case BluetoothService.STATE_CONNECTING:
					// mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					// mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;

			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				freigabe = true;
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST),

						Toast.LENGTH_SHORT).show();
				if (msg.getData().getString(TOAST)
						.equals("Unable to connect device")
						|| msg.getData().getString(TOAST)
								.equals("Device connection was lost")) {
					switch_scoreboard.setChecked(false);

					freigabe = false;

				}

				break;
			}
		}
	};
}
