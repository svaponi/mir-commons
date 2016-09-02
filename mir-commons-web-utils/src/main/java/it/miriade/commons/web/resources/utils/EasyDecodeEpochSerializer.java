package it.miriade.commons.web.resources.utils;

import java.io.IOException;
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
 * 
 * @author svaponi
 * @created Jul 25, 2016 6:25:57 PM
 */
public class EasyDecodeEpochSerializer extends JsonSerializer<Long> {

	@Override
	public void serialize(Long secondsFrom1970, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {

		String formattedDate = DateHandler.formatDate(new Date(secondsFrom1970 * 1000), Format.EASY_DECODE_TIMESTAMP);
		gen.writeString(formattedDate);
	}

}