package com.hippohappa.model.foursquare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tipp {

	@JsonProperty("created")
	private long created;

	@JsonProperty("text")
	private String text;

	public long getCreated() {
		return created;
	}

	public String getText() {
		return text;
	}

}
