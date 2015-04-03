package tk.scoreli.liveticker;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SpieleActivity extends Activity {
	private Spinner SpinneraktuelleSpiele;
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spiele);
		
	}

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
	
}
