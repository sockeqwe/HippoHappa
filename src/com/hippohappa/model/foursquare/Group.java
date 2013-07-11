package com.hippohappa.model.foursquare;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

	@JsonProperty("items")
	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}

}
