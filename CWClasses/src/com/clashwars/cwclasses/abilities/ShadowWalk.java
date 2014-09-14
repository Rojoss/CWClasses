package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class ShadowWalk implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.SHADOWWALK;
	}
	@Override
	public int getLevel() {
		return 25;
	}
}
