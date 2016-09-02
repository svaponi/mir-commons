package it.miriade.commons.model.dao;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import it.miriade.commons.model.Model;

/**
 * Factory di criteria, contiene solo metodi statici usata e viene usata per creare i criteria da dare in input al
 * {@link Dao}.
 * 
 * @author svaponi
 * @created Jun 29, 2016 7:27:58 PM
 */
public class CriteriaFactory {

	/**
	 * Come {@link CriteriaFactory#createCriteria(Object...)}, ritorna un oggetto predisposto a contenere nuovi
	 * criteri, però diversamente questo metodo aggiunge una restriction su un intervallo di tempo.
	 * 1.{@link String} = nome della proprietà su cui ordinare; 2.{@link Date} = data inizio (applica
	 * "greater than or equal" restriction); 3.{@link Date} = data fine (applica "less than or equal" restriction).<br/>
	 * <br/>
	 * <strong>Esempio:</strong>
	 * <blockquote>
	 * <code>Map<String, Object> criteria = modelService.createOrderedCriteria("name", fromDate, toDate);</code>
	 * </blockquote>
	 * 
	 * @param propertyName
	 *            {@link String} = nome della proprietà su cui ordinare
	 * @param fromDate
	 *            {@link Date} = data inizio
	 * @param toDate
	 *            {@link Date} = data fine
	 * @return
	 */
	public static Map<String, Object> createBetweenCriteria(String propertyName, Date fromDate, Date toDate) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (fromDate != null)
			criteria.put(Model.FROM, afterThanCriterion(propertyName, fromDate)); // maggiore-uguale
		if (toDate != null)
			criteria.put(Model.TO, beforeThanCriterion(propertyName, toDate)); // minore-uguale
		return criteria;
	}

	public static Criterion afterThanCriterion(String propertyName, Date date) {
		return Restrictions.ge(propertyName, date);
	}

	public static Criterion greaterThanCriterion(String propertyName, Object value) {
		return Restrictions.gt(propertyName, value);
	}

	public static Criterion greaterEqualCriterion(String propertyName, Object value) {
		return Restrictions.ge(propertyName, value);
	}

	public static Criterion beforeThanCriterion(String propertyName, Date date) {
		return Restrictions.le(propertyName, date);
	}

	public static Criterion lessThanCriterion(String propertyName, Object value) {
		return Restrictions.lt(propertyName, value);
	}

	public static Criterion lessEqualCriterion(String propertyName, Object value) {
		return Restrictions.le(propertyName, value);
	}

	/**
	 * Come {@link CriteriaFactory#createCriteria(Object...)}, ritorna un oggetto predisposto a contenere nuovi
	 * criteri, però diversamente questo metodo aggiunge subito un criterio di ordinamento ({@link Order}).
	 * Per fare ciò necessita che i primi due oggetti passati in input
	 * siano: 1.{@link String} = nome della proprietà su cui ordinare; 2.{@link Model.Order} = ASC or DESC (oggetto
	 * ENUM).<br/>
	 * <br/>
	 * <strong>Esempio:</strong>
	 * <blockquote>
	 * <code>Map<String, Object> criteria = modelService.createOrderedCriteria("name", fromDate, toDate);</code>
	 * </blockquote>
	 * 
	 * @param propertyName
	 *            {@link String} = nome della proprietà su cui ordinare
	 * @param order
	 *            {@link Model.Order} = ASC or DESC
	 * @deprecated
	 * 			sostituire con
	 *             {@link CriteriaFactory#createOrderedCriteria(it.miriade.commons.model.Model.Order, String...)} che
	 *             permette argomenti multipli.
	 * @return
	 */
	@Deprecated
	public static Map<String, Object> createOrderedCriteria(String propertyName, Model.Order order) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (Model.Order.ASC.equals(order))
			criteria.put(Model.ORDER_BY, orderAscCriterion(propertyName));
		else
			criteria.put(Model.ORDER_BY, orderDescCriterion(propertyName));
		return criteria;
	}

	/**
	 * A differenza di {@link CriteriaFactory#createOrderedCriteria(String, it.miriade.commons.model.Model.Order)}
	 * consente argomenti multipli.
	 * 
	 * @param order
	 * @param propertyNames
	 * @return
	 */
	public static Map<String, Object> createOrderedCriteria(Model.Order order, String... propertyNames) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		// TODO: da verificare => per dare maggiore rilevanza ai primi argomenti bisogna invertire l'ordine in modo che
		// vengano aggiunti per ultimi
		List<String> listToReverse = Arrays.asList(propertyNames);
		Collections.reverse(listToReverse);
		if (Model.Order.ASC.equals(order))
			for (String name : listToReverse)
				criteria.put(Model.ORDER_BY + name, orderAscCriterion(name));
		else
			for (String name : listToReverse)
				criteria.put(Model.ORDER_BY + name, orderDescCriterion(name));
		return criteria;
	}

	public static Order orderAscCriterion(String propertyName) {
		return Order.asc(propertyName);
	}

	public static Order orderDescCriterion(String propertyName) {
		return Order.desc(propertyName);
	}

	/**
	 * Come {@link CriteriaFactory#createCriteria(Object...)}, ritorna un oggetto contenente un criterio di
	 * nullità: IS NULL o IS NOT NULL.
	 * Ininput vuole il nume della proprietà da testare e un boolean per distinguere le due possibilità: true=IS NULL,
	 * false = IS NOT NULL.<br/>
	 * <br/>
	 * <strong>Esempio:</strong>
	 * <blockquote>
	 * <code>Map<String, Object> criteria = modelService.createNullCriteria("name", true);</code>
	 * </blockquote>
	 * 
	 * @param propertyName
	 * @param isNull
	 *            flag per distinguere le due possibilità: true = IS NULL, false = IS NOT NULL
	 * @deprecated
	 * 			sostituire con
	 *             {@link CriteriaFactory#createNullCriteria(boolean, String...)} che permette argomenti multipli.
	 * @return
	 */
	@Deprecated
	public static Map<String, Object> createNullCriteria(String propertyName, boolean isNull) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (isNull)
			criteria.put(Model.IS_NULL, isNullCriterion(propertyName));
		else
			criteria.put(Model.IS_NOT_NULL, isNotNullCriterion(propertyName));
		return criteria;
	}

	/**
	 * A differenza di {@link CriteriaFactory#createNullCriteria(String, boolean)} consente argomenti multipli.
	 * 
	 * @param order
	 * @param propertyNames
	 * @return
	 */
	public static Map<String, Object> createNullCriteria(boolean isNull, String... propertyNames) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		if (isNull)
			for (String name : propertyNames)
				criteria.put(name + Model.IS_NULL, isNullCriterion(name));
		else
			for (String name : propertyNames)
				criteria.put(name + Model.IS_NOT_NULL, isNotNullCriterion(name));
		return criteria;
	}

	public static Criterion isNullCriterion(String propertyName) {
		return Restrictions.isNull(propertyName);
	}

	public static Criterion isNotNullCriterion(String propertyName) {
		return Restrictions.isNotNull(propertyName);
	}

	// spostati da GenricModelService

	/**
	 * Come {@link CriteriaFactory#createCriteria(Object...)}, ritorna un oggetto predisposto a contenere nuovi
	 * criteri, però diversamente questo metodo aggiunge subito un criterio di ordinamento ({@link Order}).
	 * Per fare ciò necessita che i primi due oggetti passati in input
	 * siano: 1.{@link String} = nome della proprietà su cui ordinare; 2.{@link Model.Order} = (ASC or DESC) oggetto
	 * ENUM.<br/>
	 * <br/>
	 * <strong>Esempio:</strong>
	 * <blockquote>
	 * <code>Map<String, Object> criteria = modelService.createOrderedCriteria("name", Model.Order.ASC);</code>
	 * </blockquote>
	 * 
	 * @param rawCriteria
	 * @return
	 */
	public static Map<String, Object> createOrderedCriteria(Object... rawCriteria) {
		if (rawCriteria == null || rawCriteria.length == 0) {
			return new HashMap<String, Object>();
		} else if (rawCriteria[0] instanceof String && rawCriteria[1] instanceof Model.Order) {

			String propertyName = (String) rawCriteria[0];
			Model.Order order = (Model.Order) rawCriteria[1];
			Map<String, Object> criteria = CriteriaFactory.createOrderedCriteria(propertyName, order);
			if (rawCriteria.length > 2) {
				Object[] subRawCriteria = Arrays.copyOfRange(rawCriteria, 2, rawCriteria.length);
				criteria.putAll(createCriteria(subRawCriteria));
			}
			return criteria;
		} else {
			return createCriteria(rawCriteria);
		}
	}

	/**
	 * Come {@link CriteriaFactory#createCriteria(Object...)}, ritorna un oggetto predisposto a contenere nuovi
	 * criteri, però diversamente questo metodo aggiunge subito un criterio di ordinamento ({@link Order}).
	 * Per fare ciò necessita che i primi due oggetti passati in input
	 * siano: 1.{@link String} = nome della proprietà su cui ordinare; 2.{@link Date} = data inizio; 3.{@link Date} =
	 * data fine.<br/>
	 * <br/>
	 * <strong>Esempio:</strong>
	 * <blockquote>
	 * <code>Map<String, Object> criteria = modelService.createBetweenCriteria("name", fromDate, toDate);</code>
	 * </blockquote>
	 * 
	 * @param rawCriteria
	 * @return
	 */
	public static Map<String, Object> createBetweenCriteria(Object... rawCriteria) {
		if (rawCriteria == null || rawCriteria.length == 0) {
			return new HashMap<String, Object>();
		} else if (rawCriteria[0] instanceof String && rawCriteria[1] instanceof Model.Order) {
			String propertyName = (String) rawCriteria[0];
			Date fromDate = (Date) rawCriteria[1];
			Date toDate = (Date) rawCriteria[2];
			Map<String, Object> criteria = CriteriaFactory.createBetweenCriteria(propertyName, fromDate, toDate);
			if (rawCriteria.length > 3) {
				Object[] subRawCriteria = Arrays.copyOfRange(rawCriteria, 3, rawCriteria.length);
				criteria.putAll(createCriteria(subRawCriteria));
			}
			return criteria;
		} else {
			return createRawCriteria(rawCriteria);
		}
	}

	/*
	 * Utilities per la gestione dei CRITERIA
	 */

	/**
	 * Ritorna un oggetto predisposto a contenere nuovi criteri, oppure implementa nell'oggetto di ritorno i criteri
	 * passati come argomento. I criteri vanno SEMPRE inseriti a coppie CHIAVE-VALORE dove la chiave è il primo
	 * argomento e il valore quello seguente, e via così (chiavi: indice pari o 0; valore: indice dispari). Eventuali
	 * argomenti in eccedenza sono ignorati
	 * 
	 * @param rawCriteria
	 * @return
	 */
	public static Map<String, Object> createCriteria(Object... rawCriteria) {
		if (rawCriteria == null || rawCriteria.length == 0)
			return new HashMap<String, Object>();
		else
			return createRawCriteria(rawCriteria);
	}

	/**
	 * <strong>Updated 2015-09-09.</strong> Se tra gli oggetti in input trova una Map la aggiunge ai parametri (con un
	 * putAll() ovviamente). Inoltre è stato ottimizzato l'incremento della variabile indice dentro il ciclo.
	 * 
	 * @param rawCriteria
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> createRawCriteria(Object[] rawCriteria) {

		Map<String, Object> criteria = new HashMap<String, Object>();

		for (int i = 0; i < rawCriteria.length - 1;) {
			if (rawCriteria[i] instanceof Map)
				criteria.putAll((Map<String, ? extends Object>) rawCriteria[i++]);
			else
				criteria.put((String) rawCriteria[i++], rawCriteria[i++]);
		}

		return criteria;
	}
}
