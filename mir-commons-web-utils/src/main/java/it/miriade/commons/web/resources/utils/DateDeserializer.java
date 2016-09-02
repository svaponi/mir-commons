package it.miriade.commons.web.resources.utils;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import it.miriade.commons.utils.DateHandler;
import it.miriade.commons.utils.DateHandler.Format;
import it.miriade.commons.utils.StringHandler;

/**
 * @see DateHandler
 * @author svaponi
 * @created Mar 01, 2016 10:43 AM
 */
public class DateDeserializer extends JsonDeserializer<Date> {

	private static final Format DATE_FORMAT = Format.ISO_DATE;
	private String dateFormat;

	public DateDeserializer() {
		super();
		this.dateFormat = DATE_FORMAT.toPattern();
	}

	public DateDeserializer(String dateFormat) {
		super();
		if (StringHandler.hasText(dateFormat))
			this.dateFormat = dateFormat;
		else
			this.dateFormat = DATE_FORMAT.toPattern();
	}

	public DateDeserializer(Format format) {
		this(format.toPattern());
	}

	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String timestamp = jp.getText().trim();
		return DateHandler.parseDate(timestamp, dateFormat);
	}
}
