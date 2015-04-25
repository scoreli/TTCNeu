package tk.scoreli.liveticker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.Veranstaltung;
import tk.scoreli.liveticker.loginregister.AppConfig;
import tk.scoreli.liveticker.loginregister.AppController;
import tk.scoreli.liveticker.loginregister.LoginActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class MainActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	private static final String TAG_Veranstaltungen = "veranstaltung";
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	ListView Veranstaltungenliste;
	private ArrayAdapter<Veranstaltung> adapter;
	List<Veranstaltung> veranstaltungen;
	// Für die Veranstaltungen holen
	private static final String TAG = NeuesSpielActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	public static final String KEY = "UebergabeVeranstaltungObjekt";
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * Wird die Liste der Veranstaltungen auf in der MainActivty
		 * initalisiert.
		 */
		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		Veranstaltungenliste = (ListView) findViewById(R.id.list_AuflistungSpiele);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_Startseite);
			/**
			 * Bei der Auswahl Startseite wird die Liste der Aktuellen
			 * Veranstaltungen nicht angezeigt.
			 */
			Veranstaltungenliste.setVisibility(View.GONE);
			break;
		case 2:
			mTitle = getString(R.string.title_Sportart1);
			/**
			 * Hier werden alle Tischtennisveranstaltungen angzeigt. Hierbeit
			 * wird die Methode getTischtennisVeranstaltungen() aufgerufen.
			 * Dabei muss die Liste in ein ArrayAdapter des Typs Veranstaltung
			 * erzeugt werden und die Liste übergeben werden.
			 */
			Veranstaltungholen();
			Veranstaltungenliste.setVisibility(View.VISIBLE);
			veranstaltungen = db.getTischtennisVeranstaltungen();
			adapter = new ArrayAdapter<Veranstaltung>(this,
					android.R.layout.simple_list_item_1, veranstaltungen);
			Veranstaltungenliste.setAdapter(adapter);
			break;
		case 3:
			mTitle = getString(R.string.title_Sportart2);
			/**
			 * Hier werden alle Fußballveranstaltungen angzeigt. Hierbeit wird
			 * die Methode getFußballVeranstaltungen() aufgerufen. Dabei muss
			 * die Liste in ein ArrayAdapter des Typs Veranstaltung erzeugt
			 * werden und die Liste übergeben werden.
			 */
			Veranstaltungholen();
			Veranstaltungenliste.setVisibility(View.VISIBLE);
			veranstaltungen = db.getFussballVeranstaltungen();
			adapter = new ArrayAdapter<Veranstaltung>(this,
					android.R.layout.simple_list_item_1, veranstaltungen);
			Veranstaltungenliste.setAdapter(adapter);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
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
		if (id == R.id.menu_anmelden) {
			startActivity(new Intent(MainActivity.this, LoginActivity.class));
			return true;
		}
		if (id == R.id.menu_Scoreboard) {
			/**
			 * Hier wird ein leeres Veranstaltungsobjekt übergeben. Dies braucht
			 * man damit die Anzeigen Actitvity beim Beenden der
			 * Bluetoothverbindung der Stand von vorher bleibt.
			 */
			Intent i = new Intent(MainActivity.this, AnzeigeActivity.class);
			Veranstaltung leereVeranstaltung = new Veranstaltung("", "", "",
					"", 0, 0, "");
			byte[] uebergabe;
			try {
				uebergabe = serialize(leereVeranstaltung);
				i.putExtra(KEY, uebergabe);
				startActivity(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
		if (id == R.id.menu_Spiele) {
			startActivity(new Intent(MainActivity.this, SpieleActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	/**
	 * Function to store user in MySQL database will post params(tag, name,
	 * email, password) to register url
	 * */
	private void Veranstaltungholen() {
		// Tag used to cancel the request
		String tag_string_req = "req_holen";

		pDialog.setMessage("Holen ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VERANSTALTUNG, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG,
								"Veranstaltung Response: "
										+ response.toString());
						hideDialog();
						try {
							/*
							 * Toast.makeText(getApplicationContext(),
							 * response.toString(), Toast.LENGTH_SHORT) .show();
							 */
							JSONObject jObj = new JSONObject(response);
							// boolean error = jObj.getBoolean("error");
							boolean error = false;
							if (!error) {
								// User successfully stored in MySQL
								// Now store the user in sqlite
								db.deleteVeranstaltungen();

								JSONArray uebergabe = jObj
										.getJSONArray(TAG_Veranstaltungen);
								for (int i = 0; i < uebergabe.length(); i++) {
									JSONObject veranstaltung = uebergabe
											.getJSONObject(i);
									String idj = veranstaltung
											.getString("veranstaltung_id");
									String heimmannschaftj = veranstaltung
											.getString("heimmannschaft");
									String gastmannschaftj = veranstaltung
											.getString("gastmannschaft");
									String punkteHeimj = veranstaltung
											.getString("punkteHeim");
									String punkteGastj = veranstaltung
											.getString("punkteGast");
									String statusj = veranstaltung
											.getString("status");
									String sportartj = veranstaltung
											.getString("sportart");
									String spielbeginnj = veranstaltung
											.getString("spielbeginn");
									db.addVeranstaltung(new Veranstaltung(Long
											.parseLong(idj), sportartj,
											heimmannschaftj, gastmannschaftj,
											Integer.parseInt(punkteHeimj),
											Integer.parseInt(punkteGastj),
											spielbeginnj, statusj));

								}

								Toast.makeText(getApplicationContext(),
										"Aktualisiert", Toast.LENGTH_SHORT)
										.show();
							} else {

								// Error occurred in registration. Get the error
								// message
								// String errorMsg =
								// jObj.getString("error_msg");

								// Toast.makeText(getApplicationContext(),
								// errorMsg, Toast.LENGTH_LONG).show();

							}
						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Registration Error: " + error.getMessage());

						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();

					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "holeveranstaltungen");
				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
