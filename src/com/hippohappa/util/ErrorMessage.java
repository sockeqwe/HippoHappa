package com.hippohappa.util;

import com.hippohappa.R;
import com.hippohappa.http.ErrorState;

/**
 * Used to write an error message
 * 
 * @author Hannes Dorfmann
 * 
 */
public class ErrorMessage {

	/**
	 * Get the error message as int resource to load it from the resources
	 * 
	 * @param e
	 * @return
	 */
	public static int from(ErrorState e) {
		switch (e) {
		case NO_INTERNET:
			return R.string.error_no_internet;

		case UNSUPPORTED_ENCODING:
			return R.string.error_unsupported_encodind;

		case NO_ACCELERATION_SENSOR:
			return R.string.error_no_acceleration_sensor;

		case SERVER_NOT_REACHABLE:
		case UNEXPECTED_STATUS_CODE:
		case UNKNOWN:
		default:
			return R.string.error_general;
		}
	}
}
