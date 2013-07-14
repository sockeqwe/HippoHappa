package com.hippohappa.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.RotateAnimation;

import com.hippohappa.R;

public class LoadingView {

	private final View loadingView;
	private final ViewGroup container;
	private RotateAnimation rotateAnim;
	private int loadingViewWidth;
	private int loadingViewHeight;

	public LoadingView(ViewGroup loadingContainer) {
		this.container = loadingContainer;
		loadingView = container.findViewById(R.id.loadingViewImage);
		loadingView.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						loadingView.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						loadingViewWidth = loadingView.getMeasuredWidth();
						loadingViewHeight = loadingView.getMeasuredHeight();

						rotateAnim = new RotateAnimation(0, 360,
								loadingViewWidth / 2, loadingViewHeight / 2);
						rotateAnim.setRepeatMode(RotateAnimation.INFINITE);
						rotateAnim.setDuration(500);

						loadingView.startAnimation(rotateAnim);
						return true;
					}
				});
	}

	public void setVisible() {
		container.setVisibility(View.VISIBLE);
		if (rotateAnim != null) {
			rotateAnim.reset();
			loadingView.startAnimation(rotateAnim);
		}
	}

	public void setVisibilityGone() {
		container.setVisibility(View.GONE);
		if (rotateAnim != null)
			rotateAnim.cancel();
	}

}
