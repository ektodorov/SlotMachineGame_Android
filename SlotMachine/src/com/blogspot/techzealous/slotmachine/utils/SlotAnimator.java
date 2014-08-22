package com.blogspot.techzealous.slotmachine.utils;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.widget.TextView;

public class SlotAnimator extends Object{

	private TextView singleTextView;
	private Drawable newDrawable;
	private Animation animationIn;
	
	/**
	 * Constructor with no parameters. To set the parameters of the SlotAnimator setTextViews and setCustomCount methods should be used.
	 */
	public SlotAnimator() {
		super();
	}

	/**
	 * Animate one TextView
	 * @param animIn - The end animation
	 * @param backgroundDrawable - the new background
	 * @param text - The TextView to be animated
	 */
	public void animateOneDigit(Animation animIn, Drawable backgroundDrawable, TextView text) {
		animationIn = animIn;
		singleTextView = text;
		newDrawable = backgroundDrawable;
				
		singleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, newDrawable, null);
		singleTextView.startAnimation(animationIn);
	}
	
}
