package it.miriade.commons.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;

/**
 * <strong>Utility.</strong> Stampa le eccezioni, fornendo metodi per estrarre le varie parti dello stack-trace.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM 2015-03-01
 */
public class ExHandler {

	public static final String DATE_FORMAT_TEMPLATE = "HH:mm:ss:SSS";
	public static final String NEW_LINE = "\n";
	public static final String TAB = "\t";
	public static final String CLASSIC_STACK_TRACE_ELEMENT_FORMAT_TEMPLATE = "at %s.%s(%s:%d)";
	public static final String STACK_TRACE_ELEMENT_FORMAT_TEMPLATE = "%s.%s(%s:%d)";

	/**
	 * Questa funzione analizza il logger attualmente in uso e, secondo la priorità settata (info, debug, ecc..) sceglie
	 * cosa stampare dell'eccezione. Nello specifico se il log è in <strong>TRACE</strong> stampa tutto lo stack-trace,
	 * <strong>ALTRIMENTI</strong> (da DEBUG in su) stampa solo la Root Cause.
	 * 
	 * @param logger
	 *            logger in uso
	 * @param e
	 *            eccezione sollevata
	 */
	public static void printEx(Logger logger, Exception e) {
		if (logger instanceof Logger) {
			Logger loggr = (Logger) logger;
			if (loggr.isTraceEnabled())
				loggr.trace(getStackTrace(e));
			else
				loggr.error(getRoot(e));

		} else if (logger.isTraceEnabled())
			logger.trace(getStackTrace(e));
		else
			logger.error(getRoot(e));
	}

	/**
	 * Return a string containing the root element, that is the first row reported in stack trace.
	 * 
	 * @param e
	 * @return string containing stack trace
	 */
	public static String getRoot(Exception e) {
		return String.format("%s: %s", e.getClass().getName(), e.getMessage());
	}

	/**
	 * Return a string containing the classic format stack trace, as it would be displayed on stdOut.
	 * 
	 * @param e
	 * @return string containing stack trace
	 */
	public static String getStackTraceAsPrintedByWriter(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	/**
	 * Like getPrintedStackTrace but avoids last useless new line (\n)
	 * 
	 * @param e
	 * @return string containing stack trace
	 */
	public static String getStackTrace(Exception e, int start, int limit) {
		StringBuffer buf = new StringBuffer(getRoot(e));
		appendStackTrace(e, start, limit, buf);
		return buf.toString();
	}

	public static String getStackTrace(Exception e, int limit) {
		return getStackTrace(e, 0, limit);
	}

	public static String getStackTrace(Exception e) {
		return getStackTrace(e, 0, Integer.MAX_VALUE);
	}

	/**
	 * Like getPrintedStackTrace but skips the root element (which, in the suggested use of this method, is supposed to
	 * be already displayed).
	 * 
	 * @param e
	 * @param limit
	 *            numero di elementi dello stack da stampare
	 * @return string containing stack trace
	 */
	public static String getStackTraceButRoot(Exception e, int start, int limit) {
		StringBuffer buf = new StringBuffer("Stack trace of " + e.getClass().getName() + ":");
		appendStackTrace(e, start, limit, buf);
		return buf.toString();
	}

	public static String getStackTraceButRoot(Exception e, int limit) {
		return getStackTraceButRoot(e, 0, limit);
	}

	public static String getStackTraceButRoot(Exception e) {
		return getStackTraceButRoot(e, 0, Integer.MAX_VALUE);
	}

	/**
	 * Like getPrintedStackTrace but skips the root element (which, in the suggested use of this method, is supposed to
	 * be already displayed).
	 * 
	 * @param e
	 * @return string containing stack trace
	 */
	public static String getStackTraceElement(StackTraceElement elem) {
		return String.format(STACK_TRACE_ELEMENT_FORMAT_TEMPLATE, elem.getClassName(), elem.getMethodName(), elem.getFileName(), elem.getLineNumber());
	}

	/**
	 * Metodo ausiliare per appendere porzioni di stacktrace
	 * 
	 * @param e
	 * @param start
	 * @param limit
	 * @param buf
	 */
	private static void appendStackTrace(Exception e, int start, int limit, Appendable buf) {
		StackTraceElement[] stack = e.getStackTrace();
		try {
			for (int i = Math.min(Math.max(start, 0), stack.length); i < Math.min(stack.length, limit); i++)
				buf.append(NEW_LINE).append(TAB).append(getStackTraceElement(stack[i]));
			if (limit < stack.length)
				buf.append(NEW_LINE).append(TAB).append("...");
		} catch (IOException ignore) {
		}
	}

	// TODO: implementare stampa stack trace cpon livello di profodità
	// private void String getStackTrace(StringBuffer buf, Exception e, int level){
	// for (StackTraceElement elem : e.getStackTrace()) {
	// if (--level == 0) break;
	// buf.append( String.format(STACK_TRACE_ELEMENT_FORMAT_TEMPLATE, elem.getClassName(), elem.getMethodName(),
	// elem.getFileName(), elem.getLineNumber() ) );
	// }
	// }
}
