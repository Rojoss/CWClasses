package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Backstab implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.BACKSTAB;
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
