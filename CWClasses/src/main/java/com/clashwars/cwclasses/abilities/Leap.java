package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Leap extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.LEAP;
	}

	public int getLevel() {
		return 25;
	}
}
