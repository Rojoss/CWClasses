package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Backflip extends AbilityClass {

	public AbilityType getType() {
		return AbilityType.BACKFLIP;
	}

	public int getLevel() {
		return 60;
	}
}
