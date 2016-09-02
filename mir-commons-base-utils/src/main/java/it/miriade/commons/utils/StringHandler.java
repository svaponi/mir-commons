package it.miriade.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import it.miriade.commons.collections.CollectionUtils;

public class StringHandler {

	public enum Alphabet {
		A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;

		public static int getNum(String targ) {
			return valueOf(targ).ordinal();
		}

		public static int getNum(char targ) {
			return valueOf(String.valueOf(targ)).ordinal();
		}
	}

	public static final String DASH = "-";
	public static final String DOTs = "...";
	public static final String BLANK = "";
	public static final String EMPTY = "";
	public static final String NULL = "null";

	// assicura che ci sia una tringa valida not null
	private static String s(Object o) {
		return o == null ? "" : o.toString();
	}

	/**
	 * Wrappa Object o nella String s. La stringa deve contenere solo un '%s' altrimenti non funziona
	 * 
	 * @param s
	 * @param o
	 * @return
	 */
	private static String wrap(String s, Object o) {
		if (s(s).indexOf("%s") >= 0)
			return String.format(s, o);
		return s(o);
	}

	/**
	 * Torna il numero con il giusto ordinale: 'st', 'nd', 'rd'
	 * 
	 * @param i
	 * @return
	 */
	public static String ord(int i) {
		String prerfix = (i / 10) == 0 ? "" : String.valueOf(i / 10);
		switch (i % 10) {
		case 1:
			return prerfix + "1st";
		case 2:
			return prerfix + "2nd";
		case 3:
			return prerfix + "3rd";
		}
		return i + "th";
	}

	/**
	 * Ritorno TRUE se la string NON è nulla e contiene testo, ovvero non sono solo white-spaces.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean hasText(String s) {
		// return s != null && !s.isEmpty() && !s.equalsIgnoreCase(StringHandler.left("", s.length(), ' '));
		return s != null && !s.isEmpty() && !"".equalsIgnoreCase(s.trim());
	}

	public static boolean hasText(Object o) {
		return hasText(s(o));
	}

	/**
	 * Ritorno TRUE se la string è nulla o contiene solo white-spaces.
	 * 
	 * @param s
	 * @return
	 */
	public static boolean noText(String s) {
		return !hasText(s);
	}

	public static boolean noText(Object o) {
		return !hasText(s(o));
	}

	/**
	 * Invoca il {@link Object#toString()} anche se il object è NULL.
	 * 
	 * @param o
	 * @return
	 */
	public static String toString(Object o) {
		return s(o);
	}

	/**
	 * Invoca il {@link Object#toString()}, se il object è NULL torna il fallback
	 * 
	 * @param o
	 * @param fallback
	 * @return
	 */
	public static String toString(Object o, String fallback) {
		String s = s(o);
		return hasText(s) ? s : fallback;
	}

	/**
	 * Invoca il {@link Object#toString()} e controlla che il risultato non superi la lunghezza maxLength.
	 * 
	 * @param o
	 * @param maxLength
	 *            lunghezza massima dell'output
	 * @return
	 */
	public static String toString(Object o, int maxLength) {
		String s = s(o);
		return sub(s, 0, maxLength);
	}

	/**
	 * Ritorna il char alla posizione i dentro la string. <br>
	 * - se la string è NULL o l'indice è negativo ritorna un whitespace. <br>
	 * - se la string è troppo corta ritorna l'ultimocarattere della string. <br>
	 * 
	 * @param s
	 *            input
	 * @param i
	 * @return
	 */
	public static char charAt(String s, int i) {
		try {
			return s.charAt(i);
		} catch (Exception e) {
			return ' ';
		}
	}

	/**
	 * Invoca il {@link Object#toString()} e controlla che il risultato non superi la lunghezza maxLength.
	 * 
	 * @param o
	 * @param maxLength
	 *            lunghezza massima dell'output
	 * @return
	 */
	public static String truncString(Object o, int maxLength) {
		String s = s(o);
		return s.length() > maxLength ? s.substring(0, maxLength - DOTs.length()).concat(DOTs) : s;
	}

