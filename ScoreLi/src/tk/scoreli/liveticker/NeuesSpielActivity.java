package tk.scoreli.liveticker;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.Mitglied;
import tk.scoreli.liveticker.data.Veranstaltung;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NeuesSpielActivity extends Activity {
	private Spinner SpinnerSportart;
	private EditText txfHeimmannschaft,txfGastmannschaft,txfSpielbeginn,txfSpielstandHeim,txfSpielstandGast,txfStatus;
	private Button btnSpielerstellen;
	public String [] test = {"Tischtennis","Fußball"};
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_spiel);
		init();
		ArrayAdapter<String>Sportartspinneradapter=new ArrayAdapter<String>(NeuesSpielActivity.this,android.R.layout.simple_spinner_dropdown_item,test);
		SpinnerSportart.setAdapter(Sportartspinneradapter);
		btnSpielerstellen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Spielerstellen();
				
			}
		});
	}
public void Spielerstellen(){
	//Errors zurücksetzten
	txfHeimmannschaft.setError(null);
	txfGastmannschaft.setError(null);
	txfSpielbeginn.setError(null);
	txfSpielstandHeim.setError(null);
	txfSpielstandGast.setError(null);
	
	String heimmanschaft = txfHeimmannschaft.getText().toString();
	String gastmannschaft = txfGastmannschaft.getText().toString();
	String spielbeginn = txfSpielbeginn.getText().toString();
	String spielstandHeim = txfSpielstandHeim.getText().toString();
	String spielstandGast = txfSpielstandGast.getText().toString();
	String sportart= SpinnerSportart.getSelectedItem().toString();
	boolean cancel = false;
	View focusView = null;
	
	if (TextUtils.isEmpty(heimmanschaft)) {
	txfHeimmannschaft.setError(getString(R.string.error_field_required));
	focusView = txfHeimmannschaft;
	cancel =true;
	
	}
	if (TextUtils.isEmpty(gastmannschaft)) {
		txfGastmannschaft.setError(getString(R.string.error_field_required));
		focusView = txfGastmannschaft;
		cancel =true;
		
		}
	if (TextUtils.isEmpty(spielbeginn)) {
		txfSpielbeginn.setError(getString(R.string.error_field_required));
		focusView = txfSpielbeginn;
		cancel =true;
		
		}
	if (TextUtils.isEmpty(spielstandHeim)) {
		txfSpielstandHeim.setError(getString(R.string.error_field_required));
		focusView = txfSpielstandHeim;
		cancel =true;
		
		}
	if (TextUtils.isEmpty(spielstandGast)) {
		txfSpielstandGast.setError(getString(R.string.error_field_required));
		focusView = txfSpielstandGast;
		cancel =true;
		
		}
	/*
	 * Für alle weiteren felder bitte ausfüllen
	 * 
	 */
	if (cancel) {
		// There was an error; don't attempt login and focus the first
		// form field with an error.
		focusView.requestFocus();
	} else {
		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		//showProgress(true);
		/*
		  mAuthTask = new UserLoginTask(email, password);
		  mAuthTask.execute((Void) null);
		 */
		/*
		 * In die Datenbank schreiben
		 */
		//Veranstaltung servus = new Veranstaltung(heimmanschaft, gastmannschaft, spielbeginn, sportart, Integer.parseInt(spielstandHeim), Integer.parseInt(spielstandGast));
		
		//Toast.makeText(getApplicationContext(), servus.toString(), Toast.LENGTH_LONG).show();
		db.addVeranstaltung(new Veranstaltung(heimmanschaft, gastmannschaft, spielbeginn, sportart, Integer.parseInt(spielstandHeim), Integer.parseInt(spielstandGast)));
		
		Toast.makeText(getApplicationContext(), "Veranstaltung gespeichert ", Toast.LENGTH_LONG).show();
		finish();
	}
}

	
public void init(){
	SpinnerSportart = (Spinner) findViewById(R.id.spinner_Sportart);
	txfHeimmannschaft = (EditText)findViewById(R.id.txfHeimmannschaft);
	txfSpielstandHeim= (EditText)findViewById(R.id.txfSpielstandHeim);
	txfGastmannschaft = (EditText)findViewById(R.id.txfGastmannschaft);
	txfSpielstandGast = (EditText)findViewById(R.id.txfSpielstandGast);
	txfSpielbeginn = (EditText)findViewById(R.id.txfSpielbeginn);
	btnSpielerstellen=(Button)findViewById(R.id.btnNeuesSpiel);
	txfStatus=(EditText)findViewById(R.id.txfStatus);
}	
	
	
	
	
	
}
