package it.miriade.commons.logging;

/**
 * Utility per inizializzare i log
 *
 * @author svaponi
 * @created 2015-03-01 12:04:41 PM
 */
public interface LogInitializer {

	public String getConfigFilePath();

	public void reset();

	public void reset(String propertyFilePath);
}