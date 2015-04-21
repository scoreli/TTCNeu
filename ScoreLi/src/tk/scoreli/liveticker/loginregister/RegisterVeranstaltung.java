package tk.scoreli.liveticker.loginregister;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class RegisterVeranstaltung {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	private SessionManager session;
	//private SQLiteHandlerLogin dblogin;

	/**
	 * Function to store user in MySQL database will post params(tag, name,
	 * email, password) to register url
	 * */
	private void registerUser(final String sportart, final String user_id,
			final String heimmannschaft,final String gastmannschaft, final String punkteHeim,
			final String punkteGast, final String spielbeginn,
			final String status) {
		// Tag used to cancel the request
		String tag_string_req = "req_register";

		pDialog.setMessage("Registering ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

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
								//String uid = jObj.getString("uid");

								//JSONObject user = jObj.getJSONObject("user");
								//String name = user.getString("name");
								//String email = user.getString("email");
								//String created_at = user
								//		.getString("created_at");

								// Inserting row in users table
								//dblogin.addUser(name, email, uid, created_at);
								/*
								 * // Launch login activity Intent intent = new
								 * Intent( RegisterVeranstaltung.this,
								 * LoginActivity.class); startActivity(intent);
								 * finish();
								 */
							} else {

								// Error occurred in registration. Get the error
								// message
								String errorMsg = jObj.getString("error_msg");
								/*Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();*/
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Registration Error: " + error.getMessage());
						/*Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();*/
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "registerveranstaltung");// Zuerst Tag dann
															// Daten
				params.put("sportart", sportart);
				params.put("user_id", user_id);
				params.put("heimmannschaft", heimmannschaft);
				params.put("gastmannschaft", gastmannschaft);
				params.put("spielstandheim", punkteHeim);
				params.put("spielstandgast", punkteGast);
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
