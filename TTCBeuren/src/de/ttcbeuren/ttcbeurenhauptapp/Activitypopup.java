package de.ttcbeuren.ttcbeurenhauptapp;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Activitypopup extends Activity {
	private TextView tvtext;
	private Button btnja, btnnein;
	public String Popup_Key="jnwoiu3wi9d";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bestaetigenpopup);
		init();
		/*
		 * DisplayMetrics dm = new DisplayMetrics();
		 * getWindowManager().getDefaultDisplay().getMetrics(dm); int width =
		 * dm.widthPixels; int height = dm.heightPixels; * Display mDisplay =
		 * getWindowManager().getDefaultDisplay();
		 * if(mDisplay.getOrientation()==0)
		 */
		/**
		 * Wird die Activitygröße direkt an die layoutgröße angepasst.
		 * 
		 */
		getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		//String text = getIntent().getExtras().getString(Popup_Key);
		//if(!text.isEmpty()){
		//tvtext.setText(text);
		//}
		
		btnja.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		 btnnein.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void init() {
		tvtext = (TextView) findViewById(R.id.textview_popup);
		btnja = (Button) findViewById(R.id.btn_accept);
		btnnein = (Button) findViewById(R.id.btn_dismiss);

	}
}
