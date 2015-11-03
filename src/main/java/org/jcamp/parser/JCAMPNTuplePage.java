/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;
import org.jcamp.math.Array1D;
import org.jcamp.math.IArray2D;
import org.jcamp.math.LinearGrid1D;
import org.jcamp.math.OrderedArray1D;
import org.jcamp.math.XYArray2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class for reading JCAMP NTUPLE pages. JCAMPDataRecord objects are constructed
 * by the containing JCAMPBlock class
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 * @see JCAMPBlock
 */
public class JCAMPNTuplePage {
	private final static String CONTENT_RE = "[:alpha:][:digit:]*[:space:]*=([^,]*)";
	private final static REProgram CONTENT_REPRG;
	private final static Logger log = LoggerFactory
			.getLogger(JCAMPNTuplePage.class);
	private final static String VAR_RE = "([:alpha:][:digit:]*)[:space:]*=";
	private final static REProgram VAR_REPRG;
	static {
		try {
			RECompiler compiler = new RECompiler();
			VAR_REPRG = compiler.compile(VAR_RE);
			CONTENT_REPRG = compiler.compile(CONTENT_RE);
		} catch (RESyntaxException e) {
			throw new Error("bad RE in DataVariableInfo");
		}
	}
	private RE contentRE = new RE(CONTENT_REPRG);

	private JCAMPDataRecord dataLDR;
	private JCAMPDataRecord endLDR;
	private JCAMPNTuple ntuple;
	private JCAMPDataRecord startLDR;
	private String[] symbols;
	private String[] values;
	private DataVariableInfo varInfo;
	private RE varRE = new RE(VAR_REPRG);
	private JCAMPVariable xVar;

	private JCAMPVariable yVar;

	/**
	 * JCAMPLabel constructor comment.
	 */
	JCAMPNTuplePage(JCAMPNTuple ntuple, JCAMPDataRecord startLDR,
			JCAMPDataRecord endLDR) throws JCAMPException {
		super();
		this.startLDR = startLDR;
		this.endLDR = endLDR;
		this.ntuple = ntuple;
		init();
	}

	/**
	 * parse variable values and store them into <code>values</code>
	 *
	 * @param varlist
	 *            String
	 */
	private void analyzeContent(String varlist) {
		values = new String[symbols.length];
		int pos = 0;
		int i = 0;
		while (contentRE.match(varlist, pos)) {
			String value = varlist.substring(contentRE.getParenStart(1),
					contentRE.getParenEnd(1));
			values[i] = value.trim();
			pos += contentRE.getParenLength(0);
			i++;
		}
	}

	/**
	 * parse variable names and store them into <code>symbols</code>
	 *
	 * @param varlist
	 *            String
	 */
	private void analyzeVars(String varlist) throws JCAMPException {
		// read page variables
		ArrayList<String> tmp = new ArrayList<String>();
		int pos = 0;
		while (varRE.match(varlist, pos)) {
			String symbol = varlist.substring(varRE.getParenStart(1),
					varRE.getParenEnd(1));
			tmp.add(symbol.toUpperCase());
			pos += varRE.getParenLength(0);
		}
		symbols = new String[tmp.size()];
		for (int i = 0; i < symbols.length; i++)
			symbols[i] = tmp.get(i);

		// read data block variables
		JCAMPBlock block = this.ntuple.getBlock();
		dataLDR = getDataRecord("DATATABLE");
		if (dataLDR == null)
			throw new JCAMPException("missing required label ##DATATABLE=");
		varInfo = new DataVariableInfo(dataLDR);
		if (varInfo.getSymbols().length != 2 && log.isErrorEnabled())
			log.error("page ##DATATABLE= must be 2 dimensional");
		xVar = this.ntuple.getVariable(varInfo.getSymbols()[0]);
		// always assume first is logical x
		yVar = this.ntuple.getVariable(varInfo.getSymbols()[1]);
	}

	/**
	 * gets LDR by key within NTUPLE page.
	 *
	 * @return com.creon.chem.jcamp.JCAMPDataRecord
	 * @param key
	 *            String
	 */
	public JCAMPDataRecord getDataRecord(String key) {
		JCAMPDataRecord[] ldrs = ntuple.getBlock().getAllDataRecords();
		for (int i = startLDR.getBlockIndex(); i <= endLDR.getBlockIndex(); i++) {
			if (ldrs[i].getKey().equals(key))
				return ldrs[i];
		}
		return null;
	}

	/**
	 * gets data records within page (inclusive ##PAGE= itself).
	 *
	 * @return JCAMPDataRecord[]
	 */
	public JCAMPDataRecord[] getDataRecords() {
		int n = startLDR.getBlockIndex() - endLDR.getBlockIndex() + 1;
		JCAMPDataRecord[] blockLDRs = ntuple.getBlock().getAllDataRecords();
		JCAMPDataRecord[] ldrs = new JCAMPDataRecord[n];
		System.arraycopy(blockLDRs, startLDR.getBlockIndex(), ldrs, 0, n);
		return ldrs;
	}

	/**
	 * DATATABLE variable list.
	 *
	 * @return String[]
	 */
	public String[] getDatatableVariableSymbols() {
		return varInfo.getSymbols();
	}

	/**
	 * accessor to container.
	 *
	 * @return com.creon.chem.jcamp.JCAMPNTuple
	 */
	public JCAMPNTuple getNTuple() {
		return this.ntuple;
	}

