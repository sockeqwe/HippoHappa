package com.hippohappa.model.foursquare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {

	@JsonProperty("address")
	private String address;

	@JsonProperty("lat")
	private double latitude;

	@JsonProperty("lng")
	private double longitude;

	@JsonProperty("city")
	private String city;

	@JsonProperty("country")
	private String country;

	@JsonProperty("distance")
	private int distance;

	public int getDistance() {
		return distance;
	}

	public String getAddress() {
		return address;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}
}
