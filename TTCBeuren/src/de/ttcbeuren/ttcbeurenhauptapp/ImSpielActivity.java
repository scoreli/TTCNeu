package de.ttcbeuren.ttcbeurenhauptapp;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class ImSpielActivity extends Activity {
	/**
	 * Werte für die initalisierung von im Spiel
	 * 
	 */
	private EditText txfSpielstandHeim, txfSpielstandGast, txfStatus;
	private TextView statusScoreboard;
	private Button btnaktualisieren, btnloeschen, btnheimplusein,
			btngastpluseins, btnheimminuseins, btngastminuseins, btnzurueck;
	// private CheckBox checkboxbeenden;
	private Switch switch_scoreboard;
	DatabasehandlerSpiele dbspiele;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_im_spiel);
		init();
		dbspiele = new DatabasehandlerSpiele(this);

		btnaktualisieren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				aktualisieren();

			}

		});
		btnloeschen.setOnClickListener(new OnClickListener() {
			/**
			 * http://android-er.blogspot.kr/2012/03/example-of-using-
			 * popupwindow.html
			 */
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
						.getSystemService(LAYOUT_INFLATER_SERVICE);
				View popupView = layoutInflater.inflate(
						R.layout.bestaetigenpopup, null);
				final PopupWindow popupWindow = new PopupWindow(popupView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				Button btnDismiss = (Button) popupView
						.findViewById(R.id.dismiss);
				btnDismiss.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});
				Button btnaccept = (Button) popupView
						.findViewById(R.id.btn_accept);
				btnaccept.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {

						loeschen();

						popupWindow.dismiss();
					}

				});
				popupWindow.showAsDropDown(btnloeschen, 50, -30);

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

	private void loeschen() {
		// TODO Auto-generated method stub

	}

	private void aktualisieren() {
		// TODO Auto-generated method stub

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

	}
}
