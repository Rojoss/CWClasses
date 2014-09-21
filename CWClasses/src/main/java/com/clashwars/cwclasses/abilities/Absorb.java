package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Absorb extends AbilityClass {

	public AbilityType getType() {
		return AbilityType.ABSORB;
	}

	public boolean isPassive() {
		return true;
	}

	public int getLevel() {
		return 100;
	}
}
