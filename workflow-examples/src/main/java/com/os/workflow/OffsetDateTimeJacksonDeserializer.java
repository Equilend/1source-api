package com.os.workflow;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OffsetDateTimeJacksonDeserializer extends JsonDeserializer<OffsetDateTime> {
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException {
		
		String dateAsString = jsonParser.getText();
		if (dateAsString == null) {
			throw new IOException("OffsetDateTime argument is null.");
		}
		
		if (dateAsString.endsWith("Z")) {
			dateAsString = dateAsString.substring(0, dateAsString.indexOf("Z"));
		}
		
		int milliSize = dateAsString.substring(dateAsString.indexOf(".")+1).length();
		
		for (int i = milliSize; i<3; i++) {
			dateAsString = dateAsString + "0";
		}
		
		LocalDateTime dateTime = LocalDateTime.parse(dateAsString, DATE_TIME_FORMATTER);
		
		return OffsetDateTime.of(dateTime, ZoneOffset.UTC);
	}
}