package com.clashwars.cwclasses.abilities.internal;

import java.util.HashMap;

import com.clashwars.cwclasses.CWPlayer;

public interface AbilityClass {
	
	public boolean run(CWPlayer player);
	
	default AbilityType getType() {
		return AbilityType.UNKNOWN;
	}
	
	default int getCooldown() {
		return 0;
	}
	
	default int getLevel() {
		return 0;
	}
	
	default boolean isPassive() {
		return false;
	}
	
	default String getDescription() {
		return "&7No description available.";
	}
	
	default String getActivationInfo() {
		return "&7No activation.";
	}
	
	default HashMap<String, Scalable> getScales() {
		return null;
	}
}