	/**
	 * [Copiato da org.springframework.util.StringUtils] Count the occurrences of the substring in string s.
	 * 
	 * @param s
	 *            string to search in. Return 0 if this is null.
	 * @param sub
	 *            string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String s, String sub) {
		if (s == null || sub == null || s.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0;
		int pos = 0;
		int idx;
		while ((idx = s.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Ritorna l'indice alfabetico (UK) del carattere passato. Esempio A = 0, B = 1, ... Z = 25
	 * 
	 * @param s
	 * @return
	 */
	public static int ord(String s) {
		return hasText(s) ? s.toUpperCase().charAt(0) - 'A' : -1;
	}

	/**
	 * Ritorna l'indice alfabetico del carattere passato.
	 * 
	 * @param c
	 * @return
	 */
	public static int ord(char c) {
		return ord(String.valueOf(c));
	}

	/**
	 * Aggiunge il carattere c a sinistra fino ad arrivare ad una lunghezza totale (originale + caratteri aggiunti)
	 * della stringa di n.
	 *
	 * @param s
	 *            stringa originale
	 * @param n
	 *            lunghezza totale del risultato
	 * @param c
	 *            carattere da aggiungere
	 * @return
	 */
	public static String left(String s, int n, char c) {
		return String.format("%1$" + n + "s", s).replace(' ', c);
	}

	/**
	 * Aggiunge il carattere c a destra fino ad arrivare ad una lunghezza totale (originale + caratteri aggiunti) della
	 * stringa di n.
	 *
	 * @param s
	 *            stringa originale
	 * @param n
	 *            lunghezza totale del risultato
	 * @param c
	 *            carattere da aggiungere
	 * @return
	 */
	public static String right(String s, int n, char c) {
		return String.format("%1$-" + n + "s", s).replace(' ', c);
	}

	/**
	 * Ripete N volte il carattere C.
	 * 
	 * @param n
	 *            numero di ripetizioni
	 * @param c
	 *            carattere da ripetere
	 * @return
	 */
	public static String repeat(int n, char c) {
		if (n > 0)
			return String.format("%1$" + n + "s", "").replace(' ', c);
		else
			return BLANK;
	}

	/**
	 * Rimuove tutti i "new-lines" e "line-feeds".
	 *
	 * @param s
	 * @return
	 */
	public static String removeNewLines(String s) {
		return s(s).replaceAll("\\r|\\n", "").trim();
	}

	/**
	 * Rimuove tutti i "tabs".
	 *
	 * @param s
	 * @return
	 */
	public static String removeTabs(String s) {
		return s(s).replaceAll("\\t", "").trim();
	}

	/**
	 * Rimuove tutti i doppi "white-spaces".
	 *
	 * @param s
	 * @return
	 */
	public static String removeDoubleSpaces(String s) {
		return s(s).replaceAll("  ", " ").replaceAll("  ", " ").trim();
	}

	/**
	 * Rimuove tutti i meta-character di identazione: "new-lines", "line-feeds", doppi "white-spaces", "tabs".
	 *
	 * @param s
	 * @return
	 */
	public static String removeIdentation(String s) {
		// è indispensabile ripeter il replaceAll 2 volte per rimuovere tutti i doppi spazi
		return s(s).replaceAll("\\r|\\n|\\t", " ").replaceAll("  ", " ").replaceAll("  ", " ").trim();
	}

	/**
	 * Rimuove tutti i "new-lines" e/o "line-feeds" in dipendenza al sistema operativo.
	 *
	 * @param s
	 * @return
	 */
	public static String removeSystemNewLines(String s) {
		String newline = System.getProperty("line.separator");
		return s(s).replaceAll(newline, "").trim();
	}

	/**
	 * Ritorna la sub-stringa da "from" alla fine
	 * 
	 * @param s
	 * @param toSearch
	 * @return
	 */
	public static String from(String s, String toSearch) {
		int from = s(s).indexOf(toSearch);
		if (from < 0)
			return "";
		return s(s).substring(from + 1, s.length());
	}

	/**
	 * Ritorna la sub-stringa da "from" alla fine
	 * 
	 * @param s
	 * @param toSearch
	 * @return
	 */
	public static String fromInclusive(String s, String toSearch) {
		int from = s(s).indexOf(toSearch);
		if (from < 0)
			return "";
		return s(s).substring(from, s.length());
	}

	/**
	 * Ritorna la sub-string dall'inizio a "to"
	 * 
	 * @param text
	 * @param toSearch
	 * @return
	 */
	public static String to(String s, String toSearch) {
		int to = s(s).indexOf(toSearch);
		if (to < 0)
			return "";
		return s(s).substring(0, to);
	}

