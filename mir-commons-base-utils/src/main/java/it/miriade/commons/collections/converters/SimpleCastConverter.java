package it.miriade.commons.collections.converters;

import it.miriade.commons.collections.CollectionConverter;

public class SimpleCastConverter<X, Y> implements CollectionConverter<X, Y> {

	@Override
	@SuppressWarnings("unchecked")
	public Y convert(X toCast) {

		Y toReturn = (Y) toCast;

		return toReturn;
	}
}
