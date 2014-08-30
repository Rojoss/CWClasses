package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Assassinate implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.ASSASSINATE;
	}
	@Override
	public int getLevel() {
		return 100;
	}
}
