package it.miriade.commons.collections;

/**
 * @author matteo sumberaz
 */
public abstract class Relation<T,V> {
	
	public abstract boolean apply(T t , V v);
}
