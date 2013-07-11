package com.hippohappa.model.foursquare;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Venue {

	@JsonProperty("name")
	private String name;

	@JsonProperty("location")
	private Location location;

	@JsonProperty("categories")
	private List<Category> categories;

	@JsonProperty("stats")
	private Stats stats;

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public Stats getStats() {
		return stats;
	}

}
