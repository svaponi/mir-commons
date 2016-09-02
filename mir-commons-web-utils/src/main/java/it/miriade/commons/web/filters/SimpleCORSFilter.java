package it.miriade.commons.web.filters;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * Imposta un CORS filter con la configurazione di default (vedi constanti in {@link CORS}).
 * 
 * @see CORS
 * @see CORS#Allow_Headers_Default
 * @see CORS#Allow_Methods_Default
 * @see CORS#Allow_Origin_Default
 * @see CORS#MAX_AGE_Default
 * @author svaponi
 * @created Apr 17, 2015 11:55:49 AM
 */
public class SimpleCORSFilter extends AbstractCORSFilter {

	protected Map<String, String> initParams = new HashMap<String, String>();

	public void addHeaders(HttpServletResponse response) {
		response.setHeader(Access_Control_Allow_Headers, Allow_Headers_Default);
		response.setHeader(Access_Control_Allow_Methods, Allow_Methods_Default);
		response.setHeader(Access_Control_Allow_Origin, Allow_Origin_Default);
		response.setHeader(Access_Control_Max_Age, MAX_AGE_Default);
	}
}