	/**
	 * Ritorna la sub-string dall'inizio a "to"
	 * 
	 * @param s
	 * @param toSearch
	 * @return
	 */
	public static String toInclusive(String s, String toSearch) {
		int to = s(s).indexOf(toSearch);
		if (to < 0)
			return "";
		return s(s).substring(0, to + 1);
	}

	/*
	 * Metodi ausiliari per il JOIN o CONCATENAZIONE di collection
	 */

	/**
	 * Concatena le string con il separatore di default.
	 * 
	 * @see StringHandler#DASH
	 * @param text
	 * @param toSearch
	 * @return
	 */
	public static String join(Object... args) {
		return join(BLANK, BLANK, args);
	}

	public static String join(Collection<?> c) {
		if (CollectionUtils.notEmpty(c))
			return _join(BLANK, BLANK, c.iterator());
		return EMPTY;
	}

	/**
	 * Concatena le string con il separatore in input (primo argomento).
	 * 
	 * @param args
	 * @return
	 */
	public static String join(String separator, Object... args) {
		if (args == null || args.length <= 0)
			return "";
		return _join(separator == null ? BLANK : separator, BLANK, Arrays.asList(args).iterator());
	}

	public static String join(String separator, Collection<?> c) {
		if (CollectionUtils.notEmpty(c))
			return _join(separator, BLANK, c.iterator());
		return EMPTY;
	}

	/**
	 * Concatena e wrappa ogni elemento in wrap (che deve avere un placeholder '%s')
	 * 
	 * @param separator
	 * @param wrap
	 * @param args
	 * @return
	 */
	public static String join(String separator, String wrap, Object... args) {
		if (args == null || args.length <= 0)
			return "";
		return _join(separator == null ? BLANK : separator, wrap, Arrays.asList(args).iterator());
	}

	public static String join(String separator, String wrap, Collection<?> c) {
		if (CollectionUtils.notEmpty(c))
			return _join(separator, wrap, c.iterator());
		return EMPTY;
	}

	/**
	 * Join a basso livello senza la librearia Google.
	 * 
	 * @param separator
	 * @param parts
	 * @return
	 */
	private static String _join(String separator, String wrap, Iterator<?> parts) {
		StringBuffer appendable = new StringBuffer();
		if (parts.hasNext()) {
			appendable.append(wrap(wrap, parts.next()));
			while (parts.hasNext()) {
				appendable.append(separator);
				appendable.append(wrap(wrap, parts.next()));
			}
		}
		return appendable.toString();
	}

	/**
	 * Join a basso livello senza la librearia Google.
	 * 
	 * @param appendable
	 * @param separator
	 * @param parts
	 * @return
	 */
	public static StringBuffer join(StringBuffer appendable, String separator, Iterator<?> parts) {
		if (parts.hasNext()) {
			appendable.append(s(parts.next()));
			while (parts.hasNext()) {
				appendable.append(separator);
				appendable.append(s(parts.next()));
			}
		}
		return appendable;
	}

	// lunghezza in caratteri dal Long.MAX_VALUE (necessario perchè il Math.random() viene castato a Long per poi essere
	// convertito in stringa)
	private static final int LENGTH_LIMIT = (int) Math.ceil(Math.log10(Long.MAX_VALUE)) - 1;
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,;:-_#@!?\"£$%&/()=<>";
	private static final SecureRandom rnd = new SecureRandom();

	/**
	 * Genera una stringa di cifre random di lunghezza variabile (ricorsivamente).
	 * 
	 * @param length
	 * @return
	 */
	public static String randomDigits(int length) {
		String random = "";
		if (length > LENGTH_LIMIT) {
			length = length - LENGTH_LIMIT;
			random = randomDigits(LENGTH_LIMIT);
		}
		random += left("" + (long) (Math.random() * (Math.pow(10, length))), length, '0');
		return random;
	}

	/**
	 * Genera una stringa di caratteri random di lunghezza variabile
	 * 
	 * @param len
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	/**
	 * Converte una stringa in bytes UTF.
	 * 
	 * @param str
	 * @param off
	 * @param len
	 * @return
	 */
	public static byte[] stringToBytesUTFCustom(String str, int off, int len) {
		char[] buffer = sub(str, off, len).toCharArray();
		byte[] b = new byte[buffer.length << 1];
		for (int i = 0; i < buffer.length; i++) {
			int bpos = i << 1;
			b[bpos] = (byte) ((buffer[i] & 0xFF00) >> 8);
			b[bpos + 1] = (byte) (buffer[i] & 0x00FF);
		}
		return b;
	}

