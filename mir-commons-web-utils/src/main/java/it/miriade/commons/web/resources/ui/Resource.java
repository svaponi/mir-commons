package it.miriade.commons.web.resources.ui;

import java.io.Serializable;

public class Resource implements Serializable {

	public static final String PAGE_ATTR = "page";
	public static final String LIST_ATTR = "list";
	public static final String MSG_ATTR = "msg";

	private static final long serialVersionUID = 1L;

	public Resource() {
		super();
	}

	protected String parse(Object text) {
		return text != null ? text.toString() : "";
	}

}