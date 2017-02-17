package it.miriade.commons.model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * Classe statica contenente metodi per maneggiare gli oggetti della gerarchia
 * <code>it.miriade.commons.model.Entity</code> (e non).
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 */
public class ModelHandler {

	private static final String SEPARATOR = "#";
	private static final String GROUP_SEPARATOR = "-";
	private static final int GROUP_SIZE = 3;

	/**
	 * Scoraggio l'instanziazione, favorisco l'uso dei metodi statici.
	 */
	@Deprecated
	public ModelHandler() {
		super();
	}

	/**
	 * Genera un ID sempre uguale per la stessa combinazione di contributi in ingresso.
	 * Internamente usa {@link Object#hashCode()}.<br>
	 * Concatena i {@link Object#toString()} dei componenti e ne calcola {@link Object#hashCode()}.
	 * 
	 * <pre>
	 * ID = ("contr0#contr1#contr2").hashcode();
	 * </pre>
	 * 
	 * @param contributes
	 * @return
	 */
	public static int generateHashID(Object... contributes) {
		return generateHashID(SEPARATOR, contributes);
	}

	public static int generateHashID(String separator, Object... contributes) {
		String tmpstring = StringUtils.join(contributes, separator);
		return tmpstring.hashCode() & Integer.MAX_VALUE;
	}

	/**
	 * Genera un ID basato su hashcode, {@link Object#hashCode()}.<br>
	 * Concatena i {@link Object#toString()} dei componenti e ne calcola {@link Object#hashCode()}.
	 * 
	 * <pre>
	 * ID = ("contr0#contr1#contr2").hashcode();
	 * </pre>
	 * 
	 * @param contributes
	 * @return
	 */
	public static long generateHashLongID(Object... contributes) {
		return generateHashLongID(SEPARATOR, contributes);
	}

	public static long generateHashLongID(String separator, Object... contributes) {
		String tmpstring = StringUtils.join(contributes, separator);
		return tmpstring.hashCode() & Long.MAX_VALUE;
	}

	/**
	 * Genera un ID sempre uguale per la stessa combinazione di contributi in ingresso.
	 * Internamente usa {@link Object#hashCode()}.<br>
	 * Divide in gruppi i componenti, concatena i {@link Object#toString()} e calcola
	 * {@link Object#hashCode()} di ognuno, poi concatena gli hashcodes. Genera un ID formato da tanti hashcode
	 * concatenato quanti sono il numero di gruppi.
	 * 
	 * <pre>
	 * groupNum = (contributes.length - 1 + GROUP_SIZE) / GROUP_SIZE);
	 * </pre>
	 * 
	 * @param contributes
	 * @return
	 */
	public static String generateGroupedHashID(Object... contributes) {
		List<Integer> hash = new ArrayList<>();
		String tmpstring = null;
		int groupNum = (contributes.length - 1 + GROUP_SIZE) / GROUP_SIZE;
		for (int index = 0; index < groupNum; index++) {
			Object[] args = Arrays.copyOfRange(contributes, index * GROUP_SIZE, Math.min(contributes.length, (index + 1) * GROUP_SIZE));
			tmpstring = StringUtils.join(args, SEPARATOR);
			int tmp = tmpstring.hashCode() & Integer.MAX_VALUE;
			hash.add(tmp);
		}
		return StringUtils.join(hash.toArray(), GROUP_SEPARATOR);
	}

	// =================================================================================================================================
	// STATIC Methods
	// =================================================================================================================================

	/**
	 * Ritorna la tabella della Entity.
	 * 
	 * @param model
	 * @return
	 */
	public static String getTable(Class<?> clazz) {
		if (clazz.isAnnotationPresent(javax.persistence.Table.class)) {
			Table annotation = clazz.getAnnotation(javax.persistence.Table.class);
			String schema = annotation.schema();
			String table = annotation.name();
			if (StringUtils.isNotBlank(schema))
				table = schema + "." + table;
			return table;
		}
		return "?";
	}

