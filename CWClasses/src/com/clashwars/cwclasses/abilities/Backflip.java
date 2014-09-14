package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Backflip implements AbilityClass {
	
	@Override
	public AbilityType getType() {
		return AbilityType.BACKFLIP;
	}
	@Override
	public int getLevel() {
		return 60;
	}
}
