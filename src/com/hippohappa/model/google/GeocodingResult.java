package com.hippohappa.model.google;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This the result, retrieved from google geocoding webservice
 * 
 * @author Hannes Dorfmann
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResult {

	/**
	 * Represents a place result. Contains the title {@link #getTitle()} and the
	 * longitude and latitue
	 * 
	 * @author Hannes Dorfmann
	 * 
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class GeoResult {

		/**
		 * A little wrapper class that wraps the gemoentry data, retrieved from
		 * google geocoding (contains the longitude and latitude)
		 * 
		 * @author Hannes Dorfmann
		 * 
		 */
		@JsonIgnoreProperties(ignoreUnknown = true)
		public class GeoGeometry {

			/**
			 * Contains the longitude and latitude
			 * 
			 * @author Hannes Dorfmann
			 * 
			 */
			@JsonIgnoreProperties(ignoreUnknown = true)
			public class GeoLocation {

				private double lat;
				private double lng;
			}

			private GeoLocation location;

		}

		@JsonProperty(value = "formatted_address")
		private String title;

		private GeoGeometry geometry;

		/**
		 * Get the title, or better, the name of the Result
		 * 
		 * @return
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Get the longitude
		 * 
		 * @return
		 */
		public double getLongitude() {
			return geometry.location.lng;
		}

		/**
		 * Get the latitude
		 * 
		 * @return
		 */
		public double getLatitude() {
			return geometry.location.lat;
		}

	}

	/**
	 * Google indicates that the result is ok and can be used
	 */
	private static final String STATUS_OK = "OK";

	/**
	 * Google indicates that the answer is ok, but contains an empty list as
	 * result, because the name was not found
	 */
	private static final String STATUS_ZERO_RESULTS = "ZERO_RESULTS";

	/**
	 * Google indicates that no valid answer could be delivered, because the
	 * daily quota is exceeded
	 */
	private static final String STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";

	/**
	 * Google indicates, that the request has been denied. Maybe the sensor
	 * parameter is missing
	 */
	private static final String STATUS_REQUEST_DENIED = "REQUEST_DENIED";

	/**
	 * Google indicates, that the request has been denied, because address
	 * parameter is missing
	 */
	private static final String STATUS_INVALID_REQUEST = "INVALID_REQUEST";

	private List<GeoResult> results;
	private String status;

	public List<GeoResult> getGeoResults() {
		return results;
	}

	/**
	 * 
	 * @return true if the request has returned a valid (could also be an empty
	 *         list) result
	 */
	public boolean isValid() {
		if (status == null)
			return false;

		return status.equals(STATUS_OK) || status.equals(STATUS_ZERO_RESULTS);
	}

	/**
	 * 
	 * @return true, if the daily query limit has been exceeded, so the google
	 *         does not deliver results anymore
	 */
	public boolean isQueryLimitExceeded() {
		return status.equals(STATUS_OVER_QUERY_LIMIT);
	}

	/**
	 * 
	 * @return true, if the url was not correct. Maybe some required parameters
	 *         are missing
	 */
	public boolean isNotValidCauseError() {
		return status == null || status.equals(STATUS_REQUEST_DENIED)
				|| status.equals(STATUS_INVALID_REQUEST);
	}

}
