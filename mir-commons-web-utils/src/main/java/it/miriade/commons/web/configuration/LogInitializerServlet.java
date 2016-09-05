package it.miriade.commons.web.configuration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.miriade.commons.logging.LogInitializer;
import it.miriade.commons.utils.StringHandler;

/**
 * Questa servlet è la base per inizializzare il logging in una web-app.
 * Definisce un metodo astratto che torna il path del log4j.propeties e ne usa il risultato per inizializzare la
 * libreria.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 * @modified Jul 20, 2015 12:22:25 PM
 */
public abstract class LogInitializerServlet extends HttpServlet implements Keys {

	private static final long serialVersionUID = 1L;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected String configFilePath;
	protected LogInitializer initializer;

	/**
	 * Default initialize method called by servlet container.
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		configFilePath = this.getConfigFilePath(config);
		logger.trace("Log config file to use: %s", configFilePath);

		try {
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			Assert.notNull(initializer);
			initializer = applicationContext.getBean(LogInitializer.class);

			// Inizializzazione del logger
			initializer.reset(configFilePath);
		} catch (Exception e) {
			logger.trace("Log configuration failed! %s", e.getMessage());
		}
	}

	/**
	 * Metodo delegato a recuperare il path del configFile da caricare. Il path può essere un path assoluto, oppure un
	 * path relativo interno all'applicazione/war.
	 * 
	 * @param config
	 * @return
	 */
	protected String getConfigFilePath(ServletConfig config) {
		return getInitParameterWithSystemFallback(config, LOG_CONFIG_FILE_PATH__property);
	}

	/**
	 * Legge il paramentro <code>&lt;init-param&gt;</code> dalla configurazione della servlet, ovvero dal web.xml.
	 * 
	 * @return
	 */
	public String getInitParameter(ServletConfig config, String key) {
		String param = config.getInitParameter(key);
		if (StringHandler.hasText(param)) {
			param = param.trim();
			logger.debug("<init-param> %s = \"%s\"", key, param);
		}
		return param;
	}

	/**
	 * Legge il paramentro <code>&lt;init-param&gt;</code> dalla configurazione della servlet, ovvero dal web.xml. In
	 * caso di parametro mancante controlla tra le sistem propeties preappendendo l'identificativo di miriade
	 * "-Dmiriade."
	 * 
	 * @return
	 */
	public String getInitParameterWithSystemFallback(ServletConfig config, String key) {
		String param = config.getInitParameter(key);
		if (StringHandler.hasText(param)) {
			param = param.trim();
			logger.debug("<init-param> \"%s\" = \"%s\"", key, param);
		} else {
			param = System.getProperty("-Dmiriade." + key);
			if (StringHandler.hasText(param))
				logger.debug("<system-property> \"%s\" = \"%s\"", key, param);
			else
				logger.warn("Missing parameter \"%s\"", key, param);
		}
		return param;
	}
}
