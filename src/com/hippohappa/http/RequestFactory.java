package com.hippohappa.http;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import com.hannesdorfmann.httpkit.request.HttpGetRequest;
import com.hippohappa.model.foursquare.FoursquareResponse;

/**
 * A little factory to create http requests
 * 
 * @author Hannes Dorfmann
 * 
 */
public class RequestFactory {

	/**
	 * The basic request
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static HttpGetRequest getFoursquareRequest()
			throws UnsupportedEncodingException {
		String baseUrl = "https://api.foursquare.com/v2/venues/explore";

		HttpGetRequest r = new HttpGetRequest(baseUrl);
		r.putUrlParam("client_id", FoursquareApi.CLIENT_ID);
		r.putUrlParam("client_secret", FoursquareApi.CLIENT_SECRET);
		r.putUrlParam("limit", 35);

		r.getHttpHeaders().put("Accept-Language",
				Locale.getDefault().getCountry());

		// Settings
		r.setCacheResponse(true);
		r.setOnOfflineAllowExpiredCacheValue(true);
		r.setOnOfflineFromCache(true);

		r.setParseInto(FoursquareResponse.class);
		return r;
	}

	/**
	 * Find restaurants nearby the given location
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HttpGetRequest getHappaRequest(double latitude,
			double longitude) throws UnsupportedEncodingException {
		HttpGetRequest r = getFoursquareRequest();

		r.putUrlParam("ll",
				Double.toString(latitude) + "," + Double.toString(longitude));
		r.putUrlParam("section", "food");
		r.putUrlParam("radius", 500);

		return r;
	}

	/**
	 * Find restaurants near named place
	 * 
	 * @param cityName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static HttpGetRequest getHappaRequest(String cityName)
			throws UnsupportedEncodingException {
		HttpGetRequest r = getFoursquareRequest();

		r.putUrlParam("near", cityName);
		r.putUrlParam("section", "food");
		r.putUrlParam("radius", 500);

		return r;
	}
}
