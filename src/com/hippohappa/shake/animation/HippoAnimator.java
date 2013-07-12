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

		private final ImageView hippo;

		private final AnimatorSet animatorSet;

		private ShakeAnimator(ImageView hippo) {
			animatorSet = new AnimatorSet();
			this.hippo = hippo;

			ObjectAnimator left = ObjectAnimator.ofFloat(hippo, "translationX",
					0, -20);
			ObjectAnimator right = ObjectAnimator.ofFloat(hippo,
					"translationX", 0, 20);

			ObjectAnimator up = ObjectAnimator.ofFloat(hippo, "translationY",
					0, -10);
			ObjectAnimator down = ObjectAnimator.ofFloat(hippo, "translationY",
					0, -10);

			ObjectAnimator middleX = ObjectAnimator.ofFloat(hippo,
					"translationX", -20, 0);
			ObjectAnimator middleY = ObjectAnimator.ofFloat(hippo,
					"translationY", -10, 0);

			ObjectAnimator middleX2 = ObjectAnimator.ofFloat(hippo,
					"translationX", 20, 0);
			ObjectAnimator middleY2 = ObjectAnimator.ofFloat(hippo,
					"translationY", 10, 0);

			left.setRepeatMode(ObjectAnimator.INFINITE);
			right.setRepeatMode(ObjectAnimator.INFINITE);
			up.setRepeatMode(ObjectAnimator.INFINITE);
			down.setRepeatMode(ObjectAnimator.INFINITE);

			AnimatorSet leftUp = new AnimatorSet();
			leftUp.playTogether(left, up);

			AnimatorSet backMiddle = new AnimatorSet();
			backMiddle.playTogether(middleX, middleY);

			AnimatorSet rightDown = new AnimatorSet();
			rightDown.playTogether(right, down);

			AnimatorSet backMiddle2 = new AnimatorSet();
			backMiddle2.playTogether(middleX2, middleY2);

			animatorSet.playSequentially(left, middleX, right, middleX2);

			animatorSet.setDuration(currentDuration);

			animatorSet.addListener(new AnimListener() {

				@Override
				public void onAnimationEnd(Animator arg0) {

					if (!changedByAcceleration)
						currentDuration *= 1.25;

					changedByAcceleration = false;

					animatorSet.setDuration(currentDuration);
					animatorSet.start();
				}
			});

		}

		public void start() {
			animatorSet.start();
		}

		public void cancel() {
			animatorSet.cancel();
		}

		public boolean isRunning() {
			return animatorSet.isRunning();
		}
	}

	private final ImageView hippo;
	private float lastShakeAcceleration = 0;
	private ShakeAnimator animator;
	private int currentDuration;
	private boolean changedByAcceleration;

	public HippoAnimator(ImageView hippo) {
		this.hippo = hippo;
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
			calculateDurationTime();

		Log.d("Test", "Shake");
		lastShakeAcceleration = acceleration;

		if (animator == null) {
			animator = new ShakeAnimator(hippo);
			animator.start();
		}
	}

	public void stop() {
		animator.cancel();
		animator = null;
		hippo.clearAnimation();
		lastShakeAcceleration = 0;
		ViewHelper.setTranslationX(hippo, 0);
		ViewHelper.setTranslationY(hippo, 0);

	}

	public void start() {
		animator.start();
	}

	private void calculateDurationTime() {
		currentDuration = (int) (1000 / lastShakeAcceleration);
		changedByAcceleration = true;
	}

}
