package com.os.workflow;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateGsonTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public JsonElement serialize(final Date date, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(LocalDate.ofInstant(date.toInstant(), ZoneOffset.UTC).format(formatter));
	}

	@Override
	public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		return Date.from(LocalDate.parse(json.getAsString(), formatter).atStartOfDay(ZoneOffset.UTC).toInstant());
	}
}