	/**
	 * gets variable symbols occuring in ##PAGE= LDR.
	 *
	 * @return String[]
	 */
	public String[] getPageVariableSymbols() {
		return symbols;
	}

	/**
	 * get content of page variable e.g. for ##PAGE= t=10.0
	 * getPageVariableValue("T") returns "10.0"
	 *
	 * @return String
	 * @param symbol
	 *            String
	 */
	public String getPageVariableValue(String symbol) {
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i].equals(symbol))
				return values[i];
		}
		return null;
	}

	/**
	 * gets variable values occuring in ##PAGE= LDR.
	 *
	 * @return String[]
	 */
	public String[] getPageVariableValues() {
		return values;
	}

	/**
	 * get numerical x/y data of page
	 *
	 * @return com.creon.math.IArray2D
	 */
	public IArray2D getXYData() throws JCAMPException {
		IArray2D data = null;
		JCAMPBlock block = this.ntuple.getBlock();
		double xFactor = xVar.getFactor() == null ? 1.0 : xVar.getFactor()
				.doubleValue();
		double yFactor = xVar.getFactor() == null ? 1.0 : yVar.getFactor()
				.doubleValue();
		if (varInfo.isIncremental()) {
			String xSym = xVar.getSymbol().toUpperCase();
			// read in first x
			double xFirst;
			// check local overwrite within page (JCAMP Spec 6.0, NMR2D) with
			// ##FIRSTX=
			JCAMPDataRecord ldrFirstX = getDataRecord("FIRST" + xSym);
			if (ldrFirstX != null) {
				xFirst = Double.parseDouble(ldrFirstX.getContent());
			} else {
				// otherwise check NTUPLE header
				if (xVar.getFirst() == null)
					throw new JCAMPException(
							"missing first value for incremental variable "
									+ xVar.getSymbol());
				xFirst = xVar.getFirst().doubleValue();
			}
			// read in last x
			double xLast;
			// check local overwrite within page (JCAMP Spec 6.0, NMR2D) with
			// ##LASTX=
			JCAMPDataRecord ldrLastX = getDataRecord("LAST" + xSym);
			if (ldrLastX != null) {
				xLast = Double.parseDouble(ldrLastX.getContent());
			} else {
				// otherwise check NTUPLE header
				if (xVar.getLast() == null)
					throw new JCAMPException(
							"missing last value for incremental variable "
									+ xVar.getSymbol());
				xLast = xVar.getLast().doubleValue();
			}
			// read in x dimension
			int xDim;
			// check local overwrite within page (JCAMP Spec 6.0, NMR2D) with
			// ##NPOINTS=
			JCAMPDataRecord ldrNPoints = getDataRecord("NPOINTS");
			if (ldrNPoints != null) {
				xDim = Integer.parseInt(ldrNPoints.getContent());
			} else {
				// otherwise check NTUPLE header
				if (xVar.getDimension() == null)
					throw new JCAMPException(
							"missing dimension for incremental variable "
									+ xVar.getSymbol());
				xDim = xVar.getDimension().intValue();
			}
			double[] y = block.getASDFDecoder().decode(dataLDR, xFirst, xLast,
					xFactor, xDim);
			int n = y.length;
			// if (n != xDim)
			// block.getErrorHandler().error("data dimension missmatch");
			double[] yValues = new double[n];
			for (int i = 0; i < n; i++)
				yValues[i] = yFactor * y[i];
			data = new XYArray2D(new LinearGrid1D(xFirst, xLast, xDim),
					new Array1D(yValues));
		} else {
			ArrayList<double[]> xyVec = new ArrayList<double[]>(20);
			AFFNTokenizer tokenizer = new AFFNTokenizer(dataLDR);
			while (tokenizer.hasMoreGroups()) {
				AFFNGroup group = tokenizer.nextGroup();
				double[] xyElem = new double[2];
				xyElem[0] = group.getValue(0);
				xyElem[1] = group.getValue(1);
				xyVec.add(xyElem);
			}
			// sort by x value
			Collections.sort(xyVec, new Comparator<double[]>() {
				@Override
				public int compare(double[] o1, double[] o2) {
					double[] xy1 = o1;
					double[] xy2 = o2;
					if (xy1[0] < xy2[0])
						return -1;
					if (xy1[0] > xy2[0])
						return 1;
					return 0;
				}
			});
			// eliminate double x
			int start = 1;
			int index;
			boolean duplicateFound;
			do {
				duplicateFound = false;
				for (index = start; index < xyVec.size(); index++) {
					double[] xy2 = xyVec.get(index);
					double[] xy1 = xyVec.get(index - 1);
					if (xy1[0] == xy2[0]) {
						duplicateFound = true;
						xyVec.remove(index);
					}
				}
				start = index;
			} while (duplicateFound);
			double x[] = new double[xyVec.size()];
			double y[] = new double[xyVec.size()];
			for (int i = 0; i < xyVec.size(); i++) {
				x[i] = xyVec.get(i)[0];
				x[i] *= xFactor;
				y[i] = xyVec.get(i)[1];
				y[i] *= yFactor;
			}
			data = new XYArray2D(new OrderedArray1D(x), new Array1D(y));
		}
		return data;
	}

	/**
	 * initialize page variables.
	 *
	 */
	private void init() throws JCAMPException {
		String pageContent = startLDR.getContent();
		analyzeVars(pageContent);
		analyzeContent(pageContent);
	}
}
