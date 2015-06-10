package tk.scoreli.liveticker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class SpieleActivity extends Activity implements OnItemClickListener,
		Serializable {
	/**
	 * 
	 */
	private static final String TAG = SpieleActivity.class.getSimpleName();
	private static final String TAG_VeranstaltungenDesUsers = "veranstaltungdesusers";
	private ProgressDialog pDialog;

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
	private ListView Veranstaltungsliste;
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	DatabasehandlerUUID dbuuid = new DatabasehandlerUUID(this);
	private SessionManager session;

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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spiele);
		// Session manager
		session = new SessionManager(getApplicationContext());
		// Progress dialog
		pDialog = new ProgressDialog(this);
		/*
		 * Komischerweise führt Android automatisch die toString methode aus und
		 * gibt die Veranstaltung als String aus.
		 * http://app-makers.blogspot.de/2010
		 * /05/eine-listview-mit-inhalt-fullen.html
		 * http://www.appartig.net/?e=18
		 */
		db.deleteVeranstaltungen();
		if (session.isLoggedIn()) {
			Mitglied abfrage = dbuuid.getMitglied();
			VeranstaltungholenDesUsers(abfrage.getUuid());
		} else {
			Toast.makeText(getApplicationContext(),
					"Bitte einloggen um Veranstaltungen anzusehen",
					Toast.LENGTH_SHORT).show();
		}
		/*
		 * // Get local Bluetooth adapter mBluetoothAdapter =
		 * BluetoothAdapter.getDefaultAdapter();
		 * 
		 * // If the adapter is null, then Bluetooth is not supported if
		 * (mBluetoothAdapter == null) { Toast.makeText(this,
		 * "Bluetooth is not available", Toast.LENGTH_LONG).show(); finish();
		 * return; }
		 */
		try {
			Veranstaltungsliste = (ListView) findViewById(R.id.listVeranstaltung);
			ListAdapter listenAdapter = new ArrayAdapter<Veranstaltung>(this,
					android.R.layout.simple_list_item_1,
					db.getAllVeranstaltungen());
			Veranstaltungsliste.setAdapter(listenAdapter);
			Veranstaltungsliste.setOnItemClickListener(this);
			// Veranstaltungsliste.setOnItemLongClickListener(this);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		// if(D) Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.

		try {
			Veranstaltungsliste = (ListView) findViewById(R.id.listVeranstaltung);
			ListAdapter listenAdapter = new ArrayAdapter<Veranstaltung>(this,
					android.R.layout.simple_list_item_1,
					db.getAllVeranstaltungen());
			Veranstaltungsliste.setAdapter(listenAdapter);
			Veranstaltungsliste.setOnItemClickListener(this);
			// Veranstaltungsliste.setOnItemLongClickListener(this);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();

		}
		/*
		 * if (mChatService != null) { // Only if the state is STATE_NONE, do we
		 * know that we haven't // started already if (mChatService.getState()
		 * == BluetoothService.STATE_NONE) { // Start the Bluetooth chat
		 * services mChatService.start(); } }
		 */
	}

	public void Veranstaltungzeigen() {
		try {
			Veranstaltungsliste = (ListView) findViewById(R.id.listVeranstaltung);
			ListAdapter listenAdapter = new ArrayAdapter<Veranstaltung>(this,
					android.R.layout.simple_list_item_1,
					db.getAllVeranstaltungen());
			Veranstaltungsliste.setAdapter(listenAdapter);
			Veranstaltungsliste.setOnItemClickListener(this);
			// Veranstaltungsliste.setOnItemLongClickListener(this);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();

		}

	}

	/*
	 * @Override public synchronized void onPause() { super.onPause(); // if(D)
	 * Log.e(TAG, "- ON PAUSE -"); }
	 * 
	 * @Override public void onStop() { super.onStop(); // if(D) Log.e(TAG,
	 * "-- ON STOP --"); }
	 * 
	 * @Override public void onDestroy() { super.onDestroy(); // Stop the
	 * Bluetooth chat services if (mChatService != null) mChatService.stop(); //
	 * if(D) Log.e(TAG, "--- ON DESTROY ---"); }
	 * 
	 * @Override public void onStart() { super.onStart();
	 * 
	 * // If BT is not on, request that it be enabled. // setupChat() will then
	 * be called during onActivityResult if (!mBluetoothAdapter.isEnabled()) {
	 * Intent enableIntent = new Intent(
	 * BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * startActivityForResult(enableIntent, REQUEST_ENABLE_BT); // Otherwise,
	 * setup the chat session } else { if (mChatService == null)
	 * setupUebertragung(); } }
	 */
	/*
	 * Hier fehlt noch das die Id von der Veranstaltung angezeigt wird.
	 */
	@Override
	public void onItemClick(AdapterView<?> lV, View view, int pos, long id) {
		// Hier wird das Objekt geholt der Liste und unten die Id geholt
		Veranstaltung veranstaltung = (Veranstaltung) Veranstaltungsliste
				.getItemAtPosition(pos);
		Intent i = new Intent(SpieleActivity.this, ImSpielActivity.class);
		i.putExtra(KEY, veranstaltung.getId());
		startActivity(i);

	}

	/*
	 * @Override public boolean onItemLongClick(AdapterView<?> parent, View
	 * view, int position, long id) {
	 * 
	 * Veranstaltung veranstaltung = new Veranstaltung(); veranstaltung =
	 * (Veranstaltung) Veranstaltungsliste .getItemAtPosition(position);
	 * 
	 * sendVeranstaltung(veranstaltung); return false; }
	 */
	/*
	 * 
	 * 
	 * private static class VeranstaltungsListAdapter extends CursorAdapter {
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { Veranstaltung veranstaltung = getItem(position); return
	 * super.getView(position, convertView, parent); }
	 * 
	 * public VeranstaltungsListAdapter(Context context, int textViewResourceId,
	 * List<Veranstaltung> objects) { super(context, textViewResourceId,
	 * objects); // TODO Auto-generated constructor stub }
	 * 
	 * @Override public void bindView(View arg0, Context arg1, Cursor arg2) { //
	 * TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public View newView(Context arg0, Cursor arg1, ViewGroup arg2)
	 * { // TODO Auto-generated method stub return null; }
	 * 
	 * }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spiele, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.spiel_neueVeranstaltung) {
			startActivity(new Intent(SpieleActivity.this,
					NeuesSpielActivity.class));

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/*
	 * public void onActivityResult(int requestCode, int resultCode, Intent
	 * data) {
	 * 
	 * try { switch (requestCode) { case REQUEST_CONNECT_DEVICE: // When
	 * DeviceListActivity returns with a device to connect if (resultCode ==
	 * Activity.RESULT_OK) { // Get the device MAC address String address =
	 * data.getExtras().getString( DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	 * 
	 * // Get the BLuetoothDevice object BluetoothDevice device =
	 * mBluetoothAdapter .getRemoteDevice(address); // Attempt to connect to the
	 * device
	 * 
	 * mChatService.connect(device); } break; case REQUEST_ENABLE_BT: // When
	 * the request to enable Bluetooth returns if (resultCode ==
	 * Activity.RESULT_OK) { // Bluetooth is now enabled, so set up a chat
	 * session setupUebertragung(); } else { // User did not enable Bluetooth or
	 * an error occured // Toast.makeText(this, R.string.bt_not_enabled_leaving,
	 * // Toast.LENGTH_SHORT).show(); finish(); } } } catch (Exception e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } }
	 */
	/*
	 * private void setupUebertragung() { // Initialize the BluetoothChatService
	 * to perform bluetooth connections mChatService = new
	 * BluetoothService(this, mHandlerHier); // Initialize the buffer for
	 * outgoing messages mOutStringBuffer = new StringBuffer(""); }
	 * 
	 * private void Bluetoothaktivieren() { boolean bluetoothadapter = true; /**
	 * Hat das Gerät Bluetooth ?
	 */
	/*
	 * BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
	 * .getDefaultAdapter(); if (mBluetoothAdapter == null) {
	 * Toast.makeText(getApplicationContext(), "Bluetooth gerät nicht gefunden",
	 * Toast.LENGTH_SHORT) .show(); bluetoothadapter = false; } /**
	 * BluetoothAdapter Aktivieren wenn er nicht schon an ist.
	 */
	/*
	 * if (!mBluetoothAdapter.isEnabled() && bluetoothadapter == true) { Intent
	 * enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE);
	 * startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); }
	 * 
	 * }
	 * 
	 * private void sendVeranstaltung(Veranstaltung veranstaltung) { // Check
	 * that we're actually connected before trying anything if
	 * (mChatService.getState() != BluetoothService.STATE_CONNECTED) { //
	 * Toast.makeText(this, R.string.not_connected, //
	 * Toast.LENGTH_SHORT).show(); return; }
	 * 
	 * // Check that there's actually something to send if (veranstaltung !=
	 * null) { // Get the message bytes and tell the BluetoothChatService to
	 * write
	 * 
	 * byte[] send = null; try { send = serialize(veranstaltung); } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } // send=veranstaltung.getBytes();
	 * 
	 * mChatService.write(send);
	 * 
	 * } }
	 * 
	 * private final Handler mHandlerHier = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) { switch (msg.what) {
	 * case MESSAGE_STATE_CHANGE:
	 * 
	 * switch (msg.arg1) { case BluetoothService.STATE_CONNECTED: //
	 * mTitle.setText(R.string.title_connected_to); //
	 * mTitle.append(mConnectedDeviceName); //
	 * mConversationArrayAdapter.clear(); break; case
	 * BluetoothService.STATE_CONNECTING: //
	 * mTitle.setText(R.string.title_connecting); break; case
	 * BluetoothService.STATE_LISTEN: case BluetoothService.STATE_NONE: //
	 * mTitle.setText(R.string.title_not_connected); break; } break; case
	 * MESSAGE_WRITE: byte[] writeBuf = (byte[]) msg.obj; // construct a string
	 * from the buffer String writeMessage = new String(writeBuf); //
	 * mConversationArrayAdapter.add("Me:  " + writeMessage); break;
	 * 
	 * case MESSAGE_DEVICE_NAME: // save the connected device's name
	 * mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
	 * Toast.makeText(getApplicationContext(), "Connected to " +
	 * mConnectedDeviceName, Toast.LENGTH_SHORT).show(); break; case
	 * MESSAGE_TOAST: Toast.makeText(getApplicationContext(),
	 * msg.getData().getString(TOAST),
	 * 
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * break; } } };
	 * 
	 * public static byte[] serialize(Object obj) throws IOException {
	 * ByteArrayOutputStream b = new ByteArrayOutputStream(); ObjectOutputStream
	 * o = new ObjectOutputStream(b); o.writeObject(obj); return
	 * b.toByteArray(); }
	 */
	private void VeranstaltungholenDesUsers(final String user_id) {
		// Tag used to cancel the request
		String tag_string_req = "req_holendesUsers";

		pDialog.setMessage("Holen ...");
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

							// Toast.makeText(getApplicationContext(),
							// response.toString(), Toast.LENGTH_SHORT) .show();

							JSONObject jObj = new JSONObject(response);
							// boolean error = jObj.getBoolean("error");
							boolean error = false;
							if (!error) {
								// User successfully stored in MySQL
								// Now store the user in sqlite
								db.deleteVeranstaltungen();

								JSONArray uebergabe = jObj
										.getJSONArray(TAG_VeranstaltungenDesUsers);
								for (int i = 0; i < uebergabe.length(); i++) {
									JSONObject veranstaltung = uebergabe
											.getJSONObject(i);
									String idj = veranstaltung
											.getString("veranstaltung_id");
									String heimmannschaftj = veranstaltung
											.getString("heimmannschaft");
									String gastmannschaftj = veranstaltung
											.getString("gastmannschaft");
									String punkteHeimj = veranstaltung
											.getString("punkteHeim");
									String punkteGastj = veranstaltung
											.getString("punkteGast");
									String statusj = veranstaltung
											.getString("status");
									String sportartj = veranstaltung
											.getString("sportart");
									String spielbeginnj = veranstaltung
											.getString("spielbeginn");
									db.addVeranstaltung(new Veranstaltung(Long
											.parseLong(idj), sportartj,
											heimmannschaftj, gastmannschaftj,
											Integer.parseInt(punkteHeimj),
											Integer.parseInt(punkteGastj),
											spielbeginnj, statusj));

									Veranstaltungzeigen();
								}

								Toast.makeText(getApplicationContext(),
										"Aktualisiert", Toast.LENGTH_SHORT)
										.show();
							} else {

								// Error occurred in registration. Get the error
								// message
								// String errorMsg =
								// jObj.getString("error_msg");

								// Toast.makeText(getApplicationContext(),
								// errorMsg, Toast.LENGTH_LONG).show();

							}
						} catch (JSONException e) {
							// JSON error
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
				params.put("tag", "holeveranstaltungendesusers");
				params.put("user", user_id);
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
}