package tk.scoreli.liveticker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AnzeigeActivity extends Activity{
TextView AnzeigeHeima,AnzeigekleinHeima,AnzeigeGasta,AnzeigekleinGasta,AnzeigeStatusa;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_anzeige);
	init();
}

private void init() {
	AnzeigeHeima=(TextView)findViewById(R.id.AnzeigeHeim);
	AnzeigekleinHeima=(TextView)findViewById(R.id.AnzeigekleinHeim);
	AnzeigeGasta=(TextView)findViewById(R.id.AnzeigeGast);
	AnzeigekleinGasta=(TextView)findViewById(R.id.AnzeigekleinGast);
	AnzeigeStatusa=(TextView)findViewById(R.id.AnzeigeStatus);
}
	
	
	
	
	

	
}
