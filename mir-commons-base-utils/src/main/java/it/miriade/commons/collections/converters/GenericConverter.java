package it.miriade.commons.collections.converters;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.miriade.commons.collections.CollectionConverter;
import it.miriade.commons.utils.ExHandler;

/**
 * Converte un oggetto di classe X in uno di classe Y ammesso che: Y sia castabile ad X o possieda un costruttore con X
 * come unico argomento
 * 
 * @author svaponi
 * @created Jul 24, 2016 1:08:31 PM
 * @param <X>
 * @param <Y>
 */
public class GenericConverter<X, Y> implements CollectionConverter<X, Y> {

	protected Class<X> xClass;
	protected Class<Y> yClass;

	protected Logger _logger = LoggerFactory.getLogger(this.getClass());

	public GenericConverter(Class<X> xClass, Class<Y> yClass) {
		this.xClass = xClass;
		this.yClass = yClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Y convert(X entity) {

		Y resource = null;

		// prima tenta un semplice cast
		try {
			return (Y) entity;
		} catch (Exception e) {
			_logger.debug(ExHandler.getRoot(e));
		}

		// poi cerca un costruttore pubblico
		try {
			return (Y) xClass.getConstructor(yClass).newInstance(entity);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			_logger.debug(ExHandler.getRoot(e));
		}

		// poi cerca un qualsiasi costruttore valido
		try {
			return (Y) xClass.getDeclaredConstructor(yClass).newInstance(entity);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			_logger.warn(ExHandler.getRoot(e));
		}

		// infine invoca il costruttore di default senza parametri
		try {
			return (Y) xClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			_logger.error(ExHandler.getRoot(e));
		}
		return resource;
	}
}