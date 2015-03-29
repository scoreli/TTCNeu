package tk.scoreli.liveticker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NeuesSpielActivity extends Activity {
	public Spinner SpinnerSportart;
	public String [] test = {"Tischtennis","Fußball","Volleyball","Handball"};
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
	
}	
	
	
	
	
	
}
