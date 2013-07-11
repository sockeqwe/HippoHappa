package com.hippohappa;

import com.actionbarsherlock.app.SherlockActivity;
import com.hannesdorfmann.httpkit.HttpKit;

/**
 * The common base activity
 * 
 * @author Hannes Dorfmann
 * 
 */
public class BaseActivity extends SherlockActivity {

	protected HippoHappaApplication getApp() {
		return (HippoHappaApplication) getApplication();
	}

	protected HttpKit getHttpKit() {
		return getApp().getHttpKit();
	}

}
