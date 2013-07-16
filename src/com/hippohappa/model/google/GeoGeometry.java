package com.hippohappa.model.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A little wrapper class that wraps the gemoentry data, retrieved from google
 * geocoding (contains the longitude and latitude)
 * 
 * @author Hannes Dorfmann
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoGeometry {

	@JsonProperty("location")
	private GeoLocation location;

	public GeoLocation getLocation() {
		return location;
	}

}