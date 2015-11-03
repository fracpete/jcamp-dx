/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * enumeration of all standard AFFN data table types.
 * 
 * @author Thomas Weber
 */
public final class DataType implements java.io.Serializable {

	/** for serialization. */
	private static final long serialVersionUID = -4383759508820515637L;

	private final String[] symbols;
	private final String key;
	private final int ordinal;
	public final static DataType XY = new DataType(0, new String[] { "X", "Y" });
	public final static DataType XYW = new DataType(1, new String[] { "X", "Y",
			"W" });
	public final static DataType XYA = new DataType(2, new String[] { "X", "Y",
			"A" });
	public final static DataType XYWA = new DataType(3, new String[] { "X",
			"Y", "W", "A" });
	public final static DataType XYM = new DataType(4, new String[] { "X", "Y",
			"M" });
	public final static DataType XYMA = new DataType(5, new String[] { "X",
			"Y", "M", "A" });
	public final static DataType XYMWA = new DataType(6, new String[] { "X",
			"Y", "M", "W", "A" });
	private final static DataType[] TYPES = new DataType[] { XY, XYW, XYA,
			XYWA, XYM, XYMA, XYMWA };
	private final static List<DataType> TYPES_LIST = Collections
			.unmodifiableList(Arrays.asList(TYPES));

	private DataType(int ordinal, String[] symbols) {
		this.ordinal = ordinal;
		this.symbols = symbols;
		StringBuilder tmp = new StringBuilder();
		for (int i = 0; i < symbols.length; i++)
			tmp.append(symbols[i]);
		key = tmp.toString();
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof DataType && ((DataType) obj) == this)
			return true;
		return false;
	}

	/**
	 * gets variable symbols.
	 * 
	 * @return String[]
	 */
	public String[] getSymbols() {
		return symbols;
	}

	@Override
	public final int hashCode() {
		return ordinal;
	}

	private Object readResolve() throws java.io.ObjectStreamException {
		return TYPES[ordinal];
	}

	@Override
	public String toString() {
		return key;
	}

	public Collection<DataType> types() {
		return TYPES_LIST;
	}
}
