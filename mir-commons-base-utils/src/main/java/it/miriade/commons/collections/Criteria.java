package it.miriade.commons.collections;

/**
 * @author matteo sumberaz
 */
public abstract class Criteria<T> {
	
	public abstract boolean apply(T t);
}
