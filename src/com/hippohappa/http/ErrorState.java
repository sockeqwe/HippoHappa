package com.hippohappa.http;

import java.io.UnsupportedEncodingException;

import com.hannesdorfmann.httpkit.exception.HttpExceptionChecker;

/**
 * This class indicates which kind of error has occurred while retrieving data
 * from the webserver
 * 
 * @author Hannes Dorfmann
 * 
 */
public enum ErrorState {

	/**
	 * Indicates that the Internet connection has been lost, while trying to
	 * connect to the server or the server is down and can therefore not deliver
	 * a valid response
	 */
	SERVER_NOT_REACHABLE,

	/**
	 * Indicates that the users device does not support the required encoding
	 * for sending URLEncoded requests. Therefore the app is not useable
	 */
	UNSUPPORTED_ENCODING,

	/**
	 * Indicates that the server could not authenticate the device (http
	 * response status code 403) and therefore the response for a http request
	 * is not delivered.
	 */
	SERVER_AUTHENTICATION,

	/**
	 * Indicates that the users device has no active internet connection
	 */
	NO_INTERNET,

	/**
	 * Indicates that the server has returned an unexpected http status code
	 */
	UNEXPECTED_STATUS_CODE,

	/**
	 * Indicates that the users device has no acceleration sensor
	 */
	NO_ACCELERATION_SENSOR,

	/**
	 * Indicates that the error reason is unknown (or none of the other
	 * {@link ErrorState}s)
	 */
	UNKNOWN;

	/**
	 * Determines the {@link ErrorState} from the exceptions type
	 * 
	 * @param e
	 *            The exception
	 * @return The {@link ErrorState}
	 */
	public static ErrorState from(Exception e) {

		if (HttpExceptionChecker.isServerNotReachable(e))
			return SERVER_NOT_REACHABLE;

		if (HttpExceptionChecker.isNoInternetConnection(e))
			return NO_INTERNET;

		if (e instanceof UnsupportedEncodingException)
			return UNSUPPORTED_ENCODING;

		if (HttpExceptionChecker.isUnexpectesStatusCode(e))
			return UNEXPECTED_STATUS_CODE;

		return UNKNOWN;

	}
}
