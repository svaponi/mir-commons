package it.miriade.commons.dbutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import it.miriade.commons.utils.StringHandler;

/**
 * Incapsula il risultato della query.
 * 
 * @author svaponi
 * @created Jul 21, 2016 13:10:30 PM
 */
public class DbResult {

	public List<List<Object>> set = new ArrayList<List<Object>>();
	public List<String> columns = new Vector<String>();
	public List<String> types = new Vector<String>();
	public List<String> classes = new Vector<String>();

	@Override
	public String toString() {
		final StringBuilder strb = new StringBuilder();
		strb.append("COLUMNS:");
		strb.append("\n" + Arrays.deepToString(columns.toArray()));
		strb.append("\nRECORDS:");
		for (final List<Object> row : set)
			strb.append("\n" + Arrays.deepToString(row.toArray()));
		return strb.toString();
	}

	public int size() {
		return set.size();
	}

	public Object get(final int rowId, final String colName) {
		if (StringHandler.noText(colName))
			throw new IllegalArgumentException("Invalid column name");
		if (!columns.contains(colName))
			throw new IllegalArgumentException("Invalid column name <" + colName + ">");
		return this.get(rowId, columns.indexOf(colName));
	}

	public Object get(final int rowId, final int colId) {
		List<Object> row;
		try {
			row = set.get(rowId);
		} catch (final Exception e) {
			throw new IllegalArgumentException("Invalid row number [" + rowId + "]");
		}
		try {
			return row.get(colId);
		} catch (final Exception e) {
			throw new IllegalArgumentException("Invalid column number [" + colId + "]");
		}
	}

}
