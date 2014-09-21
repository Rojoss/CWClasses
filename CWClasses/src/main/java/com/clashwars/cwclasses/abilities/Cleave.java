package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Cleave extends AbilityClass {


	public AbilityType getType() {
		return AbilityType.CLEAVE;
	}

	public boolean isPassive() {
		return true;
	}

	public int getLevel() {
		return 100;
	}
}
