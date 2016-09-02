package it.miriade.commons.web.configuration;

/**
 * Contenitore di costanti. Sono init-params usati dal {@link InitParamReader} o da altri Servlet/Filter.
 * 
 * @author svaponi
 * @created Sep 15, 2015 5:26:46 PM
 */
public interface Keys {

	public static final String MIRIADE_PREFIX = "miriade.";

	/**
	 * Parametro di identificazoine del ambiente corrente: DEVelopment, system TEST, PRODuzione
	 */
	public static final String CURRENTENV = "currentenv";

	/**
	 * Nome della proprietà (o <init-param>) contenente il template per identificare il log4j.properties, es:
	 * logging/log4j_%s.properties
	 */
	public static final String LOG_CONFIG_FILE_NAMING_TEMPLATE = "log.config.file.naming.template";

	/**
	 * Nome della proprietà (o <init-param>) con il path al log4j.properties
	 */
	public static final String LOG_CONFIG_FILE_PATH__property = "log.config.file.path";

	/**
	 * Parametro che permette di aggiungere un suffisso alla SYSTEM_PROPERTY in modo da identificare una applicazione
	 * tra le altre che condividono lo stesso ambiente (servlet container) e
	 * utilizzano lo stesso LogInitializer. Così è possibile utilizzare diversi file di configurazione.
	 */
	public static final String LOG_CONFIG_FILE_PATH__propertySuffix = "log.config.file.path.property.suffix";

	/**
	 * Formato da applicare alle date
	 */
	public static final String DEFAULT_DATE_FORMAT_KEY = "default.date.format";

}
