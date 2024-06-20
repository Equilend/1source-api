package com.os.events.websockets;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class OffsetDateTimeTypeAdapter implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

	@Override
	public JsonElement serialize(final OffsetDateTime date, final Type typeOfSrc, final JsonSerializationContext context) {
		return new JsonPrimitive(date.format(formatter));
	}

	@Override
	public OffsetDateTime deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		return OffsetDateTime.parse(json.getAsString(), formatter);
	}
}