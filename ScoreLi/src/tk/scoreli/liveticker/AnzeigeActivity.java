package tk.scoreli.liveticker;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AnzeigeActivity extends Activity{
TextView AnzeigeHeima,AnzeigekleinHeima,AnzeigeGasta,AnzeigekleinGasta,AnzeigeStatusa;
private final static int REQUEST_ENABLE_BT = 1;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_anzeige);
	init();
	/**
     * Hat das Gerät Bluetooth ?
     */
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter == null) {
        // Das Gerät verfügt über kein Bluetooth.
    }
    /**
     * BluetoothAdapter Aktivieren wenn er nicht schon an ist.
     */
    if (!mBluetoothAdapter.isEnabled()) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
	
	
}

/**
 * Wird geprüft ob Bluetooth auch aktiviert wurde. Sonst wird wieder gefragt usw.
 */
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	   if (resultCode == Activity.RESULT_OK) {
	      // Bluetooth ist aktiv.
	   } else {
		   if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	        }
	   }
	}  

private void init() {
	AnzeigeHeima=(TextView)findViewById(R.id.AnzeigeHeim);
	AnzeigekleinHeima=(TextView)findViewById(R.id.AnzeigekleinHeim);
	AnzeigeGasta=(TextView)findViewById(R.id.AnzeigeGast);
	AnzeigekleinGasta=(TextView)findViewById(R.id.AnzeigekleinGast);
	AnzeigeStatusa=(TextView)findViewById(R.id.AnzeigeStatus);
}
	
	
	
	
	

	
}
