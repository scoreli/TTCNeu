package tk.scoreli.liveticker;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.Mitglied;
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
	private EditText txfHeimmannschaft,txfGastmannschaft,txfSpielbeginn,txfSpielstandHeim,txfSpielstandGast;
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
	String gastmanschaft = txfGastmannschaft.getText().toString();
	String spielbeginn = txfSpielbeginn.getText().toString();
	String spielstandheim = txfSpielstandHeim.getText().toString();
	String spielstandgast = txfSpielstandGast.getText().toString();
	
	boolean cancel = false;
	View focusView = null;
	
	if (TextUtils.isEmpty(heimmanschaft)) {
	txfHeimmannschaft.setError(getString(R.string.error_field_required));
	focusView = txfHeimmannschaft;
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
		
		Toast.makeText(getApplicationContext(), "Veranstaltung gespeichert ", Toast.LENGTH_LONG).show();
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
}	
	
	
	
	
	
}
