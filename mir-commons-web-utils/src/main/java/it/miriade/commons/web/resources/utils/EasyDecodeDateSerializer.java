package it.miriade.commons.web.resources.utils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import it.miriade.commons.utils.DateHandler;
import it.miriade.commons.utils.DateHandler.Format;

/**
 * Usa il formato {@link Format.EASY_DECODE_TIMESTAMP} facile da decodificare lato client, basta splittare per '-' e si
 * ottiene un array con tutte le componenti del timestamp.<br/>
 * Il JSON risultante avrà un campo Date in formato string human-readble, esempio
 * <strong>2015-04-24-09-58-58-012</strong>.
 * <br/>
 * Accetta anche {@link Timestamp}.
 * 
 * @see DateHandler
 * @see Format.EASY_DECODE_TIMESTAMP
 * @author svaponi
 * @created Apr 24, 2015 9:58:58 AM
 */
public class EasyDecodeDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {

		String formattedDate = DateHandler.formatDate(date, Format.EASY_DECODE_TIMESTAMP);
		gen.writeString(formattedDate);
	}

}