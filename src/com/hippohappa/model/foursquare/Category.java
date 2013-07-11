package com.hippohappa.model.foursquare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
