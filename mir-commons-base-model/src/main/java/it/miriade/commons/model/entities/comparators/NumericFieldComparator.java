package it.miriade.commons.model.entities.comparators;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Comparator;

import it.miriade.commons.model.entities.ModelEntity;

/**
 * Comparator che confronta due oggetti POJO in base al valore numerico di una loro proprietà, sia questa {@link Short},
 * {@link Integer}, {@link BigDecimal}, {@link Long}, {@link Float} o {@link Double}. Il nome di tale proprietà
 * è settato nel costruttore.
 * 
 * @See {@link Comparator}
 * @author svaponi
 */
public class NumericFieldComparator implements Comparator<ModelEntity<?>> {

	private String getterName;

	public NumericFieldComparator(String fieldName) {
		super();
		this.getterName = "get" + fieldName;
	}

	@Override
	public int compare(ModelEntity<?> one, ModelEntity<?> another) {
		try {
			for (Method getter : one.getClass().getMethods()) {
				if (getter.getName().equalsIgnoreCase(getterName)) {
					Object result = getter.invoke(one);
					Object anotherResult = getter.invoke(another);
					if (result instanceof Short)
						return ((Short) result).compareTo((Short) anotherResult);
					else if (result instanceof Integer)
						return ((Integer) result).compareTo((Integer) anotherResult);
					else if (result instanceof BigDecimal)
						return ((BigDecimal) result).compareTo((BigDecimal) anotherResult);
					else if (result instanceof Long)
						return ((Long) result).compareTo((Long) anotherResult);
					else if (result instanceof Float)
						return ((Float) result).compareTo((Float) anotherResult);
					else if (result instanceof Double)
						return ((Double) result).compareTo((Double) anotherResult);
				}
			}

		} catch (Exception e) {
		}
		return 1;
	}
}
