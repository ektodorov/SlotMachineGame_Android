package com.blogspot.techzealous.slotmachine.utils;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

	private Paint sunPaint;
	private Paint cloudPaint;
	private Paint textPaint;
	private float posX;
	private float posY;
	private ArrayList<SMCloud> clouds = new ArrayList<SMCloud>();
	
	public MyView(Context context) {
		super(context);
		myInit();
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		myInit();
	}
	
	public MyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		myInit();
	}
	
	private void myInit() {
		sunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		sunPaint.setColor(Color.YELLOW);
		cloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		cloudPaint.setColor(Color.WHITE);
		cloudPaint.setAlpha(240);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.RED);
		textPaint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//make the sun
		sunPaint.setAlpha(170);
		canvas.drawCircle(0, 0, 15, sunPaint);
		sunPaint.setAlpha(100);
		canvas.drawCircle(0, 0, 20, sunPaint);
		sunPaint.setAlpha(40);
		canvas.drawCircle(0, 0, 60, sunPaint);
		
		//make clouds
		for(SMCloud cloud : clouds) {
			posX = cloud.getPosX();
			posY = cloud.getPosY();
			canvas.drawCircle(posX, posY, 7, cloudPaint);
			canvas.drawCircle(posX + 10, posY, 10, cloudPaint);
			canvas.drawCircle(posX + 20, posY, 7, cloudPaint);
		}
	}
	
	public void setArrayClouds(ArrayList<SMCloud> aClouds) {
		clouds = aClouds;
	}
}
