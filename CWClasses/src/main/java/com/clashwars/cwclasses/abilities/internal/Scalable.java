package com.clashwars.cwclasses.abilities.internal;



public class Scalable {
	private int lvl;
	private int minLvl;
	private int maxLvl;
	
	private int val;
	private int minVal;
	private int maxVal;
	
	private boolean isScale = false; 
	
	
	//Non scaled
	public Scalable(int lvl, int val) {
		this.lvl = lvl;
		this.val = val;
		isScale = false;
	}
	
	public int getLevel() {
		return lvl;
	}
	
	public int getPercentage() {
		return val;
	}
	
	
	//scaled
	public Scalable(int minLvl, int maxLvl, int minVal, int maxVal) {
		this.minLvl = minLvl;
		this.maxLvl = maxLvl;
		this.minVal = minVal;
		this.maxVal = maxVal;
		isScale = true;
	}
	
	public int getMinLevel() {
		return minLvl;
	}
	
	public int getMaxLevel() {
		return maxLvl;
	}
	
	public int getMinValue() {
		return minVal;
	}
	
	public int getMaxValue() {
		return maxVal;
	}
	
	public int getValueAtLevel(int level) {
		return Math.min(Math.round(minVal + ((float)(maxVal - minVal) / (maxLvl - minLvl)) * (level- minLvl)), maxVal);
	}
	//End
	
	
	public boolean isScaled() {
		return isScale;
	}
}
