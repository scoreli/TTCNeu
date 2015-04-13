package tk.scoreli.liveticker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tk.scoreli.liveticker.bluetooth.BluetoothService;
import tk.scoreli.liveticker.data.Veranstaltung;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class AnzeigeActivity extends Activity {
	TextView AnzeigeHeima, AnzeigekleinHeima, AnzeigeGasta, AnzeigekleinGasta,
			AnzeigeStatusa;
	private final static int REQUEST_ENABLE_BT = 1;
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	Veranstaltung veranstaltung;

	// Layout Views
	private TextView mTitle;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	// private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothService mChatService = null;
	//Hier das sie überall zu verfügung steht.
	Veranstaltung zeigeVeranstaltung = new Veranstaltung();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anzeige);
		init();
		try {
			aktualisiereScoreboard(deserialize(getIntent().getExtras().getByteArray(MainActivity.KEY)));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * Hat das Gerät Bluetooth ?
		 */
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

	}

	/**
	 * Wird geprüft ob Bluetooth auch aktiviert wurde. Sonst wird wieder gefragt
	 * usw.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (resultCode == Activity.RESULT_OK) {
			// Bluetooth ist aktiv.
		} else {
			/*if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}*/
			finish();
		}
	}

	private void init() {
		AnzeigeHeima = (TextView) findViewById(R.id.AnzeigeHeim);
		AnzeigekleinHeima = (TextView) findViewById(R.id.AnzeigekleinHeim);
		AnzeigeGasta = (TextView) findViewById(R.id.AnzeigeGast);
		AnzeigekleinGasta = (TextView) findViewById(R.id.AnzeigekleinGast);
		AnzeigeStatusa = (TextView) findViewById(R.id.AnzeigeStatus);

	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");
		
		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");
		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");
		init();

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
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

			case MESSAGE_READ:

				byte[] readBuf = (byte[]) msg.obj;
				System.out.println(readBuf);
				// construct a string from the valid bytes in the buffer
				// #
				// String readMessage = new S(readBuf, 0, msg.arg1);
				// Toast.makeText(AnzeigeActivity.this, readMessage,
				// Toast.LENGTH_SHORT).show();

				
				try {
				//	uebergabe = deserialize(readBuf);
				//	zeigeVeranstaltung = (Veranstaltung) uebergabe;
					zeigeVeranstaltung = deserialize(readBuf);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 aktualisiereScoreboard(zeigeVeranstaltung);

				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Verbunden mit " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				if (msg.getData().getString(TOAST)
						.equals("Device connection was lost")) {// Wenn einmal
																// die
																// Verbindung
																// abgebrochen
																// ist kann sie
																// nicht erneut
																// aufgebaut
																// werden(Also
																// in gleicher
																// Richtung)deswegen
																// neustart
					Intent i = new Intent(AnzeigeActivity.this, AnzeigeActivity.class);
		           
					byte[] uebergabe;
					try {//Daten empfangen
						uebergabe = serialize(zeigeVeranstaltung);
						i.putExtra(MainActivity.KEY,uebergabe);
						startActivity(i);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();

				}
				break;
			}
		}
	};

	public void aktualisiereScoreboard(Veranstaltung veranstaltung) {
		AnzeigeHeima.setText("" + veranstaltung.getSpielstandHeim());
		AnzeigekleinHeima.setText(veranstaltung.getHeimmanschaft());
		AnzeigeGasta.setText("" + veranstaltung.getSpielstandGast());
		AnzeigekleinGasta.setText(veranstaltung.getGastmannschaft());
		AnzeigeStatusa.setText(veranstaltung.getStatus());
	}

	public static Veranstaltung deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return (Veranstaltung) o.readObject();
	}
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}
	
}
