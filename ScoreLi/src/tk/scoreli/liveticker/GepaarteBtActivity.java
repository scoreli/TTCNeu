package tk.scoreli.liveticker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.Veranstaltung;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class GepaarteBtActivity extends Activity implements OnItemClickListener {
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;
	private ListView gepaarteGeräte;
	private ArrayAdapter<String> adapter;
	private BluetoothSocket socket;
	private OutputStreamWriter os;
	private InputStream is;
	private BluetoothDevice selectedDevice;
	String zwischen;
	int index = 0;

	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	// Den Startstring für die Ermittlung wann es losgeht für den Bluetooth
	// Server
	String startstring = "Akd834208c9324j92fds4 ";// Leerzeile das alles
													// Übertragen wird.
	// private BluetoothDevice remoteDevice;
	private boolean CONTINUE_READ = true;

	
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gepaarte_bt);
		mBluetoothAdapter = Btanfrage(mBluetoothAdapter);
		/*
		 * String[]ein=new String[6]; Veranstaltung
		 * veranstaltung=db.getVeranstaltung(1); ein[0]=startstring;
		 * ein[1]=veranstaltung.getHeimmanschaft()+" ";
		 * ein[2]=veranstaltung.getGastmannschaft()+" ";
		 * ein[3]=veranstaltung.getSpielstandHeim()+" ";
		 * ein[4]=veranstaltung.getSpielstandGast()+" ";
		 * ein[5]=veranstaltung.getStatus()+" "; //mChatService = new
		 * BluetoothService(this, mHandler); // Initialize the buffer for
		 * outgoing messages // StringBuffer mOutStringBuffer = new
		 * StringBuffer(""); // mChatService.start();
		 * 
		 * 
		 * 
		 * /* Kann nicht weggelassen werden, da sonst es eine Nullpointer gibt.
		 * http
		 * ://schuelerlabor.informatik.rwth-aachen.de/eLearning_Einheiten/NXTApp
		 * /4.htm
		 */

		/*
		 * Alle Gepaarte Geräte suchen
		 * https://www.youtube.com/watch?v=-OICAj3VHPY
		 */

		String blist[];
		Set<BluetoothDevice> pairedDevices;
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		int count = pairedDevices.size();
		blist = new String[count];
		int j = 0;
		for (BluetoothDevice device : pairedDevices) {
			blist[j] = device.getName().toString();
			j++;
		}
		gepaarteGeräte = (ListView) findViewById(R.id.list_geraete);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, blist);
		gepaarteGeräte.setAdapter(adapter);
		gepaarteGeräte.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String geraet = (String) gepaarteGeräte.getItemAtPosition(position);
		Toast.makeText(getApplicationContext(), geraet, Toast.LENGTH_SHORT)
				.show();
		mBluetoothAdapter = Btanfrage(mBluetoothAdapter);
		Set<BluetoothDevice> pairedDevices;
		pairedDevices = mBluetoothAdapter.getBondedDevices();
		for (BluetoothDevice device : pairedDevices) {
			if (device.getName().equals(geraet)) {
				selectedDevice = device;
			}
		}

		/*
		 * Verbindung herstellen
		 */
		if (index == 0) {
			new Thread(reader).start();
			index++;
		}
		// Toast.makeText(this, selectedDevice.getAddress(),
		// Toast.LENGTH_SHORT).show();
		// remoteDevice.getParcelableExtra(selectedDevice);
		// Attempt to connect to the device
		// if(index==0){
		// mChatService.connect(selectedDevice);
		// while(mChatService.getState()!=3){
		//

	}

	private BluetoothAdapter Btanfrage(BluetoothAdapter mBluetoothAdapter) {
		boolean bluetoothadapter = true;
		/**
		 * Hat das Gerät Bluetooth ?
		 */
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),
					"Bluetooth gerät nicht gefunden", Toast.LENGTH_SHORT)
					.show();
			bluetoothadapter = false;
		}
		/**
		 * BluetoothAdapter Aktivieren wenn er nicht schon an ist.
		 */
		if (!mBluetoothAdapter.isEnabled() && bluetoothadapter == true) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		return mBluetoothAdapter;
	}

	private Runnable reader = new Runnable() {

		@Override
		public void run() {
			try {
				android.util.Log.e("TrackingFlow",
						"Found: " + selectedDevice.getName());
				UUID uuid = UUID
						.fromString("4e5d48e0-75df-11e3-981f-0800200c9a66");
				socket = selectedDevice.createRfcommSocketToServiceRecord(uuid);
				socket.connect();
				android.util.Log.e("TrackingFlow", "Connected...");
				os = new OutputStreamWriter(socket.getOutputStream());
				is = socket.getInputStream();
				android.util.Log.e("TrackingFlow", "WWWTTTFFF34243");
				new Thread(writter).start();
				android.util.Log.e("TrackingFlow",
						"WWWTTTFFF3wwgftggggwww4243: " + CONTINUE_READ);
				int bufferSize = 1024;
				int bytesRead = -1;
				byte[] buffer = new byte[bufferSize];
				// Keep reading the messages while connection is open...
				while (CONTINUE_READ) {
					android.util.Log.e("TrackingFlow", "WWWTTTFFF3wwwww4243");
					final StringBuilder sb = new StringBuilder();
					bytesRead = is.read(buffer);
					if (bytesRead != -1) {
						String result = "";
						while ((bytesRead == bufferSize)
								&& (buffer[bufferSize - 1] != 0)) {
							result = result
									+ new String(buffer, 0, bytesRead - 1);
							bytesRead = is.read(buffer);
						}
						result = result + new String(buffer, 0, bytesRead - 1);
						sb.append(result);
					}

					android.util.Log
							.e("TrackingFlow", "Read: " + sb.toString());

					// Show message on UIThread
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(GepaarteBtActivity.this,
									sb.toString(), Toast.LENGTH_SHORT).show();
							if (sb.toString().equals("Done")) {
								finish();
							}
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private Runnable writter = new Runnable() {

		@Override
		public void run() {
			int index = 0;
			while (CONTINUE_READ) {
				{
				}
				String[] ein = new String[6];
				Veranstaltung veranstaltung = db.getVeranstaltung(1);

				ein[0] = startstring;
				ein[1] = veranstaltung.getHeimmanschaft() + " ";
				ein[2] = veranstaltung.getGastmannschaft() + " ";
				ein[3] = veranstaltung.getSpielstandHeim() + " ";
				ein[4] = veranstaltung.getSpielstandGast() + " ";
				ein[5] = veranstaltung.getStatus() + " ";
				try {
					if (index > 5) {
						CONTINUE_READ = false;
						finish();
					}
					// String zwei= ein[index];

					os.write(ein[index]);
					os.flush();
					index++;
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	
}