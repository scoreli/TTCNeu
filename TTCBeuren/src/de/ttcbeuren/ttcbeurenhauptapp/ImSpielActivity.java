package de.ttcbeuren.ttcbeurenhauptapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;

import java.io.IOException;

import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentConfirm;
import de.ttcbeuren.ttcbeurenhauptapp.alertdialogs.AlertFragmentNotify;
import de.ttcbeuren.ttcbeurenhauptapp.detailspiel.DetailActivity;
import de.ttcbeuren.ttcbeurenhauptapp.ergebnisse.ErgebnisseFragment;
import de.ttcbeuren.ttcbeurenhauptapp.internet.InternetService;
import de.ttcbeuren.ttcbeurenhauptapp.loginregister.DatabasehandlerUUID;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.DatabasehandlerSpiele;
import de.ttcbeuren.ttcbeurenhauptapp.spiele.Spiel;
/*
Muss von ActionBarActivity erben laut Android-Dokumentation
 */
public class ImSpielActivity extends ActionBarActivity implements
		AlertFragmentConfirm.AlertDialogListener {
	/*
	Für Google Cast Anbindung
	 */
	private MediaRouter mMediaRouter;
	private MediaRouteSelector mMediaRouteSelector;
	private MediaRouter.Callback mMediaRouterCallback;
	private CastDevice mSelectedDevice;
	private GoogleApiClient mApiClient;
	private Cast.Listener mCastListener;
	private ConnectionCallbacks mConnectionCallbacks;
	private ConnectionFailedListener mConnectionFailedListener;
	private HelloWorldChannel mHelloWorldChannel;
	private boolean mApplicationStarted;
	private boolean mWaitingForReconnect;
	private String mSessionId;
	private static final String TAG = ImSpielActivity.class.getSimpleName();

	private static final int REQUEST_CODE = 1;


	/**
	 * Werte für die initalisierung von im Spiel
	 * 
	 */
	private CheckBox checkspielistentschieden;
	private TimePicker tpSpielende;
	private EditText txfSpielstandHeim, txfSpielstandGast, txfStatus;
	private TextView statusScoreboard, etxtspielende;
	private Button btnaktualisieren, btnloeschen, btnheimplusein,
			btngastpluseins, btnheimminuseins, btngastminuseins, btnzurueck;
	// private CheckBox checkboxbeenden;
	private int anzahlGewinnpunkte;
	private Switch switch_scoreboard;
	DatabasehandlerSpiele dbspiele;
	DatabasehandlerUUID dbuuid;
	private InternetService internetservice;
	private ConnectionDetector myConnection;
	int uebergabespiel_id;
	Spiel uebergabespiel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_im_spiel);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(
				getResources().getColor(android.R.color.transparent)));
		init();

		dbspiele = new DatabasehandlerSpiele(this);
		dbuuid = new DatabasehandlerUUID(this);
		internetservice = new InternetService(this);
		//

		myConnection = new ConnectionDetector(getApplicationContext());
		uebergabespiel_id = getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY);
		uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
		/**
		 * Dieser Abschnitt dient zur Herausfindung von der Anzahl der Gewinnpunkte.
		 * Daraus bekommt man dann einen Integerwert für die Anzahl.
		 */
		String[] gewinnpunkte = uebergabespiel.getSpielsystem().split(" ");
		anzahlGewinnpunkte = Integer.parseInt(gewinnpunkte[0]);

		plusminusHeim(0, uebergabespiel,anzahlGewinnpunkte);



		checkspielistentschieden
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							etxtspielende.setVisibility(View.VISIBLE);
							tpSpielende.setVisibility(View.VISIBLE);
							try {
								String jsonUebergabe = uebergabespiel.toJSon();
								sendMessage(jsonUebergabe);
								Log.d(TAG, jsonUebergabe);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							etxtspielende.setVisibility(View.GONE);
							tpSpielende.setVisibility(View.GONE);
						}

					}
				});
		btnaktualisieren.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myConnection.isConnectingToInternet()) {
					if (checkspielistentschieden.isChecked()) {
						DialogFragment bestaetigenFragment = new AlertFragmentConfirm();

						bestaetigenFragment.show(getFragmentManager(),
								"bestaetigenaktualisieren");
					} else {
						aktualisieren();
					}

				} else {
					DialogFragment notifyFragment = new AlertFragmentNotify();
					notifyFragment.show(getFragmentManager(), "notify");

				}

			}
		});
		btnloeschen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myConnection.isConnectingToInternet()) {
					DialogFragment bestaetigenFragment = new AlertFragmentConfirm();
					bestaetigenFragment.show(getFragmentManager(),
							"bestaetigenloeschen");
				} else {
					DialogFragment notifyFragment = new AlertFragmentNotify();
					notifyFragment.show(getFragmentManager(), "notify");

				}

			}

		});

		btnheimplusein.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Heim plus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusHeim(1, uebergabespiel,anzahlGewinnpunkte);
			}
		});
		btngastpluseins.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Gast plus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusGast(1, uebergabespiel,anzahlGewinnpunkte);
			}
		});
		btnheimminuseins.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Heim minus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusHeim(-1, uebergabespiel,anzahlGewinnpunkte);
			}
		});
		btngastminuseins.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button Gast minus eins gedrückt, wird nochmal die
			 * Veranstaltung geholt und dann mit der Methode plusminusEins
			 * aufgerufen und dann auf der internen Datenbank auch gleichzeitig
			 * aktualisiert.
			 */
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int uebergabespiel_id = getIntent().getExtras().getInt(
						ErgebnisseFragment.KEY);
				Spiel uebergabespiel = dbspiele.getSpiel(uebergabespiel_id);
				plusminusGast(-1, uebergabespiel,anzahlGewinnpunkte);
			}
		});
		btnzurueck.setOnClickListener(new OnClickListener() {
			/**
			 * Wird der Button zurück gedrückt wird die Acitivty beendet und man
			 * gelangt auf die Veranstaltungen des Users.
			 */
			@Override
			public void onClick(View v) {
				finish();

			}
		});

		//Konfiguriere die Google-Cast-Geräte suche
		mMediaRouter = MediaRouter.getInstance(getApplicationContext());
		mMediaRouteSelector = new MediaRouteSelector.Builder()
				.addControlCategory(CastMediaControlIntent.categoryForCast(getResources()
						.getString(R.string.app_id))).build();
		mMediaRouterCallback = new MyMediaRouterCallback();


	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		switch (dialog.getTag()) {
		case "bestaetigenloeschen":
			loeschen();
			break;

		case "bestaetigenaktualisieren":
			aktualisieren();
			break;

		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}

	private void loeschen() {

		internetservice.Spielloeschen(
				"" + getIntent().getExtras().getInt(ErgebnisseFragment.KEY), ""
						+ dbuuid.getBenutzer().get_id());
		dbspiele.deleteSpiel(new Spiel(getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY)));
		finish();
	}

	private void aktualisieren() {
		/*
		 * Hier wird die Zahl(id) der Veranstaltung geholt
		 */

		Spiel updatespiel = dbspiele.getSpiel(getIntent().getExtras().getInt(
				ErgebnisseFragment.KEY));
		String spielstandheim = txfSpielstandHeim.getText().toString();
		String spielstandgast = txfSpielstandGast.getText().toString();
		String spielstatus = txfStatus.getText().toString();

		if (TextUtils.isEmpty(spielstandheim) == false) {
			updatespiel.setPunkteHeim(Integer.parseInt(spielstandheim));
		}
		if (TextUtils.isEmpty(spielstandgast) == false) {
			updatespiel.setPunkteGast(Integer.parseInt(spielstandgast));
		}
		if (TextUtils.isEmpty(spielstatus) == false) {
			updatespiel.setStatus(spielstatus);
		}
		if (checkspielistentschieden.isChecked()) {
			String spielendeString = "" + tpSpielende.getCurrentHour() + ":"
					+ tpSpielende.getCurrentMinute() + ":" + 30;
			updatespiel.setSpielende(spielendeString);
			updatespiel.setIstspielbeendet(1);

		}
		internetservice.updateSpiel("" + updatespiel.getPunkteHeim(), ""
				+ updatespiel.getPunkteGast(), updatespiel.getStatus(), ""
				+ updatespiel.getSpiel_id(), "" + updatespiel.getBenutzer_id(),
				"" + updatespiel.getIstspielbeendet(),
				updatespiel.getSpielende());
	}

	/**
	 * Bei dieser Methode wird der Wert des Parameters i dem Spielstand der
	 * Heimmannschaft der zu bearbeitenden Mannschaft addiert. Außerdem
	 * aktualisiert die Methode noch den Spielstand den sie als Hint ausgibt.
	 * 
	 * @param i
	 * @param spiel
	 */
	public void plusminusHeim(int i, Spiel spiel, int gewinnpunkte) {
		int uebergabe = spiel.getPunkteHeim();
		/**
		 * Die Gewinnpunkte sind dafür da das man nicht mehr Eingeben kann als das Spielsystem erlaubt.
		 */
		boolean freigabe = uebergabe < gewinnpunkte && i > 0 || uebergabe > 0 && i < 1;
		if (freigabe) {
			uebergabe = uebergabe + i;
			spiel.setPunkteHeim(uebergabe);
		}
		txfSpielstandHeim.setHint("Heim: " + spiel.getPunkteHeim());
		txfSpielstandGast.setHint("Gast: " + spiel.getPunkteGast());
		try {
			dbspiele.updateSpiel(spiel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Bei dieser Methode wird der Wert des Parameters i dem Spielstand der
	 * Gastmannschaft der zu bearbeitenden Mannschaft addiert. Außerdem
	 * aktualisiert die Methode noch den Spielstand den sie als Hint ausgibt.
	 * 
	 * @param i
	 * @param spiel
	 */
	public void plusminusGast(int i, Spiel spiel, int gewinnpunkte) {
		int uebergabe = spiel.getPunkteGast();
		/**
		 * Die Gewinnpunkte sind dafür da das man nicht mehr Eingeben kann als das Spielsystem erlaubt.
		 */
		boolean freigabe = uebergabe < gewinnpunkte && i > 0 || uebergabe > 0 && i < 1;
		if (freigabe) {
			uebergabe = uebergabe + i;
			spiel.setPunkteGast(uebergabe);
		}
		txfSpielstandHeim.setHint("Heim: " + spiel.getPunkteHeim());
		txfSpielstandGast.setHint("Gast: " + spiel.getPunkteGast());
		try {
			dbspiele.updateSpiel(spiel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.im_spiel, menu);
		MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
		MediaRouteActionProvider mediaRouteActionProvider
				= (MediaRouteActionProvider) MenuItemCompat
				.getActionProvider(mediaRouteMenuItem);
		// Set the MediaRouteActionProvider selector for device discovery.
		mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
		return true;
	}
	@Override
	protected void onStart() {
		super.onStart();
		// Start media router discovery
		mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
				MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
	}

	@Override
	protected void onStop() {
		// End media router discovery
		mMediaRouter.removeCallback(mMediaRouterCallback);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		teardown(true);
		super.onDestroy();
	}
	/**
	 * Callback for MediaRouter events
	 */
	private class MyMediaRouterCallback extends MediaRouter.Callback {

		@Override
		public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
			Log.d(TAG, "onRouteSelected");
			// Handle the user route selection.
			mSelectedDevice = CastDevice.getFromBundle(info.getExtras());

			launchReceiver();
		}

		@Override
		public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
			Log.d(TAG, "onRouteUnselected: info=" + info);
			teardown(false);
			mSelectedDevice = null;
		}
	}

	/**
	 * Start the receiver app
	 */
	private void launchReceiver() {
		try {
			mCastListener = new Cast.Listener() {

				@Override
				public void onApplicationDisconnected(int errorCode) {
					Log.d(TAG, "application has stopped");
					teardown(true);
				}

			};
			// Connect to Google Play services
			mConnectionCallbacks = new ConnectionCallbacks();
			mConnectionFailedListener = new ConnectionFailedListener();
			Cast.CastOptions.Builder apiOptionsBuilder = Cast.CastOptions
					.builder(mSelectedDevice, mCastListener);
			mApiClient = new GoogleApiClient.Builder(this)
					.addApi(Cast.API, apiOptionsBuilder.build())
					.addConnectionCallbacks(mConnectionCallbacks)
					.addOnConnectionFailedListener(mConnectionFailedListener)
					.build();

			mApiClient.connect();
		} catch (Exception e) {
			Log.e(TAG, "Failed launchReceiver", e);
		}
	}

	/**
	 * Google Play services callbacks
	 */
	private class ConnectionCallbacks implements
			GoogleApiClient.ConnectionCallbacks {

		@Override
		public void onConnected(Bundle connectionHint) {
			Log.d(TAG, "onConnected");

			if (mApiClient == null) {
				// We got disconnected while this runnable was pending
				// execution.
				return;
			}

			try {
				if (mWaitingForReconnect) {
					mWaitingForReconnect = false;

					// Check if the receiver app is still running
					if ((connectionHint != null)
							&& connectionHint.getBoolean(Cast.EXTRA_APP_NO_LONGER_RUNNING)) {
						Log.d(TAG, "App  is no longer running");
						teardown(true);
					} else {
						// Re-create the custom message channel
						try {
							Cast.CastApi.setMessageReceivedCallbacks(
									mApiClient,
									mHelloWorldChannel.getNamespace(),
									mHelloWorldChannel);
						} catch (IOException e) {
							Log.e(TAG, "Exception while creating channel", e);
						}
					}
				} else {
					// Launch the receiver app
					Cast.CastApi.launchApplication(mApiClient, getString(R.string.app_id), false)
							.setResultCallback(
									new ResultCallback<Cast.ApplicationConnectionResult>() {
										@Override
										public void onResult(
												Cast.ApplicationConnectionResult result) {
											Status status = result.getStatus();
											Log.d(TAG,
													"ApplicationConnectionResultCallback.onResult:"
															+ status.getStatusCode());
											if (status.isSuccess()) {
												ApplicationMetadata applicationMetadata = result
														.getApplicationMetadata();
												mSessionId = result.getSessionId();
												String applicationStatus = result
														.getApplicationStatus();
												boolean wasLaunched = result.getWasLaunched();
												Log.d(TAG, "application name: "
														+ applicationMetadata.getName()
														+ ", status: " + applicationStatus
														+ ", sessionId: " + mSessionId
														+ ", wasLaunched: " + wasLaunched);
												mApplicationStarted = true;

												// Create the custom message
												// channel
												mHelloWorldChannel = new HelloWorldChannel();
												try {
													Cast.CastApi.setMessageReceivedCallbacks(
															mApiClient,
															mHelloWorldChannel.getNamespace(),
															mHelloWorldChannel);
												} catch (IOException e) {
													Log.e(TAG, "Exception while creating channel",
															e);
												}

												// set the initial instructions
												// on the receiver
												try {
													sendMessage(uebergabespiel.toJSon());
												} catch (JSONException e) {
													e.printStackTrace();
												}
											} else {
												Log.e(TAG, "application could not launch");
												teardown(true);
											}
										}
									});
				}
			} catch (Exception e) {
				Log.e(TAG, "Failed to launch application", e);
			}
		}

		@Override
		public void onConnectionSuspended(int cause) {
			Log.d(TAG, "onConnectionSuspended");
			mWaitingForReconnect = true;
		}
	}

	/**
	 * Google Play services callbacks
	 */
	private class ConnectionFailedListener implements
			GoogleApiClient.OnConnectionFailedListener {

		@Override
		public void onConnectionFailed(ConnectionResult result) {
			Log.e(TAG, "onConnectionFailed ");

			teardown(false);
		}
	}

	/**
	 * Tear down the connection to the receiver
	 */
	private void teardown(boolean selectDefaultRoute) {
		Log.d(TAG, "teardown");
		if (mApiClient != null) {
			if (mApplicationStarted) {
				if (mApiClient.isConnected() || mApiClient.isConnecting()) {
					try {
						Cast.CastApi.stopApplication(mApiClient, mSessionId);
						if (mHelloWorldChannel != null) {
							Cast.CastApi.removeMessageReceivedCallbacks(
									mApiClient,
									mHelloWorldChannel.getNamespace());
							mHelloWorldChannel = null;
						}
					} catch (IOException e) {
						Log.e(TAG, "Exception while removing channel", e);
					}
					mApiClient.disconnect();
				}
				mApplicationStarted = false;
			}
			mApiClient = null;
		}
		if (selectDefaultRoute) {
			mMediaRouter.selectRoute(mMediaRouter.getDefaultRoute());
		}
		mSelectedDevice = null;
		mWaitingForReconnect = false;
		mSessionId = null;
	}

	/**
	 * Send a text message to the receiver
	 */
	private void sendMessage(String message) {
		if (mApiClient != null && mHelloWorldChannel != null) {
			try {
				Cast.CastApi.sendMessage(mApiClient,
						mHelloWorldChannel.getNamespace(), message).setResultCallback(
						new ResultCallback<Status>() {
							@Override
							public void onResult(Status result) {
								if (!result.isSuccess()) {
									Log.e(TAG, "Sending message failed");
								}
							}
						});
			} catch (Exception e) {
				Log.e(TAG, "Exception while sending message", e);
			}
		} else {
			Toast.makeText(ImSpielActivity.this, message, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Custom message channel
	 */
	class HelloWorldChannel implements Cast.MessageReceivedCallback {

		/**
		 * @return custom namespace
		 */
		public String getNamespace() {
			return getString(R.string.namespace);
		}

		/*
         * Receive message from the receiver app
         */
		@Override
		public void onMessageReceived(CastDevice castDevice, String namespace,
									  String message) {
			Log.d(TAG, "onMessageReceived: " + message);
		}

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.change_todetail) {
			Intent i = new Intent(ImSpielActivity.this, DetailActivity.class);
			i.putExtra(ErgebnisseFragment.KEY, uebergabespiel.getSpiel_id());
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Initialisieren der grafischen Elementen
	 */
	private void init() {
		txfSpielstandHeim = (EditText) findViewById(R.id.txfHeimmannschaftAktualisiere);
		txfSpielstandGast = (EditText) findViewById(R.id.txfGastmannschaftAktualisiere);
		txfStatus = (EditText) findViewById(R.id.txfAktualisiereStatus);
		btnaktualisieren = (Button) findViewById(R.id.btnaktualisieren);
		btnloeschen = (Button) findViewById(R.id.btnloeschen);
		btnheimplusein = (Button) findViewById(R.id.btnplus1heim);
		btngastpluseins = (Button) findViewById(R.id.btnplus1gast);
		btnheimminuseins = (Button) findViewById(R.id.btnminus1heim);
		btngastminuseins = (Button) findViewById(R.id.btnminus1gast);
		switch_scoreboard = (Switch) findViewById(R.id.switch_scoreboard);
		/**
		 * Für Spätere implementation
		 */
		switch_scoreboard.setVisibility(View.GONE);

		statusScoreboard = (TextView) findViewById(R.id.statusscoreboard);
		btnzurueck = (Button) findViewById(R.id.btn_zurueck);
		checkspielistentschieden = (CheckBox) findViewById(R.id.check_Spielistentschieden);
		etxtspielende = (TextView) findViewById(R.id.textView_Spielentschieden);
		etxtspielende.setVisibility(View.GONE);
		tpSpielende = (TimePicker) findViewById(R.id.time_Spielende);
		tpSpielende.setIs24HourView(true);
		tpSpielende.setVisibility(View.GONE);
	}

	/*
	 * public class AlertFragmentNotify extends DialogFragment {
	 * 
	 * @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
	 * AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 * builder.setMessage(
	 * "Funktion benötigt eine bestehende Internetverbinung")
	 * .setTitle("Warnung !") .setIcon(R.drawable.ic_launcher)
	 * 
	 * .setNeutralButton("Ok", new DialogInterface.OnClickListener() { public
	 * void onClick(DialogInterface dialog, int id) {
	 * 
	 * } }); return builder.create(); } }
	 */

}