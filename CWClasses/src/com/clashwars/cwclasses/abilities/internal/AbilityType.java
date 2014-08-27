package com.clashwars.cwclasses.abilities.internal;

import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

import org.bukkit.ChatColor;

import com.clashwars.cwclasses.abilities.*;
import com.clashwars.cwclasses.classes.ClassType;

public enum AbilityType {
	UNKNOWN(0, "Unknown", ChatColor.WHITE, ClassType.UNKNOWN, AbilityClass.class),
	TRUESTRIKE(1, "TrueStrike", ChatColor.AQUA, ClassType.ARCHER, TrueStrike.class);
	
	
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
