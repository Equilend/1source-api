package com.os.replay.util;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.*;

public class OffsetDateTimeTypeAdapter implements JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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