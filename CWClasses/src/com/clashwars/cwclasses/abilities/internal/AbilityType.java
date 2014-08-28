package com.clashwars.cwclasses.abilities.internal;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import com.clashwars.cwclasses.abilities.Absorb;
import com.clashwars.cwclasses.abilities.Assassinate;
import com.clashwars.cwclasses.abilities.Backflip;
import com.clashwars.cwclasses.abilities.Backstab;
import com.clashwars.cwclasses.abilities.Beserk;
import com.clashwars.cwclasses.abilities.Cleave;
import com.clashwars.cwclasses.abilities.FrostArrow;
import com.clashwars.cwclasses.abilities.Guard;
import com.clashwars.cwclasses.abilities.Leap;
import com.clashwars.cwclasses.abilities.PickPocket;
import com.clashwars.cwclasses.abilities.Rage;
import com.clashwars.cwclasses.abilities.Repel;
import com.clashwars.cwclasses.abilities.ShadowWalk;
import com.clashwars.cwclasses.abilities.Smash;
import com.clashwars.cwclasses.abilities.TrueStrike;
import com.clashwars.cwclasses.abilities.Vitality;
import com.clashwars.cwclasses.classes.ClassType;

public enum AbilityType {
	UNKNOWN(0, "Unknown", ChatColor.WHITE, ClassType.UNKNOWN, null),
	//Archer
	TRUESTRIKE(1, "TrueStrike", ChatColor.AQUA, ClassType.ARCHER, new TrueStrike()),
	FROSTARROW(2, "FrostArrow", ChatColor.DARK_AQUA, ClassType.ARCHER, new FrostArrow()),
	BACKFLIP(3, "Backflip", ChatColor.GREEN, ClassType.ARCHER, new Backflip()),
	REPEL(4, "Repel", ChatColor.YELLOW, ClassType.ARCHER, new Repel()),
	//Warrior
	BESERK(1, "Beserk", ChatColor.DARK_RED, ClassType.WARRIOR, new Beserk()),
	LEAP(2, "Leap", ChatColor.GRAY, ClassType.WARRIOR, new Leap()),
	RAGE(3, "Rage", ChatColor.RED, ClassType.WARRIOR, new Rage()),
	CLEAVE(4, "Cleave", ChatColor.DARK_PURPLE, ClassType.WARRIOR, new Cleave()),
	//Guardian
	SMASH(1, "Smash", ChatColor.DARK_GRAY, ClassType.GUARDIAN, new Smash()),
	VITALITY(2, "Vitality", ChatColor.GOLD, ClassType.GUARDIAN, new Vitality()),
	GUARD(3, "Guard", ChatColor.YELLOW, ClassType.GUARDIAN, new Guard()),
	ABSORB(4, "Absorb", ChatColor.GREEN, ClassType.GUARDIAN, new Absorb()),
	//Rogue
	PICKPOCKET(1, "PickPocket", ChatColor.DARK_GRAY, ClassType.ROGUE, new PickPocket()),
	SHADOWWALK(2, "ShadowWalk", ChatColor.GRAY, ClassType.ROGUE, new ShadowWalk()),
	BACKSTAB(3, "Backstab", ChatColor.DARK_RED, ClassType.ROGUE, new Backstab()),
	ASSASSINATE(4, "Assassinate", ChatColor.BLACK, ClassType.ROGUE, new Assassinate());
	
	
	//Vars
	private static Map<String, AbilityType> types;
	private int id;
	private String name;
	private ChatColor color;
	private ClassType classType;
	private AbilityClass clazz;
	
	
	//Constructor
	private AbilityType (int id, String name, ChatColor color, ClassType classType, AbilityClass clazz) {
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
	public AbilityClass getAbilitClass() {
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
