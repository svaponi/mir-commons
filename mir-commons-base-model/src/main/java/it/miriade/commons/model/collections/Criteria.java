package it.miriade.commons.model.collections;

/**
 * @author matteo sumberaz
 */
public abstract class Criteria<T> {
	
	public abstract boolean apply(T t);
}
