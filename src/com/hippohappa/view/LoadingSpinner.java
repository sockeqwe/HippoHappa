package com.hippohappa.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hippohappa.R;

public class LoadingSpinner extends ImageView {

	private Drawable toRotateDrawable;

	public LoadingSpinner(Context context) {
		super(context);
		init();
	}

	public LoadingSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		this.setImageResource(R.drawable.loading_spinner);
		LayerDrawable drawable = (LayerDrawable) getDrawable();
		toRotateDrawable = drawable.getDrawable(0);

	}

}
