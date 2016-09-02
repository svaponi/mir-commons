package it.miriade.commons.logging;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import it.miriade.commons.utils.ExHandler;
import it.miriade.commons.utils.FileHandler;
import it.miriade.commons.utils.StringHandler;

/**
 * @author svaponi
 * @created 2015-03-01 12:04:41 PM
 */
public class LogbackInitializer implements LogInitializer {

	public static final String LOGBACK_DEFAUL_CONFIG_FILE = "logback.xml";

	// Ha senso usare il logger in una classe fatta per inizializzare il logger stesso?
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String configFilePath = LOGBACK_DEFAUL_CONFIG_FILE;

	public String getConfigFilePath() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		URL url = new ContextInitializer(loggerContext).findURLOfDefaultConfigurationFile(false);
		return url.getFile();
	}

	public void reset() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.reset();
	}

	public void reset(String configFilePath) {
		new LogbackInitializer(configFilePath).init();
	}

	/**
	 * Configure Log4J in current execution. Need to be initiated using <code>init()</code> method. Suggested use:<br/>
	 * <br/>
	 * <code>String configFileURI = "logback.cml";<br/>
	 * new LogbackInitializer(configFileURI).init();
	 * </code>
	 *
	 * @param configFile
	 *            (path + name) If invlid
	 */
	public LogbackInitializer(String configFile) {
		super();

		if (StringHandler.hasText(configFile))
			this.configFilePath = configFile;

		logger.info("Logback config file: {}", configFile);
	}

	/**
	 * Initiate and configure Log4J in current execution.
	 */
	public LogbackInitializer init() {
		try {

			reset();
			configure(configFilePath);

		} catch (Exception e) {
			logger.error(ExHandler.getRoot(e));
			logger.trace(ExHandler.getStackTraceButRoot(e));
			logger.info("Due to %s now using (logback) default configuration", e.getClass().getName());
		}
		return this;
	}

	/**
	 * Configura il root logger con il xml file in ingresso. Se non trova il xml file solleva una
	 * RuntimeException.
	 * 
	 * @param configFilePath
	 *            path al logback.properties
	 * @throws RuntimeException
	 */
	public void configure(String configFilePath) throws RuntimeException {
		String absolutePath;
		File configFile = null;

		// controlla i vari casi in cui il path è assoluto
		if (StringHandler.toString(configFilePath).startsWith("file://")) {

			configFile = new File(configFilePath.substring("file://".length()));
			if (configFile != null && configFile.exists() && configFile.isFile())
				absolutePath = configFile.getAbsolutePath();
			else
				throw new RuntimeException("File " + configFilePath + " not found");

		} else if (StringHandler.toString(configFilePath).startsWith("file:")) {

			configFile = new File(configFilePath.substring("file:".length()));
			if (configFile != null && configFile.exists() && configFile.isFile())
				absolutePath = configFile.getAbsolutePath();
			else
				throw new RuntimeException("File " + configFilePath + " not found");

		} else if (FileHandler.isAbsolutePath(configFilePath)) {

			configFile = new File(configFilePath);
			if (configFile != null && configFile.exists() && configFile.isFile())
				absolutePath = configFile.getAbsolutePath();
			else
				throw new RuntimeException("File " + configFilePath + " not found");

		} else {

			// Se il path è relativo viene usato il FileHandler.findURL per cercare dentro al classpath
			absolutePath = FileHandler.findURL(configFilePath);
		}

		if (StringHandler.hasText(absolutePath))
			try {
				System.setProperty("logback.configurationFile", absolutePath);
				LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
				/*
				 * TODO verificare quale tra i seguenti è il metodo corretto per caricare il file di configurazione
				 */
				// loggerContext.reset();
				// new ContextInitializer(loggerContext).autoConfig();
				absolutePath = "file:" + absolutePath;
				new ContextInitializer(loggerContext).configureByResource(new URL(absolutePath));
				logger.info("Logback xml file {} successfully loaded! [path:{}]", FileHandler.getName(absolutePath), FileHandler.getParent(absolutePath));
			} catch (JoranException e) {
				logger.error(e.getMessage());
			} catch (MalformedURLException e) {
				logger.error(e.getMessage());
			}
		else {
			logger.info("Logback configuration ignored due to a NULL xml file");
		}
	}
}
