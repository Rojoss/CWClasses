package com.clashwars.cwclasses;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;



public class ClassExp {
	private CWPlayer cwp;
	private double		exp;
	private final int	levelCap = 100;

	//Cached data variables
	private int			level;
	private double		levelProgression;
	private double		expToNextLevel;

	private final float	expGap	= 10.5F;
	private final float	defaultGap = -40F;
	private final float	offset = (1 - defaultGap) / expGap;

	public ClassExp(CWPlayer cwp) {
		this(cwp, 0);
	}

	public ClassExp(CWPlayer cwp, double exp) {
		this.exp = exp;
		this.cwp = cwp;
	}

	//Getters
	public double getExp() {
		return exp;
	}

	public int getLevel() {
		return level;
	}

	
	public double getLevelProgression() {
		return levelProgression;
	}
	
	public double getExpToNextLevel() {
		return expToNextLevel;
	}

	public int getLevelPercentage() {
		return (int) (exp / (exp + getExpToNextLevel())) * 100;
	}

	
	//Calculations.
	public void recalculate() {
		int prevLevel = level;
		level = calculateLevel();
		levelProgression = calculateLevelProgression();
		expToNextLevel = calculateExpToNextLevel();
		if (prevLevel != level) {
			//Call custom ClassLevelupEvent
			ClassLevelupEvent e = new ClassLevelupEvent(cwp);
			Bukkit.getServer().getPluginManager().callEvent(e);
		}
	}
	public int calculateLevel() {
		return (int) Math.sqrt(exp / 30);
		//return (int) Math.min(Math.max(Math.floor(expGap * Math.log(exp + offset) + defaultGap), 1), levelCap);
	}

	public double calculateLevelProgression() {
		return exp - calculateExpForLevel(getLevel() - 1);
	}

	public double calculateExpToNextLevel() {
		return calculateExpForLevel(getLevel() + 1) - exp;
	}

	public int calculateExpForLevel(int level) {
		return (int) Math.ceil(Math.pow(level, 2) * 30);
		//return (int) Math.ceil(Math.pow(Math.E, (level - defaultGap) / expGap) - offset);
	}
	
	
	//Exp edit
	public double incrementExp(double exp) {
		this.exp += exp;
		recalculate();
		return this.exp;
	}

	public double decrementExp(double exp) {
		this.exp = Math.max(0, this.exp - exp);
		recalculate();
		return exp;
	}

	public void setExp(double exp) {
		if (exp < 0) {
			throw new IllegalArgumentException("The total experience must be positive.");
		}

		this.exp = exp;
		recalculate();
	}
	
	
	//Level edit.
	public double incrementLevel(int levels) {
		if (levels < 1) {
			throw new IllegalArgumentException("The number of levels must be positive. To decrease levels, use decrementLevels.");
		}

		setExp(calculateExpForLevel(Math.min(getLevel() + levels, levelCap)));
		return exp;
	}
	
	public double decrementLevel(int levels) {
		if (levels < 1) {
			throw new IllegalArgumentException("The number of levels must be positive. To increase levels, use incrementLevels.");
		}

		setExp(calculateExpForLevel(Math.max(getLevel() - levels, 1)));
		return exp;
	}
	
	
	
	//Custom ClassLevelupEvent.
	public static class ClassLevelupEvent extends Event {
		private CWPlayer cwp;
		
	    public ClassLevelupEvent(CWPlayer cwp) {
	    	this.cwp = cwp;
	    }
	    
	    public CWPlayer getCWPlayer() {
	    	return cwp;
	    }

	    private static final HandlerList handlers = new HandlerList();
	    @Override
	    public HandlerList getHandlers() {
	        return handlers;
	    }
	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
	}
}
