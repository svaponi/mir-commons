package it.miriade.commons.model.test;

import it.miriade.commons.logging.LogbackInitializer;

public class Constants {

	public static final String CONTEXT_XML_FILE = "classpath:context.test.xml";
	public static final String LOG_CONFIG_FILE = "logback.test.xml";

	public static final String DB_SCHEMA = "public";
	public static final String FOO_TABLE = "FOO";

	static {
		new LogbackInitializer(LOG_CONFIG_FILE).init();
	}

}