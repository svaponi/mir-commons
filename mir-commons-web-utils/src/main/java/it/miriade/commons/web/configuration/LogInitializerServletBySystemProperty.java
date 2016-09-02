package it.miriade.commons.web.configuration;

import javax.servlet.ServletConfig;

import it.miriade.commons.utils.StringHandler;

/**
 * Questa servlet ha lo scopo principale di inizializzare Log4J.<br>
 * Legge il Log4j.properties file path da una <strong>system property</strong> in modo da poter cambiare la
 * configurazione dei log senza dove ri-deploy-are l'applicazione.
 * 
 * <pre>
 * -Dmiriade.log.config.file.path=/absolute/path/to/log4j.properties
 * </pre>
 * 
 * Oppure è possibile impostare un init-param {@link Keys#LOG_CONFIG_FILE_PATH__propertySuffix}
 * che permette di utilizzare <strong>system property</strong> differenti (utile se si voglino differenziare i
 * log4j.properties di diverse applicazioni), es:
 * <h4>web.xml applicazione 1</h4>
 * 
 * <pre>
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;{@link Keys#LOG_CONFIG_FILE_PATH__propertySuffix}&lt;/param-name&gt;
 * 	&lt;param-value&gt;appl01&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * 
 * <h4>web.xml applicazione 2</h4>
 * 
 * <pre>
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;{@link Keys#LOG_CONFIG_FILE_PATH__propertySuffix}&lt;/param-name&gt;
 * 	&lt;param-value&gt;appl02&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 * 
 * <pre>
 * -Dmiriade.log4j.properties.path.appl01=/absolute/path/to/log4j_01.properties
 * -Dmiriade.log4j.properties.path.appl02=/absolute/path/to/log4j_02.properties
 * </pre>
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 * @modified Jul 20, 2015 12:22:25 PM
 */
public class LogInitializerServletBySystemProperty extends LogInitializerServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Metodo delegato a recuperare il path del configFile da caricare. Il path può essere un path assoluto, oppure un
	 * path relativo interno all'applicazione/war. Sovrascrivendolo si sovrascrive il comportamento del LogInitializer.
	 * Ritorna il PropertyFilePath leggendolo da una system property.
	 */
	@Override
	protected String getConfigFilePath(ServletConfig config) {
		String filePath = System.getProperty(getParamKey(config));
		logger.trace("<system-property> \"%s\" = \"%s\"", getParamKey(config), filePath);
		if (StringHandler.noText(filePath))
			return null;
		else
			return filePath;
	}

	/**
	 * Cerca se è stato definito un PARAM_KEY custom tra gli init-params in web.xml altrimenti usa quello di default.
	 * 
	 * @return
	 */
	private String getParamKey(ServletConfig config) {
		String customPathParamKey = config.getInitParameter(LOG_CONFIG_FILE_PATH__propertySuffix);
		if (StringHandler.hasText(customPathParamKey)) {
			customPathParamKey = customPathParamKey.trim();
			logger.trace("<init-param> %s = \"%s\"", LOG_CONFIG_FILE_PATH__propertySuffix, customPathParamKey);
			return MIRIADE_PREFIX + LOG_CONFIG_FILE_PATH__property + "." + customPathParamKey;
		}
		return MIRIADE_PREFIX + LOG_CONFIG_FILE_PATH__property;
	}
}
