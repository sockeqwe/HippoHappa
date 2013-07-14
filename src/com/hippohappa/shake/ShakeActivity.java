package com.hippohappa.shake;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.hippohappa.BaseActivity;
import com.hippohappa.R;
import com.hippohappa.http.ErrorState;
import com.hippohappa.model.foursquare.Item;
import com.hippohappa.model.google.GeocodingResult.GeoResult;
import com.hippohappa.shake.animation.HippoAnimator;
import com.hippohappa.util.ErrorMessage;

/**
 * This is the activiy where the user has to shake to get a list of restaurants
 * 
 * @author Hannes Dorfmann
 * 
 */
public class ShakeActivity extends BaseActivity implements ShakeView,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 23;

	private AdView adView;

	private ImageView hippoView;

	private MenuItem searchItem;
	private SearchView searchView;
	private TextView hintView;
	private TextView errorView;
	private ListView geocodingListView;

	private List<Item> items;
	private boolean itemLoaded;

	private GeocodingAdapter geoAdapter;

	private LocationClient locationClient;
	private Location currentLocation;
	private boolean locationClientReconnectOnDisconnect;
	private boolean locationClientError = false;

	private ShakePresenter presenter;
	private HippoAnimator hippo;

	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float accelCurrent; // current acceleration including gravity
	private float accelLast; // last acceleration including gravity

	private final SensorEventListener sensorListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			accelLast = accelCurrent;
			accelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = accelCurrent - accelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter

			checkAccelaration(Math.abs(mAccel));
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);

		// AdMob
		adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

		// Hippo
		hippoView = (ImageView) findViewById(R.id.hippo);
		hintView = (TextView) findViewById(R.id.hint);

		hippo = new HippoAnimator(hippoView, hintView);

		// Error Message
		errorView = (TextView) findViewById(R.id.errorMessage);
		errorView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				restoreViewAfterError();
			}
		});

		// ListVIew
		geocodingListView = (ListView) findViewById(R.id.geocodingList);
		geoAdapter = new GeocodingAdapter(getApplicationContext());
		geocodingListView.setAdapter(geoAdapter);

		// Init the sensor
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		if (mSensorManager != null) {
			mSensorManager.registerListener(sensorListener,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			mAccel = 0.00f;
			accelCurrent = SensorManager.GRAVITY_EARTH;
			accelLast = SensorManager.GRAVITY_EARTH;
		} else {
			showHappaError(ErrorState.NO_ACCELERATION_SENSOR);
		}

		presenter = new ShakePresenter(this, getHttpKit());

		locationClient = new LocationClient(this, this, this);
	}

	/**
	 * Restores the view after an error has occurred and the user has clicked on
	 * the retry button
	 */
	private void restoreViewAfterError() {
		errorView.setVisibility(View.GONE);

		if (currentLocation != null) {
			hippo.showHippoReadyForShaking();
		} else
			onLocationCouldNotBeDetected();

		hippo.setVisible();

	}

	/**
	 * Starts the request to find happa happa
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void findHappa() {
		try {
			presenter.findHappa(currentLocation.getLatitude(),
					currentLocation.getLongitude());
		} catch (UnsupportedEncodingException e) {
			showHappaError(ErrorState.UNEXPECTED_STATUS_CODE);
		}
	}

	/**
	 * This method is called to start the geo coding request
	 * 
	 * @param query
	 */
	private void findGeoLocation(String query) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		presenter.onDestroy(presenter);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {

		// Disconnecting the client invalidates it.
		locationClientReconnectOnDisconnect = false;
		locationClient.disconnect();
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(sensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

		if (currentLocation == null && !locationClientError)
			hippo.showHippoLookingMap();

		if (locationClientError)
			hippo.showHippoCouldNotDetectLocation();

		if (currentLocation != null)
			hippo.showHippoReadyForShaking();
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(sensorListener);
		hippo.stop();
		super.onPause();

	}

	/**
	 * Checks the accelleration an starts the animation if needed
	 * 
	 * @param acceleration
	 */
	private void checkAccelaration(float acceleration) {

		if (hippo.isVisible() && acceleration > 12) {
			hippo.setShakeAcceleration(acceleration);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			onLocationCouldNotBeDetected();
		}

	}

	/**
	 * Called, if the location could not be detected from the
	 * {@link LocationManager}
	 */
	private void onLocationCouldNotBeDetected() {

		hippo.showHippoCouldNotDetectLocation();
		// Expand to show the input field
		searchView.setIconified(false);
	}

	@Override
	public void onConnected(Bundle connectionHint) {

		currentLocation = locationClient.getLastLocation();
		if (currentLocation == null)
			onLocationCouldNotBeDetected();
		else {
			locationClientError = false;
			findHappa();
			hippo.showHippoReadyForShaking();

		}
	}

	@Override
	public void onDisconnected() {
		if (locationClientReconnectOnDisconnect)
			locationClient.connect();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */

			if (resultCode == Activity.RESULT_OK)
				locationClient.connect();
			else {
				locationClientError = true;
				onLocationCouldNotBeDetected();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.shake, (com.actionbarsherlock.view.Menu) menu);

		searchItem = menu.findItem(R.id.actionSearch);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setQueryHint(getString(R.string.search_hint));
		searchItem.setIcon(R.drawable.ic_action_search);

		searchView.setIconifiedByDefault(true);

		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				if (query.length() > 0 || !query.equals("")) {
					searchView.setIconified(true);
					findGeoLocation(query);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return true;
			}
		});
		// Connect the client.
		locationClientReconnectOnDisconnect = true;
		locationClient.connect();

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (!searchView.isIconified()) {
			searchView.setIconified(true);
		} else
			super.onBackPressed();
	}

	@Override
	public void setItem(List<Item> randomItems) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showHappaError(ErrorState e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGeocodingResut(List<GeoResult> results) {
		geoAdapter.setGeoResults(results);
		geoAdapter.notifyDataSetChanged();

	}

	@Override
	public void setGeocodingError(ErrorState e) {
		showError(e);
	}

	private void showError(ErrorState e) {
		boolean retry = true;
		if (e == ErrorState.NO_ACCELERATION_SENSOR
				|| e == ErrorState.NO_ACCELERATION_SENSOR)

			retry = false;

		String msg = getString(ErrorMessage.from(e));
		errorView.setText(msg);
		errorView.setClickable(retry);
	}

	@Override
	public void showHippo() {
		errorView.setVisibility(View.GONE);
		geocodingListView.setVisibility(View.GONE);
		hippo.setVisible();

	}

	@Override
	public void showGeocodingList() {
		hippo.setVisibilityGone();
		errorView.setVisibility(View.GONE);
		geocodingListView.setVisibility(View.VISIBLE);

	}

}
