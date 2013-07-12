package com.hippohappa.shake.animation;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

/**
 * A {@link AnimatorListener} that gets informed when an Animation gets repeated
 * 
 * @author Hannes Dorfmann
 * 
 */
public abstract class AnimListener implements AnimatorListener {

	@Override
	public void onAnimationCancel(Animator arg0) {
	}

	@Override
	public void onAnimationEnd(Animator arg0) {
	}

	@Override
	public void onAnimationRepeat(Animator an){
	}

	@Override
	public void onAnimationStart(Animator arg0) {
	}

}
