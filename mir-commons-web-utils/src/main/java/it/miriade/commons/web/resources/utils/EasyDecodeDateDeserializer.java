package it.miriade.commons.web.resources.utils;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import it.miriade.commons.utils.DateHandler;
import it.miriade.commons.utils.DateHandler.Format;

/**
 * @see EasyDecodeDateSerializer
 * @author svaponi
 * @created Mar 01, 2016 10:43 AM
 */
public class EasyDecodeDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String timestamp = jp.getText().trim();
		return DateHandler.parseDate(timestamp, Format.EASY_DECODE_TIMESTAMP);
	}
}
