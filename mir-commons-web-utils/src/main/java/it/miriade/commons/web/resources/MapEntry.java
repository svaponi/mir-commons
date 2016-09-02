package it.miriade.commons.web.resources;

import java.util.Map.Entry;

import it.miriade.commons.utils.StringHandler;

public class MapEntry extends BaseResourceJsonIgnoreGetters<String> {

	private static final long serialVersionUID = 1L;
	private Object value;

	public MapEntry() {
		super();
	}

	public MapEntry(String key, Object value) {
		super();
		this.uid = key;
		this.value = value;
	}

	public MapEntry(Entry<String, String> entry) {
		super();
		this.uid = entry.getKey();
		this.value = entry.getValue();
	}

	public boolean isNull() {
		return StringHandler.noText(uid) || StringHandler.noText(StringHandler.toString(value));
	}

	public String getKey() {
		return uid;
	}

	public void setKey(String key) {
		this.uid = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
