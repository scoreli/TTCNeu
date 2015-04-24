package tk.scoreli.liveticker;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tk.scoreli.liveticker.data.DatabasehandlerSpiele;
import tk.scoreli.liveticker.data.DatabasehandlerUUID;
import tk.scoreli.liveticker.data.Mitglied;
import tk.scoreli.liveticker.data.Veranstaltung;
import tk.scoreli.liveticker.loginregister.AppConfig;
import tk.scoreli.liveticker.loginregister.AppController;
import tk.scoreli.liveticker.loginregister.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class NeuesSpielActivity extends Activity {
	private Spinner SpinnerSportart;
	private EditText txfHeimmannschaft, txfGastmannschaft, txfSpielbeginn,
			txfSpielstandHeim, txfSpielstandGast, txfStatus;
	private Button btnSpielerstellen;
	public String[] test = { "Tischtennis", "Fussball", "Volleyball" };
	DatabasehandlerSpiele db = new DatabasehandlerSpiele(this);
	DatabasehandlerUUID dbuuid = new DatabasehandlerUUID(this);
	private SessionManager session;
	private static final String TAG = NeuesSpielActivity.class.getSimpleName();
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_spiel);
		init();
		// Session manager
		session = new SessionManager(getApplicationContext());
		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		ArrayAdapter<String> Sportartspinneradapter = new ArrayAdapter<String>(
				NeuesSpielActivity.this,
				android.R.layout.simple_spinner_dropdown_item, test);
		SpinnerSportart.setAdapter(Sportartspinneradapter);
		btnSpielerstellen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Spielerstellen();

			}
		});
	}

	public void Spielerstellen() {
		// Errors zurücksetzten
		txfHeimmannschaft.setError(null);
		txfGastmannschaft.setError(null);
		txfSpielbeginn.setError(null);
		txfSpielstandHeim.setError(null);
		txfSpielstandGast.setError(null);

		String heimmannschaft = txfHeimmannschaft.getText().toString();
		String gastmannschaft = txfGastmannschaft.getText().toString();
		String spielbeginn = txfSpielbeginn.getText().toString();
		String spielstandHeim = txfSpielstandHeim.getText().toString();
		String spielstandGast = txfSpielstandGast.getText().toString();
		String sportart = SpinnerSportart.getSelectedItem().toString();
		String status = txfStatus.getText().toString();
		boolean cancel = false;
		View focusView = null;
		boolean cancellogin = false;
		if (TextUtils.isEmpty(heimmannschaft)) {
			txfHeimmannschaft
					.setError(getString(R.string.error_field_required));
			focusView = txfHeimmannschaft;
			cancel = true;

		}
		if (TextUtils.isEmpty(gastmannschaft)) {
			txfGastmannschaft
					.setError(getString(R.string.error_field_required));
			focusView = txfGastmannschaft;
			cancel = true;

		}
		if (TextUtils.isEmpty(spielbeginn)) {
			txfSpielbeginn.setError(getString(R.string.error_field_required));
			focusView = txfSpielbeginn;
			cancel = true;

		}
		if (TextUtils.isEmpty(spielstandHeim)) {
			txfSpielstandHeim
					.setError(getString(R.string.error_field_required));
			focusView = txfSpielstandHeim;
			cancel = true;

		}
		if (TextUtils.isEmpty(spielstandGast)) {
			txfSpielstandGast
					.setError(getString(R.string.error_field_required));
			focusView = txfSpielstandGast;
			cancel = true;

		}
		if (!session.isLoggedIn()) {

			cancellogin = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else if (cancellogin) {
			Toast.makeText(getApplicationContext(), "Bitte einloggen",
					Toast.LENGTH_SHORT).show();

		} else {
			Mitglied abfrage = dbuuid.getMitglied();

			try {
				registerVeranstaltung(sportart, abfrage.getUuid(),
						heimmannschaft, gastmannschaft, spielstandHeim,
						spielstandGast, spielbeginn, status);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Toast.makeText(getApplicationContext(),
					"Veranstaltung gespeichert", Toast.LENGTH_LONG).show();

		}
	}

	public void init() {
		SpinnerSportart = (Spinner) findViewById(R.id.spinner_Sportart);
		txfHeimmannschaft = (EditText) findViewById(R.id.txfHeimmannschaft);
		txfSpielstandHeim = (EditText) findViewById(R.id.txfSpielstandHeim);
		txfGastmannschaft = (EditText) findViewById(R.id.txfGastmannschaft);
		txfSpielstandGast = (EditText) findViewById(R.id.txfSpielstandGast);
		txfSpielbeginn = (EditText) findViewById(R.id.txfSpielbeginn);
		btnSpielerstellen = (Button) findViewById(R.id.btnNeuesSpiel);
		txfStatus = (EditText) findViewById(R.id.txfStatus);
	}

	/**
	 * Function to store user in MySQL database will post params(tag, name,
	 * email, password) to register url
	 * */
	private void registerVeranstaltung(final String sportart,
			final String user_id, final String heimmannschaft,
			final String gastmannschaft, final String punkteHeim,
			final String punkteGast, final String spielbeginn,
			final String status) {
		// Tag used to cancel the request
		String tag_string_req = "req_registerveranstaltung";

		pDialog.setMessage("Erstellen ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_VERANSTALTUNG,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Register Response: " + response.toString());
						hideDialog();
						try {
							JSONObject jObj = new JSONObject(response);
							boolean error = jObj.getBoolean("error");
							if (!error) {
								// User successfully stored in MySQL
								// Now store the user in sqlite
								// String uid = jObj.getString("uid");

								JSONObject user = jObj
										.getJSONObject("veranstaltung");
								String sportartj = user.getString("sportart");
								String heimmannschaftj = user
										.getString("heimmannschaft");
								String gastmannschaftj = user
										.getString("gastmannschaft");
								String punkteHeimj = user
										.getString("punkteHeim");
								String punkteGastj = user
										.getString("punkteGast");
								String spielbeginnj = user
										.getString("spielbeginn");
								String statusj = user.getString("status");

								db.addVeranstaltung(new Veranstaltung(
										heimmannschaftj, gastmannschaftj,
										spielbeginnj, sportartj, Integer
												.parseInt(punkteHeimj), Integer
												.parseInt(punkteGastj), statusj));
								/*
								 * // Launch login activity Intent intent = new
								 * Intent( RegisterVeranstaltung.this,
								 * LoginActivity.class); startActivity(intent);
								 * finish();
								 */
								finish();// Hat beendet da man was aufgerufen
											// hat obwohl es beendet worden ist.
							} else {

								// Error occurred in registration. Get the error
								// message
								String errorMsg = jObj.getString("error_msg");

								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();

							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									e.toString(), Toast.LENGTH_LONG).show();
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
				params.put("tag", "veranstaltung");// Zuerst Tag dann
													// Daten
				params.put("sportart", sportart);
				params.put("user", user_id);
				params.put("heimmannschaft", heimmannschaft);
				params.put("gastmannschaft", gastmannschaft);
				params.put("punkteHeim", punkteHeim);
				params.put("punkteGast", punkteGast);
				params.put("spielbeginn", spielbeginn);
				params.put("status", status);
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
