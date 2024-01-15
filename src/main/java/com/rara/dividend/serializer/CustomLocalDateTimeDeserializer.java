package com.rara.dividend.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Override
	public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
		throws IOException, JacksonException {

		return LocalDateTime.parse(parser.getText(),FORMATTER);
	}

	@Override
	public Object deserializeWithType(JsonParser parser, DeserializationContext context,
		TypeDeserializer typeDeserializer) throws IOException, JacksonException {

		return LocalDateTime.parse(parser.getText(),FORMATTER);
	}
}
