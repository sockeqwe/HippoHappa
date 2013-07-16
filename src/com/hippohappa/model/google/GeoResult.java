package com.hippohappa.model.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a place result. Contains the title {@link #getTitle()} and the
 * longitude and latitue
 * 
 * @author Hannes Dorfmann
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoResult {

	@JsonProperty("formatted_address")
	private String title;

	@JsonProperty("geometry")
	private GeoGeometry geometry;

	public GeoResult() {

	}

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
		return geometry.getLocation().getLng();
	}

	/**
	 * Get the latitude
	 * 
	 * @return
	 */
	public double getLatitude() {
		return geometry.getLocation().getLat();
	}

}
