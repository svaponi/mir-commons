package it.miriade.commons.model.dao;

import java.util.List;

import it.miriade.commons.model.entities.ModelEntity;
import it.miriade.commons.utils.StringHandler;

/**
 * Classe utility con metodi visibili solo all'interno del package.
 * 
 * @author svaponi
 * @created Jun 29, 2016 7:26:48 PM
 */
class PrintUtil {

	static String printResult(List<?> results, boolean isTraceEnabled) {
		return results == null ? "FAIL (exception occurred)"
				: results.size() == 1 ? "SUCCESS" : String.format("FAIL (size: %s) %s", results.size(), isTraceEnabled ? deepPrint(results) : "");
	}

	/**
	 * Metodo ausiliario usato in: save(), saveAndGetId(), merge().
	 * 
	 * @param entity
	 * @return
	 */
	static String smartPrint(ModelEntity<?> entity, boolean isTraceEnabled) {
		return isTraceEnabled ? deepPrint(entity) : simplePrint(entity);
	}

	static String simplePrint(ModelEntity<?> entity) {
		return entity.getClass().getSimpleName() + " (" + entity.getUid() + ")";
	}

	static String deepPrint(Object obj) {
		return StringHandler.pojoToString(obj);
	}
}
