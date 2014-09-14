package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Leap implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.LEAP;
	}
	@Override
	public int getLevel() {
		return 25;
	}
}
