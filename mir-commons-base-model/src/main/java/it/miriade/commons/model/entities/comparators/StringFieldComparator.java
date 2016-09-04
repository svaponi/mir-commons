package it.miriade.commons.model.entities.comparators;

import java.lang.reflect.Method;
import java.util.Comparator;

import it.miriade.commons.model.entities.ModelEntity;

/**
 * Comparator che confronta due oggetti POJO in base al valore di una loro proprietà di testo. Il nome di tale proprietà
 * è settato nel costruttore.
 * 
 * @See {@link Comparator}
 * @author svaponi
 */
public class StringFieldComparator implements Comparator<ModelEntity<?>> {

	private String getterName;

	public StringFieldComparator(String fieldName) {
		super();
		this.getterName = "get" + fieldName;
	}

	@Override
	public int compare(ModelEntity<?> one, ModelEntity<?> another) {
		try {
			for (Method getter : one.getClass().getMethods()) {
				if (getter.getName().equalsIgnoreCase(getterName)) {
					String result = (String) getter.invoke(one);
					String anotherResult = (String) getter.invoke(another);
					return result.compareTo(anotherResult);
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return 1;
	}
}
