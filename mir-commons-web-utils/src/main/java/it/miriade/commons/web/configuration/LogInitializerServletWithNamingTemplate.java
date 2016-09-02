package it.miriade.commons.web.configuration;

import java.util.Map;

import javax.servlet.ServletConfig;

import it.miriade.commons.utils.StringHandler;

/**
 * Questa servlet ha lo scopo principale di inizializzare Log4J, per fare ciò sfrutta le potenzialità delle Servlet
 * come: leggere parametri di condifgurazione dal web.xml (init-param) e partire automaticamente allo start-up
 * dell'applicazione.<br/>
 * {@link LogInitializerServletWithNamingTemplate#LOG_CONFIG_FILE_NAMING_TEMPLATE}: init-param che definisce il
 * template
 * per
 * costruire il nome del property file di log4j. Per costruire il path finale al file viene fatto il merge con il
 * parametro dell'environment corrente<br/>
 * <br/>
 * <strong>Esempio:</strong>
 * 
 * <pre>
 * &lt;init-param&gt;
 * 	&lt;param-name&gt;log.config.file.naming.template&lt;/param-name&gt;
 * 	&lt;param-value&gt;path/to/log4j%s.properties&lt;/param-value&gt;
 * &lt;/init-param&gt;
 * </pre>
 *
 * Ovviamente occorre definire il parametro dell'environment:
 * 
 * <pre>
 * JAVA_OPTS = &quot;$JAVA_OPTS -Dmiriade.currentenv=PROD&quot;
 * </pre>
 * 
 * Da questa configurazione risulterà:
 * 
 * <pre>
 * <code>path/to/log4j%s.properties + PROD ==> path/to/log4jPROD.properties</code>
 * </pre>
 * 
 * Quest'ultimo sarà il file di log usato per l'ambiente PROD. A seguire come inserire il parametro in web.xml.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 * @modified Jul 20, 2015 12:22:25 PM
 */
public class LogInitializerServletWithNamingTemplate extends LogInitializerServlet implements Keys {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_FILE_NAMING_TEMPLATE_defaultValue = "logging/log4j_%s.properties";

	/*
	 * Esempio di utilizzo dei parametri di inizializzazione della servlet:
	 * <init-param>
	 * <param-name>propertyFileNamingTemplate</param-name>
	 * <param-value>path/to/logging/configuration/directory/log4j%s.properties</param-value>
	 * </init-param>
	 */

	/**
	 * Metodo delegato a recuperare il path del propertyFile di log4j
	 * 
	 * @param config
	 * @return
	 */
	protected String getConfigFilePath(ServletConfig config) {
		return String.format(this.readNamingTemplate(config), this.detectEnv(MIRIADE_PREFIX + CURRENTENV));
	}

	/**
	 * Cerca nelle System Propeties o nel Java Environment il parametro {@link Keys#CURRENTENV}
	 * 
	 * @return
	 */
	public String readNamingTemplate(ServletConfig config) {
		String param = config.getInitParameter(LOG_CONFIG_FILE_NAMING_TEMPLATE);
		if (StringHandler.hasText(param)) {
			param = param.trim();
			if (param.startsWith("classpath:"))
				param = param.substring("classpath:".length());
			logger.trace("<init-param> %s = \"%s\"", LOG_CONFIG_FILE_NAMING_TEMPLATE, param);
		} else {
			param = PROPERTY_FILE_NAMING_TEMPLATE_defaultValue;
		}
		return param;
	}

	/**
	 * Cerca nelle System Propeties o nel Java Environment il parametro {@link Keys#CURRENTENV}
	 * 
	 * @return
	 */
	public String detectEnv(String propertyName) {
		String tmp = "";
		Map<String, String> env = System.getenv();
		if (env.containsKey(propertyName))
			tmp += env.get(propertyName);
		else if (System.getProperty(propertyName) != null)
			tmp += System.getProperty(propertyName);

		logger.info("Detected environment \"%s\" = \"%s\"", propertyName, tmp);
		return tmp.trim();
	}
}
