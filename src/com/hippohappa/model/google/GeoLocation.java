package com.hippohappa.model.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains the longitude and latitude
 * 
 * @author Hannes Dorfmann
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoLocation {

	@JsonProperty("lat")
	private double lat;

	@JsonProperty("lng")
	private double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
}
