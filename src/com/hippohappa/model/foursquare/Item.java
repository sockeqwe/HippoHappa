package com.hippohappa.model.foursquare;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

	@JsonProperty("tips")
	private List<Tipp> tips;

	@JsonProperty("venue")
	private Venue venue;

}
