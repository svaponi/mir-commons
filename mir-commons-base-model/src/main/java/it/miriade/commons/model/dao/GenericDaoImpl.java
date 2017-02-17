package it.miriade.commons.model.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import it.miriade.commons.model.Model;
import it.miriade.commons.model.entities.ModelEntity;
import it.miriade.commons.model.utils.ToStringUtil;

/**
 * @See {@link GenericDao}
 * @author svaponi
 * @param <E>
 * @param <PK>
 */
@SuppressWarnings("unchecked")
public class GenericDaoImpl<E extends ModelEntity<PK>, PK extends Serializable> implements GenericDao<E, PK> {

	@Value("${miriade.dao.throwex:false}")
	protected boolean throwEx = false;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final boolean isTraceEnabled = logger.isTraceEnabled();
	private static final String PREFIX_TEMPLATE = "<%s>";
	private String prefixInfo = "<?>";

	@Autowired
	private HibernateTemplate hibernateTemplate;

	private Class<E> entityClass;

	public GenericDaoImpl() {
		super();
	}

	public GenericDaoImpl(Class<E> entityClass) {
		super();
		this.setEntityClass(entityClass);
	}

	/**
	 * Imposta se rilanciare le eventuali eccezioni dopo averle
	 * catch-ate/loggate.
	 * 
	 * @param throwEx
	 */
	public void throwEx(boolean throwEx) {
		this.throwEx = throwEx;
	}

