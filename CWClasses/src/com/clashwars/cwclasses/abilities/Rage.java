package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Rage implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.RAGE;
	}
	@Override
	public boolean isPassive() {
		return true;
	}
	@Override
	public int getLevel() {
		return 60;
	}
}
