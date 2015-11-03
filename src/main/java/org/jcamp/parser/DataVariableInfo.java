/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.util.Vector;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

/**
 * helper class for analyzing the type of a data block.
 * 
 * @author Thomas Weber
 */
public class DataVariableInfo {
	private final static String INCR_RE = "[:space:]*\\(([:alpha:][:digit:]*)\\+\\+\\(([:alpha:][:digit:]*)\\.\\.\\2\\)\\)[:space:]*";
	private final static String LIST_RE = "[:space:]*\\((([:alpha:][:digit:]*){2,})(\\.\\.\\1)?\\)[:space:]*";
	private final static String VAR_RE = "[:alpha:][:digit:]*";
	private final static REProgram INCR_REPRG;
	private final static REProgram LIST_REPRG;
	private final static REProgram VAR_REPRG;
	static {
		try {
			RECompiler compiler = new RECompiler();
			INCR_REPRG = compiler.compile(INCR_RE);
			LIST_REPRG = compiler.compile(LIST_RE);
			VAR_REPRG = compiler.compile(VAR_RE);
		} catch (RESyntaxException e) {
			throw new Error("bad RE in DataVariableInfo");
		}
	}
	private RE incrRE = new RE(INCR_REPRG);
	private RE listRE = new RE(LIST_REPRG);
	private RE varRE = new RE(VAR_REPRG);

	private String[] symbols;
	private boolean incremental;

	/**
	 * ctor from ##XYDATA=, ##XYPOINTS= or ##DATATABLE= LDR.
	 * 
	 * @param ldr
	 *            com.creon.chem.jcamp.JCAMPDataRecord
	 */
	public DataVariableInfo(JCAMPDataRecord xyDataLDR) throws JCAMPException {
		String key = xyDataLDR.getKey();
		if (xyDataLDR.isData()) {
			String xyData = xyDataLDR.getContent();
			String varType;
			int lf = xyData.indexOf("\r\n");
			if (lf < 0) {
				lf = xyData.indexOf("\n"); // non-standard Unix
				if (lf < 0) {
					lf = xyData.indexOf("\r"); // non-standard Mac
					if (lf < 0)
						// confused???
						throw new JCAMPException("corrupt ##" + key + "=");
				}
				varType = xyData.substring(0, lf);
			} else {
				varType = xyData.substring(0, lf);
			}
			if (incrRE.match(varType)) {
				symbols = new String[] { incrRE.getParen(1), incrRE.getParen(2) };
				incremental = true;
			} else if (listRE.match(varType)) {
				String varlist = listRE.getParen(1);
				analyzeVarList(varlist);
				incremental = false;
			} else
				throw new JCAMPException(
						"bad ##XYDATA= variable descriptor: \"" + varType
								+ "\"");
		} else
			throw new JCAMPException("not a data LDR: \"##" + key + "\"");
	}

	/**
	 * build variable info from type indicator (text after ##XYDATA=).
	 * 
	 * @parameter String type indicator
	 * @exception JCAMPException
	 *                thrown if indicator cannot be parsed.
	 */
	public DataVariableInfo(String type) throws JCAMPException {
		super();
		if (incrRE.match(type)) {
			symbols = new String[] { incrRE.getParen(1), incrRE.getParen(2) };
			incremental = true;
		} else if (listRE.match(type)) {
			String varlist = listRE.getParen(1);
			analyzeVarList(varlist);
			incremental = false;
		} else
			throw new JCAMPException("bad variable descriptor: \"" + type
					+ "\"");
	}

	/**
	 * parse variable names from list and store them into <code>symbols</code>
	 * attribute.
	 * 
	 * @param varlist
	 *            String
	 */
	private void analyzeVarList(String varlist) {
		Vector<String> tmp = new Vector<String>();
		int pos = 0;
		while (varRE.match(varlist, pos)) {
			String symbol = varlist.substring(varRE.getParenStart(0),
					varRE.getParenEnd(0));
			tmp.addElement(symbol);
			pos += varRE.getParenLength(0);
		}
		symbols = new String[tmp.size()];
		for (int i = 0; i < symbols.length; i++)
			symbols[i] = tmp.elementAt(i);
	}

	/**
	 * gets recognized variable symbols
	 * 
	 * @return String[]
	 */
	public String[] getSymbols() {
		return symbols;
	}

	/**
	 * indicates (X++(Y..Y)) type of data.
	 * 
	 * @return boolean
	 */
	public boolean isIncremental() {
		return incremental;
	}

	/**
	 * output of symbols
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuilder tmp = new StringBuilder("[");
		for (int i = 0; i < symbols.length; i++) {
			tmp.append(symbols[i]);
			if (i < symbols.length - 1)
				tmp.append(',');
		}
		tmp.append("]");
		return tmp.toString();
	}
}
