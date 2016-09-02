package it.miriade.commons.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Utilità per le collezioni (per originali vedi it.miriade.ubiika)
 * 
 * @author matteo sumberaz
 */
public class CollectionHandler {

	/**
	 * Test se la collection IS EMPTY
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Test se la collection IS NOT EMPTY
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean notEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static boolean notEmpty(Object[] array) {
		return !isEmpty(array);
	}

	public static boolean notEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * Ritorna la dimensione della collection anche se è nulla (NULL).
	 * 
	 * @param collection
	 * @return
	 */
	public static int size(Collection<?> collection) {
		return isEmpty(collection) ? 0 : collection.size();
	}

	/**
	 * Ritorna un oggetto random della collezione.
	 * 
	 * @param collection
	 * @return
	 */
	public static Object random(Collection<?> collection) {
		int index = new Random().nextInt(collection.size());
		if (collection instanceof List<?>)
			return ((List<?>) collection).get(index);

		Iterator<?> it = collection.iterator();
		Object result = null;
		for (int i = 0; it.hasNext() && i <= index; i++) // esce con i==index
			result = it.next();
		return result;
	}

	/**
	 * Copia il contenuto della prima lista (master) nella seconda (slave). La lista master contiene elemento che master
	 * estendono quelli dello slave.
	 * 
	 * @param master
	 * @param slave
	 * @return
	 */
	public static <T> List<T> copyListDowngrade(List<? extends T> master, List<T> slave) {
		slave.clear();
		for (T dto : master) {
			slave.add(dto);
		}
		return slave;
	}

	/**
	 * Copia il contenuto della prima lista (master) nella seconda (slave).
	 * 
	 * @param master
	 * @param slave
	 * @return
	 */
	public static <T> List<T> copyList(List<T> master, List<T> slave) {
		slave.clear();
		slave.addAll(slave);
		return slave;
	}

	/**
	 * Copia il contenuto della prima lista (master) nella seconda (slave), uno ad uno in modo che vengano eseguiti gli
	 * eventuali <strong>cast impliciti</strong>.
	 * 
	 * @param master
	 * @param slave
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List copyListRaw(List master, List slave) {
		slave.clear();
		for (Object dto : master) {
			slave.add(dto);
		}
		return slave;
	}

}
