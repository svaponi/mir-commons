package it.miriade.commons.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 */
public class CollectionFormatter {

	public static final String DELIM = ", ";

	public static String merge(Collection<?> coll) {

		return merge(DELIM, coll);
	}

	public static String merge(String delim, Collection<?> coll) {

		StringBuffer buf = new StringBuffer();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {

			Object obj = it.next();
			buf.append(obj);
			if (it.hasNext())
				buf.append(delim);

		}
		return buf.toString();
	}

	public static String join(Object... args) {

		return join(DELIM, args);
	}

	public static String join(String delim, Object... args) {

		StringBuffer format = new StringBuffer();
		for (int i = 0; i < args.length; i++) {

			if (i > 0)
				format.append(delim);

			if (args[i] instanceof Object[] || args[i] instanceof String[]) {

				Object[] temp = (Object[]) args[i];
				format.append("%s");
				args[i] = Arrays.deepToString(temp);

			} else if (args[i] instanceof Collection<?>) {

				Collection<?> temp = (Collection<?>) args[i];
				format.append("%s");
				args[i] = CollectionFormatter.merge(temp);

			} else if (args[i] instanceof Integer || args[i] instanceof Long || args[i] instanceof Short)

				format.append("%d");
			else
				format.append("%s");

		}
		return String.format(format.toString(), args);
	}
}
