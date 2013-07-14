package com.hippohappa.shake;

import java.util.List;

import com.hippohappa.http.ErrorState;
import com.hippohappa.model.foursquare.Item;
import com.hippohappa.model.google.GeocodingResult.GeoResult;

/**
 * 
 * @author Hannes Dorfmann
 * 
 */
public interface ShakeView {

	/**
	 * Set the randomly selected {@link Item}
	 * 
	 * @param randomVenue
	 */
	public void setItem(List<Item> randomItems);

	/**
	 * Shows an error message to inform the user that something went wrong
	 */
	public void showHappaError(ErrorState e);

	/**
	 * Set the list of geocoding result
	 * 
	 * @param result
	 */
	public void setGeocodingResut(List<GeoResult> result);

	/**
	 * Set the error that has been occurred while retrieving geo coding results
	 * 
	 * @param e
	 */
	public void showGeocodingError(ErrorState e);

	/**
	 * Shows the hippo components
	 */
	public void showHippo();

	/**
	 * Shows the Geocoding list result
	 */
	public void showGeocodingList();

}
