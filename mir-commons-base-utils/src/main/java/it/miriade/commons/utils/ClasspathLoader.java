package it.miriade.commons.utils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <strong>Utility.</strong> cerca un file in tutto il classpath. Cicla in tutte
 * le locations del classpath e cerca il path relativo al file (passato come
 * parametro) a partire da ognuno.
 *
 * @see ClasspathLoader#lookForFileOnClasspath(String)
 * @see ClasspathLoader#getClasspathLocations()
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM 2015-03-01
 */
public class ClasspathLoader {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Cerca <code>relativeFilePath</code> in tutte le locations (source-folders) del classpath, ritorna il path
	 * completo al file (in string).<br>
	 * <strong>ATTENZIONE:</strong> <code>relativeFilePath</code> è (deve essere) il path relativo partendo da una delle
	 * source-folders del classpath, questo perchè la ricerca viene fatta sul percorso risultante da
	 * <code>source-folder + relativeFilePath</code>.
	 * Ovvero la ricerca non è ricorsiva, non entra dentro le sotto-cartelle delle source-folders.<br>
	 * Esempio:<br>
	 * 
	 * <pre>
	 * String fileName = "logging/log4j.propeties";
	 * File logProps = new ClasspathLoader().lookForFileOnClasspath(fileName);
	 * logger.debug(logProps.getAbsolutePath());
	 * // stampa: /home/user/workspace/project/target/classes/logging/log4j.propeties
	 * </pre>
	 * 
	 * Questo utilizzo è corretto. Se fosse <code>relativeFilePath = "log4j.propeties"</code> il file non sarebbe
	 * individuato.
	 *
	 * @param relativeFilePath
	 * @return File Oggetto java.io.File cercato nel classpath
	 * @throws RuntimeException
	 */
	public File lookForFileOnClasspath(String relativeFilePath) throws RuntimeException {
		URL[] locs = this.getClasspathLocations();
		logger.trace("Looking for file " + relativeFilePath + " in " + locs.length + " classpath locations");

		/**
		 * Cicla su tutti i path del classpath attualmente in uso (escludendo i
		 * JAR file)
		 */
		for (URL url : Arrays.asList(locs)) {

			if (!url.getPath().endsWith(".jar")) {
				try {

					String path = url.getPath();

					if (!path.endsWith("/")) {
						path = path.concat("/");
					}

					logger.trace("Classpath location: " + path);

					String fullPath = path + relativeFilePath;
					String fName = FileHandler.getName(fullPath);

					File file = new File(fullPath);
					if (file.exists() && !file.isDirectory()) {
						logger.trace("File " + fName + " successfully found in classpath!");
						return file;
					} else
						logger.trace("File " + fName + " not found.. ");

				} catch (Exception e) {
					logger.error("Error searching in " + url.getPath());
				}
			}
		}

		throw new RuntimeException("File " + relativeFilePath + " not found in any classpath location");
	}

	/**
	 * Return an array containing all classpath locations, including JAR files.
	 *
	 * @return
	 */
	public URL[] getClasspathLocations() {
		return ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs();
	}

	/**
	 * Return only one location, useless.
	 *
	 * @return
	 */
	public URL getClasspathLocation() {
		return this.getClass().getProtectionDomain().getCodeSource().getLocation();
	}
}
