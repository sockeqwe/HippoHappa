package com.hippohappa.shake;

import android.widget.ImageView;

import com.hippohappa.R;

public class HippoAnimator {

	private final ImageView hippo;

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

}
