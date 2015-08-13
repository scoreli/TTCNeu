package de.ttcbeuren.ttcbeurenhauptapp.aktuelles;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class AktuellesFragment extends Fragment {
	List<Spiel> spiele;
	ListView Veranstaltungenliste;
	DatabasehandlerSpiele dbspiele;
	private ArrayAdapter<Spiel> adapter;

	public AktuellesFragment newInstance() {
		AktuellesFragment fragment = new AktuellesFragment();
		return fragment;

	}

	@Override
	public void onAttach(Activity activity) {
		// Wichtig hier muss die Section angegeben werden ???
		super.onAttach(activity);
		((MainActivityStartseite) activity).onSectionAttached(1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setHasOptionsMenu(true);
		Log.d("dfsdf",
				"Register Response: " );
		dbspiele = new DatabasehandlerSpiele(getActivity());
		View root = inflater.inflate(R.layout.fragment_aktuelles, container,
				false);
		Veranstaltungenliste = (ListView) root
				.findViewById(R.id.list_AuflistungSpiele);
		spiele = dbspiele.getAllVeranstaltungen();
		if (spiele != null) {
		}
		adapter = new ArrayAdapter<Spiel>(getActivity(),
				android.R.layout.simple_list_item_1, spiele);
		Veranstaltungenliste.setAdapter(adapter);
		// WebView browser = (WebView) root.findViewById(R.id.webviewakuelles);
		// browser.loadUrl("http://www.ttc-beuren.de");
		return root;
	}
}
