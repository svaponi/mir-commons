package it.miriade.commons.model.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import it.miriade.commons.collections.CollectionUtils;
import it.miriade.commons.model.Model;
import it.miriade.commons.utils.ExHandler;
import it.miriade.commons.utils.StringHandler;

/**
 * @See {@link Dao}
 * @author svaponi
 */
public class DaoImpl implements Dao {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected boolean isTraceEnabled = logger.isTraceEnabled();
	protected boolean throwEx = false;
	protected String prefixInfo = "<any>";

	@Autowired
	protected HibernateTemplate hibernateTemplate;

	/**
	 * Imposta se rilanciare le eventuali eccezioni dopo averle catchate/loggate.
	 * 
	 * @param throwEx
	 */
	public void throwEx(boolean throwEx) {
		this.throwEx = throwEx;
	}

	// Getters and setters

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	// Constructors

	public DaoImpl() {
	}

	@Autowired
	public DaoImpl(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	// Metodi a basso livello (attenzione alle sessioni aperte)
	// ============================================================================================================

	/**
	 * @see it.miriade.commons.model.dao.Dao#executeUpdateQuery(String)
	 */
	public int executeUpdateQuery(String sql) {
		int result = -1;
		try {
			Session session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
			result = session.createSQLQuery(sql).executeUpdate();
		} catch (HibernateException e) {
			// in caso di org.hibernate.HibernateException: No Session found for current thread
			Session session = this.hibernateTemplate.getSessionFactory().openSession();
			Transaction tr = session.beginTransaction();
			result = session.createSQLQuery(sql).executeUpdate();
			tr.commit();
			session.close();
		}
		return result;
	}

	/**
	 * @see it.miriade.commons.model.dao.Dao#executeSelectQuery(String)
	 */
	public List<?> executeSelectQuery(String sql) {
		List<?> result = Collections.emptyList();
		try {
			Session session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
			result = session.createSQLQuery(sql).list();
		} catch (HibernateException e) {
			// in caso di org.hibernate.HibernateException: No Session found for current thread
			Session session = this.hibernateTemplate.getSessionFactory().openSession();
			Transaction tr = session.beginTransaction();
			result = session.createSQLQuery(sql).list();
			tr.commit();
			session.close();
		}
		return result;
	}

	/**
	 * @see it.miriade.commons.model.dao.Dao#beginSession()
	 */
	public Session beginSession() {
		Session session = this.hibernateTemplate.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}

	/**
	 * @see it.miriade.commons.model.dao.Dao#endSession()
	 */
	public void endSession(Session session) {
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * @see it.miriade.commons.model.dao.Dao#getSession()
	 */
	public Session getSession() {
		Session session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
		if (session == null || !session.isConnected())
			throw new RuntimeException("No session");
		return session;
	}

	// Metodi comnuni non legati a una entity class
	// ============================================================================================================

	/**
	 * @see it.miriade.commons.model.dao.Dao#getSequenceNextValue(String)
	 */
	@Override
	public Long getSequenceNextValue(String sequence) {
		Long key = (long) Model.ERROR_CODE;
		try {
			String SQL = String.format("select nextval('%s')", sequence);
			Query query = this.getSession().createSQLQuery(SQL);
			key = (Long) query.uniqueResult();
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug("%s retrieve sequence next value %d (sequence: %s)", prefixInfo, key, sequence);
		}
		return key;
	}

	/**
	 * @see it.miriade.commons.model.dao.Dao#deleteAll(String)
	 */
	@Override
	@Transactional(readOnly = false)
	public int deleteAll(String entityHqlName) {
		if (StringHandler.hasText(entityHqlName))
			try {
				String hql = String.format("delete from %s", entityHqlName);
				Query query = this.getSession().createQuery(hql);
				int result = query.executeUpdate();
				logger.debug("%s delete all entities of type %s, %d deleted", prefixInfo, entityHqlName, result);
				return result;
			} catch (ObjectNotFoundException | DataAccessException e) {
				logger.warn(ExHandler.getRoot(e));
				logger.debug(ExHandler.getStackTraceButRoot(e));
				if (throwEx)
					throw e;
			}
		return Model.ERROR_CODE;
	}

	/*
	 * HQL methods
	 * = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
	 */

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getTupleCollectionByHQL(String hql, Map<String, ?> params) {
		List<Object[]> records = new ArrayList<Object[]>();
		try {
			Query query = this.getSession().createQuery(hql);
			if (CollectionUtils.notEmpty(params))
				for (String param : params.keySet())
					if (params.get(param) instanceof Collection<?>)
						query.setParameterList(param, (Collection<?>) params.get(param));
					else
						query.setParameter(param, params.get(param));

			records = (List<Object[]>) query.list();
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve list of %d records (hql-query: %s, params: %s)", prefixInfo, CollectionUtils.size(records), hql, params));
		}
		/*
		 * svaponi - 21 Apr 2015
		 * Sostituisco il ritorno di una collection vuota per poter distinguere a valle lka differenza tra una query con
		 * risultato nullo ed una che ha generato un'eccezione
		 * return new ArrayList<Object[]>();
		 */
		return records;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object> getValueCollectionByHQL(String hql, Map<String, ?> params) {
		List<Object> records = new ArrayList<Object>();
		try {
			Query query = this.getSession().createQuery(hql);
			if (CollectionUtils.notEmpty(params))
				for (String param : params.keySet())
					if (params.get(param) instanceof Collection<?>)
						query.setParameterList(param, (Collection<?>) params.get(param));
					else
						query.setParameter(param, params.get(param));

			records = (List<Object>) query.list();
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve list of %d records (hql-query: %s, params: %s)", prefixInfo, CollectionUtils.size(records), hql, params));
		}
		return records;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object[] getTupleByHQL(String hql, Map<String, ?> params) {
		Object[] tuple = new Object[] {};
		try {
			Query query = this.getSession().createQuery(hql);
			if (CollectionUtils.notEmpty(params))
				for (String param : params.keySet())
					if (params.get(param) instanceof Collection<?>)
						query.setParameterList(param, (Collection<?>) params.get(param));
					else
						query.setParameter(param, params.get(param));

			List<Object[]> records = (List<Object[]>) query.list();
			tuple = CollectionUtils.notEmpty(records) ? (Object[]) records.get(0) : new Object[] {};
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve single record with %d columns (hql-query: %s, params: %s)", prefixInfo, tuple.length, hql, params));
		}
		return tuple;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Object getValueByHQL(String hql, Map<String, ?> params) {
		Object value = null;
		try {
			Query query = this.getSession().createQuery(hql);
			if (CollectionUtils.notEmpty(params))
				for (String param : params.keySet())
					if (params.get(param) instanceof Collection<?>)
						query.setParameterList(param, (Collection<?>) params.get(param));
					else
						query.setParameter(param, params.get(param));

			List<Object[]> list = (List<Object[]>) query.list();
			if (CollectionUtils.notEmpty(list))
				value = list.get(0);
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve single value (hql-query: %s, params: %s)", prefixInfo, hql, params));
		}
		return value;
	}

	/*
	 * TUPLE methods
	 * = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
	 */

	/**
	 * @see it.miriade.commons.model.dao.Dao#getTupleCollectionByNamedQueryAndParams(String, Map)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object[]> getTupleCollectionByNamedQueryAndParams(String queryName, Map<String, ?> params) {
		List<Object[]> records = new ArrayList<Object[]>();
		try {
			if (CollectionUtils.isEmpty(params)) {
				records = (List<Object[]>) this.hibernateTemplate.findByNamedQuery(queryName);
			} else {
				String[] paramKeys = params.keySet().toArray(new String[] {});
				Object[] paramValues = params.values().toArray(new Object[] {});
				records = (List<Object[]>) this.hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramKeys, paramValues);
			}
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve list of %d records (named-query: %s, params: %s)", prefixInfo, CollectionUtils.size(records), queryName,
					params));
		}
		return records;
	}

	/**
	 * @see it.miriade.commons.model.dao.Dao#getValueCollectionByNamedQueryAndParams(String, Map)
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Object> getValueCollectionByNamedQueryAndParams(String queryName, Map<String, ?> params) {
		List<Object> records = new ArrayList<Object>();
		try {
			if (CollectionUtils.isEmpty(params)) {
				records = (List<Object>) this.hibernateTemplate.findByNamedQuery(queryName);
			} else {
				String[] paramKeys = params.keySet().toArray(new String[] {});
				Object[] paramValues = params.values().toArray(new Object[] {});
				records = (List<Object>) this.hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramKeys, paramValues);
			}
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve list of %d records (named-query: %s, params: %s)", prefixInfo, CollectionUtils.size(records), queryName,
					params));
		}
		return records;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getTupleByNamedQueryAndParams(String queryName, Map<String, ?> params) {
		Object[] tuple = new Object[] {};
		try {
			List<Object[]> records;
			if (CollectionUtils.isEmpty(params)) {
				records = (List<Object[]>) this.hibernateTemplate.findByNamedQuery(queryName);
			} else {
				String[] paramKeys = params.keySet().toArray(new String[] {});
				Object[] paramValues = params.values().toArray(new Object[] {});
				records = (List<Object[]>) this.hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramKeys, paramValues);
			}
			tuple = CollectionUtils.notEmpty(records) ? (Object[]) records.get(0) : new Object[] {};
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve single record with %d columns (named-query: %s, params: %s)", prefixInfo, tuple.length, queryName, params));
		}
		return tuple;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValueByNamedQueryAndParams(String queryName, Map<String, ?> params) {
		Object value = null;
		try {
			List<Object[]> records;
			if (CollectionUtils.isEmpty(params)) {
				records = (List<Object[]>) this.hibernateTemplate.findByNamedQuery(queryName);
			} else {
				String[] paramKeys = params.keySet().toArray(new String[] {});
				Object[] paramValues = params.values().toArray(new Object[] {});
				records = (List<Object[]>) this.hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramKeys, paramValues);
			}
			if (CollectionUtils.notEmpty(records))
				value = records.get(0);
		} catch (Exception e) {
			logger.warn(ExHandler.getRoot(e));
			logger.debug(ExHandler.getStackTraceButRoot(e));
			if (throwEx)
				throw e;
		} finally {
			logger.debug(String.format("%s retrieve single value (named-query: %s, params: %s)", prefixInfo, queryName, params));
		}
		return value;
	}

}
