package com.clashwars.cwclasses.abilities.internal;

public class Scalable {
	private int lvl;
	private int minLvl;
	private int maxLvl;
	
	private float percent;
	private float minPercent;
	private float maxPercent;
	
	private boolean isScale = false; 
	
	
	//Non scaled
	public Scalable(int lvl, float percent) {
		this.lvl = lvl;
		this.percent = percent;
		isScale = false;
	}
	
	public int getLevel() {
		return lvl;
	}
	
	public float getPercentage() {
		return percent;
	}
	
	
	//scaled
	public Scalable(int minLvl, int maxLvl, float minPercent, float maxPercent) {
		this.minLvl = minLvl;
		this.maxLvl = maxLvl;
		isScale = true;
	}
	
	public int getMinLevel() {
		return minLvl;
	}
	
	public int getMaxLevel() {
		return maxLvl;
	}
	
	public float getMinPercentage() {
		return minPercent;
	}
	
	public float getMaxPercentage() {
		return maxPercent;
	}
	//End
	
	
	public boolean isScaled() {
		return isScale;
	}
}
