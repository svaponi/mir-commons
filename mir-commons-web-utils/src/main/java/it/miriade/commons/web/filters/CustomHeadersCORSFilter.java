package it.miriade.commons.web.filters;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;

/**
 * Permette di impostare <strong>qualunque</strong> header nel CORS filter, ovvero ogni init-param viene direttamente
 * mappato ad un header:
 * 
 * <pre>
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;Access-Control-Allow-Methods&lt;/param-name&gt;
 * 	&lt;param-value&gt;GET,POST,PUT,DELETE,OPTIONS,HEAD&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;pinco-pallo&lt;/param-name&gt;
 * 	&lt;param-value&gt;pallo-pinco&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * 
 * Viene tradotto programmaticamente in:
 * 
 * <pre>
 * httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
 * httpServletResponse.setHeader("pinco-pallo", "pallo-pinco");
 * </pre>
 * 
 * @author svaponi
 * @created 09 feb 2016, 11:36
 */
public class CustomHeadersCORSFilter extends SimpleCORSFilter {

	protected Map<String, String> accessControlHeaders;

	public Map<String, String> getAccessControlHeaders() {
		return accessControlHeaders;
	}

	public void addHeaders(HttpServletResponse response) {
		for (String headerName : accessControlHeaders.keySet())
			response.setHeader(headerName, accessControlHeaders.get(headerName));
	}

	public void init(FilterConfig config) {

		accessControlHeaders = new HashMap<String, String>();
		accessControlHeaders.put(Access_Control_Allow_Headers, Allow_Headers_Default);
		accessControlHeaders.put(Access_Control_Allow_Methods, Allow_Methods_Default);
		accessControlHeaders.put(Access_Control_Allow_Origin, Allow_Origin_Default);
		accessControlHeaders.put(Access_Control_Max_Age, MAX_AGE_Default);

		Enumeration<String> names = config.getInitParameterNames();
		String param, value;
		while (names.hasMoreElements()) {
			param = names.nextElement();
			value = config.getInitParameter(param);
			accessControlHeaders.put(param, value);
		}

		if (_logger.isDebugEnabled())
			for (String headerName : accessControlHeaders.keySet())
				_logger.debug("CORS filter header \"%s\" => \"%s\" ", headerName, accessControlHeaders.get(headerName));

		super.init(config);
	}
}