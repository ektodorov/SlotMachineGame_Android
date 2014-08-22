package com.blogspot.techzealous.slotmachine.utils;

public class SMCloud extends Object {

	private float posX;
	private float posY;
	private int direction;
	
	public SMCloud(int aPosX, int aPosY, int aDirection) {
		super();
		posX = aPosX;
		posY = aPosY;
		direction = aDirection;
	}
	
	public float getPosX() {
		return posX;
	}
	
	public float getPosY() {
		return posY;
	}
	
	public void setPosX(float aPosX) {
		posX = aPosX;
	}
	
	public void setPosY(float aPosY) {
		posY = aPosY;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int aDirection) {
		direction = aDirection;
	}
}
