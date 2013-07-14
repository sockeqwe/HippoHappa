package com.hippohappa.shake.animation;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hippohappa.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Animates (shakes) the hippo and reactes on shakin acceleration
 * 
 * @author Hannes Dorfmann
 * 
 */
public class HippoAnimator {

	private class ShakeAnimator {

		private AnimatorSet animatorSet;

		public void start(ImageView hippo) {
			animatorSet = new AnimatorSet();

			ObjectAnimator left = ObjectAnimator.ofFloat(hippo, "translationX",
					0, -20);
			ObjectAnimator right = ObjectAnimator.ofFloat(hippo,
					"translationX", 0, 20);

			ObjectAnimator middleX = ObjectAnimator.ofFloat(hippo,
					"translationX", -20, 0);

			ObjectAnimator middleX2 = ObjectAnimator.ofFloat(hippo,
					"translationX", 20, 0);

			animatorSet.addListener(new AnimListener() {

				@Override
				public void onAnimationStart(Animator a) {
					Log.d("Test", "started " + currentDuration);
				}

				@Override
				public void onAnimationEnd(Animator arg0) {

					currentDuration = (int) (currentDuration * 1.1 + 0.5);

					animatorSet.setDuration(currentDuration);
					animatorSet.start();
				}
			});

			animatorSet.playSequentially(left, middleX, right, middleX2);

			animatorSet.setDuration(currentDuration);
			animatorSet.start();
		}

		public void cancel() {
			if (animatorSet != null)
				animatorSet.cancel();
		}

		public boolean isRunning() {
			return animatorSet == null ? false : animatorSet.isRunning();
		}

		public void adjustDuration(int newDuration) {
			if (animatorSet != null)
				animatorSet.setDuration(newDuration);
		}
	}

	private final ImageView hippo;
	private final TextView textView;
	private float lastShakeAcceleration = 0;
	private final ShakeAnimator animator;
	private int currentDuration = 10;

	public HippoAnimator(ImageView hippo, TextView textView) {
		this.hippo = hippo;
		this.textView = textView;
		animator = new ShakeAnimator();
	}

	/**
	 * Should be called when the hippo can be shaken
	 */
	public void showHippoReadyForShaking() {
		setViews(R.drawable.hippo_smiling_center, R.string.hippo_shake_phone);
	}

	/**
	 * This should be called, right before the vomit animation starts
	 */
	public void showHippoBeforeVomit() {
		setViews(R.drawable.hippo_open_mouth, R.string.hippo_ready_to_vomit);
	}

	/**
	 * This should be called, after the hippo has been shaken a little bit and
	 * before vomitting
	 */
	public void showHippoSick() {
		setViews(R.drawable.hippo_sick, R.string.hippo_is_sick);
	}

	/**
	 * This should be called while the users location will be retrieved
	 */
	public void showHippoLookingMap() {
		setViews(R.drawable.hippo_looking_map, R.string.hippo_shake_phone);
	}

	/**
	 * Load a string from resources
	 * 
	 * @param resId
	 * @return
	 */
	private String getString(int resId) {

		return textView.getContext().getString(resId);
	}

	public void setShakeAcceleration(float acceleration) {

		if (acceleration > lastShakeAcceleration)
			currentDuration = (int) Math.min(10, 50 - acceleration + 0.5);

		lastShakeAcceleration = acceleration;

		if (!animator.isRunning()) {
			animator.start(hippo);
		} else
			animator.adjustDuration(currentDuration);
	}

	public void stop() {
		animator.cancel();
		hippo.clearAnimation();
		lastShakeAcceleration = 0;
		ViewHelper.setTranslationX(hippo, 0);
		ViewHelper.setTranslationY(hippo, 0);

	}

	/**
	 * Shows the hippo
	 */
	public void showHippoCouldNotDetectLocation() {
		setViews(R.drawable.hippo_looking_map,
				R.string.error_location_manager_no_location);
	}

	private void setViews(int hippoImageRes, int stringRes) {
		hippo.setImageResource(hippoImageRes);
		textView.setText(getString(stringRes));
	}

	public void setVisible() {
		hippo.setVisibility(View.VISIBLE);
		textView.setVisibility(View.VISIBLE);
	}

	public void setVisibilityGone() {
		hippo.setVisibility(View.GONE);
		textView.setVisibility(View.GONE);
		stop();
	}

	public boolean isVisible() {
		return hippo.isShown();
	}
}
