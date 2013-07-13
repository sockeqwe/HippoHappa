package com.hippohappa.shake;

import com.hippohappa.model.foursquare.Item;

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
	public void setItem(Item randomItem);

	/**
	 * Shows an error message to inform the user that something went wrong
	 */
	public void showHappaError(Exception e);

}
