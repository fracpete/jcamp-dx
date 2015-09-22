/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import org.jcamp.spectrum.Multiplicity;

/**
 * tokenizer (parser) that handles data in peak tables and peak assignments.
 * 
 * @author Thomas Weber
 */
public class DatatableTokenizer implements java.util.Enumeration<DataGroup> {

	private final DataType type;
	private String data;
	private final String[] varSymbols;
	private int pos;
	private int length;

	/**
	 * AFFNTokenizer constructor comment.
	 */
	public DatatableTokenizer(String[] symbols, String data)
			throws JCAMPException {
		super();
		this.type = checkSymbols(symbols);
		if (this.type == null)
			this.varSymbols = symbols;
		else
			this.varSymbols = this.type.getSymbols();
		this.data = normalizeData(varSymbols.length, data);
		this.length = this.data.length();
	}

	/**
	 * AFFNTokenizer constructor comment.
	 */
	public DatatableTokenizer(JCAMPDataRecord ldr) throws JCAMPException {
		super();
		DataVariableInfo varInfo = new DataVariableInfo(ldr);
		this.type = checkSymbols(varInfo.getSymbols());
		if (this.type == null)
			this.varSymbols = varInfo.getSymbols();
		else
			this.varSymbols = this.type.getSymbols();
		String ldrData = ldr.getContent();
		LineTokenizer lt = new LineTokenizer(ldrData);
		lt.nextLine(); // skip variable declaration
		this.data = normalizeData(varSymbols.length,
				ldrData.substring(lt.getPosition()));
		this.length = this.data.length();
	}

