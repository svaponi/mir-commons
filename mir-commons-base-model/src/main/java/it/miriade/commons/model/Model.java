package it.miriade.commons.model;

public interface Model {

	public enum Order {
		ASC, DESC;
	}

	public static final int ERROR_CODE = -1;

	// nome della named query usata per ritornare una collection di elementi con id compresi nella collection di ID
	// passata come argomento
	public static final String findByIDListQueryName = ".findByIDList";

	public static final String UID = "uid";

	public static final String IS_NULL = "IsNull";
	public static final String IS_NOT_NULL = "IsNotNull";
	public static final String ORDER_BY = "orderBy";
	public static final String FROM = "from";
	public static final String TO = "to";

}
