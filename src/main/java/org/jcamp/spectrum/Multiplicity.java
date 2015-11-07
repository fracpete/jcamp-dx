/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * ****************************************************************************
 */
package org.jcamp.spectrum;

/**
 * enumeration class for pattern multiplicities.
 *
 * @author Thomas Weber
 */
public class Multiplicity
		implements java.io.Serializable, Comparable {

	/**
	 * for serialization.
	 */
	private static final long serialVersionUID = -6303420820712169742L;

	private final String symbol;
	private final int ordinal;
	public final static Multiplicity UNKNOWN = new Multiplicity(0, "U");
	public final static Multiplicity SINGULET = new Multiplicity(1, "S");
	public final static Multiplicity DOUBLET = new Multiplicity(2, "D");
	public final static Multiplicity TRIPLET = new Multiplicity(3, "T");
	public final static Multiplicity QUADRUPLET = new Multiplicity(4, "Q");
	public final static Multiplicity MULTIPLET = new Multiplicity(5, "M");
	private final static Multiplicity[] TYPES
			= new Multiplicity[]{UNKNOWN, SINGULET, DOUBLET, TRIPLET, QUADRUPLET, MULTIPLET};
	public final static java.util.List<Multiplicity> TYPES_LIST
			= java.util.Collections.unmodifiableList(java.util.Arrays.asList(TYPES));

	private Multiplicity(int ordinal, String symbol) {
		this.ordinal = ordinal;
		this.symbol = symbol;
	}

	/**
	 * @see java.lang.Comparable
	 */
	public int compareTo(java.lang.Object o) {
		return ordinal - ((Multiplicity) o).ordinal;
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Multiplicity && ((Multiplicity) obj) == this) {
			return true;
		}
		return false;
	}

	/**
	 * gets variable symbol.
	 *
	 * @return String
	 */
	public String getSymbol() {
		return symbol;
	}

	public int getOrdinal() {
		return ordinal;
	}

	@Override
	public final int hashCode() {
		return ordinal;
	}

	/**
	 * helper
	 *
	 * @return int
	 * @param symbol String
	 */
	public static Multiplicity multiplicityOf(char m) {
		switch (m) {
			case 'S':
			case 's':
				return SINGULET;
			case 'D':
			case 'd':
				return DOUBLET;
			case 'T':
			case 't':
				return TRIPLET;
			case 'Q':
			case 'q':
				return QUADRUPLET;
			case 'M':
			case 'm':
				return MULTIPLET;
			default:
				return UNKNOWN;
		}
	}

	/**
	 * helper
	 *
	 * @return int
	 * @param symbol String
	 */
	public static Multiplicity multiplicityOf(int m) {
		if (m >= MULTIPLET.ordinal) {
			return MULTIPLET;
		} else if (m <= 0) {
			return UNKNOWN;
		} else {
			return TYPES[m];
		}
	}

	/**
	 * helper
	 *
	 * @return int
	 * @param symbol String
	 */
	public static Multiplicity multiplicityOf(String symbol) {
		symbol = symbol.trim().toUpperCase();
		for (int i = 0; i < TYPES.length; i++) {
			if (symbol.equals(TYPES[i].getSymbol())) {
				return TYPES[i];
			}
		}
		return Multiplicity.UNKNOWN;
	}

	private Object readResolve() throws java.io.ObjectStreamException {
		return TYPES[ordinal];
	}

	@Override
	public String toString() {
		return symbol;
	}

	public static java.util.Collection types() {
		return TYPES_LIST;
	}
}
