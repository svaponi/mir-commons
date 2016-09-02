package it.miriade.commons.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileHandler {

	public static final String TILDE = "~";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String DOT = ".";
	public static final String BLANK = "";

	public static final ClasspathLoader loader = new ClasspathLoader();
	public static final String SEPARATOR;
	public static final int KB = 1024;
	public static final int MB = KB * KB;
	public static int BUFFER_SIZE = 16 * KB;

	static {
		String separator = System.getProperty("file.separator");

		if (StringHandler.noText(separator))
			separator = System.getProperty("path.separator");

		if (StringHandler.noText(separator))
			try {
				separator = FileSystems.getDefault().getSeparator();
			} catch (Exception e) {
				e.printStackTrace();
			}

		if (StringHandler.noText(separator))
			switch (OSValidator.getOS()) {
			case WIN:
				separator = BACKSLASH;
				break;

			default:
				separator = SLASH;
				break;
			}

		SEPARATOR = separator;
	}

	/**
	 * Crea un file con il contenuto in ingresso.
	 * 
	 * @param bytes
	 *            contenuto da scrive su file
	 * @param path
	 *            path del file da creare
	 * @return
	 * @throws IOException
	 */
	public static File writeOnFile(byte[] bytes, String path) throws IOException {
		return writeOnFile(bytes, new File(path));
	}

	public static File writeOnFile(byte[] bytes, File file) throws IOException {
		if (bytes == null || file == null)
			throw new IllegalArgumentException("Null argument");
		Files.write(Paths.get(file.toURI()), bytes, file.exists() ? StandardOpenOption.APPEND : StandardOpenOption.CREATE_NEW);
		return file;
	}

	/**
	 * Crea un file con il contenuto in ingresso (senza l'ausilio di librerie).
	 * 
	 * @param content
	 *            contenuto da scrive su file
	 * @param path
	 *            path del file da creare
	 * @return
	 * @throws IOException
	 */
	public static File writeOnFileLowLevel(String content, String path) throws IOException {
		File file = new File(path);
		byte[] bytes = content.getBytes();
		try (OutputStream out = new FileOutputStream(file)) {
			int len = bytes.length;
			int rem = len;
			while (rem > 0) {
				int n = Math.min(rem, BUFFER_SIZE);
				out.write(bytes, (len - rem), n);
				rem -= n;
			}
		}
		return file;
	}

	/**
	 * Ritorna il contenuto del file.
	 * 
	 * @param path
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * Ritorna il contenuto del file.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {
		return readFile(path, Charset.defaultCharset());
	}

	/**
	 * Ritorna l'estensione del file, ovvero la sub-stringa del nome dopo
	 * l'ultimo punto '.'
	 *
	 * @param fileName
	 * @return String extension of file
	 */
	public static String getExtension(String fileName) {
		int from = fileName.lastIndexOf(DOT);
		if (from < 0)
			return BLANK;
		return fileName.substring(from + 1, fileName.length());
	}

	/**
	 * Ritorna l'estensione del file, ovvero la sub-stringa del nome dopo
	 * l'ultimo punto '.'
	 * 
	 * @param file
	 * @return String extension of file
	 */
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	/**
	 * Ritorna il nome del file senza l'estensione, ovvero senza la sub-stringa
	 * del nome dopo l'ultimo punto '.' Se il filename è un path, la funzione
	 * esclude il percorso fino al'ultimo slash (o backslash) per ritornare solo
	 * in nome.
	 *
	 * @param fileName
	 * @return
	 */
	public static String getBasename(String fileName) {
		if (isPath(fileName))
			fileName = getName(fileName);
		int x = fileName.lastIndexOf(DOT);
		if (x < 0)
			return fileName;
		return fileName.substring(0, x);
	}

	/**
	 * Ritorna il nome del file senza l'estensione, ovvero senza la sub-stringa
	 * del nome dopo l'ultimo punto '.' Se il filename è un path, la funzione
	 * esclude il percorso fino al'ultimo slash (o backslash) per ritornare solo
	 * in nome.
	 *
	 * @param file
	 * @return
	 */
	public static String getBasename(File file) {
		return getBasename(file.getName());
	}

	/**
	 * Ritorna il nome del file, completo di estensione, escludendo il percorso
	 * fino al'ultimo slash (o backslash)
	 *
	 * @param fileName
	 * @return
	 */
	public static String getName(String fileName) {
		int x = fileName.lastIndexOf(SEPARATOR);
		if (x < 0)
			return fileName;
		return fileName.substring(x + 1, fileName.length());
	}

	/**
	 * Ritorna il percorso relativo (partendo dall' argomento filename) al file
	 * fino al'ultimo slash (o backslash), <strong>escluso il nome del file
	 * stesso</strong>.
	 *
	 * @param fileName
	 * @return
	 */
	public static String getPath(String fileName) {
		int x = fileName.lastIndexOf(SEPARATOR);
		if (x < 0)
			return "";
		return fileName.substring(0, x + 1);
	}

	/**
	 * Ritorna il percorso della directory padre, se esiste.
	 *
	 * @param fileName
	 * @return
	 */
	public static String getParent(String fileName) {
		int x = fileName.lastIndexOf(getName(fileName));
		if (x <= 0)
			return "";
		else
			return fileName.substring(0, x);
	}

	/**
	 * Ritorna un array con i componenti del filepath: path del padre, basename e extension.
	 *
	 * @param fileName
	 * @return
	 */
	public static String[] splitPath(String fileName) {
		return new String[] { getPath(fileName), getBasename(fileName), getExtension(fileName) };
	}

	/**
	 * Controlla se filename è un percorso, ovvero se contiene slash (o
	 * backslash)
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isPath(String fileName) {
		return fileName.lastIndexOf(SEPARATOR) >= 0;
	}

	/**
	 * Controlla se filename è un percorso assoluto, ovvero se comincia con uno
	 * slash (o backslash)
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean isAbsolutePath(String fileName) {
		return (fileName.startsWith(SEPARATOR) || fileName.startsWith(TILDE + SLASH)) && isPath(fileName);
	}

	/**
	 * Ritorna il path in String CON lo slash finale
	 * 
	 * @param filepath
	 * @return
	 */
	public static String addSlash(String filepath) {
		String path = StringHandler.toString(filepath);
		if (path.endsWith(SEPARATOR))
			return path;
		else
			return path + SEPARATOR;
	}

	/**
	 * Ritorna il path in String SENZA lo slash finale
	 * 
	 * @param filepath
	 * @return
	 */
	public static String removeSlash(String filepath) {
		String path = StringHandler.toString(filepath);
		if (path.endsWith(SEPARATOR))
			return path.substring(0, path.length() - 2);
		else
			return path;
	}

	/**
	 * Crea una directory eliminando quella precedentemente presente.
	 * 
	 * @param dirPath
	 * @return
	 */
	public static File mkdir(String dirPath) {
		return mkdir(dirPath, true);
	}

	/**
	 * Crea una directory eventualmente eliminando quella precedentemente presente.
	 * 
	 * @param dirPath
	 * @return
	 */
	public static File mkdir(String dirPath, boolean force) {
		File mkdir = new File(dirPath);
		if (force && mkdir.exists()) {
			try {
				forceDelete(mkdir);
			} catch (IOException e) {
			}
		}

		if (!mkdir.exists())
			mkdir.mkdirs();

		return mkdir;
	}

	/**
	 * Svuota una directory
	 * 
	 * @param directory
	 * @return
	 */
	public static boolean cleanDirectory(File directory) {

		if (!directory.exists() || !directory.isDirectory())
			return false;

		File[] files = directory.listFiles();
		boolean error = false;
		for (int i = 0; i < files.length; i++) {
			try {
				forceDelete(files[i]);
			} catch (IOException ioe) {
				error = true;
			}
		}

		return !error;
	}

	/**
	 * Rimuove forzatamente un file o directory.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean forceDelete(File file) throws IOException {

		if (!file.exists())
			return true;

		if (file.isDirectory())
			cleanDirectory(file);

		return file.delete();
	}

	/*
	 * Utility per la ricerca (si appoggiano sul ClasspathLoader)
	 */

	/**
	 * Cerca in tutto il classpath il percorso completo al file fino al'ultimo slash ({@link FileHandler#SEPARATOR}),
	 * <strong>escluso il nome del file stesso</strong>.
	 *
	 * @see ClasspathLoader#lookForFileOnClasspath(String)
	 * @see FileHandler#getPath(String)
	 * @param relPath
	 * @return
	 */
	public static String findPath(String relPath) {
		String absolutePath = loader.lookForFileOnClasspath(relPath).getAbsolutePath();
		return getPath(absolutePath);
	}

	/**
	 * Cerca in tutto il classpath il percorso completo al file e poi ritorna il path alla directory padre.
	 *
	 * @see ClasspathLoader#lookForFileOnClasspath(String)
	 * @see FileHandler#getParent(String)
	 * @param relPath
	 * @return
	 */
	public static String findParent(String relPath) {
		String absolutePath = loader.lookForFileOnClasspath(relPath).getAbsolutePath();
		return getParent(absolutePath);
	}

	/**
	 * Cerca in tutto il classpath il percorso completo al file <strong>incluso il nome del file stesso</strong>.
	 *
	 * @see ClasspathLoader#lookForFileOnClasspath(String)
	 * @param relPath
	 * @return String abolute path
	 */
	public static String findURL(String relPath) {
		return loader.lookForFileOnClasspath(relPath).getAbsolutePath();
	}

	/**
	 * Cerca in tutto il classpath e ritorna un oggetto {@link File}.
	 *
	 * @see ClasspathLoader#lookForFileOnClasspath(String)
	 * @param relPath
	 * @return
	 */
	public static File findFileOnClasspath(String relPath) {
		return loader.lookForFileOnClasspath(relPath);
	}

}