	/**
	 * check for errornous symbols or standard symbols.
	 * 
	 */
	private static DataType checkSymbols(String[] varSymbols)
			throws JCAMPException {
		DataType dataType = null;
		if (varSymbols == null || varSymbols.length == 0)
			throw new JCAMPException("bad variable declaration");
		if (varSymbols.length == 2) {
			if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y"))
				dataType = DataType.XY;
		} else if (varSymbols.length == 3) {
			if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("W"))
				dataType = DataType.XYW;
			else if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("M"))
				dataType = DataType.XYM;
			else if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("A"))
				dataType = DataType.XYA;
		} else if (varSymbols.length == 4) {
			if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("W")
					&& varSymbols[3].equalsIgnoreCase("A"))
				dataType = DataType.XYWA;
			else if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("M")
					&& varSymbols[3].equalsIgnoreCase("A"))
				dataType = DataType.XYMA;
		} else if (varSymbols.length == 5) {
			if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("M")
					&& varSymbols[3].equalsIgnoreCase("W")
					&& varSymbols[4].equalsIgnoreCase("A"))
				dataType = DataType.XYMWA;
		}
		return dataType;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @return com.creon.chem.jcamp.DataType
	 */
	public final DataType getType() {
		return type;
	}

	/**
	 * returns <code>true</code> if more data is available.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean hasMoreElements() {
		return hasMoreGroups();
	}

	/**
	 * returns <code>true</code> if more data is available.
	 * 
	 * @return boolean
	 */
	public boolean hasMoreGroups() {
		if (pos < length)
			return true;
		return false;
	}

	/**
	 * parse next double
	 * 
	 * @return double
	 */
	private Double nextDouble(char delimiter) throws JCAMPException {
		Double x = null;
		String s = nextString(delimiter);
		try {
			x = new Double(s);
		} catch (NumberFormatException e) {
			throw new JCAMPException("bad number format");
		}
		return x;
	}

	/**
	 * gets next data group.
	 * 
	 * @return Object
	 */
	@Override
	public DataGroup nextElement() {
		try {
			return nextGroup();
		} catch (JCAMPException e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.DataGroup
	 */
	public DataGroup nextGroup() throws JCAMPException {
		if (pos >= length)
			throw new JCAMPException("parsed beyond end of data block");
		if (type.equals(DataType.XY))
			return nextXYGroup();
		if (type.equals(DataType.XYW))
			return nextXYWGroup();
		if (type.equals(DataType.XYM))
			return nextXYMGroup();
		if (type.equals(DataType.XYA))
			return nextXYAGroup();
		if (type.equals(DataType.XYWA))
			return nextXYWAGroup();
		if (type.equals(DataType.XYMA))
			return nextXYMAGroup();
		if (type.equals(DataType.XYMWA))
			return nextXYMWAGroup();
		int n = varSymbols.length;
		Object[] values = new Object[n];
		for (int i = 0; i < n - 1; i++) {
			try {
				values[i] = nextQString(',');
			} catch (JCAMPException e) {
				// try numeric data
				String s = nextString(',');
				try {
					values[i] = new Double(s);
				} catch (NumberFormatException ex) {
					values[i] = s;
				}
			}
		}
		try {
			values[n - 1] = nextQString(';');
		} catch (JCAMPException e) {
			// try numeric data
			String s = nextString(';');
			try {
				values[n - 1] = new Double(s);
			} catch (NumberFormatException ex) {
				values[n - 1] = s;
			}
		}
		return new DataGroup(varSymbols, values);
	}

	/**
	 * parse next multiplet char.
	 * 
	 * @return char
	 * @param delimiter
	 *            char
	 */
	private char nextMulti(char delimiter) throws JCAMPException {
		char m;
		String s = nextString(delimiter);
		if (s.length() == 1) {
			m = s.charAt(0);
			if (!(m == 'S' || m == 'D' || m == 'T' || m == 'Q' || m == 'M' || m == 'U'))
				throw new JCAMPException("bad multiplet data: \"" + s + "\"");
		} else {
			throw new JCAMPException("bad multiplet data: \"" + s + "\"");
		}
		return m;
	}

	/**
	 * parse next quoted string.
	 * 
	 * @return java.lang.String
	 * @param delimiter
	 *            char
	 * @exception com.creon.chem.jcamp.JCAMPException
	 *                The exception description.
	 */
	private String nextQString(char delimiter) throws JCAMPException {
		String a;
		int p;
		if (data.charAt(pos) == '<') { // strings are quoted
			pos++;
			p = data.indexOf('>', pos);
			if (p < 0)
				throw new JCAMPException("missing closing '>' quote");
			a = data.substring(pos, p);
			pos = p + 1;
			p = data.indexOf(delimiter, pos);
			if (p < 0)
				throw new JCAMPException("bad data");
			pos = p + 1;
		} else {
			throw new JCAMPException("unquoted string");
		}
		return a;
	}

	/**
	 * parse next unquoted string.
	 * 
	 * @return java.lang.String
	 * @param delimiter
	 *            char
	 * @exception com.creon.chem.jcamp.JCAMPException
	 *                The exception description.
	 */
	private String nextString(char delimiter) throws JCAMPException {
		String a;
		int p;
		p = data.indexOf(delimiter, pos);
		if (p < 0)
			throw new JCAMPException("missing data");
		a = data.substring(pos, p);
		pos = p + 1;
		return a;
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.DataGroup
	 */
	private DataGroup nextXYAGroup() throws JCAMPException {
		Double x;
		Double y;
		String a;
		x = nextDouble(',');
		y = nextDouble(',');
		a = nextQString(';');
		return new DataGroup(x, y, a);
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.AFFNGroup
	 */
	private DataGroup nextXYGroup() throws JCAMPException {
		Double x;
		Double y;
		x = nextDouble(',');
		y = nextDouble(';');
		return new DataGroup(x, y);
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.DataGroup
	 */
	private DataGroup nextXYMAGroup() throws JCAMPException {
		Double x;
		Double y;
		char m;
		String a;
		x = nextDouble(',');
		y = nextDouble(',');
		m = nextMulti(',');
		a = nextQString(';');
		return new DataGroup(x, y, Multiplicity.multiplicityOf(m), a);
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.DataGroup
	 */
	private DataGroup nextXYMGroup() throws JCAMPException {
		Double x;
		Double y;
		char m = 'U';
		x = nextDouble(',');
		y = nextDouble(',');
		m = nextMulti(';');
		return new DataGroup(x, y, Multiplicity.multiplicityOf(m));
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.DataGroup
	 */
	private DataGroup nextXYMWAGroup() throws JCAMPException {
		Double x;
		Double y;
		char m;
		Double w;
		String a;
		x = nextDouble(',');
		y = nextDouble(',');
		m = nextMulti(',');
		w = nextDouble(',');
		a = nextQString(';');
		return new DataGroup(x, y, Multiplicity.multiplicityOf(m), w, a);
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.DataGroup
	 */
	private DataGroup nextXYWAGroup() throws JCAMPException {
		Double x;
		Double y;
		Double w;
		String a;
		x = nextDouble(',');
		y = nextDouble(',');
		w = nextDouble(',');
		a = nextQString(';');
		return new DataGroup(x, y, w, a);
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.AFFNGroup
	 */
	private DataGroup nextXYWGroup() throws JCAMPException {
		Double x;
		Double y;
		Double w;
		x = nextDouble(',');
		y = nextDouble(',');
		w = nextDouble(';');
		return new DataGroup(x, y, w);
	}

	/**
	 * normalize data block in standard form: values are separated by ',',
	 * groups are separated by ';' whitespace is skipped outside quotes quotes
	 * are '&lt;','&gt;' as defined in JCAMP standard
	 * 
	 * @return java.lang.String
	 * @param orig
	 *            java.lang.String
	 */
	private static String normalizeData(int groupLength, String orig)
			throws JCAMPException {
		StringBuilder normal = new StringBuilder();
		boolean inDataValue = false;
		boolean inQuote = false;
		int j = 0;
		for (int i = 0; i < orig.length(); i++) {
			char c = orig.charAt(i);
			if (inQuote) {
				if (c == '>') {
					inQuote = false;
				}
				normal.append(c);
				continue;
			}
			if (c == '<') {
				inDataValue = true;
				inQuote = true;
				normal.append(c);
				continue;
			}
			if (c == '(' || c == ')')
				continue;
			if (Character.isWhitespace(c)) {
				if (inDataValue) {
					inDataValue = false;
					j++;
					if (j < groupLength)
						normal.append(',');
					else {
						normal.append(';');
						j = 0;
					}
				}
				continue;
			}
			if (c == ';') {
				if (inDataValue) {
					inDataValue = false;
					j++;
					if (j < groupLength)
						throw new JCAMPException("missing numbers in data");
					if (j > groupLength)
						throw new JCAMPException("extra numbers in data");
					normal.append(';');
					j = 0;
				}
				continue;
			}
			if (c == ',') {
				if (inDataValue) {
					inDataValue = false;
					j++;
					if (j >= groupLength)
						throw new JCAMPException("extra commas in data");
					normal.append(',');
				}
				continue;
			}
			if (Character.isLetterOrDigit(c) || c == '.' || c == '+'
					|| c == '-') {
				normal.append(c);
				inDataValue = true;
			} else {
				throw new JCAMPException("unexpected character \'" + c
						+ "\' in data");
			}
		}
		normal.append(';');
		return normal.toString();
	}
}
