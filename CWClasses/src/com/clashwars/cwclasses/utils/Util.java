package com.clashwars.cwclasses.utils;

public class Util {

	public static String[] trimFirst(String[] arr) {
		String[] ret = new String[arr.length - 1];

		for (int i = 1; i < arr.length; i++) {
			ret[i - 1] = arr[i];
		}

		return ret;
	}
}
