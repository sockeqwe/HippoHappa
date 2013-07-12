package com.hippohappa.shake.animation;

import android.util.Log;
import android.widget.ImageView;

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
			animatorSet.cancel();
		}

		public boolean isRunning() {
			return animatorSet == null ? false : animatorSet.isRunning();
		}
	}

	private final ImageView hippo;
	private float lastShakeAcceleration = 0;
	private final ShakeAnimator animator;
	private int currentDuration = 10;

	public HippoAnimator(ImageView hippo) {
		this.hippo = hippo;
		animator = new ShakeAnimator();
	}

	public void showHippoSmiling() {
		hippo.setImageResource(R.drawable.hippo_smiling_center);
	}

	public void showHippoOpenMouth() {
		hippo.setImageResource(R.drawable.hippo_open_mouth);
	}

	public void showHippoSick() {
		hippo.setImageResource(R.drawable.hippo_sick);
	}

	public void setShakeAcceleration(float acceleration) {

		if (acceleration > lastShakeAcceleration)
			currentDuration = (int) Math.min(10, 50 - acceleration + 0.5);

		Log.d("Test", "Shake " + acceleration);
		lastShakeAcceleration = acceleration;

		if (!animator.isRunning()) {
			animator.start(hippo);
			Log.d("Test", "starting");
		}
	}

	public void stop() {
		animator.cancel();
		hippo.clearAnimation();
		lastShakeAcceleration = 0;
		ViewHelper.setTranslationX(hippo, 0);
		ViewHelper.setTranslationY(hippo, 0);

	}

}
