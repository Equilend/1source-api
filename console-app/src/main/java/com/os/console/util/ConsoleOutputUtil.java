package com.os.console.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class ConsoleOutputUtil {

	private static final String spaces = "                                                                                                                 ";

	public static String padSpaces(String field, int maxLength) {
	
		String ret = (field == null ? "" : field) + spaces;
		
		return ret.substring(0, maxLength);
	}

	public static String padSpaces(Integer field, int maxLength) {
		
		String ret = (field == null ? "" : field.toString()) + spaces;
		
		return ret.substring(0, maxLength);
	}

	public static String padSpaces(LocalDate field, int maxLength) {
		
		String ret = (field == null ? "" : field.toString()) + spaces;
		
		return ret.substring(0, maxLength);
	}

	public static String padSpaces(OffsetDateTime field, int maxLength) {
		
		String ret = (field == null ? "" : field.toString()) + spaces;
		
		return ret.substring(0, maxLength);
	}

}
