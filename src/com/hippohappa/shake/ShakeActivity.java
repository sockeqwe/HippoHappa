package com.hippohappa.shake;

import java.io.UnsupportedEncodingException;

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
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.hippohappa.BaseActivity;
import com.hippohappa.R;
import com.hippohappa.exception.NoSensorException;
import com.hippohappa.model.foursquare.Item;
import com.hippohappa.shake.animation.HippoAnimator;

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

	private ImageView hippo;

	private TextView shakeHint;

	private Item randomItem;
	private boolean itemLoaded;

	private LocationClient locationClient;
	private Location currentLocation;
	private boolean locationClientReconnectOnDisconnect;

	private ShakePresenter presenter;
	private HippoAnimator hippoAnimator;

	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float accelCurrent; // current acceleration including gravity
	private float accelLast; // last acceleration including gravity

	private boolean showingSearchField = false;

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

		hippo = (ImageView) findViewById(R.id.hippo);
		shakeHint = (TextView) findViewById(R.id.shakeHint);

		hippoAnimator = new HippoAnimator(hippo);

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
			showError(new NoSensorException());
		}

		presenter = new ShakePresenter(this, getHttpKit());

		locationClient = new LocationClient(this, this, this);
	}

	private void findHappa(double latitude, double longitude) {
		try {
			presenter.findHappa(49.016674, 12.095343);
		} catch (UnsupportedEncodingException e) {
			showError(e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		presenter.onDestroy(presenter);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Connect the client.
		locationClientReconnectOnDisconnect = true;
		locationClient.connect();
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
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(sensorListener);
		hippoAnimator.stop();
		super.onPause();

	}

	/**
	 * Checks the accelleration an starts the animation if needed
	 * 
	 * @param acceleration
	 */
	private void checkAccelaration(float acceleration) {

		if (acceleration > 12) {
			hippoAnimator.setShakeAcceleration(acceleration);
		}
	}

	@Override
	public void setItem(Item randomVenue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showError(Exception e) {

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

		Toast.makeText(this, R.string.error_location_manager_no_location,
				TOAST_DURATION).show();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		getCurrentLocationFromPlayService();
	}

	@Override
	public void onDisconnected() {
		if (locationClientReconnectOnDisconnect)
			locationClient.connect();
	}

	private void getCurrentLocationFromPlayService() {
		currentLocation = locationClient.getLastLocation();
		String info = "CONNECTED " + currentLocation.getLatitude() + " "
				+ currentLocation.getLongitude();
		Toast.makeText(this, info, TOAST_DURATION).show();

		Log.d("Test", info);

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
			else
				onLocationCouldNotBeDetected();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.shake, (com.actionbarsherlock.view.Menu) menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.actionSearch:
			showSearchFieldInActionBar();
			return true;
		}
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		menu.findItem(R.id.actionSearch).setVisible(!showingSearchField);
		menu.findItem(R.id.actionSettings).setVisible(!showingSearchField);

		return true;
	}

	/**
	 * shows the searchfield in the actionbar
	 */
	private void showSearchFieldInActionBar() {
		showingSearchField = true;
		supportInvalidateOptionsMenu();
	}

	/**
	 * Shows the normal actionbar
	 */
	private void hideSearchFieledInActionBar() {
		showingSearchField = false;
		supportInvalidateOptionsMenu();
	}

	@Override
	public void onBackPressed() {
		if (showingSearchField)
			hideSearchFieledInActionBar();
		else
			super.onBackPressed();
	}
}
