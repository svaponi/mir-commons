package it.miriade.commons.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import it.miriade.commons.model.entities.ModelEntity;

/**
 * Questa classe fornisce metodi di accesso al dato senza essere vincolata ad una particolare entity e/o tabella, ovvero
 * può essere usata per tutte le entities che estendono {@link ModelEntity}, come anche per eseguire SQL o named-query o
 * azioni indipendenti dagli oggetti come <code>select NEXTVAL from sequence</code>.
 * 
 * @author svaponi
 * @created Feb 5, 2016 9:39:13 AM
 */
public interface Dao {

	/**
	 * Imposta se rilanciare le eventuali eccezioni dopo averle catchate/loggate.
	 * 
	 * @param throwEx
	 */
	public void throwEx(boolean throwEx);

	/**
	 * Esegue uno blocco di SQL grezzo, ritorna un INT che è generalmente il numero di righe aggiornate.
	 * 
	 * @param sql
	 * @return
	 */
	public int executeUpdateQuery(String sql);

	/**
	 * Esegue uno blocco di SQL grezzo, ritorna una List che contien il risultato della query.
	 * 
	 * @param sql
	 * @return
	 */
	public List<?> executeSelectQuery(String sql);

	/**
	 * Apre una nuova transaction.
	 * 
	 * @return
	 */
	public Session beginSession();

	/**
	 * Chiude una transactgion aperta con il metodo {@link Dao#beginSession()}
	 * 
	 * @param session
	 */
	public void endSession(Session session);

	/**
	 * Ritorna la session aperta in uso.
	 * 
	 * @return
	 */
	public Session getSession();

	/**
	 * Estrae nextval da una sequence.
	 * 
	 * @param sequence
	 * @return
	 */
	public Long getSequenceNextValue(String sequence);

	/**
	 * Svuota la tabella relativa alla entity.
	 * 
	 * @param entityHqlName
	 * @return
	 */
	public int deleteAll(String entityHqlName);

	/**
	 * Esegue la named-query e ritorna il result-set con una lista di array, ogni elemento della lista è un record, ogni
	 * elemento dell'array è una cella/campo del record.
	 * 
	 * @param queryName
	 * @param params
	 * @return
	 */
	public List<Object[]> getTupleCollectionByNamedQueryAndParams(String queryName, Map<String, ?> params);

	public List<Object[]> getTupleCollectionByHQL(String hql, Map<String, ?> params);

	/**
	 * Esegue la named-query e ritorna il un singolo record come array di Object, ogni elemento dell'array è una
	 * cella/campo del record.La named-query deve ritornare una singola riga.
	 * 
	 * @param queryName
	 * @param params
	 * @return
	 */
	public Object[] getTupleByNamedQueryAndParams(String queryName, Map<String, ?> params);

	public Object[] getTupleByHQL(String hql, Map<String, ?> params);

	/**
	 * Esegue la named-query e ritorna il result-set con una lista singoli valori, ogni elemento della lista è una
	 * cella/campo del record.
	 * 
	 * @param queryName
	 * @param params
	 * @return
	 */
	public List<Object> getValueCollectionByNamedQueryAndParams(String queryName, Map<String, ?> params);

	public List<Object> getValueCollectionByHQL(String hql, Map<String, ?> params);

	/**
	 * Esegue la named-query e ritorna il un singolo valore. La named-query deve ritornare una singola riga con una
	 * colonna.
	 * 
	 * @param queryName
	 * @param params
	 * @return
	 */
	public Object getValueByNamedQueryAndParams(String queryName, Map<String, ?> params);

	public Object getValueByHQL(String hql, Map<String, ?> params);

}