package de.ttcbeuren.ttcbeurenhauptapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class NeuesSpielActivity extends Activity {
	private CheckBox checkbeurenistheim, checkspielistentschieden;
	private Spinner spnSpielsystem, spnMannschaftsart, spnBeurenMannschaft,
			spnGegnerMannschaft, spnGegnermannschaftsnummer;
	private TimePicker tpSpielbeginn, tpSpielende;
	private NumberPicker npHeim, npGast;
	private Button btnerstellen;
	private DatePicker spieldatum;
	private TextView etxtspielende;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_spiel);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.neues_spiel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_gegnerlisteaktualisieren) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void init() {
		viewsfreferenz();
		checkbeurenistheim = (CheckBox) findViewById(R.id.check_beurenistheim);
		// Erst wenn die Checkbox spielentschieden aktiviert wird soll spielende
		// angezeigt werden und der time picker dafür.
		checkspielistentschieden = (CheckBox) findViewById(R.id.check_Spielistentschieden);
		etxtspielende = (TextView) findViewById(R.id.textView_Spielentschieden);
		etxtspielende.setVisibility(View.GONE);
		spnSpielsystem = (Spinner) findViewById(R.id.spn_spielsystem);
		spnMannschaftsart = (Spinner) findViewById(R.id.spn_mannschaftart);
		spnBeurenMannschaft = (Spinner) findViewById(R.id.spn_beurenmannschaft);
		spnGegnerMannschaft = (Spinner) findViewById(R.id.spn_gegnermannschaft);
		spnGegnermannschaftsnummer = (Spinner) findViewById(R.id.spn_gegnermannschaftsnummer);
		tpSpielbeginn = (TimePicker) findViewById(R.id.time_Spielbeginn);
		tpSpielbeginn.setIs24HourView(true);
		tpSpielende = (TimePicker) findViewById(R.id.time_Spielende);
		tpSpielende.setIs24HourView(true);
		tpSpielende.setVisibility(View.GONE);
		npHeim = (NumberPicker) findViewById(R.id.np_heim);
		npHeim.setMinValue(0);
		npHeim.setMaxValue(9);
		npHeim.setWrapSelectorWheel(true);
		// Blockt die Änderung für den Benutzer
		npHeim.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		npGast = (NumberPicker) findViewById(R.id.np_gast);
		npGast.setMinValue(0);
		npGast.setMaxValue(9);
		npGast.setWrapSelectorWheel(true);
		// Blockt die Änderung für den Benutzer
		npGast.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		spieldatum = (DatePicker) findViewById(R.id.spieldatum);
		spieldatum.setCalendarViewShown(false);
		btnerstellen = (Button) findViewById(R.id.btn_erstellen);

	}

	private void viewsfreferenz() {

	}
}