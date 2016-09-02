package it.miriade.commons.web.resources.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author matteo
 *         27/03/14:17.14
 */
public class UnixDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        if(value != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(value);
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            long gmtTime = cal.getTime().getTime();
            jgen.writeString((gmtTime/1000)+"");
        }
    }
}
