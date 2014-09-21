package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Backstab extends AbilityClass {

	public AbilityType getType() {
		return AbilityType.BACKSTAB;
	}

	public boolean isPassive() {
		return true;
	}

	public int getLevel() {
		return 60;
	}
}
