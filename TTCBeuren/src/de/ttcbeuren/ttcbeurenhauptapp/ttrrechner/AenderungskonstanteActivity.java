package de.ttcbeuren.ttcbeurenhauptapp.ttrrechner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.R;

/**
 * Hier wird die Aenderungskonstante festgelegt und erfasst. Dabei wird auf
 * Preferencen zugegriffen die man vorher als Einträge in einer XML Datei
 * festlegt.
 * 
 * @author philipp
 *
 */
public class AenderungskonstanteActivity extends PreferenceActivity implements
		Preference.OnPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_aenderungskonstante);

	}

	@Override
	protected void onStop() {
		int aenderungskonstante = 16;
		boolean[] arrayb = new boolean[5];
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// Auslesen des Anzeige-Modus aus den SharedPreferences
		arrayb[0] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_unter16_key), false);
		arrayb[1] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_unter21_key), false);
		arrayb[2] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_unter1jahrkein_key), false);
		arrayb[3] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_wenigerals30_key), false);
		arrayb[4] = sPrefs.getBoolean(
				getString(R.string.preference_ptext_nachwuchsausgleich_key),
				false);
		/**
		 * Hier wird geprüft wie sich die Änderungskonstante sich zusammensetzt.
		 */
		for (int i = 0; i < arrayb.length; i++) {
			if (arrayb[i]) {
				if (i < 4) {
					aenderungskonstante = aenderungskonstante + 4;
				} else {
					aenderungskonstante = aenderungskonstante + 2;
				}
			}
		}
		// Hier wird die Aenderungskonstante zwischengespeichert. Diese ist in
		// einer XML Datei dann abgelegt.
		SharedPreferences prefs = getSharedPreferences("Zwischenwerte",
				MODE_PRIVATE);
		prefs.edit().putInt("Aenderungskonstante", aenderungskonstante).apply();
		super.onStop();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}

}
