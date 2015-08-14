package de.ttcbeuren.ttcbeurenhauptapp.ergebnisse;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import de.ttcbeuren.ttcbeurenhauptapp.MainActivityStartseite;
import de.ttcbeuren.ttcbeurenhauptapp.R;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;

public class ErgebnisseFragment extends Fragment implements
		OnItemSelectedListener {
	List<Spiel> spielelive, spielebeendet;
	ListView listview;
	Spinner auswahlspinner;
	DatabasehandlerSpiele dbspiele;
	// private MyArrayAdapter adapterlive,adapterbeendet;
	private ListViewadapter mAdapter;
	public String[] Mannschaften = { "Alle Spiele", "1. Herren", "2. Herren",
			"3. Herren", "4. Herren.", "1. Damen", "1. Jugend", "2. Jugend",
			"3. Jugend", "4. Jugend", "5. Jugend", "6. Jugend" };

	// WebView browser = (WebView) root.findViewById(R.id.webviewakuelles);
	// browser.loadUrl("http://www.ttc-beuren.de");
	public ErgebnisseFragment newInstance() {
		ErgebnisseFragment fragment = new ErgebnisseFragment();
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
		mAdapter = new ListViewadapter(getActivity());
		dbspiele = new DatabasehandlerSpiele(getActivity());
		View root = inflater.inflate(R.layout.fragment_ergebnisse, container,
				false);
		auswahlspinner = (Spinner) root.findViewById(R.id.spin_Mannschaften);
		listview = (ListView) root.findViewById(R.id.listV_ergebnisse);

		ArrayAdapter<String> Auswahlspinneradapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				Mannschaften);
		auswahlspinner.setAdapter(Auswahlspinneradapter);
		auswahlspinner.setOnItemSelectedListener(this);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * Die 1 ist wichtig damit nicht die Überschriften genommen werden
				 */
				if(mAdapter.getItemViewType(position)!=1){
Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();}


			}
		});
		return root;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String NameDerMannschaft = (String) parent.getItemAtPosition(position);
		switch (NameDerMannschaft) {
		case "Alle Spiele":
			spielelive = dbspiele.getAllLiveSpiele();
			spielebeendet = dbspiele.getAllBeendetSpiele();
			break;
		case "1. Herren":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_1), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_1), 1);
			break;
		case "2. Herren":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_2), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_2), 1);
			break;
		case "3. Herren":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_3), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_3), 1);
			break;
		case "4. Herren":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_4), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_herren),
					getString(R.string.mannschaftsnummer_4), 1);
			break;
		case "1. Damen":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_damen),
					getString(R.string.mannschaftsnummer_1), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_damen),
					getString(R.string.mannschaftsnummer_1), 1);
			break;
		case "1. Jugend":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_1), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_1), 1);
			break;
		case "2. Jugend":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_2), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_2), 1);
			break;
		case "3. Jugend":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_3), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_3), 1);
			break;
		case "4. Jugend":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_4), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_4), 1);
			break;
		case "5. Jugend":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_5), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_5), 1);
			break;
		case "6. Jugend":
			spielelive = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_6), 0);
			spielebeendet = dbspiele.getVariableSpiele(
					getString(R.string.mannschaftsart_jugend),
					getString(R.string.mannschaftsnummer_6), 1);
			break;

		}
		mAdapter.deletelist();
		mAdapter.addSectionHeaderItem("Live :");
		for (int i = 0; i < spielelive.size(); i++) {
			mAdapter.addItem(spielelive.get(i).toString());
		}
		mAdapter.addSectionHeaderItem("Beendet :");
		for (int i = 0; i < spielebeendet.size(); i++) {
			mAdapter.addItem(spielebeendet.get(i).toString());
		}
		listview.setAdapter(mAdapter);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}
}
