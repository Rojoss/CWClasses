package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class FrostArrow implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.FROSTARROW;
	}
	@Override
	public int getLevel() {
		return 25;
	}
}
