package it.miriade.commons.web.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;

import it.miriade.commons.utils.StringHandler;

/**
 * Permette di impostare gli header tipici (Access-Control-Allow-Headers, Access-Control-Allow-Methods,
 * Access-Control-Allow-Origin, Access-Control-Max-Age) con valori custom, letti da init-params in web.xml.
 * 
 * <pre>
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;Access-Control-Allow-Headers&lt;/param-name&gt;
 * 	&lt;param-value&gt;Access-Control-Allow-Headers,ecc..&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;Access-Control-Allow-Methods&lt;/param-name&gt;
 * 	&lt;param-value&gt;GET,POST,PUT,DELETE,OPTIONS,HEAD&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;Access-Control-Allow-Origin&lt;/param-name&gt;
 * 	&lt;param-value&gt;*&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;Access-Control-Max-Age&lt;/param-name&gt;
 * 	&lt;param-value&gt;3600&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * 
 * Viene tradotto programmaticamente in:
 * 
 * <pre>
 * httpServletResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers,ecc..");
 * httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
 * httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
 * httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
 * </pre>
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM 2015-02-20.12:04
 */
public class ConfigurableCORSFilter extends SimpleCORSFilter {

	protected List<String> configHeaders = new ArrayList<String>();
	protected List<String> configMethods = new ArrayList<String>();
	protected List<String> configOrigin = new ArrayList<String>();
	protected List<String> configMaxAge = new ArrayList<String>();

	public List<String> getConfigHeaders() {
		return configHeaders;
	}

	public List<String> getConfigMethods() {
		return configMethods;
	}

	public List<String> getConfigOrigin() {
		return configOrigin;
	}

	public List<String> getConfigMaxAge() {
		return configMaxAge;
	}

	public void addHeaders(HttpServletResponse response) {
		response.setHeader(Access_Control_Allow_Headers, configHeaders.isEmpty() ? Allow_Headers_Default : StringHandler.join(",", configHeaders));
		response.setHeader(Access_Control_Allow_Methods, configMethods.isEmpty() ? Allow_Methods_Default : StringHandler.join(",", configMethods));
		response.setHeader(Access_Control_Allow_Origin, configOrigin.isEmpty() ? Allow_Origin_Default : StringHandler.join(",", configOrigin));
		response.setHeader(Access_Control_Max_Age, configMaxAge.isEmpty() ? MAX_AGE_Default : StringHandler.join(",", configMaxAge));
	}

	public void init(FilterConfig config) {

		String configHeadersStr = config.getInitParameter(ALLOW_HEADERS_ParamKey);
		if (configHeadersStr != null && !configHeadersStr.equals("")) {
			initParams.put(ALLOW_HEADERS_ParamKey, configHeadersStr);
			String[] splittedHeaders = configHeadersStr.split(",");
			configHeaders.clear();
			configHeaders.addAll(Arrays.asList(splittedHeaders));
		}

		String configMethodsStr = config.getInitParameter(ALLOW_METHODS_ParamKey);
		if (configMethodsStr != null && !configMethodsStr.equals("")) {
			initParams.put(ALLOW_METHODS_ParamKey, configMethodsStr);
			String[] splittedMethods = configMethodsStr.split(",");
			configMethods.clear();
			configMethods.addAll(Arrays.asList(splittedMethods));
		}

		String configOriginStr = config.getInitParameter(ALLOW_ORIGIN_ParamKey);
		if (configOriginStr != null && !configOriginStr.equals("")) {
			initParams.put(ALLOW_ORIGIN_ParamKey, configOriginStr);
			String[] splittedOrigin = configOriginStr.split(",");
			configOrigin.clear();
			configOrigin.addAll(Arrays.asList(splittedOrigin));
		}

		String configMaxAgeStr = config.getInitParameter(MAX_AGE_ParamKey);
		if (configMaxAgeStr != null && !configMaxAgeStr.equals("")) {
			initParams.put(MAX_AGE_ParamKey, configMaxAgeStr);
			String[] splittedMaxAge = configMaxAgeStr.split(",");
			configMaxAge.clear();
			configMaxAge.addAll(Arrays.asList(splittedMaxAge));
		}

		super.init(config);
	}
}