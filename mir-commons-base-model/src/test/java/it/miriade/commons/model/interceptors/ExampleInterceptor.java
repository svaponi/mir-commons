package it.miriade.commons.model.interceptors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * INTERCEPTOR GENERICO: ascolta/intercetta gli eventi di scrittura/lettura sul database. Stampa in log il nome dei
 * metodi per individuarne la sequenzialità.
 * 
 * @author svaponi
 * @created Nov 13, 2015 12:01:46 PM
 */
public class ExampleInterceptor extends EmptyInterceptor {

	/*
	 * Gli interceptors vanno definiti nel sessionFactory, ovvero nella configurazione di hibernate.
	 * <bean id="sessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
	 * [...]
	 * <property name="entityInterceptor" ref="defaultInterceptor" />
	 * </bean>
	 * <bean id="defaultInterceptor" class="it.miriade.commons.model.interceptors.ExampleInterceptor" />
	 */

	private static final long serialVersionUID = 5557009880503302678L;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.debug("onLoad({}, {}, {}, {}, {})", entity, id, Arrays.deepToString(state), Arrays.deepToString(propertyNames), Arrays.deepToString(types));
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.debug("onDelete({}, {}, {}, {}, {})", entity, id, Arrays.deepToString(state), Arrays.deepToString(propertyNames), Arrays.deepToString(types));
		super.onDelete(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		log.debug("onFlushDirty({}, {}, {}, {}, {}, {})", entity, id, Arrays.deepToString(currentState), Arrays.deepToString(previousState),
				Arrays.deepToString(propertyNames), Arrays.deepToString(types));
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		log.debug("onSave({}, {}, {}, {}, {})", entity, id, Arrays.deepToString(state), Arrays.deepToString(propertyNames), Arrays.deepToString(types));
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void postFlush(Iterator entities) {
		log.debug("postFlush({}, {}, [..])", entities.hasNext() ? entities.next() : "", entities.hasNext() ? entities.next() : "");
		super.postFlush(entities);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void preFlush(Iterator entities) {
		log.debug("preFlush({}, {}, [..])", entities.hasNext() ? entities.next() : "", entities.hasNext() ? entities.next() : "");
		super.preFlush(entities);
	}

	@Override
	public Boolean isTransient(Object entity) {
		log.debug("isTransient({})", entity);
		return super.isTransient(entity);
	}

	@Override
	public Object instantiate(String entityName, EntityMode entityMode, Serializable id) {
		log.debug("instantiate({}, {}, {})", entityName, entityMode, id);
		return super.instantiate(entityName, entityMode, id);
	}

	@Override
	public void afterTransactionBegin(Transaction tx) {
		log.debug("afterTransactionBegin({})", "<tx>");
		super.afterTransactionBegin(tx);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		log.debug("afterTransactionCompletion({})", "<tx>");
		super.afterTransactionCompletion(tx);
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		log.debug("beforeTransactionCompletion({})", "<tx>");
		super.beforeTransactionCompletion(tx);
	}

	@Override
	public String onPrepareStatement(String sql) {
		log.debug("onPrepareStatement({})", sql);
		return super.onPrepareStatement(sql);
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
		log.debug("onCollectionRemove({}, {})", collection, key);
		super.onCollectionRemove(collection, key);
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
		log.debug("onCollectionRecreate({}, {})", collection, key);
		super.onCollectionRecreate(collection, key);
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
		log.debug("onCollectionUpdate({}, {})", collection, key);
		super.onCollectionUpdate(collection, key);
	}

}