package com.os.console.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.os.console.api.LocalDateTypeGsonAdapter;
import com.os.console.api.OffsetDateTimeTypeGsonAdapter;

public class ConsoleOutputUtil {

	private static final Gson payloadGson = new GsonBuilder()
			.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
			.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

	private static final Gson printGson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LocalDate.class, new LocalDateTypeGsonAdapter())
			.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeGsonAdapter()).create();

	public static String createJsonPayload(Object o) {
		return payloadGson.toJson(o);
	}
	
	public static void printObject(Object o) {
		System.out.println();
		System.out.println(printGson.toJson(o));
		System.out.println();
	}
	
	private static final String spaces = "                                                                                                                 ";

	public static String padSpaces(String field, int maxLength) {
	
		String ret = (field == null ? "" : field) + spaces;
		
		return ret.substring(0, maxLength);
	}

	public static String padSpaces(Integer field, int maxLength) {
		
		String ret = (field == null ? "" : field.toString()) + spaces;
		
		return ret.substring(0, maxLength);
	}

	public static String padSpaces(Double field, int maxLength) {
		
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