	/**
	 * Ritorna lo schema della Entity.
	 * 
	 * @param model
	 * @return
	 */
	public static String getSchema(Class<?> clazz) {
		if (clazz.isAnnotationPresent(javax.persistence.Table.class)) {
			Table annotation = clazz.getAnnotation(javax.persistence.Table.class);
			String schema = annotation.schema();
			return Objects.toString(schema, "");
		}
		return "?";
	}

	/**
	 * Metodo estrae tutte le proprietà dell'oggetto (in formato json-like).
	 * 
	 * @param model
	 * @return
	 */
	public static String entityToString(Object model) {
		return entityToString(model, 1);
	}

	/**
	 * Come {@link ModelHandler#entityToString(Object)} però scende ricorsivamente fino al livello in input.
	 * 
	 * @param model
	 * @param level
	 * @return
	 */
	public static String entityToString(Object model, int level) {
		if (model == null)
			return "** NULL **";
		StringBuffer buf = new StringBuffer();
		if (model instanceof List<?>) {
			List<?> listModel = (List<?>) model;
			buf.append("[");
			for (Object listItem : listModel) {
				buf.append(entityToString(listItem, --level));
			}
			buf.append("[");
			return buf.toString();
		}
		buf.append(model.getClass().getSimpleName() + " {");
		if (model instanceof Object[]) {
			Object[] obj = (Object[]) model;
			buf.append(Arrays.deepToString(obj));
		} else
			try {
				buf.append("{");
				for (Method method : model.getClass().getMethods())
					if (method.getDeclaringClass().equals(model.getClass()) // 1)
						&& !Modifier.isStatic(method.getModifiers()) 	// 2)
						&& method.getName().startsWith("get") 			// 3)
						&& method.getParameterTypes().length == 0 		// 4)
						&& method.invoke(model) != null					// 5)
					) {
						Object returned = method.invoke(model);
						if (returned instanceof Object[]) {
							buf.append(entityToString(returned, --level));
							buf.append(", ");
						} else if (level > 0 && returned.getClass().isAnnotationPresent(Entity.class)) {
							buf.append(entityToString(returned, --level));
							buf.append(", ");
						} else {
							buf.append(String.format("'%s' : '%s'", method.getName().substring("get".length()), returned));
							buf.append(", ");
						}
					}
				if (model.getClass().getSimpleName().length() + ", ".length() < buf.length())
					buf.delete(buf.length() - ", ".length(), buf.length());
			} catch (Exception e) {
				e.printStackTrace();
				buf.append(" ..an exception occurred! ");
			} finally {
				buf.append(" }");
			}
		return buf.toString();
	}

	/**
	 * Copia tutte le properties della source nel target.
	 * 
	 * @param source
	 * @param target
	 */
	public static Object merge(Object source, Object target) {
		if (source == null || target == null)
			return target;
		BeanUtils.copyProperties(source, target);
		return target;
	}

	@SuppressWarnings("unused")
	private static void mergeBackup(Object source, Object target) {
		if (source == null)
			return;
		if (source instanceof Collection<?>)
			return;

		Object tmp;
		try {
			/*
			 * Usando la classe Field della libreria Reflection non e' stato possibile accedere a tutte le proprietà
			 * perche' non si puo' accedere ad un field privato, allora workaround: ciclo tutti i GETTERs!
			 */
			for (Method method : source.getClass().getMethods())
				/*
				 * Per individuare su method e' GETTER eseguo test if:
				 * 1) comincia con get
				 * 2) non ha parametri/argomenti
				 * 3) e' definito dalla classe runtime (ovvero non appartiene alla superclasse)
				 * 4) non e' statico
				 * 5) torna un oggetto non nullo
				 */
				if (method.getName().startsWith("get") 							// 1
					&& method.getParameterTypes().length == 0 				// 2
					&& method.getDeclaringClass().equals(source.getClass()) // 3
					&& !Modifier.isStatic(method.getModifiers()) 			// 4
					&& method.invoke(source) != null						// 5
				) {
					// prendo il valore dal getter
					tmp = method.invoke(source);
					String methodName = "s" + method.getName().substring(1);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
