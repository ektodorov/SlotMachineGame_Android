package com.blogspot.techzealous.slotmachine.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.blogspot.techzealous.slotmachine.R;

public class SMCalculator extends Object {

	private Resources res;

	private int positionFirst;
	private int positionSecond;
	private int positionThird;
	private int score;

	private TextView textViewInfo;
	private TextView textViewPlusSign;
	private TextView textViewWinAmmount;
	private TextView textViewScore;

	public SMCalculator(Context context, Activity myActivity) {
		res = context.getResources();

		textViewInfo = (TextView) myActivity.findViewById(R.id.textViewInfo);
		textViewPlusSign = (TextView) myActivity.findViewById(R.id.textViewPlusSign);
		textViewWinAmmount = (TextView) myActivity.findViewById(R.id.textViewCurrentWining);
		textViewScore = (TextView) myActivity.findViewById(R.id.textViewScore);
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int aScore) {
		score = aScore;
		textViewScore.setText(String.valueOf(score));
	}
	
	public void setPositions(int first, int second, int third) {
		positionFirst = first;
		positionSecond = second;
		positionThird = third;
	}
	
	public void substractFromSpin() {
		score = score - 5;
		textViewScore.setText(String.valueOf(score));
		textViewInfo.setText(res.getString(R.string.playing));
	}

	public void calculateScore() {

		if (positionFirst == 4 && positionSecond == positionThird) {
			setWinAmmount(75);
		}
		if (positionSecond == 4 && positionFirst == positionThird) {
			setWinAmmount(75);
		}
		if (positionThird == 4 && positionSecond == positionFirst) {
			setWinAmmount(75);
		}

		if (positionFirst == positionSecond && positionFirst == positionThird) {
			setWinAmmount(100);
		} else {
			textViewInfo.setText(res.getString(R.string.lose));
		}
	}

	private void setWinAmmount(int win) {
		score = score + win;
		textViewInfo.setText(res.getString(R.string.win));

		textViewPlusSign.setVisibility(View.VISIBLE);
		textViewWinAmmount.setVisibility(View.VISIBLE);
		textViewWinAmmount.setText(String.valueOf(win));

		textViewScore.setText(String.valueOf(score));
	}
}
