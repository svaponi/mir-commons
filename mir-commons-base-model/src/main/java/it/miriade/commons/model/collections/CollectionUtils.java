package it.miriade.commons.model.collections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utilità per le collezioni (per originali vedi it.miriade.ubiika)
 * 
 * @author matteo sumberaz
 */
public class CollectionUtils {

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

	// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =

	public static <T, V> CollectionConverter<T, V> buildConverter(final String method) {
		return new CollectionConverter<T, V>() {

			@SuppressWarnings("unchecked")
			public V convert(T v) {
				try {
					Method m = v.getClass().getMethod(method);
					return (V) m.invoke(v);
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	/**
	 * Trasforma una collezione di un tipo in una collezione di un altro tipo
	 * 
	 * @param converter
	 *            il metodo di trasformazione
	 * @param collection
	 *            la collezione da trasformare
	 * @return una nuova lista contenente gli elementi della collezione trasformati dal metodo
	 */
	public static <T, V> List<V> convertList(CollectionConverter<T, V> converter, Collection<T> collection) {

		return convertList(converter, collection, false);
	}

	/**
	 * Trasforma una collezione di un tipo in una collezione di un altro tipo
	 * 
	 * @param converter
	 *            il metodo di trasformazione
	 * @param collection
	 *            la collezione da trasformare
	 * @param nulls
	 *            aggiunge i nulli alla lista o li ignora
	 * @return una nuova lista contenente gli elementi della collezione trasformati dal metodo
	 */
	public static <T, V> List<V> convertList(CollectionConverter<T, V> converter, Collection<T> collection, boolean nulls) {

		if (collection == null)
			throw new IllegalArgumentException("Collection is null");

		List<V> toRet = new ArrayList<V>(collection.size());

		for (T t : collection) {
			V v = converter.convert(t);
			if (v != null || nulls)
				toRet.add(v);
		}

		return toRet;
	}

	public static <T, V> Set<V> convertSet(CollectionConverter<T, V> converter, Collection<T> collection) {

		Set<V> toRet = new HashSet<V>(collection.size());

		for (T t : collection)
			toRet.add(converter.convert(t));

		return toRet;
	}

	public static <T, V> Map<T, V> buildSimpleMap(CollectionConverter<V, T> converter, Collection<V> collection) {

		Map<T, V> map = new HashMap<T, V>(collection.size());

		for (V v : collection) {
			map.put(converter.convert(v), v);
		}

		return map;
	}

	public static <T, V, K> Map<T, V> buildMap(CollectionConverter<K, T> keyConverter, CollectionConverter<K, V> valueConverter, Collection<K> collection) {

		Map<T, V> map = new HashMap<T, V>(collection.size());

		for (K k : collection) {
			map.put(keyConverter.convert(k), valueConverter.convert(k));
		}

		return map;
	}

	public static <T> List<T> buildList(Map<? extends Number, T> map) {

		Set<Number> keys = new TreeSet<Number>(map.keySet());
		List<T> list = new ArrayList<T>(keys.size());

		for (Number key : keys) {
			list.add(map.get(key));
		}

		return list;
	}

	public static <T, V> boolean exists(Collection<T> coll, V element, Relation<T, V> criteria) {

		for (T t : coll)
			if (criteria.apply(t, element))
				return true;

		return false;
	}

	public static <T, V> T find(Collection<T> coll, V element, Relation<T, V> criteria) {

		for (T t : coll)
			if (criteria.apply(t, element))
				return t;

		return null;
	}

	public static <T> List<T> filter(List<T> coll, Criteria<T> criteria) {

		List<T> toRet = new ArrayList<T>();

		for (T t : coll)
			if (criteria.apply(t))
				toRet.add(t);

		return toRet;
	}

	@SuppressWarnings("unchecked")
	public static <T, C extends Collection<T>> C[] split(Collection<T> collection, C[] array, int pageSize) {

		try {

			int size = (int) Math.ceil((collection.size() / (double) pageSize));

			Object[] toReturn = new Object[size];
			int index = 0;

			C currentCollection = (C) array.getClass().getComponentType().newInstance();
			int count = 0;

			for (T obj : collection) {
				currentCollection.add(obj);
				count++;
				if (count == pageSize) {
					toReturn[index++] = currentCollection;
					currentCollection = (C) array.getClass().getComponentType().newInstance();
					count = 0;
				}
			}

			if (count > 0) {
				toReturn[index++] = currentCollection;
			}

			return (C[]) Arrays.copyOf(toReturn, size, array.getClass());

		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> List<T> collapseList(List<List<T>> list) {

		List<T> res = new ArrayList<T>();

		for (Iterable<T> l : list) {
			for (T ls : l) {
				res.add(ls);
			}
		}

		return res;
	}

	public static <T> List<T> toList(T[] array) {
		ArrayList<T> toRet = new ArrayList<T>();
		Collections.addAll(toRet, array);
		return toRet;
	}

	public static <T> List<T> sort(List<T> collection, final String fieldName) {

		if (collection.isEmpty())
			return collection;

		Collections.sort(collection, new Comparator<T>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public int compare(Object o1, Object o2) {

				Comparable val1 = null;
				Comparable val2 = null;
				try {

					Field field1 = findField(o1.getClass(), fieldName, null);
					field1.setAccessible(true);
					val1 = (Comparable) field1.get(o1);

					Field field2 = findField(o2.getClass(), fieldName, null);
					field2.setAccessible(true);
					val2 = (Comparable) field2.get(o2);

					return val1.compareTo(val2);
				} catch (Exception e) {
					e.printStackTrace();
					throw new IllegalArgumentException("Objects not comparable for field " + "\"" + fieldName + "\"" + " beacuse the types are: "
							+ val1.getClass() + ", " + val2.getClass());
				}
			}
		});

		return collection;
	}

	/**
	 * @see org.springframework.util.ReflectionUtils
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		if (clazz == null)
			new IllegalArgumentException("Class must not be null");
		if (name != null || type != null)
			new IllegalArgumentException("Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
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
