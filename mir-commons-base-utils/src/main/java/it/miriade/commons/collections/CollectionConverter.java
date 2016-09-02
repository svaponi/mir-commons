package it.miriade.commons.collections;

/**
 * @author matteo sumberaz
 */
public interface CollectionConverter<V,T> {

	public T convert(V v);
}
