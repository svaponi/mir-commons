package it.miriade.commons.model.collections;

/**
 * @author matteo sumberaz
 */
public interface CollectionConverter<V,T> {

	public T convert(V v);
}
