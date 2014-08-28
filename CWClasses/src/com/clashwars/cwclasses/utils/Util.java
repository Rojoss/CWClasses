package com.clashwars.cwclasses.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;

public class Util {
	
	private static Random random;

	static {
		random = new Random();
	}
	
	//Format a message
	public static String formatMsg(String msg) {
		return integrateColor("&8[&4CW Classes&8] &6" + msg);
	}
	
	
	//Integrate colors in a string
	public static String integrateColor(String str) {
		for (ChatColor c : ChatColor.values()) {
			str = str.replaceAll("&" + c.getChar() + "|&" + Character.toUpperCase(c.getChar()), c.toString());
		}
		return str;
	}
	
	public static String[] integrateColor(String[] str) {
		for (int i = 0; i < str.length; i++) {
			for (ChatColor c : ChatColor.values()) {
				str[i] = str[i].replaceAll("&" + c.getChar() + "|&" + Character.toUpperCase(c.getChar()), c.toString());
			}
		}
		return str;
	}
	
	public static String stripAllColour(String str) {
		return ChatColor.stripColor(integrateColor(str));
	}
	
	public static String removeColour(String str) {
		for (ChatColor c : ChatColor.values()) {
			str = str.replace(c.toString(), "&" + c.getChar());
		}

		return str;
	}
	
	
	//Trim first string from string array
	public static String[] trimFirst(String[] arr) {
		String[] ret = new String[arr.length - 1];

		for (int i = 1; i < arr.length; i++) {
			ret[i - 1] = arr[i];
		}

		return ret;
	}

	//Convert string to lore.
	public static List<String> loreFromString(String loreStr) {
		List<String> lore = null;
		loreStr = integrateColor(loreStr);
		String[] split = loreStr.split("\n");
		lore = Arrays.asList(split);
		return lore;
	}
	
	//Convert a long time in ms to a formatted string min:sec
	public static String getMinSecStr(long time) {
		return getMinSecStr(time, null, null);
	}
	public static String getMinSecStr(long time, ChatColor timeColor, ChatColor color) {
		time = time / 1000;
		int minsLeft = (int) time / 60;
		int secsLeft = (int) time - minsLeft * 60;
		if (color == null || timeColor == null) {
			return minsLeft + ":" + secsLeft;
		} else {
			return "" + timeColor + minsLeft + color + ":" + timeColor + secsLeft;
		}
	}
	
	public static int random(int start, int end) {
		return start + random.nextInt(end - start + 1);
	}
	
	public static boolean checkChance(int percentage) {
		return percentage >= random(0,100);
	}
}
