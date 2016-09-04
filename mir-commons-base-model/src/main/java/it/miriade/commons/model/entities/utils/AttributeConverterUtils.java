package it.miriade.commons.model.entities.utils;

/**
 * Utility con metodi statici per convertire oggetti.
 * 
 * @author svaponi
 */
public class AttributeConverterUtils {

	public static Integer castToInteger(final String uid) {
		try {
			return Integer.decode(uid);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static String convertFromInteger(final Integer id) {
		try {
			return id.toString();
		} catch (Exception nfe) {
			return null;
		}
	}

	public static Long castToLong(final String uid) {
		try {
			return Long.decode(uid);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static Short castToShort(final String uid) {
		try {
			return Short.decode(uid);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static Float castToFloat(final String uid) {
		try {
			return Float.parseFloat(uid);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}

	public static Double castToDouble(final String uid) {
		try {
			return Double.parseDouble(uid);
		} catch (NumberFormatException nfe) {
			return null;
		}
	}
}