	/**
	 * @return la classe della entity
	 */
	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
		this.prefixInfo = String.format(PREFIX_TEMPLATE, entityClass.getSimpleName());
	}

	/**
	 * 
	 * @return
	 */
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * @return la sessione attiva di Hibernate
	 */
	public Session getSession() {
		try {
			Session session = this.hibernateTemplate.getSessionFactory().getCurrentSession();
			if (session == null || !session.isConnected())
				session = this.hibernateTemplate.getSessionFactory().openSession();
			return session;
		} catch (HibernateException e) {
		}
		return this.hibernateTemplate.getSessionFactory().openSession();
	}

	// Object methods
	// ============================================================================================================

	public E getInstance() {
		if (entityClass != null)
			try {
				return this.entityClass.newInstance();
			} catch (Exception e) {
				logger.error("Unable to create new instance of " + this.entityClass.getSimpleName());
			}
		return null;
	}

	@Override
	protected E clone() {
		E cloned = getInstance();
		return cloned;
	}

	@Override
	public String toString() {
		return String.format("%s<%s> [%s] ", this.getClass().getSimpleName(), entityClass.getSimpleName(),
				super.toString());
	}

	// GENERIC DAO methods
	// ============================================================================================================

	@Transactional(readOnly = false)
	public void save(E entity) {
		try {
			this.hibernateTemplate.saveOrUpdate(entity);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s save entity %s ", prefixInfo, entityToString(entity)));
		}
	}

	@Transactional(readOnly = false)
	public PK saveAndGetId(E entity) {
		try {
			Serializable result = this.hibernateTemplate.save(entity);
			return (PK) result;
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s save entity %s ", prefixInfo, entityToString(entity)));
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void merge(E entity) {
		try {
			this.hibernateTemplate.merge(entity);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s merge entity %s ", prefixInfo, entityToString(entity)));
		}
	}

	@Transactional(readOnly = false)
	public E remove(PK id) {
		E entity = null;
		try {
			entity = (E) this.hibernateTemplate.get(this.entityClass, id);
			if (entity != null)
				this.hibernateTemplate.delete(entity);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s remove entity with id %s %s", prefixInfo, id,
					entity != null ? "SUCCESS" : "FAIL"));
		}
		return entity;
	}

	@Transactional(readOnly = false)
	public int deleteAll() {
		try {
			String hql = String.format("delete from %s", this.entityClass.getSimpleName());
			Query query = this.getSession().createQuery(hql);
			int result = query.executeUpdate();
			logger.debug(String.format("%s delete all entities, %d deleted", prefixInfo, result));
			return result;
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		}
		return Model.ERROR_CODE;
	}

	@Transactional(readOnly = false)
	public int deleteAll(Collection<E> entities) {
		try {
			if (entities == null || entities.isEmpty())
				return 0;
			int result = entities.size();
			this.hibernateTemplate.deleteAll(entities);
			logger.debug(String.format("%s delete all collection entities, %d deleted", prefixInfo, result));
			return result;
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		}
		return Model.ERROR_CODE;
	}

	// @Transactional READ ONLY

	@Transactional(readOnly = true)
	public int getCount() {
		List<E> list = this.getList();
		if (list != null)
			return list.size();
		else
			return Model.ERROR_CODE;
	}

	@Transactional(readOnly = true)
	public E get(PK id) {
		E entity = null;
		try {
			entity = (E) this.hibernateTemplate.get(this.entityClass, id);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve entity with id %s %s", prefixInfo, id,
					entity != null ? "SUCCESS" : "FAIL"));
		}
		return entity;
	}

	@Transactional(readOnly = true)
	public boolean exists(PK id) {
		E entity = null;
		try {
			entity = (E) this.hibernateTemplate.get(this.entityClass, id);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s search entity with id %s %s", prefixInfo, id,
					entity != null ? "EXISTS" : "NOT EXISTS"));
		}
		return entity != null;
	}

	public Set<PK> getAllIDs() {
		Set<PK> uniqueIDs = new HashSet<PK>();
		List<E> list = this.getList();
		if (list != null)
			for (E e : list)
				if (!uniqueIDs.contains(e.getUid()))
					uniqueIDs.add(e.getUid());
		return uniqueIDs;
	}

	@Transactional(readOnly = true)
	public List<E> getList() {
		List<E> results = null;
		try {
			String hql = "from " + this.entityClass.getSimpleName();
			results = (List<E>) this.hibernateTemplate.find(hql);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(
					String.format("%s retrieve list of %d items", prefixInfo, (results == null ? 0 : results.size())));
		}
		return results;
	}

	@Transactional(readOnly = true)
	public List<E> getList(int offset, int size) {
		List<E> results = null;
		try {
			/*
			 * DISTINCT_ROOT_ENTITY aggiunto per evitare doppioni nelle entity
			 * che contengono collection @OneToMany su tabelle esterne.
			 * ATTENZIONE! La paginazione funziona solo se LAZY fetch,
			 * altrimenti prima limita il numero di risulti poi fa la DISTINCT,
			 * ritornando un numero inferiore di elementi per pagina.
			 */
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(this.entityClass);
			detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			results = (List<E>) this.hibernateTemplate.findByCriteria(detachedCriteria, offset, size);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve list of %d items (offset: %s, size: %s)", prefixInfo,
					(results == null ? 0 : results.size()), offset, size));
		}
		return results;
	}

	@Transactional(readOnly = true)
	public List<E> getListByIDs(String idname, Set<?> ids) {
		List<E> results = null;
		String hql = String.format("from %s as e where e.%s in (:ids)", getClass().getSimpleName(), idname);
		try {
			Query query = this.getSession().createQuery(hql);
			query.setParameterList("ids", ids);
			results = (List<E>) query.list();
		} catch (Exception e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve list by IDs (named-query: %s, IDs: %s)", prefixInfo,
					(results == null ? 0 : results.size()), hql, Arrays.deepToString(ids.toArray())));
		}
		return results;
	}

	/*
	 * =========================================================================
	 * =================================== Sequence NEXT VAL (HQL)
	 * =========================================================================
	 * ===================================
	 */

	public Long getSequenceNextValue(String sequence) {
		Long key = null;
		try {
			String SQL = String.format("select nextval('%s')", sequence);
			Query query = this.getSession().createSQLQuery(SQL);
			key = (Long) query.uniqueResult();
		} catch (Exception e) {
			key = (long) Model.ERROR_CODE;
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve sequence next value %d (sequence: %s)", prefixInfo, key, sequence));
		}
		return key;
	}

	/*
	 * =========================================================================
	 * =================================== HQL Queries methods
	 * =========================================================================
	 * ===================================
	 */

	@Transactional(readOnly = true)
	public List<E> getListByHqlQueryAndParams(String hql, Map<String, ?> params) {
		List<E> results = null;
		try {
			Query query = this.getSession().createQuery(hql);
			if (params != null)
				for (String param : params.keySet()) {
					if (params.get(param) instanceof Collection<?>)
						query.setParameterList(param, (Collection<?>) params.get(param));
					else
						query.setParameter(param, params.get(param));
				}
			results = (List<E>) query.list();
		} catch (Exception e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve list of %d items (hql-query: %s, params: %s)", prefixInfo,
					(results == null ? 0 : results.size()), hql, params));
		}
		/*
		 * svaponi - 21 Apr 2015 Sostituisco il ritorno di una collection vuota
		 * per poter distinguere a valle lka differenza tra una query con
		 * risultato nullo ed una che ha generato un'eccezione return new
		 * ArrayList<Object[]>();
		 */
		return results;
	}

	public E getEntityByHqlQueryAndParams(String hql, Map<String, ?> params) {
		List<E> results = null;
		try {
			Query query = this.getSession().createQuery(hql);
			if (params != null)
				for (String param : params.keySet()) {
					if (params.get(param) instanceof Collection<?>)
						query.setParameterList(param, (Collection<?>) params.get(param));
					else
						query.setParameter(param, params.get(param));
				}
			results = (List<E>) query.list();
		} catch (Exception e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve entity (named-query: %s, params: %s) %s", prefixInfo, hql, params,
					resultToString(results)));
		}
		// ** returns single ENTITY **
		return (results != null && results.size() > 0) ? (E) results.get(0) : null;
	}

	/*
	 * =========================================================================
	 * =================================== Named Queries methods
	 * =========================================================================
	 * ===================================
	 */

	@Transactional(readOnly = true)
	public List<E> getListByNamedQueryAndParams(String queryName, Map<String, ?> params) {
		return this.getListByNamedQueryAndParams(queryName, params, -1, -1);
	}

	/**
	 * Come <code>getListByNamedQueryAndParams</code> però con paginazione.<br/>
	 * Presuppone che esista la named-query con i parametri appositi, ovvero
	 * (offset, size):<br/>
	 * <code>LIMIT :size OFFSET :offset</code>
	 */
	@Transactional(readOnly = true)
	public List<E> getListByNamedQueryAndParams(String queryName, Map<String, ?> params, int offset, int size) {
		List<E> results = null;
		try {
			if (params == null)
				params = new HashMap<>();
			Map<String, Object> p = new HashMap<String, Object>();
			p.putAll(params);
			if (size >= 0)
				p.put("size", size);
			if (offset >= 0)
				p.put("offset", offset);
			String[] paramKeys = p.keySet().toArray(new String[] {});
			Object[] paramValues = p.values().toArray(new Object[] {});
			results = (List<E>) this.hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramKeys, paramValues);
		} catch (Exception e) {
			handleException(e);
		} finally {
			logger.debug(
					String.format("%s retrieve list of %d items (named-query: %s, params: %s, offset: %s, size: %s)",
							prefixInfo, (results == null ? 0 : results.size()), queryName, params, offset, size));
		}
		/*
		 * svaponi - 21 Apr 2015 Sostituisco il ritorno di una collection vuota
		 * per poter distinguere a valle lka differenza tra una query con
		 * risultato nullo ed una che ha generato un'eccezione return new
		 * ArrayList<Object[]>();
		 */
		return results;
	}

	public E getEntityByNamedQueryAndParams(String queryName, Map<String, ?> params) {
		List<E> results = null;
		try {
			if (params == null)
				params = new HashMap<>();
			String[] paramKeys = params.keySet().toArray(new String[] {});
			Object[] paramValues = params.values().toArray(new Object[] {});
			results = (List<E>) this.hibernateTemplate.findByNamedQueryAndNamedParam(queryName, paramKeys, paramValues);
		} catch (Exception e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve entity (named-query: %s, params: %s) %s", prefixInfo, queryName,
					params, resultToString(results)));
		}
		// ** returns single ENTITY **
		return (results != null && results.size() > 0) ? (E) results.get(0) : null;
	}

	/*
	 * =========================================================================
	 * =================================== Criteria methods
	 * =========================================================================
	 * ===================================
	 */

	/**
	 * Il metodo rihiama hibernateTemplate.findByCriteria passandogli un
	 * DetachedCriteria. I Criteria sono costruiti con
	 * <ul>
	 * <li><strong>Restrictions.in</strong> se criteria.get(key) è una
	 * collection di valori</li>
	 * <li><strong>Restrictions.eq</strong> altrimenti</li>
	 * </ul>
	 * <strong>Updated 2015-09-09.</strong> In input accetta anche un oggetto di
	 * tipo {@link Criterion}, in questo modo non serve implementare tutte le
	 * casistiche. Ovvero per ogni caso speciale è sufficiente costrire il
	 * Criterion nel relativo Service che usa questo DAO e passarlo dentro la
	 * mappa di parametri.
	 *
	 * @param criteria
	 *            oggetto Map contente i criteri di restrizione
	 */
	@Transactional(readOnly = true)
	public List<E> getListByCriteria(Map<String, ?> criteria) {
		List<E> results = null;
		try {
			DetachedCriteria detachedCriteria = this.buildCriteria(criteria);
			detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			results = (List<E>) this.hibernateTemplate.findByCriteria(detachedCriteria);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve list of %d items (criteria: %s)", prefixInfo,
					(results == null ? 0 : results.size()), criteria));
		}
		return results;
	}

	/**
	 * Metodo equivalente a {@link CopyOfGenericDaoImpl#getListByCriteria(Map)},
	 * con in più la possibilità di paginare i risultati.
	 * 
	 * @param criteria
	 * @param offset
	 * @param size
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<E> getListByCriteria(Map<String, ?> criteria, int offset, int size) {
		List<E> results = null;
		try {
			DetachedCriteria detachedCriteria = this.buildCriteria(criteria);
			detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			results = (List<E>) this.hibernateTemplate.findByCriteria(detachedCriteria, offset * size, size);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve list of %d items (criteria: %s, offset: %s, size: %s)", prefixInfo,
					(results == null ? 0 : results.size()), criteria, offset, size));
		}
		return results;
	}

	/**
	 * @see GenericDao#getListByCriteria(Object...)
	 */
	@Override
	public List<E> getListByCriteria(Object... rawCriteria) {
		return this.getListByCriteria(CriteriaFactory.createRawCriteria(rawCriteria));
	}

	/**
	 * @see GenericDao#getListByCriteria(int, int, Object...)
	 */
	@Override
	public List<E> getListByCriteria(int offset, int size, Object... rawCriteria) {
		return this.getListByCriteria(CriteriaFactory.createRawCriteria(rawCriteria), offset, size);
	}

	/**
	 * Metodo equivalente a {@link CopyOfGenericDaoImpl#getListByCriteria(Map)},
	 * però ritorna una sola entity di tipo dinamico {@link E} al posto della
	 * solita {@link List<E>}.
	 * 
	 * @param criteria
	 *            oggetto Map contente i criteri di restrizione
	 */
	@Transactional(readOnly = true)
	public E getEntityByCriteria(Map<String, ?> criteria) {
		List<E> results = null;
		try {
			DetachedCriteria detachedCriteria = this.buildCriteria(criteria);
			detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			results = (List<E>) this.hibernateTemplate.findByCriteria(detachedCriteria);
		} catch (ObjectNotFoundException | DataAccessException e) {
			handleException(e);
		} finally {
			logger.debug(String.format("%s retrieve entity (criteria: %s) %s", prefixInfo, criteria,
					resultToString(results)));
		}
		// ** returns single ENTITY **
		return (results != null && results.size() > 0) ? (E) results.get(0) : null;
	}

	/**
	 * @see GenericDao#getEntityByCriteria(Object...)
	 */
	@Override
	public E getEntityByCriteria(Object... rawCriteria) {
		return this.getEntityByCriteria(CriteriaFactory.createRawCriteria(rawCriteria));
	}

	/*
	 * Metodi ausiliari
	 */

	/**
	 * Metodo base per costrire un {@link DetachedCriteria} partendo dalla mappa
	 * di parametri in input.
	 * 
	 * @param criteria
	 * @return
	 */
	private DetachedCriteria buildCriteria(Map<String, ?> criteria) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(this.entityClass);
		// old version --> detachedCriteria.add(Restrictions.allEq(criteria));
		if (criteria != null)
			for (String param : criteria.keySet()) {
				if (criteria.get(param) instanceof Collection<?>) {
					detachedCriteria.add(Restrictions.in(param, (Collection<?>) criteria.get(param)));
				} else if (criteria.get(param) instanceof Object[]) {
					detachedCriteria.add(Restrictions.in(param, (Object[]) criteria.get(param)));
				} else if (criteria.get(param) instanceof Order) {
					Order orderBy = (Order) criteria.get(param);
					detachedCriteria.addOrder(orderBy);
				} else if (criteria.get(param) instanceof Criterion) {
					Criterion criterion = (Criterion) criteria.get(param);
					detachedCriteria.add(criterion);
				} else {
					detachedCriteria.add(Restrictions.eq(param, criteria.get(param)));
				}
			}
		return detachedCriteria;
	}

	/**
	 * In caso il flag {@link #throwEx} sia TRUE rilancio la eccezione (solo se
	 * è una {@link RuntimeException}). Se il log è a DEBUG stampo tutto lo
	 * stacktrace altrimenti solo il message della root.
	 * 
	 * @param e
	 * @throws RuntimeException
	 */
	private void handleException(Exception e) throws RuntimeException {
		if (logger.isDebugEnabled())
			logger.error(e.getMessage(), e);
		else
			logger.error(e.getMessage());
		if (throwEx && e instanceof RuntimeException)
			throw (RuntimeException) e;
	}

	/**
	 * 
	 * Stampa l'input con verbosità variabile in base al livello di log
	 * 
	 * @param results
	 * @return
	 */
	private String resultToString(List<?> results) {
		return results == null ? "FAIL (exception occurred)"
				: results.size() == 1 ? "SUCCESS"
						: String.format("FAIL (size: %s) %s", results.size(),
								logger.isDebugEnabled() ? ToStringUtil.recursiveToString(results) : "");
	}

	/**
	 * 
	 * Stampa l'input con verbosità variabile in base al livello di log
	 * 
	 * @param entity
	 * @return
	 */
	private String entityToString(E entity) {
		return logger.isTraceEnabled() ? ToStringUtil.recursiveToString(entity)
				: entity.getClass().getSimpleName() + " (" + entity.getUid() + ")";
	}

}
