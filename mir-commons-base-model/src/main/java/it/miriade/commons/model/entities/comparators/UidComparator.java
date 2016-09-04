package it.miriade.commons.model.entities.comparators;

import java.io.Serializable;
import java.util.Comparator;

import it.miriade.commons.model.entities.ModelEntity;

/**
 * Comparator che confronta due oggetti {@link ModelEntity} in base al valore del loro Unique ID.
 * 
 * @See {@link Comparator}
 * @author svaponi
 */
public class UidComparator<PK extends Serializable> implements Comparator<ModelEntity<PK>> {

	@SuppressWarnings("unchecked")
	@Override
	public int compare(ModelEntity<PK> one, ModelEntity<PK> another) {
		if (one == null && another == null)
			return 0;
		if (one == null)
			return -1;
		if (another == null)
			return 1;
		PK oneId = one.getUid();
		PK anotherId = another.getUid();
		if (oneId == null && anotherId == null)
			return 0;
		if (oneId == null)
			return -1;
		if (anotherId == null)
			return 1;
		if (oneId instanceof Comparable<?>)
			return ((Comparable<PK>) oneId).compareTo((PK) anotherId);
		else
			return oneId.equals(anotherId) ? 0 : 1;
	}

}
