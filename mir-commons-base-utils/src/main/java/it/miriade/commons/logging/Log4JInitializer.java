package it.miriade.commons.logging;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.miriade.commons.utils.ExHandler;
import it.miriade.commons.utils.FileHandler;
import it.miriade.commons.utils.StringHandler;

/**
 * <strong>Utility per inizializzare i log.</strong><br/>
 * Carica il file properties di configurazione cercandolo in tutto il
 * classpath (per questo utilizza <code>ClasspathLoader</code>)
 * <dl>
 * <dt>Esempio</dt>
 * <dd><code>new Log4JInitializer(LOG4J_PROPERTIES).init();</code></dd>
 * </dl>
 *
 * @see it.miriade.utils.ClasspathLoader
 * @author svaponi
 * @created 2015-03-01 12:04:41 PM
 */
public class Log4JInitializer implements LogInitializer {

	public static final String LOG4J_DEFAULT_PROPERTY_FILE = "log4j.properties";

	// Ha senso usare il logger in una classe fatta per inizializzare il logger stesso?
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String propertyFilePath = LOG4J_DEFAULT_PROPERTY_FILE;

	private static String lastPropertyFilePath;

	public static String getLastPropertyFilePath() {
		return lastPropertyFilePath;
	}

	public String getConfigFilePath() {
		return lastPropertyFilePath;
	}

	public void reset() {
		if (StringHandler.noText(lastPropertyFilePath))
			throw new RuntimeException("Missing log4j property file path!");
		reset(lastPropertyFilePath);
	}

	public void reset(String propertyFilePath) {
		new Log4JInitializer(propertyFilePath).init();
	}

	/**
	 * Configure Log4J in current execution. Need to be initiated using <code>init()</code> method. Suggested use:<br/>
	 * <br/>
	 * <code>
	 * String propertyFileURI = "commons/model/testLog4J.properties";<br/>
	 * new Log4JInitializer(propertyFileURI).init();
	 * </code>
	 *
	 * @param propertyFile
	 *            (path + name) If invlid
	 */
	public Log4JInitializer(String propertyFile) {
		super();

		if (StringHandler.hasText(propertyFile))
			this.propertyFilePath = propertyFile;

		logger.info(String.format("Log4j property file: %s", propertyFile));
	}

	/**
	 * Initiate and configure Log4J in current execution.
	 */
	public Log4JInitializer init() {
		this.loadPropertyConfiguration(propertyFilePath);
		return this;
	}

	/**
	 * Configura il root logger con il property file in ingresso.
	 *
	 * @param propertyFilePath
	 *            path al log4j.properties
	 */
	public void loadPropertyConfiguration(String propertyFilePath) {
		try {

			configureLog4j(propertyFilePath);
			lastPropertyFilePath = propertyFilePath;

		} catch (Exception e) {
			logger.error(ExHandler.getRoot(e));
			logger.trace(ExHandler.getStackTraceButRoot(e));
			logger.info("Due to %s now using (log4j) default configuration", e.getClass().getName());
		}
	}

	/**
	 * Configura il root logger con il property file in ingresso. Se non trova il property file solleva una
	 * RuntimeException.
	 * 
	 * @param propertyFilePath
	 *            path al log4j.properties
	 * @throws RuntimeException
	 */
	public void configureLog4j(String propertyFilePath) throws RuntimeException {
		String absolutePath;
		File propertyFile = null;

		// controlla i vari casi in cui il path è assoluto
		if (StringHandler.toString(propertyFilePath).startsWith("file://")) {

			propertyFile = new File(propertyFilePath.substring("file://".length()));
			if (propertyFile != null && propertyFile.exists() && propertyFile.isFile())
				absolutePath = propertyFile.getAbsolutePath();
			else
				throw new RuntimeException("File " + propertyFilePath + " not found");

		} else if (StringHandler.toString(propertyFilePath).startsWith("file:")) {

			propertyFile = new File(propertyFilePath.substring("file:".length()));
			if (propertyFile != null && propertyFile.exists() && propertyFile.isFile())
				absolutePath = propertyFile.getAbsolutePath();
			else
				throw new RuntimeException("File " + propertyFilePath + " not found");

		} else if (FileHandler.isAbsolutePath(propertyFilePath)) {

			propertyFile = new File(propertyFilePath);
			if (propertyFile != null && propertyFile.exists() && propertyFile.isFile())
				absolutePath = propertyFile.getAbsolutePath();
			else
				throw new RuntimeException("File " + propertyFilePath + " not found");

		} else {

			// Se il path è relativo viene usato il FileHandler.findURL per cercare dentro al classpath
			absolutePath = FileHandler.findURL(propertyFilePath);
		}

		// Se in fine ha un path valido procede con la configurazione

		if (StringHandler.hasText(absolutePath)) {
			// PropertyConfigurator.configure(absolutePath);
			logger.info(
					"Log4j property file " + FileHandler.getName(absolutePath) + " successfully loaded! [path:" + FileHandler.getParent(absolutePath) + "]");
		} else {
			logger.info("Log4j configuration ignored due to a NULL property file");
		}
	}

}