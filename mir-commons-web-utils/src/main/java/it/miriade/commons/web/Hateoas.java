package it.miriade.commons.web;

/**
 * Interfaccia intesa come contenitore di costanti di comune utilità per l'implementazione del protocollo HATEOAS
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:55:55 PM
 */
public interface Hateoas {

	public final String sl = "/";
	public final String sp = ":";
	public final String lb = "{"; // left bracket
	public final String rb = "}"; // right bracket

	public final String WEB_ROOT = "";
	public final String REST_BASE_URL = WEB_ROOT + "/api";

	public final String COLLECTION = "collection";
	public final String RESOURCE = "resource";
	public final String RESOURCEs = "resources";

	public final String pathVariableTmpl = "{%s}";

	public final String[] allowGetOptions = { "GET", "OPTIONS" };
	public final String[] allowPutOptions = { "PUT", "OPTIONS" };
	public final String[] allowDeleteOptions = { "DELETE", "OPTIONS" };
	public final String[] allowPostOptions = { "POST", "OPTIONS" };

	public final String[] allowGetPostOptions = { "GET", "POST", "OPTIONS" };
	public final String[] allowGetPutOptions = { "GET", "PUT", "OPTIONS" };
	public final String[] allowGetDeleteOptions = { "GET", "DELETE", "OPTIONS" };
	public final String[] allowPostPutOptions = { "POST", "PUT", "OPTIONS" };
	public final String[] allowPostDeleteOptions = { "POST", "DELETE", "OPTIONS" };
	public final String[] allowPutDeleteOptions = { "PUT", "DELETE", "OPTIONS" };

	public final String[] allowGetPostPutOptions = { "GET", "POST", "PUT", "OPTIONS" };
	public final String[] allowGetPostDeleteOptions = { "GET", "POST", "DELETE", "OPTIONS" };
	public final String[] allowGetPutDeleteOptions = { "GET", "PUT", "DELETE", "OPTIONS" };
	public final String[] allowPostPutDeleteOptions = { "POST", "PUT", "DELETE", "OPTIONS" };

	public final String[] allowGetPostPutDeleteOptions = { "GET", "POST", "PUT", "DELETE", "OPTIONS" };

}
