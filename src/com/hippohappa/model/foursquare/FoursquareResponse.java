package com.hippohappa.model.foursquare;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class Response {

	@JsonProperty("groups")
	private List<Group> groups;

	public List<Group> getGroups() {
		return groups;
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquareResponse {

	@JsonProperty("response")
	private Response response;

	/**
	 * Get the list of items
	 * 
	 * @return
	 */
	public List<Item> getItemsList() {
		if (response == null)
			return null;

		if (response.getGroups() == null)
			return null;

		if (response.getGroups().get(0) == null)
			return null;

		return response.getGroups().get(0).getItems();
	}
}
