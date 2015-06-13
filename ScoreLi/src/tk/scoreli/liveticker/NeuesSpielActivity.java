package tk.scoreli.liveticker;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.DatabasehandlerUUID;
import tk.scoreli.liveticker.data.Mitglied;
import tk.scoreli.liveticker.internet.InternetService;
import tk.scoreli.liveticker.loginregister.SessionManager;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NeuesSpielActivity extends Activity {
	private Spinner SpinnerSportart;
	private EditText txfHeimmannschaft, txfGastmannschaft, txfSpielbeginn,
			txfSpielstandHeim, txfSpielstandGast, txfStatus;
	private Button btnSpielerstellen;
	//private CheckBox veranstaltungbeendet;
	public String[] test = { "Tischtennis", "Fussball", "Handball" };
	//private int istbeendet=0;
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	DatabasehandlerUUID dbuuid = new DatabasehandlerUUID(this);
	private SessionManager session;

	/**
	 * Für die Internet Klasse
	 * 
	 */
	private InternetService internetservice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_spiel);
		init();
		// Session manager
		session = new SessionManager(getApplicationContext());
		// Progress dialog
		// pDialog = new ProgressDialog(this);
		// pDialog.setCancelable(false);
		internetservice = new InternetService(this);
		ArrayAdapter<String> Sportartspinneradapter = new ArrayAdapter<String>(
				NeuesSpielActivity.this,
				android.R.layout.simple_spinner_dropdown_item, test);
		SpinnerSportart.setAdapter(Sportartspinneradapter);
		btnSpielerstellen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Spielerstellen();

			}
		});
		
	}

	public void Spielerstellen() {
		// Errors zurücksetzten
		txfHeimmannschaft.setError(null);
		txfGastmannschaft.setError(null);
		txfSpielbeginn.setError(null);
		txfSpielstandHeim.setError(null);
		txfSpielstandGast.setError(null);

		String heimmannschaft = txfHeimmannschaft.getText().toString();
		String gastmannschaft = txfGastmannschaft.getText().toString();
		String spielbeginn = txfSpielbeginn.getText().toString();
		String spielstandHeim = txfSpielstandHeim.getText().toString();
		String spielstandGast = txfSpielstandGast.getText().toString();
		String sportart = SpinnerSportart.getSelectedItem().toString();
		String status = txfStatus.getText().toString();
		boolean cancel = false;
		View focusView = null;
		boolean cancellogin = false;
		if (TextUtils.isEmpty(heimmannschaft)) {
			txfHeimmannschaft
					.setError(getString(R.string.error_field_required));
			focusView = txfHeimmannschaft;
			cancel = true;

		}
		if (TextUtils.isEmpty(gastmannschaft)) {
			txfGastmannschaft
					.setError(getString(R.string.error_field_required));
			focusView = txfGastmannschaft;
			cancel = true;

		}
		if (TextUtils.isEmpty(spielbeginn)) {
			txfSpielbeginn.setError(getString(R.string.error_field_required));
			focusView = txfSpielbeginn;
			cancel = true;

		}
		if (TextUtils.isEmpty(spielstandHeim)) {
			txfSpielstandHeim
					.setError(getString(R.string.error_field_required));
			focusView = txfSpielstandHeim;
			cancel = true;

		}
		if (TextUtils.isEmpty(spielstandGast)) {
			txfSpielstandGast
					.setError(getString(R.string.error_field_required));
			focusView = txfSpielstandGast;
			cancel = true;

		}
		if (!session.isLoggedIn()) {

			cancellogin = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else if (cancellogin) {
			Toast.makeText(getApplicationContext(), "Bitte einloggen",
					Toast.LENGTH_SHORT).show();

		} else {
			Mitglied abfrage = dbuuid.getMitglied();

			try {
				internetservice.registerVeranstaltung(sportart,
						abfrage.getUuid(), heimmannschaft, gastmannschaft,
						spielstandHeim, spielstandGast, spielbeginn, status);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(),
					"Veranstaltung gespeichert", Toast.LENGTH_LONG).show();
			// finish();
		}
	}

	public void init() {
		SpinnerSportart = (Spinner) findViewById(R.id.spinner_Sportart);
		txfHeimmannschaft = (EditText) findViewById(R.id.txfHeimmannschaft);
		txfSpielstandHeim = (EditText) findViewById(R.id.txfSpielstandHeim);
		txfGastmannschaft = (EditText) findViewById(R.id.txfGastmannschaft);
		txfSpielstandGast = (EditText) findViewById(R.id.txfSpielstandGast);
		txfSpielbeginn = (EditText) findViewById(R.id.txfSpielbeginn);
		btnSpielerstellen = (Button) findViewById(R.id.btnNeuesSpiel);
		txfStatus = (EditText) findViewById(R.id.txfStatus);
		
	}

}
