package com.hippohappa.shake;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.hannesdorfmann.httpkit.HttpKit;
import com.hannesdorfmann.httpkit.exception.UnexpectedHttpStatusCodeException;
import com.hannesdorfmann.httpkit.mvp.HttpPresenter;
import com.hannesdorfmann.httpkit.request.HttpGetRequest;
import com.hannesdorfmann.httpkit.request.HttpRequest;
import com.hannesdorfmann.httpkit.response.HttpResponse;
import com.hannesdorfmann.httpkit.response.HttpResponseReceiver;
import com.hippohappa.http.ErrorState;
import com.hippohappa.http.RequestFactory;
import com.hippohappa.model.foursquare.FoursquareResponse;
import com.hippohappa.model.foursquare.Item;
import com.hippohappa.model.google.GeocodingResult;

/**
 * This is the presenter for the {@link ShakeView}. Its responsible to load the
 * list of restaurants and to pick a random one
 * 
 * @author Hannes Dorfmann
 * 
 */
public class ShakePresenter extends HttpPresenter<ShakeView> {

	private HttpRequest currentRequest;

	public ShakePresenter(ShakeView view, HttpKit httpKit) {
		super(view, httpKit);
	}

	/**
	 * Executes the http request to get a list of restaurants at the given
	 * position
	 * 
	 * @param latitude
	 * @param longitude
	 * @throws UnsupportedEncodingException
	 */
	public void findHappa(double latitude, double longitude)
			throws UnsupportedEncodingException {

		HttpGetRequest request = RequestFactory.getHappaRequest(latitude,
				longitude);
		request.setOwner(this);
		currentRequest = request;
		httpKit.execute(request,
				new HttpResponseReceiver<FoursquareResponse>() {

					@Override
					public void onFailure(HttpRequest req, Exception arg1) {
						if (view != null && currentRequest == req)
							view.showHappaError(ErrorState.from(arg1));

					}

					@Override
					public void onSuccess(HttpResponse<FoursquareResponse> res) {
						if (view != null
								&& currentRequest == res.getHttpRequest()) {

							if (res.isResponseOkOrNotModified()) {
								List<Item> items = res.getValue()
										.getItemsList();

								if (items == null)
									view.setItem(null);
								else {
								}

							} else
								onFailure(res.getHttpRequest(),
										new UnexpectedHttpStatusCodeException(
												res));
						}
					}
				});
	}

	/**
	 * Load a list of places with
	 * 
	 * @param search
	 * @throws UnsupportedEncodingException
	 */
	public void loadPlaces(String search) throws UnsupportedEncodingException {
		HttpGetRequest r = RequestFactory.getGeoCodingByAdress(search);

		httpKit.execute(r, new HttpResponseReceiver<GeocodingResult>() {

			@Override
			public void onFailure(HttpRequest req, Exception e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(HttpResponse<GeocodingResult> res) {

			}
		});

	}
}
