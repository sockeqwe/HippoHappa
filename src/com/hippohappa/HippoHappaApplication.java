package com.hippohappa;

import java.io.File;
import java.io.IOException;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.hannesdorfmann.httpkit.DefaultHttpKit;
import com.hannesdorfmann.httpkit.HttpKit;
import com.hannesdorfmann.httpkit.HttpKitLogger;
import com.hannesdorfmann.httpkit.cache.Cache;
import com.hannesdorfmann.httpkit.cache.HybridCache;
import com.hannesdorfmann.httpkit.parser.ImageParserWriter;
import com.hannesdorfmann.httpkit.parser.JsonParserWriter;
import com.hannesdorfmann.httpkit.parser.MimeType;

/**
 * This is the {@link Application} that holds all important data references like
 * the {@link Cache} and the {@link HttpKit}
 * 
 * @author Hannes Dorfmann
 * 
 */
public class HippoHappaApplication extends Application {

	private HttpKit httpKit;
	private HybridCache<String, Object> cache;

	@Override
	public void onCreate() {
		super.onCreate();

		initHttpKit();
		try {
			cache.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		cache.clearMemoryCache();
	}

	public HttpKit getHttpKit() {
		return httpKit;
	}

	/**
	 * Initializes the http kit, cache etc.
	 */
	private void initHttpKit() {

		// Setup the cache
		File diskCachePath;
		diskCachePath = getExternalCacheDir();
		if (diskCachePath == null)
			diskCachePath = getCacheDir();

		cache = new HybridCache<String, Object>(400, 10 * 1024, diskCachePath,
				getAppVersion());

		// Setup the HttpKit
		httpKit = new DefaultHttpKit(getApplicationContext());
		httpKit.setCache(cache);

		// TODO remove debbuging
		HttpKitLogger.setLogging(true);

		// Setup the parsers
		ImageParserWriter imageParser = new ImageParserWriter();
		JsonParserWriter jsonParser = new JsonParserWriter();
		httpKit.addParserWriter(MimeType.Application.JSON, jsonParser);
		httpKit.addParserWriter(MimeType.Image.JPEG, imageParser);
		httpKit.addParserWriter(MimeType.Image.JPG, imageParser);
		httpKit.addParserWriter(MimeType.Image.PNG, imageParser);
	}

	/**
	 * Get the app version code number as specified in the android manifest
	 * 
	 * @return
	 */
	public int getAppVersion() {

		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return pInfo.versionCode;

		} catch (NameNotFoundException e) {
			// This should never be reached
			e.printStackTrace();
			return -1;
		}
	}

}
