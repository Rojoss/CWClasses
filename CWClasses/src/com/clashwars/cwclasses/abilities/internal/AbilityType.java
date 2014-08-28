package com.clashwars.cwclasses.abilities.internal;

import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

import org.bukkit.ChatColor;

import com.clashwars.cwclasses.abilities.*;
import com.clashwars.cwclasses.classes.ClassType;

public enum AbilityType {
	UNKNOWN(0, "Unknown", ChatColor.WHITE, ClassType.UNKNOWN, AbilityClass.class),
	//Archer
	TRUESTRIKE(1, "TrueStrike", ChatColor.AQUA, ClassType.ARCHER, TrueStrike.class),
	FROSTARROW(2, "FrostArrow", ChatColor.DARK_AQUA, ClassType.ARCHER, FrostArrow.class),
	BACKFLIP(3, "Backflip", ChatColor.GREEN, ClassType.ARCHER, Backflip.class),
	REPEL(4, "Repel", ChatColor.YELLOW, ClassType.ARCHER, Repel.class),
	//Warrior
	BESERK(1, "Beserk", ChatColor.DARK_RED, ClassType.WARRIOR, Beserk.class),
	LEAP(2, "Leap", ChatColor.GRAY, ClassType.WARRIOR, Leap.class),
	RAGE(3, "Rage", ChatColor.RED, ClassType.WARRIOR, Rage.class),
	CLEAVE(4, "Cleave", ChatColor.DARK_PURPLE, ClassType.WARRIOR, Cleave.class),
	//Guardian
	SMASH(1, "Smash", ChatColor.DARK_GRAY, ClassType.GUARDIAN, Smash.class),
	VITALITY(2, "Vitality", ChatColor.GOLD, ClassType.GUARDIAN, Vitality.class),
	GUARD(3, "Guard", ChatColor.YELLOW, ClassType.GUARDIAN, Guard.class),
	ABSORB(4, "Absorb", ChatColor.GREEN, ClassType.GUARDIAN, Absorb.class),
	//Rogue
	PICKPOCKET(1, "PickPocket", ChatColor.DARK_GRAY, ClassType.ROGUE, PickPocket.class),
	SHADOWWALK(2, "ShadowWalk", ChatColor.GRAY, ClassType.ROGUE, ShadowWalk.class),
	BACKSTAB(3, "Backstab", ChatColor.DARK_RED, ClassType.ROGUE, Backstab.class),
	ASSASSINATE(4, "Assassinate", ChatColor.BLACK, ClassType.ROGUE, Assassinate.class);
	
	
	//Vars
	private static Map<String, AbilityType> types;
	private int id;
	private String name;
	private ChatColor color;
	private ClassType classType;
	private Class<? extends AbilityClass> clazz;
	
	
	//Constructor
	private AbilityType (int id, String name, ChatColor color, ClassType classType, Class<? extends AbilityClass> clazz) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.classType = classType;
		this.clazz = clazz;
	}
	
	
	//Get classType by name
	public static AbilityType fromString(String name) {
        if (types == null) {
            initTypes();
        }
        return types.get(name);
    }
	
	//Get ID
	public int getID() {
		return id;
	}
	
	//Get name
	public String getName() {
        return name;
    }
	
	//Get color
	public ChatColor getColor() {
		return color;
	}
	
	//Get the class type
	public ClassType getClassType() {
		return classType;
	}
	
	//Get the ability Class
	public Class<? extends AbilityClass> getAbilitClass() {
		return clazz;
	}
	
	
	//Fill hashmap with types.
	private static void initTypes() {
		types = new HashMap<String, AbilityType>();
        for (AbilityType c : values()) {
        	types.put(c.name, c);
        }
    }
}
