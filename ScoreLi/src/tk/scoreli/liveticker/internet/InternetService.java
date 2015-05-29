package tk.scoreli.liveticker.internet;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.scoreli.liveticker.NavigationDrawerFragment;
import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.Veranstaltung;
import tk.scoreli.liveticker.loginregister.AppConfig;
import tk.scoreli.liveticker.loginregister.AppController;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

public class InternetService extends Activity {

	DatabasehandlerSpiele db;
	private static final String TAG_Veranstaltungen = "veranstaltung";
	// Für die Veranstaltungen holen
	private static final String TAG = NavigationDrawerFragment.class
			.getSimpleName();
	private ProgressDialog pDialog;
	Activity hans;

	/**
	 * Hierbei wird die Activity übergeben und dann dem Attribut der Klasse
	 * Internet Service übergeben. Dies ist nötig, da die Methoden auf die
	 * Activity zugreigen. Und so können sie das.
	 * 
	 * @param servus
	 */
	public InternetService(Activity servus) {
		db = new DatabasehandlerSpiele(servus.getApplicationContext());
		hans = servus;
		// Progress dialog
		//Geht nicht 
		//pDialog = new ProgressDialog(servus.getApplicationContext());
		 //pDialog.setCancelable(false);
	}

	/**
	 * Function to store user in MySQL database will post params(tag, name,
	 * email, password) to register url
	 * */
	public void Veranstaltungholen() {
		// Tag used to cancel the request
		String tag_string_req = "req_holen";

		//pDialog.setMessage("Holen ...");
	//	showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VERANSTALTUNG, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG,
								"Veranstaltung Response: "
										+ response.toString());
						// hideDialog();
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

								Toast.makeText(hans.getApplicationContext(),
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

						Toast.makeText(hans.getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						// hideDialog();

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
