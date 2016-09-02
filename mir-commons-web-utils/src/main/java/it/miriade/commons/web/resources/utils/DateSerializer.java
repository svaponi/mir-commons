package it.miriade.commons.web.resources.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import it.miriade.commons.utils.DateHandler;
import it.miriade.commons.utils.DateHandler.Format;
import it.miriade.commons.utils.StringHandler;

/**
 * @see DateHandler
 * @author svaponi
 * @created Apr 24, 2015 9:42:35 AM
 */
public class DateSerializer extends JsonSerializer<Date> {

	private static final SimpleDateFormat DATE_FORMATTER = DateHandler.getDateFormatter(Format.ISO_DATE);
	private SimpleDateFormat dateFormatter;

	public DateSerializer() {
		super();
		this.dateFormatter = DATE_FORMATTER;
	}

	public DateSerializer(String dateFormat) {
		super();
		if (StringHandler.hasText(dateFormat))
			this.dateFormatter = DateHandler.getDateFormatter(dateFormat);
		else
			this.dateFormatter = DATE_FORMATTER;
	}

	public DateSerializer(Format format) {
		this(format.toPattern());
	}

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {

		TimeZone tz = TimeZone.getTimeZone("UTC");
		dateFormatter.setTimeZone(tz);
		String formattedDate = dateFormatter.format(date);
		gen.writeString(formattedDate);
	}

}