package it.miriade.commons.logging;

import java.util.Arrays;

import org.slf4j.LoggerFactory;

import it.miriade.commons.utils.StringHandler;

/**
 * Classe DECORATOR, aggiunge nuove funzionalità al {@link LogWrapper}, in particolare la possibilità di usare lo
 * {@link String#format(String, Object...)} direttamente sul oggetto. Esempio:
 * <blockquote>
 * <code>final LogWrapper log = LogWrapper.getWrapper(this.getClass());</code>
 * <code>log.info("Property => %s=%s", key, props.get(key));</code>
 * </blockquote>
 * Con Logback o slf4j è inutile perchè hanno il param injection con il {}
 * 
 * @author svaponi
 * @created Jun 30, 2016 10:45:11 AM
 */
public class LogWrapper {

	public static LogWrapper getLogger(final Class<?> clazz) {
		return new LogWrapper(clazz);
	}

	public static LogWrapper getLogger(final org.slf4j.Logger log4j) {
		return new LogWrapper(log4j);
	}

	private org.slf4j.Logger log;

	public LogWrapper(final Class<?> clazz) {
		log = LoggerFactory.getLogger(clazz);
	}

	public LogWrapper(final org.slf4j.Logger log4j) {
		log = log4j;
	}

	public void fatal(final String format, final Object... args) {
		this.error(format, args);
	}

	public void error(final String format, final Object... args) {
		log.error(String.format(format, args));
	}

	public void warn(final String format, final Object... args) {
		log.warn(String.format(format, args));
	}

	public void info(final String format, final Object... args) {
		log.info(String.format(format, args));
	}

	public void debug(final String format, final Object... args) {
		log.debug(String.format(format, args));
	}

	public void trace(final String format, final Object... args) {
		log.trace(String.format(format, args));
	}

	// ============================================================================================

	public void fatal(final Object... args) {
		this.error(args);
	}

	public void error(final Object... args) {
		if (args != null && args.length == 1)
			log.error(StringHandler.toString(args[0]));
		else if (args != null && args.length > 1)
			log.error(String.format(StringHandler.toString(args[0]), Arrays.copyOfRange(args, 1, args.length)));
	}

	public void warn(final Object... args) {
		if (args != null && args.length == 1)
			log.warn(StringHandler.toString(args[0]));
		else if (args != null && args.length > 1)
			log.warn(String.format(StringHandler.toString(args[0]), Arrays.copyOfRange(args, 1, args.length)));
	}

	public void info(final Object... args) {
		if (args != null && args.length == 1)
			log.info(StringHandler.toString(args[0]));
		else if (args != null && args.length > 1)
			log.info(String.format(StringHandler.toString(args[0]), Arrays.copyOfRange(args, 1, args.length)));
	}

	public void debug(final Object... args) {
		if (args != null && args.length == 1)
			log.debug(StringHandler.toString(args[0]));
		else if (args != null && args.length > 1)
			log.debug(String.format(StringHandler.toString(args[0]), Arrays.copyOfRange(args, 1, args.length)));
	}

	public void trace(final Object... args) {
		if (args != null && args.length == 1)
			log.trace(StringHandler.toString(args[0]));
		else if (args != null && args.length > 1)
			log.trace(String.format(StringHandler.toString(args[0]), Arrays.copyOfRange(args, 1, args.length)));
	}
	// ============================================================================================

	// public void fatal(final Object... args) {
	// log4j.error(CollectionFormatter.join(args));
	// }
	//
	// public void error(final Object... args) {
	// log4j.error(CollectionFormatter.join(args));
	// }
	//
	// public void warn(final Object... args) {
	// log4j.warn(CollectionFormatter.join(args));
	// }
	//
	// public void info(final Object... args) {
	// log4j.info(CollectionFormatter.join(args));
	// }
	//
	// public void debug(final Object... args) {
	// log4j.debug(CollectionFormatter.join(args));
	// }
	//
	// public void trace(final Object... args) {
	// log4j.trace(CollectionFormatter.join(args));
	// }

	// ============================================================================================

	public void fatal(final String content) {
		this.error(content);
	}

	public void error(final String content) {
		log.error(content);
	}

	public void warn(final String content) {
		log.warn(content);
	}

	public void info(final String content) {
		log.info(content);
	}

	public void debug(final String content) {
		log.debug(content);
	}

	public void trace(final String content) {
		log.trace(content);
	}

	// ============================================================================================

	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

}
