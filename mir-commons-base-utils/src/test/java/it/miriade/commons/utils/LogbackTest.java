package it.miriade.commons.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackTest {

	URL url;
	String path = "logging/logback.test.xml";

	@Before
	public void setup() {
		try {
			File tmp = new ClasspathLoader().lookForFileOnClasspath(path);
			Assert.assertNotNull("File tmp not NULL", tmp);
			url = new URL("file:" + tmp.getAbsolutePath());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// Assert.assertNotNull("URL url not NULL", url);
	}

	// @Test
	public void setupLogbackContext() {

		// assume SLF4J is bound to logback in the current environment
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			// Call context.reset() to clear any previous configuration, e.g. default
			// configuration. For multi-step configuration, omit calling context.reset().
			context.reset();
			configurator.doConfigure(url);
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);

		Logger logger = LoggerFactory.getLogger(LogbackTest.class);
		Assert.assertNotNull("logger not NULL", logger);
		Assert.assertEquals("Logger ROOT_LOGGER_NAME should be equals to", "ROOT", Logger.ROOT_LOGGER_NAME);

		logger = LoggerFactory.getLogger("test.info.logger");
		Assert.assertEquals("Logger name should be equals to", "test.info.logger", logger.getName());
		Assert.assertTrue("Logger \"test.info.logger\" should be enabled for INFO", logger.isInfoEnabled());
		Assert.assertFalse("Logger \"test.info.logger\" should NOT be enabled for DEBUG", logger.isDebugEnabled());
		Assert.assertFalse("Logger \"test.info.logger\" should NOT be enabled for TRACE", logger.isTraceEnabled());

		logger = LoggerFactory.getLogger("test.debug.logger");
		Assert.assertEquals("Logger name should be equals to", "test.debug.logger", logger.getName());
		Assert.assertTrue("Logger \"test.debug.logger\" should be enabled for INFO", logger.isInfoEnabled());
		Assert.assertTrue("Logger \"test.debug.logger\" should be enabled for DEBUG", logger.isDebugEnabled());
		Assert.assertFalse("Logger \"test.debug.logger\" should NOT be enabled for TRACE", logger.isTraceEnabled());

		logger = LoggerFactory.getLogger("test.trace.logger");
		Assert.assertEquals("Logger name should be equals to", "test.trace.logger", logger.getName());
		Assert.assertTrue("Logger \"test.trace.logger\" should be enabled for INFO", logger.isInfoEnabled());
		Assert.assertTrue("Logger \"test.trace.logger\" should be enabled for DEBUG", logger.isDebugEnabled());
		Assert.assertTrue("Logger \"test.trace.logger\" should be enabled for TRACE", logger.isTraceEnabled());

	}
}