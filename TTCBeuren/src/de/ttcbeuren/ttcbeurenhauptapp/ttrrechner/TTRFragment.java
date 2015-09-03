package de.ttcbeuren.ttcbeurenhauptapp.ttrrechner;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;

public class TTRFragment extends Fragment {
	private EditText ttra, ttrb0, ttrb1, ttrb2, ttrb3, ttrb4;
	private CheckBox check_b0, check_b1, check_b2, check_b3, check_b4;
	private Button btnberechnen;
	private static final String ARG_SECTION_NUMBER = "servus";
	MainActivityStartseite mainac;

	// public int AENDERUNGSKONSTANTE = 16;

	public TTRFragment newInstance() {
		TTRFragment fragment = new TTRFragment();
		return fragment;

	}

	@Override
	public void onAttach(Activity activity) {
		// Wichtig hier muss die Section angegeben werden ???
		super.onAttach(activity);
		((MainActivityStartseite) activity).onSectionAttached(2);
	}
/*
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("text", "" + ttra.getText());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			ttra.setText(savedInstanceState.getString("text"));
		}
	}
*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		View root = inflater.inflate(R.layout.fragment_ttrrechner, container,
				false);
		ttra = (EditText) root.findViewById(R.id.etxt_ttra);
		ttra.requestFocus();
		ttrb0 = (EditText) root.findViewById(R.id.etxt_ttrb0);
		ttrb1 = (EditText) root.findViewById(R.id.etxt_ttrb1);
		ttrb2 = (EditText) root.findViewById(R.id.etxt_ttrb2);
		ttrb3 = (EditText) root.findViewById(R.id.etxt_ttrb3);
		ttrb4 = (EditText) root.findViewById(R.id.etxt_ttrb4);
		check_b0 = (CheckBox) root.findViewById(R.id.box_siegb0);
		check_b1 = (CheckBox) root.findViewById(R.id.box_siegb1);
		check_b2 = (CheckBox) root.findViewById(R.id.box_siegb2);
		check_b3 = (CheckBox) root.findViewById(R.id.box_siegb3);
		check_b4 = (CheckBox) root.findViewById(R.id.box_siegb4);
		btnberechnen = (Button) root.findViewById(R.id.btn_berechnen);

		// TODO Auto-generated method stub
		/**
		 * Es sollte behoben werden das der richtige Titel angezeigt wird.
		 */
		// restoreActionBar()
		btnberechnen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ttrberechnen();
			}
		});
		return root;
	}

	/**
	 * http://www.tt-tsvgomaringen.de/news/view.php?sid=689
	 */
	private void ttrberechnen() {
		int gesamt = 0;
		double gw0 = 0, gw1 = 0, gw2 = 0, gw3 = 0, gw4 = 0;
		boolean ttrab = false;
		int ittra = 0, ittrb0 = 0, ittrb1 = 0, ittrb2 = 0, ittrb3 = 0, ittrb4 = 0;
		// Text aus den Felder in Strings umwandeln
		String sttra = ttra.getText().toString();
		String sttrb0 = ttrb0.getText().toString();
		String sttrb1 = ttrb1.getText().toString();
		String sttrb2 = ttrb2.getText().toString();
		String sttrb3 = ttrb3.getText().toString();
		String sttrb4 = ttrb4.getText().toString();
		/**
		 * Hier wird abgeprüft ob die Felder leer sind. Außerdem ob das ttra
		 * feld nicht leer ist.
		 */
		if (!TextUtils.isEmpty(sttra)) {
			ittra = Integer.parseInt(sttra);
			ttrab = true;
		}
		if (!TextUtils.isEmpty(sttrb0) && ttrab) {
			ittrb0 = Integer.parseInt(sttrb0);

			gw0 = gewinnwahrscheinlichkeit(ittra, ittrb0);
			// Wird geprüft ob gewonnen oder nicht
			if (check_b0.isChecked()) {
				gesamt = gewinnpunkte(gw0);
			} else {
				gesamt = verlustpunkte(gw0);

			}

		}
		if (!TextUtils.isEmpty(sttrb1) && ttrab) {
			ittrb1 = Integer.parseInt(sttrb1);

			gw1 = gewinnwahrscheinlichkeit(ittra, ittrb1);
			int zwischen = gesamt;
			// Wird geprüft ob gewonnen oder nicht
			if (check_b1.isChecked()) {
				gesamt = gewinnpunkte(gw1);
			} else {
				gesamt = verlustpunkte(gw1);
			}

			gesamt = gesamt + zwischen;
		}
		if (!TextUtils.isEmpty(sttrb2) && ttrab) {
			ittrb2 = Integer.parseInt(sttrb2);

			gw2 = gewinnwahrscheinlichkeit(ittra, ittrb2);
			int zwischen = gesamt;
			// Wird geprüft ob gewonnen oder nicht
			if (check_b2.isChecked()) {
				gesamt = gewinnpunkte(gw2);

			} else {
				gesamt = verlustpunkte(gw2);
			}

			gesamt = gesamt + zwischen;
		}
		if (!TextUtils.isEmpty(sttrb3) && ttrab) {
			ittrb3 = Integer.parseInt(sttrb3);

			gw3 = gewinnwahrscheinlichkeit(ittra, ittrb3);
			int zwischen = gesamt;
			// Wird geprüft ob gewonnen oder nicht
			if (check_b3.isChecked()) {
				gesamt = gewinnpunkte(gw3);

			} else {
				gesamt = verlustpunkte(gw3);
			}

			gesamt = gesamt + zwischen;
		}
		if (!TextUtils.isEmpty(sttrb4) && ttrab) {
			ittrb4 = Integer.parseInt(sttrb4);

			gw4 = gewinnwahrscheinlichkeit(ittra, ittrb4);
			int zwischen = gesamt;
			// Wird geprüft ob gewonnen oder nicht
			if (check_b4.isChecked()) {
				gesamt = gewinnpunkte(gw4);

			} else {
				gesamt = verlustpunkte(gw4);
			}

			gesamt = gesamt + zwischen;
		}
		Toast.makeText(
				getActivity(),
				"Du hast " + gesamt + " Punke gemacht! Dein neuer TTR ist: "
						+ (gesamt + ittra), Toast.LENGTH_SHORT).show();

	}

	public double gewinnwahrscheinlichkeit(int ttra, int ttrb) {
		double ttrzwischen = ttrb - ttra;// In einzelschritte aufgeteilt
		// da er sonst null castet
		ttrzwischen = ttrzwischen / 150;
		return (1 / (1 + Math.pow(10, ttrzwischen)));

	}

	public int gewinnpunkte(double gewinnwahrscheinlichkeit) {
		/**
		 * Hier wird die Zwischengespeicherte Aenderungskonstante wieder geholt
		 * von der XML Datei
		 */
		SharedPreferences preferences = this.getActivity()
				.getSharedPreferences("Zwischenwerte", Context.MODE_PRIVATE);
		return (int) Math.round(((1 - gewinnwahrscheinlichkeit) * preferences
				.getInt("Aenderungskonstante", 16)));

	}

	public int verlustpunkte(double gewinnwahrscheinlichkeit) {
		/**
		 * Hier wird die Zwischengespeicherte Aenderungskonstante wieder geholt
		 * von der XML Datei
		 */
		SharedPreferences preferences = this.getActivity()
				.getSharedPreferences("Zwischenwerte", Context.MODE_PRIVATE);

		return (int) Math.round((-gewinnwahrscheinlichkeit * preferences
				.getInt("Aenderungskonstante", 16)));

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.ttrfragment, menu);

		super.onCreateOptionsMenu(menu, inflater);
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
		if (id == R.id.menu_aenderungskonstante) {
			startActivity(new Intent(getActivity(),
					AenderungskonstanteActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void restoreActionBar() {
		ActionBar actionBar = this.getActivity().getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(R.string.title_ttrrechner);
	}
}
