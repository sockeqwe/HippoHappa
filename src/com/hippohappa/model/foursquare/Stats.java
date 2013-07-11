package com.hippohappa.model.foursquare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

	@JsonProperty("checkinsCount")
	private int checkinsCount;

	@JsonProperty("usersCount")
	private int usersCount;

	@JsonProperty("tipCount")
	private int tipCount;

	public int getCheckinsCount() {
		return checkinsCount;
	}

	public int getUsersCount() {
		return usersCount;
	}

	public int getTipCount() {
		return tipCount;
	}

}
