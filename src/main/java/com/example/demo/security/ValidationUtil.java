package com.example.demo.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

	public static boolean isNullOrEmpty(String value) {
		if (value == null || value.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNull(Integer value) {
		if (value == 0) {
			return true;
		}
		return false;
	}

	public static boolean isNull(Double value) {
		if (value == 0) {
			return true;
		}
		return false;
	}

	public static boolean isStartWithUpperCase(String value) {
		if (Character.isUpperCase(value.charAt(0))) {
			return true;
		}
		return false;
	}

	public static boolean isValidEmail(String value) {
		String regex = "^[A-Za-z0-9. _%-]+@[A-Za-z0-9. -]+\\\\. [A-Za-z]{2,4}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(value);
		return m.matches();
	}

}
