package it.miriade.commons.model.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.miriade.commons.model.entities.ModelEntity;

/**
 * Classe chiave del modulo, definisce i metodi di accesso al dato che l'implementazione del DAO dovrà implementare e
 * che saranno dunque esposti all'utente.
 * 
 * @author svaponi
 * @param <E>
 * @param <PK>
 */
public interface GenericDao<E extends ModelEntity<PK>, PK extends Serializable> {

	// =================================================================================================================
	// Type parameter methods
	// =================================================================================================================

	/**
	 * Ritorna la classe runtime della relativa entity.
	 * 
	 * @return
	 */
	public Class<E> getEntityClass();

	/**
	 * Ritorna una nuova instanza vuota della relativa entity.
	 * 
	 * @return
	 */
	public E getInstance();

	// =================================================================================================================
	// basic methods
	// =================================================================================================================

	/**
	 * Conta gli elementi della collezione di entity.
	 * 
	 * @return
	 */
	public int getCount();

	/**
	 * Controlla se la entity esiste.
	 * 
	 * @param id
	 * @return
	 */
	public boolean exists(PK id);

	/**
	 * Salva la entity e ritorna l'ID, utile in caso di ID autogenerato al momento del salvataggio.
	 * 
	 * @param model
	 * @return
	 */
	public PK saveAndGetId(E model);

	/**
	 * Salva la entity.
	 * 
	 * @param model
	 */
	public void save(E model);

	/**
	 * Aggiorna la entity.
	 * 
	 * @param model
	 */
	public void merge(E model);

	/**
	 * Recupera la entity by ID.
	 * 
	 * @param id
	 * @return
	 */
	public E get(PK id);

	/**
	 * Rimuove la entity e la ritorna.
	 * 
	 * @param id
	 * @return
	 */
	public E remove(PK id);

	/**
	 * Ritorna l'intera collezione di entity.
	 * 
	 * @return
	 */
	public List<E> getList();

	/**
	 * Ritorna una pagina della collezione di entity.
	 * 
	 * @param offset
	 * @param size
	 * @return
	 */
	public List<E> getList(int offset, int size);

	/**
	 * Questo metodo ritorna una collection di elementi con id compresi nella collection di ID passata come argomento.
	 * 
	 * @param idname
	 *            nome del property marcata @Id
	 * @param ids
	 *            collection di ID
	 * @return
	 */
	public List<E> getListByIDs(String idname, Set<?> ids);

	/**
	 * Ritorna la collezione di ID relativi alle entity.
	 * 
	 * @return
	 */
	public Set<PK> getAllIDs();

	/**
	 * Elimina l'intera collezione di entity.
	 * 
	 * @return
	 */
	public int deleteAll();

	/**
	 * Elimina le entity contenute nella collection in input.
	 * 
	 * @param collection
	 * @return
	 */
	public int deleteAll(Collection<E> collection);

	/*
	 * ============================================================================================================
	 * HQL Queries methods
	 * ============================================================================================================
	 */

	/**
	 * Il metodo esegue la query HQL contenuta nel primo argomento associandovi i parametri contenuti nel secondo
	 * argomento.<br/>
	 * 
	 * @param hql
	 *            query con sintassi HQL
	 * @param params
	 *            mappa contenente i named-parameters (nome-parametro => valore)
	 */
	public List<E> getListByHqlQueryAndParams(String hql, Map<String, ?> params);

	/**
	 * Ritorna una singola entity risultato di una query query HQL.
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public E getEntityByHqlQueryAndParams(String hql, Map<String, ?> params);

	/*
	 * ============================================================================================================
	 * Named Queries methods
	 * ============================================================================================================
	 */

	/**
	 * Il metodo esegue la named-query identificata dal primo argomento associandovi i parametri contenuti nel secondo
	 * argomento.<br/>
	 * 
	 * @param queryName
	 *            nome della named-query
	 * @param params
	 *            mappa contenente i named-parameters (nome-parametro => valore)
	 */
	public List<E> getListByNamedQueryAndParams(String queryName, Map<String, ?> params);

	/**
	 * Implementa la stessa logica di <code>getListByNamedQueryAndParams</code> con la particolarità di controllare che
	 * il
	 * risultato della named-query sia un unico elemento, ritornando dunque una singola entità.<br/>
	 * <strong>Attenzione: </strong> può ritornare <code>null</code>!
	 * In caso il risultato non dia un solo elemento allora viene forzato il ritorno di <code>null</code>.
	 * 
	 * @see GenericDao#getListByNamedQueryAndParams(String, Map)
	 */
	public E getEntityByNamedQueryAndParams(String queryName, Map<String, ?> params);

	/*
	 * ============================================================================================================
	 * Criteria methods
	 * ============================================================================================================
	 */

	/**
	 * Il metodo permette di richiamare una collection di istanze di classe <code>E</code> (classe definita a run-time)
	 * applicando i criteri di restrizione implementati dall'oggetto <code>criteria</code> di tipo <code>Map</code>.
	 * Le restrizioni applicate coincidono in numero con le entries <code>key => value</code> di <code>criteria</code>,
	 * nello specifico la tipologia di restrizione è definita in base alla classe run-time di ciascun <code>value</code>
	 * , ovvero se:
	 * <ul>
	 * <li><code>value</code> è una collection ==> <code>where key in value</code></li>
	 * <li><code>value</code> NON è una collection ==> <code>where key = value</code></li>
	 * </ul>
	 *
	 * @param criteria
	 *            oggetto Map contente i criteri di restrizione
	 */
	public List<E> getListByCriteria(Map<String, ?> criteria);

	/**
	 * Implementa la stessa logica di {@link GenericDao#getListByCriteria(Map)} con in più la paginazione dei risultati.
	 * 
	 * @see GenericDao#getListByCriteria(Map)
	 */
	public List<E> getListByCriteria(Map<String, ?> criteria, int offset, int size);

	/**
	 * Implementa la stessa logica di {@link GenericDao#getListByCriteria(Map)}.
	 * 
	 * @see GenericDao#getListByCriteria(Map)
	 */
	public List<E> getListByCriteria(Object... rawCriteria);

	/**
	 * Implementa la stessa logica di {@link GenericDao#getListByCriteria(Map)} con in più la paginazione dei risultati.
	 * Dato che i criteria in input sono varargs, i parametri offset e size vanno per primi.
	 * 
	 * @see GenericDao#getListByCriteria(Map, int, int)
	 */
	public List<E> getListByCriteria(int offset, int size, Object... rawCriteria);

	/**
	 * Implementa la stessa logica di {@link GenericDao#getListByCriteria(Map)} con la particolarità di controllare che
	 * il
	 * risultato delle restrizioni sia un unico elemento, ritornando dunque una singola entità.<br/>
	 * <strong>Attenzione: </strong> può ritornare <code>null</code>!
	 * In caso il risultato delle restrizioni applicate non dia un solo elemento allora viene forzato il ritorno di
	 * <code>null</code>.
	 * 
	 * @see GenericDao#getListByCriteria(Map)
	 */
	public E getEntityByCriteria(Map<String, ?> criteria);

	/**
	 * Chiama GenericDao e casta le Entities di ritorno in DTOs.
	 * 
	 * @see GenericDao#getEntityByCriteria(Map)
	 */
	public E getEntityByCriteria(Object... rawCriteria);

}