package com.blogspot.techzealous.slotmachine.utils;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.blogspot.techzealous.slotmachine.R;

public class SMAnimator extends Object {

	private TextView textViewFirst;
	private TextView textViewSecond;
	private TextView textViewThird;
	private TextView textViewInfo;
	private MyView myView;

	private Context ctx;
	private Handler handlerSlots;
	private Runnable runnableIn;
	private Runnable runnableOut;
	private Runnable runnableHit;
	private SlotAnimator directorFirst;
	private SlotAnimator directorSecond;
	private SlotAnimator directorThird;
	private Animation animOut;
	private Animation animIn;

	private int myWidth;
	private int myHeight;
	private ArrayList<SMCloud> clouds;
	private Drawable[] myBackgrounds;
	private int positionFirst;
	private int positionSecond;
	private int positionThird;
	private int blink;
	private Random myRandom;
	private SMCalculator calculator;

	private Resources res;

	public SMAnimator(Context context, Activity myActivity, SMCalculator aCalculator) {
		ctx = context;
		res = ctx.getResources();
		calculator = aCalculator;
		
		myBackgrounds = new Drawable[5];
        myBackgrounds[0] = res.getDrawable(R.drawable.gold);
        myBackgrounds[1] = res.getDrawable(R.drawable.snowflake);
        myBackgrounds[2] = res.getDrawable(R.drawable.seven);
        myBackgrounds[3] = res.getDrawable(R.drawable.cherry);
        myBackgrounds[4] = res.getDrawable(R.drawable.question);

		textViewFirst = (TextView) myActivity.findViewById(R.id.textViewSlot1);
		textViewSecond = (TextView) myActivity.findViewById(R.id.textViewSlot2);
		textViewThird = (TextView) myActivity.findViewById(R.id.textViewSlot3);
		textViewInfo = (TextView) myActivity.findViewById(R.id.textViewInfo);
		myView = (MyView) myActivity.findViewById(R.id.myViewClouds);

		handlerSlots = new Handler();

		directorFirst = new SlotAnimator();
		directorSecond = new SlotAnimator();
		directorThird = new SlotAnimator();
		animOut = AnimationUtils.loadAnimation(ctx, R.anim.centertobottom);
		animIn = AnimationUtils.loadAnimation(ctx, R.anim.toptocenter);

		myRandom = new Random();
		
		clouds = new ArrayList<SMCloud>();

		/* Create the runnables for the handlers's post() methods */
		runnableHit = new Runnable() {
			public void run() {
				calculator.calculateScore();
			}
		};

		runnableOut = new Runnable() {
			public void run() {
				myView.setArrayClouds(clouds);

				// redraw the view
				myView.invalidate();

				// change location of the clouds
				for (SMCloud cloud : clouds) {
					if (cloud.getPosX() > myWidth || cloud.getPosX() < 0) {
						cloud.setPosX(myRandom.nextInt(myWidth));
						cloud.setPosY(myRandom.nextInt(myHeight));
						cloud.setDirection(myRandom.nextInt(8));
					}
					if (cloud.getPosY() > myHeight || cloud.getPosY() < 0) {
						cloud.setPosX(myRandom.nextInt(myWidth));
						cloud.setPosY(myRandom.nextInt(myHeight));
						cloud.setDirection(myRandom.nextInt(8));
					}

					switch (cloud.getDirection()) {
					case 0:// move to right
						cloud.setPosX(cloud.getPosX() + 0.3f);
						break;
					case 1:// move to left
						cloud.setPosX(cloud.getPosX() - 0.3f);
						break;
					case 2:// move to down
						cloud.setPosY(cloud.getPosY() + 0.3f);
						break;
					case 3:// move to up
						cloud.setPosY(cloud.getPosY() - 0.3f);
						break;
					case 4://move to up-right
						cloud.setPosX(cloud.getPosX() + 0.3f);
						cloud.setPosY(cloud.getPosY() - 0.3f);
						break;
					case 5:// move to down-right
						cloud.setPosX(cloud.getPosX() + 0.3f);
						cloud.setPosY(cloud.getPosY() + 0.3f);
						break;
					case 6:// move to down-left
						cloud.setPosX(cloud.getPosX() - 0.3f);
						cloud.setPosY(cloud.getPosY() + 0.3f);
						break;
					case 7:// move to up-left
						cloud.setPosX(cloud.getPosX() - 0.3f);
						cloud.setPosY(cloud.getPosY() - 0.3f);
					}
				}

				textViewFirst.startAnimation(animOut);
				textViewSecond.startAnimation(animOut);
				textViewThird.startAnimation(animOut);

				positionFirst = getRandomPosition();
				positionSecond = getRandomPosition();
				positionThird = getRandomPosition();
			}
		};

		runnableIn = new Runnable() {
			public void run() {
				blink++;
				if (blink % 2 == 0) {
					textViewInfo.setTextColor(res.getColor(R.color.orange));
					blink = 0;
				} else {
					textViewInfo.setTextColor(Color.WHITE);
				}
				directorFirst.animateOneDigit(animIn,
						myBackgrounds[positionFirst], textViewFirst);
				directorSecond.animateOneDigit(animIn,
						myBackgrounds[positionSecond], textViewSecond);
				directorThird.animateOneDigit(animIn,
						myBackgrounds[positionThird], textViewThird);
			}
		};
	}

	public void animate() {
		handlerSlots.post(runnableOut);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		handlerSlots.post(runnableIn);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void animateHit() {
		handlerSlots.post(runnableOut);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		handlerSlots.post(runnableIn);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		handlerSlots.post(runnableHit);
	}
	
	public int getFirst() {
		return positionFirst;
	}
	
	public int getSecond() {
		return positionSecond;
	}
	
	public int getThird() {
		return positionThird;
	}
	
	public void setDimensions(int aWidth, int aHeight) {
		myWidth = aWidth;
		myHeight = aHeight;
		for(int x = 0; x < 9; x++) {
			SMCloud myCloud = new SMCloud(myRandom.nextInt(myWidth), myRandom.nextInt(myHeight), myRandom.nextInt(8));
			clouds.add(myCloud);
		}
	}
	
	private int getRandomPosition() {
		return myRandom.nextInt(5);
	}
}
