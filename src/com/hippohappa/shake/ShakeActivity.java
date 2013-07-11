package com.hippohappa.shake;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hippohappa.BaseActivity;
import com.hippohappa.R;
import com.hippohappa.exception.NoSensorException;
import com.hippohappa.model.foursquare.Item;

/**
 * This is the activiy where the user has to shake to get a list of restaurants
 * 
 * @author Hannes Dorfmann
 * 
 */
public class ShakeActivity extends BaseActivity implements ShakeView {

	private ImageView hippo;

	private TextView shakeHint;

	private Item randomItem;
	private boolean itemLoaded;

	private ShakePresenter presenter;
	private HippoAnimator hippoAnimator;

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

			checkAccelaration(mAccel);
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

		findHappa(49.016674, 12.095343);
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
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(sensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(sensorListener);
		super.onPause();
	}

	/**
	 * Checks the accelleration an starts the animation if needed
	 * 
	 * @param acceleration
	 */
	private void checkAccelaration(double acceleration) {
		if (acceleration > 12) {
			hippoAnimator.showHippoSick();
		}
	}

	@Override
	public void setItem(Item randomVenue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showError(Exception e) {
		// TODO Auto-generated method stub

	}

}
