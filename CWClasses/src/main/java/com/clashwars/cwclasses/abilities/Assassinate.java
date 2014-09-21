package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Assassinate extends AbilityClass {

	public AbilityType getType() {
		return AbilityType.ASSASSINATE;
	}

	public int getLevel() {
		return 100;
	}
}
