package it.miriade.commons.dbutils;

import it.miriade.commons.logging.LogbackInitializer;

public interface Constants {

	public static final String CONTEXT_XML_FILE = "classpath:context.test.xml";
	public static final String LOG_CONFIG_FILE = "logback.test.xml";
	// inizializza il logger
	public static final LogbackInitializer loginit = new LogbackInitializer(LOG_CONFIG_FILE).init();
}