package tk.scoreli.liveticker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NeuesSpielActivity extends Activity {
	private Spinner SpinnerSportart;
	private EditText txfHeimmannschaft,txfGastmannschaft,txfSpielbeginn;
	private Button btnSpielerstellen;
	public String [] test = {"Tischtennis","Fußball"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_spiel);
		init();
		ArrayAdapter<String>Sportartspinneradapter=new ArrayAdapter<String>(NeuesSpielActivity.this,android.R.layout.simple_spinner_dropdown_item,test);
		SpinnerSportart.setAdapter(Sportartspinneradapter);
	}

	
public void init(){
	SpinnerSportart = (Spinner) findViewById(R.id.spinner_Sportart);
	txfHeimmannschaft = (EditText)findViewById(R.id.txfHeimmannschaft);
	txfGastmannschaft = (EditText)findViewById(R.id.txfGastmannschaft);
	txfSpielbeginn = (EditText)findViewById(R.id.txfSpielbeginn);
	btnSpielerstellen=(Button)findViewById(R.id.btnNeuesSpiel);
}	
	
	
	
	
	
}
