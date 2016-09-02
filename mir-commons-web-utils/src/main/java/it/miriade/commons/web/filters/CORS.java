package it.miriade.commons.web.filters;

import javax.servlet.http.HttpServletResponse;

public interface CORS {

	/*
	 * Default header names
	 */
	public static final String Access_Control_Allow_Headers = "Access-Control-Allow-Headers";
	public static final String Access_Control_Allow_Methods = "Access-Control-Allow-Methods";
	public static final String Access_Control_Allow_Origin = "Access-Control-Allow-Origin";
	public static final String Access_Control_Max_Age = "Access-Control-Max-Age";
	/*
	 * Default header values (valori comunemente usati)
	 */
	public static final String Allow_Headers_Default = "Access-Control-Allow-Headers,Access-Control-Allow-Methods,Access-Control-Allow-Origin,Access-Control-Max-Age,Authorization,Content-Type,X-Requested-With";
	public static final String Allow_Methods_Default = "GET,POST,PUT,DELETE,OPTIONS";
	public static final String Allow_Origin_Default = "*";
	public static final String MAX_AGE_Default = "3600";

	/*
	 * init-params (chiavi usate in web.xml per definire i parametri)
	 */
	public static final String LOG_LEVEL_ParamKey = "log-level";
	public static final String ALLOW_HEADERS_ParamKey = "allow-headers";
	public static final String ALLOW_METHODS_ParamKey = "allow-methods";
	public static final String ALLOW_ORIGIN_ParamKey = "allow-origin";
	public static final String MAX_AGE_ParamKey = "max-age";

	public void addHeaders(HttpServletResponse response);

}