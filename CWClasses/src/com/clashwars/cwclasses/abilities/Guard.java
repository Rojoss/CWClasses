package com.clashwars.cwclasses.abilities;

import com.clashwars.cwclasses.abilities.internal.AbilityClass;
import com.clashwars.cwclasses.abilities.internal.AbilityType;

public class Guard implements AbilityClass {

	@Override
	public AbilityType getType() {
		return AbilityType.GUARD;
	}
	@Override
	public int getLevel() {
		return 60;
	}
}
