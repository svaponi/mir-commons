package it.miriade.commons.web.resources.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author matteo
 *         27/03/14:17.11
 */
public class UnixDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String timestamp = jp.getText().trim();

		return deserialize(timestamp);
	}

	public Date deserialize(String timestamp) {
		try {
			return new Date(new BigDecimal(timestamp).multiply(new BigDecimal(1000)).longValue());
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