	/**
	 * Converte una stringa in byte ASCII.
	 * 
	 * @param str
	 * @param off
	 * @param len
	 * @return
	 */
	public static byte[] stringToBytesASCII(String str, int off, int len) {
		char[] buffer = sub(str, off, len).toCharArray();
		byte[] b = new byte[buffer.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) buffer[i];
		}
		return b;
	}

	/**
	 * Ritorna una porzione di stringa in sicurezza, senza superare i limiti.
	 * 
	 * @param str
	 * @param from
	 *            start index
	 * @param to
	 *            end index
	 * @return
	 */
	public static String sub(String s, int from, int to) {
		int strlen = s(s).length();
		return s(s).substring(Math.min(from, strlen), Math.min(Math.max(to, from), strlen));
	}

	/**
	 * Mette la iniziala maiuscola
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalize(final String s) {
		return handle(s).first().upper().s + handle(s).last(1).lower().s;
	}

	/**
	 * Mette tutte le iniziali maiuscole
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalizeAll(final String s) {
		final String[] words = s.split(" ");
		if (words.length == 1)
			return capitalize(s);
		else
			return capitalizeAll(words);
	}

	public static String capitalizeAll(final String[] words) {
		final StringBuilder strb = new StringBuilder();
		int i = 0;
		for (final String word : words)
			if (i++ == 0)
				strb.append(capitalize(word));
			else
				strb.append(" " + capitalize(word));
		return strb.toString();
	}

	public static Handler handle(final String s) {
		final Handler h = new Handler(s);
		return h;
	}

	public static class Handler {
		String s;

		private Handler(final String s) {
			this.s = StringHandler.toString(s);
		}

		@Override
		public String toString() {
			return s;
		}

		public String str() {
			return s;
		}

		public Handler concat(final String s2) {
			s += StringHandler.toString(s2);
			return this;
		}

		public Handler replace(final String from, final String to) {
			s = s.replaceAll(from, to);
			return this;
		}

		public Handler upper() {
			s = s.toUpperCase();
			return this;
		}

		public Handler lower() {
			s = s.toLowerCase();
			return this;
		}

		public Handler capital() {
			s = StringHandler.capitalizeAll(s);
			return this;
		}

		public Handler sub(final int from, final int to) {
			s = StringHandler.sub(s, from, to);
			return this;
		}

		public Handler first() {
			s = StringHandler.sub(s, 0, 1);
			return this;
		}

		public Handler first(final int to) {
			s = StringHandler.sub(s, 0, to);
			return this;
		}

		public Handler last() {
			s = StringHandler.sub(s, s.length() - 1, s.length());
			return this;
		}

		public Handler last(final int from) {
			s = StringHandler.sub(s, from, s.length());
			return this;
		}
	}

	/*
	 * ESCAPE methods
	 */

	/**
	 * Raddoppia tutti gli apici, (') => ('').
	 *
	 * @param s
	 * @return
	 */
	public static String toSql(final String s) {
		return StringHandler.toString(s).replace("'", "''");
	}

	/*
	 * POJO INSPECT
	 */

	/**
	 * Con l'ausilio delle classi java.lang.reflect.* ispeziona tutte le proprietà del POJO (purchè siano accessibili da
	 * un GETTER) e mi ritorna una stringa che le concatena in formato JSON-like.
	 * 
	 * @param model
	 * @return
	 */
	public static String pojoToString(Object model) {
		if (model == null)
			return NULL;
		StringBuffer buf = new StringBuffer();
		/*
		 * Se incontra una Collection<?> invoca ricorsivamente per ogni elemento.
		 */
		if (model instanceof Collection<?>) {
			Collection<?> collectionModel = (Collection<?>) model;
			buf.append("[");
			for (Object listItem : collectionModel) {
				buf.append(pojoToString(listItem));
			}
			buf.append("[");
			return buf.toString();
		}
		buf.append(model.getClass().getSimpleName() + " {");
		try {
			/*
			 * Usando la classe Field della libreria Reflection non e' stato possibile accedere a tutte le proprietà
			 * perche' non si puo'
			 * accedere ad un field privato, allora workaround: ciclo tutti i metodi e uso solo i GETTERs!
			 */
			for (Method method : model.getClass().getMethods())
				/*
				 * Per individuare su method e' GETTER eseguo test if:
				 * 1) e' definito dalla classe runtime (ovvero non appartiene alla superclasse)
				 * 2) non e' statico
				 * 3) comincia con get
				 * 4) non ha parametri/argomenti
				 * inoltre controllo che invocandolo ..
				 * 5) non mi torna un oggetto nullo
				 */
				if (method.getDeclaringClass().equals(model.getClass()) // 1)
						&& !Modifier.isStatic(method.getModifiers()) 	// 2)
						&& method.getName().startsWith("get") 			// 3)
						&& method.getParameterTypes().length == 0 		// 4)
						&& method.invoke(model) != null					// 5)
				) {
					buf.append(String.format("'%s' : '%s'", method.getName().substring("get".length()), method.invoke(model)));
					buf.append(", ");
				}
			if (model.getClass().getSimpleName().length() + ", ".length() < buf.length())
				buf.delete(buf.length() - ", ".length(), buf.length());
		} catch (Exception e) {
			e.printStackTrace();
			buf.append(" ..an exception occurred! ");
		}
		return buf.append(" }").toString();
	}

	/**
	 * Come pojoToString però stampa anche le proprietà delle superclassi, purchè siano accessibili da un GETTER.
	 * 
	 * @param model
	 * @return
	 */
	public static String pojoHierarchyToString(Object model) {
		if (model == null)
			return NULL;
		StringBuffer buf = new StringBuffer();
		/*
		 * Se incontra una Collection<?> invoca ricorsivamente per ogni elemento.
		 */
		if (model instanceof Collection<?>) {
			Collection<?> collectionModel = (Collection<?>) model;
			buf.append("[");
			for (Object listItem : collectionModel) {
				buf.append(pojoToString(listItem));
			}
			buf.append("[");
			return buf.toString();
		}
		buf.append(model.getClass().getSimpleName() + " {");
		try {
			/*
			 * Qui andiamo a cercare anche i GETTERs dichiarati dalle superclassi, dunque tralasciamo il test numero 1).
			 */
			for (Method method : model.getClass().getMethods())
				/*
				 * Per individuare su method e' GETTER eseguo test if:
				 * 1) e' definito dalla classe runtime ==> ELIMINO questo test così leggo anche i metodi della
				 * superclasse
				 * 2) non e' statico
				 * 3) comincia con get
				 * 4) non ha parametri/argomenti
				 * inoltre controllo che invocandolo ..
				 * 5) non mi torna un oggetto nullo
				 */
				if (!Modifier.isStatic(method.getModifiers()) 		// 2)
						&& method.getName().startsWith("get") 		// 3)
						&& method.getParameterTypes().length == 0 	// 4)
						&& method.invoke(model) != null				// 5)
				) {
					buf.append(String.format("'%s' : '%s'", method.getName().substring("get".length()), method.invoke(model)));
					buf.append(", ");
				}
			if (model.getClass().getSimpleName().length() + ", ".length() < buf.length())
				buf.delete(buf.length() - ", ".length(), buf.length());
		} catch (Exception e) {
			e.printStackTrace();
			buf.append(" ..an exception occurred! ");
		}
		return buf.append(" }").toString();
	}

	// delimiter dei campi JSON
	public static final String DEL = "\"";

	/**
	 * Generic {@link Object#toString()}. Ritorna un JSON con le properieà dell'oggetto in input.
	 * 
	 * @param klazz
	 */
	public static <T> String toStringJson(T o) {
		if (o == null)
			return NULL;
		List<String> props = new ArrayList<String>();
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field : fields)
			try {
				Method method = o.getClass().getDeclaredMethod("get" + StringHandler.capitalize(field.getName()));
				Object value = method.invoke(o);
				if (value instanceof String || value instanceof Date)
					value = DEL + value + DEL;
				props.add(String.format("%s: %s", DEL + field.getName() + DEL, value));
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		// return String.format("{\n 'class': %s,\n 'properties': {\n%s\n }\n}", o.getClass().getName(), join(",\n", "
		// %s", props));
		return "{\n" + join(",\n", "  %s", props) + "\n}";
	}
}
