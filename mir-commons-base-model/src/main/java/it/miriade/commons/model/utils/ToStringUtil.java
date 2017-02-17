package it.miriade.commons.model.utils;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ToStringUtil {

	/**
	 * Stampa ricorsivamente tutti i field dell'oggetto in input
	 * 
	 * @param obj
	 * @return
	 */
	public static String recursiveToString(Object obj) {
		return new ReflectionToStringBuilder(obj, new RecursiveToStringStyle()).toString();
	}

}
