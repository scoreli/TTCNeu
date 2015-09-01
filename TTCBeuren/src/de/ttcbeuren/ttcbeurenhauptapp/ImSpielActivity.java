package de.ttcbeuren.ttcbeurenhauptapp;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;
import de.ttcbeuren.ttcbeurenhauptapp.internet.InternetService;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.DatabasehandlerUUID;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class ImSpielActivity extends Activity implements AlertFragment.AlertDialogListener {
	/**
	 * Werte für die initalisierung von im Spiel
	 * 
	 */
	private CheckBox checkspielistentschieden;
	private TimePicker tpSpielende;
	private EditText txfSpielstandHeim, txfSpielstandGast, txfStatus;
	private TextView statusScoreboard, etxtspielende;
	private Button btnaktualisieren, btnloeschen, btnheimplusein,
			btngastpluseins, btnheimminuseins, btngastminuseins, btnzurueck;
	// private CheckBox checkboxbeenden;
	private Switch switch_scoreboard;
	DatabasehandlerSpiele dbspiele;
	DatabasehandlerUUID dbuuid;
	private InternetService internetservice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_im_spiel);
		init();
		dbspiele = new DatabasehandlerSpiele(this);
		dbuuid = new DatabasehandlerUUID(this);
		internetservice = new InternetService(this);
		//
		int uebergabespiel_id = getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY);
		Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
		plusminusHeim(0, uebergabespiel);
		checkspielistentschieden
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							etxtspielende.setVisibility(View.VISIBLE);
							tpSpielende.setVisibility(View.VISIBLE);

						} else {
							etxtspielende.setVisibility(View.GONE);
							tpSpielende.setVisibility(View.GONE);
						}

					}
				});
		btnaktualisieren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(checkspielistentschieden.isChecked()){DialogFragment bestaetigenFragment = new AlertFragment();
				 
			    bestaetigenFragment.show(getFragmentManager(), "bestaetigenaktualisieren");		}
				else {
					aktualisieren();	
				}
				 
		
			

			}});
		btnloeschen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 DialogFragment bestaetigenFragment = new AlertFragment();
				    bestaetigenFragment.show(getFragmentManager(), "bestaetigenloeschen");				
			
			
			
			}
			
		});
		
		btnheimplusein.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Heim plus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusHeim(1, uebergabespiel);
			}
		});
		btngastpluseins.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Gast plus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusGast(1, uebergabespiel);
			}
		});
		btnheimminuseins.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Heim minus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusHeim(-1, uebergabespiel);
			}
		});
		btngastminuseins.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Gast minus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusGast(-1, uebergabespiel);
			}
		});
		btnzurueck.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button zurück gedrückt wird die Acitivty beendet und man
			 * gelangt auf die Veranstaltungen des Users.
			 */
			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		switch(dialog.getTag()){
		case "bestaetigenloeschen":
			loeschen();
			break;
		
		case "bestaetigenaktualisieren":
			aktualisieren();
			break;
		
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	private void loeschen() {

		internetservice.Spielloeschen(
				"" + getIntent().getExtras().getInt(ErgebnisseFragment.KEY), ""
						+ dbuuid.getBenutzer().get_id());
		dbspiele.deleteSpiel(new Spiel(getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY)));
		finish();
	}

	private void aktualisieren() {
		/*
		 * Hier wird die Zahl(id) der Veranstaltung geholt
		 */

		Spiel updatespiel = dbspiele.getSpiel(getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY));
		String spielstandheim = txfSpielstandHeim.getText().toString();
		String spielstandgast = txfSpielstandGast.getText().toString();
		String spielstatus = txfStatus.getText().toString();

		if (TextUtils.isEmpty(spielstandheim) == false) {
			updatespiel.setPunkteHeim(Integer.parseInt(spielstandheim));
		}
		if (TextUtils.isEmpty(spielstandgast) == false) {
			updatespiel.setPunkteHeim(Integer.parseInt(spielstandgast));
		}
		if (TextUtils.isEmpty(spielstatus) == false) {
			updatespiel.setStatus(spielstatus);
		}
		if (checkspielistentschieden.isChecked()) {
			String spielendeString = "" + tpSpielende.getCurrentHour() + ":"
					+ tpSpielende.getCurrentMinute() + ":" + 30;
			updatespiel.setSpielende(spielendeString);
			updatespiel.setIstspielbeendet(1);

		}
		internetservice.updateSpiel("" + updatespiel.getPunkteHeim(), ""
				+ updatespiel.getPunkteGast(), updatespiel.getStatus(), ""
				+ updatespiel.getSpiel_id(), "" + updatespiel.getBenutzer_id(),
				"" + updatespiel.getIstspielbeendet(),
				updatespiel.getSpielende());
	}

	/**
	 * Bei dieser Methode wird der Wert des Parameters i dem Spielstand der
	 * Heimmannschaft der zu bearbeitenden Mannschaft addiert. Außerdem
	 * aktualisiert die Methode noch den Spielstand den sie als Hint ausgibt.
	 * 
	 * @param i
	 * @param spiel
	 */
	public void plusminusHeim(int i, Spiel spiel) {
		int uebergabe = spiel.getPunkteHeim();
		boolean freigabe = i > 0 || uebergabe > 0 && i < 1;
		if (freigabe) {
			uebergabe = uebergabe + i;
			spiel.setPunkteHeim(uebergabe);
		}
		txfSpielstandHeim.setHint("Heim: " + spiel.getPunkteHeim());
		txfSpielstandGast.setHint("Gast: " + spiel.getPunkteGast());
		try {
			dbspiele.updateSpiel(spiel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Bei dieser Methode wird der Wert des Parameters i dem Spielstand der
	 * Gastmannschaft der zu bearbeitenden Mannschaft addiert. Außerdem
	 * aktualisiert die Methode noch den Spielstand den sie als Hint ausgibt.
	 * 
	 * @param i
	 * @param spiel
	 */
	public void plusminusGast(int i, Spiel spiel) {
		int uebergabe = spiel.getPunkteGast();
		boolean freigabe = i > 0 || uebergabe > 0 && i < 1;
		if (freigabe) {
			uebergabe = uebergabe + i;
			spiel.setPunkteGast(uebergabe);
		}
		txfSpielstandHeim.setHint("Heim: " + spiel.getPunkteHeim());
		txfSpielstandGast.setHint("Gast: " + spiel.getPunkteGast());
		try {
			dbspiele.updateSpiel(spiel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.im_spiel, menu);
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

	/**
	 * Initialisieren der grafischen Elementen
	 */
	private void init() {
		txfSpielstandHeim = (EditText) findViewById(R.id.txfHeimmannschaftAktualisiere);
		txfSpielstandGast = (EditText) findViewById(R.id.txfGastmannschaftAktualisiere);
		txfStatus = (EditText) findViewById(R.id.txfAktualisiereStatus);
		btnaktualisieren = (Button) findViewById(R.id.btnaktualisieren);
		btnloeschen = (Button) findViewById(R.id.btnloeschen);
		btnheimplusein = (Button) findViewById(R.id.btnplus1heim);
		btngastpluseins = (Button) findViewById(R.id.btnplus1gast);
		btnheimminuseins = (Button) findViewById(R.id.btnminus1heim);
		btngastminuseins = (Button) findViewById(R.id.btnminus1gast);
		switch_scoreboard = (Switch) findViewById(R.id.switch_scoreboard);
		statusScoreboard = (TextView) findViewById(R.id.statusscoreboard);
		btnzurueck = (Button) findViewById(R.id.btn_zurueck);
		checkspielistentschieden = (CheckBox) findViewById(R.id.check_Spielistentschieden);
		etxtspielende = (TextView) findViewById(R.id.textView_Spielentschieden);
		etxtspielende.setVisibility(View.GONE);
		tpSpielende = (TimePicker) findViewById(R.id.time_Spielende);
		tpSpielende.setIs24HourView(true);
		tpSpielende.setVisibility(View.GONE);
	}

	

	
}
