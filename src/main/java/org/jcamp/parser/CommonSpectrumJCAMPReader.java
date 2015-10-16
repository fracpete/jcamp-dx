/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * ****************************************************************************
 */
package org.jcamp.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;

import org.jcamp.math.IArray2D;
import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IAssignmentTarget;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.Multiplicity;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.Spectrum1D;
import org.jcamp.spectrum.assignments.AtomReference;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * common implementation of JCAMPReader, implements shorthands for common LDRs.
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 * Kerner</a>
 */
public class CommonSpectrumJCAMPReader implements ISpectrumJCAMPReader {

	private final static Logger log = LoggerFactory
			.getLogger(CommonSpectrumJCAMPReader.class);

	/**
	 * analyse assignment text for targets.
	 *
	 * currently assumes SpecInfo convention of a list of integer atom numbers
	 *
	 * @return IAssignmentTarget[]
	 * @param assign java.lang.String
	 */
	protected static IAssignmentTarget[] parseAssignment(String assign) {
		ArrayList<AtomReference> targets = new ArrayList<AtomReference>(5);
		StringTokenizer tokenizer = new StringTokenizer(assign, ",");
		while (tokenizer.hasMoreTokens()) {
			String target = tokenizer.nextToken();
			try {
				int atomNo = Integer.parseInt(target.trim());
				targets.add(new AtomReference(null, atomNo));
			} catch (Exception e) {
			}
		}
		IAssignmentTarget[] assigns = new IAssignmentTarget[targets.size()];
		for (int i = 0; i < targets.size(); i++) {
			assigns[i] = targets.get(i);
		}
		return assigns;
	}

	/**
	 * create peak spectrum from peak table. adds all intensities belonging to
	 * the same x-position up
	 *
	 * @param peaks Peak1D[]
	 * @return double[][] array of {x, y}
	 */
	protected static double[][] peakTableToPeakSpectrum(Peak1D[] peaks)
			throws JCAMPException {
		int n = peaks.length;
		if (n == 0) {
			throw new JCAMPException("empty peak table");
		}
		Arrays.sort(peaks);
		ArrayList<Double> px = new ArrayList<Double>(n);
		ArrayList<Double> py = new ArrayList<Double>(n);
		double x0 = peaks[0].getPosition()[0];
		double y0 = peaks[0].getHeight();
		for (int i = 1; i < n; i++) {
			double x = peaks[i].getPosition()[0];
			double y = peaks[i].getHeight();
			if (x - x0 > Double.MIN_VALUE) {
				px.add(new Double(x0));
				py.add(new Double(y0));
				x0 = x;
				y0 = y;
			} else {
				y0 += y;
			}
		}
		px.add(new Double(x0));
		py.add(new Double(y0));
		double[][] xy = new double[2][px.size()];
		for (int i = 0; i < px.size(); i++) {
			xy[0][i] = px.get(i).doubleValue();
			xy[1][i] = py.get(i).doubleValue();
		}
		return xy;
	}

	/**
	 * CommonJCAMPAdapter constructor comment.
	 */
	protected CommonSpectrumJCAMPReader() {
		super();
	}

	/**
	 * createSpectrum method comment.
	 */
	@Override
	public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
		int expectedSpectrumType = getExpectedSpectrumType();
		if (expectedSpectrumType != -1
				&& block.getSpectrumType() != expectedSpectrumType) {
			throw new JCAMPException("JCAMP reader adapter missmatch");
		}
		Spectrum spectrum = null;
		BlockType type = block.getBlockType();
		if (type.equals(BlockType.FULLSPECTRUM)) {
			spectrum = createFS(block);
		} else if (type.equals(BlockType.PEAKTABLE)) {
			spectrum = createPeakTable(block);
		} else if (type.equals(BlockType.ASSIGNMENT)) {
			spectrum = createPeakTable(block);
		} else {
			if (!type.equals(BlockType.STRUCTURE)) {
				java.util.logging.Logger.getLogger(getClass().getName()).log(Level.WARNING, "Illegal block type");
			}
			return null;
		}
		setNotes(block, spectrum);
		return spectrum;
	}

	protected int getExpectedSpectrumType() {
		return -1;
	}

	protected Spectrum createFS(JCAMPBlock block) throws JCAMPException {
		Spectrum1D spectrum;
		Unit xUnit = null;
		try {
			xUnit = getXUnits(block);
		} catch (JCAMPException e) {
		}
		Unit yUnit = null;
		try {
			yUnit = getYUnits(block);
		} catch (JCAMPException e) {
		}
		double xFactor;
		try {
			xFactor = getXFactor(block);
		} catch (JCAMPException e) {
			xFactor = 1.0;
		}
		double yFactor;
		try {
			yFactor = getYFactor(block);
		} catch (JCAMPException e) {
			yFactor = 1.0;
		}
		int nPoints = getNPoints(block);
		if (block.getDataRecord("XYDATA") != null) {
			double firstX = getFirstX(block);
			double lastX = getLastX(block);
			double[] intensities = getXYData(block, firstX, lastX, nPoints,
					xFactor, yFactor);
			if (intensities.length != nPoints) {
				throw new JCAMPException(
						"incorrect ##NPOINTS= or bad ##XYDATA=");
			}
			IOrderedDataArray1D x = new EquidistantData(firstX, lastX, nPoints,
					xUnit);
			IDataArray1D y = new ArrayData(intensities, yUnit);
			spectrum = new Spectrum1D(x, y, true);
		} else if (block.getDataRecord("XYPOINTS") != null) {
			double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
			IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
			IDataArray1D y = new ArrayData(xy[1], yUnit);
			spectrum = new Spectrum1D(x, y, false);
		} else {
			throw new JCAMPException(
					"missing data: ##XYDATA= or ##XYPOINTS= required.");
		}
		return spectrum;
	}

	protected Spectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
		Spectrum1D spectrum = null;
		Unit xUnit = getXUnits(block),
				yUnit = getYUnits(block);
		double xFactor = getXFactor(block);
		double yFactor = getYFactor(block);
		int nPoints = getNPoints(block);
		Object[] tables = getPeaktable(block, nPoints, xFactor, yFactor);
		Peak1D[] peaks = (Peak1D[]) tables[0];
		if (nPoints != peaks.length) {
			if (log.isErrorEnabled()) {
				log.error("incorrect ##NPOINTS=: expected "
						+ Integer.toString(nPoints) + " but got "
						+ Integer.toString(peaks.length));
			}
			nPoints = peaks.length;
		}
		double[][] xy = peakTableToPeakSpectrum(peaks);
		IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
		IDataArray1D y = new ArrayData(xy[1], yUnit);
		spectrum = new Spectrum1D(x, y, false);
		spectrum.setPeakTable(peaks);
		if (tables.length > 1) {
			spectrum.setPatternTable((Pattern[]) tables[1]);
			if (tables.length > 2) {
				spectrum.setAssignments((Assignment[]) tables[2]);
			}
		}
		return spectrum;
	}

	/**
	 * gets ##LASTX= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected double getFirstX(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable x = block.getVariable("X");
		if (x == null || x.getFirst() == null) {
			if (log.isErrorEnabled()) {
				log.error("missing first x");
			}
		}
		return x.getFirst().doubleValue();
	}

	/**
	 * gets ##FIRSTY= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected double getFirstY(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable y = block.getVariable("Y");
		if (y == null || y.getFirst() == null) {
			if (log.isErrorEnabled()) {
				log.error("missing first y");
			}
		}
		return y.getFirst().doubleValue();
	}

	/**
	 * gets ##LASTX= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected double getLastX(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable x = block.getVariable("X");
		if (x == null || x.getLast() == null) {
			if (log.isErrorEnabled()) {
				log.error("missing last x");
			}
		}
		return x.getLast().doubleValue();
	}

	/**
	 * gets ##NPOINTS= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected int getNPoints(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrNPoints = block.getDataRecord("NPOINTS");
		if (ldrNPoints == null) {
			if (log.isErrorEnabled()) {
				log.error("missing required label ##NPOINTS=");
			}
		}
		String nPoints = ldrNPoints.getContent();
		return Integer.parseInt(nPoints);
	}

	/**
	 * gets NTuple Page data.
	 *
	 * @param block com.creon.chem.jcamp.JCAMPBlock
	 * @param page com.creon.chem.jcamp.JCAMPNTuplePage
	 * @return IArray2D
	 */
	protected IArray2D getNTuplePageData(JCAMPNTuplePage page)
			throws JCAMPException {
		return page.getXYData();
	}

	/**
	 * gets ##DATATABLE= content
	 *
	 * @return double[]
	 * @param block JCAMPBlock
	 * @param page JCAMPNTuplePage
	 * @param firstX double starting x value (from ##FIRSTX=)
	 * @param lastX double ending x value (from ##LASTX=)
	 * @param nPoints int number of data points (from ##NPOINTS=)
	 * @param xFactor double factor to be applied to x values (from ##XFACTOR=)
	 * @param yFactor double factor to be applied to y values (from ##YFACTOR=)
	 * @exception JCAMPException The exception description.
	 * @deprecated
	 */
	@Deprecated
	protected double[] getNTupleXYData(JCAMPBlock block, JCAMPNTuplePage page,
			double firstX, double lastX, int nPoints, double xFactor,
			double yFactor) throws JCAMPException {

		JCAMPDataRecord ldrXYData = page.getDataRecord("DATATABLE");
		if (ldrXYData == null) {
			throw new JCAMPException("missing required label ##DATATABLE=");
		}
		DataVariableInfo varInfo = new DataVariableInfo(ldrXYData);
		if (!varInfo.isIncremental()) {
			throw new JCAMPException("data form missmatch");
		}

		double[] y = block.getASDFDecoder().decode(ldrXYData, firstX, lastX,
				xFactor, nPoints);
		int n = y.length;
		double[] yValues = new double[n];
		for (int i = 0; i < n; i++) {
			yValues[i] = yFactor * y[i];
		}
		return yValues;
	}

	/**
	 * gets ##DATATABLE= content
	 *
	 * @return double[]
	 * @param block JCAMPBlock
	 * @param nPoints int number of data points (from ##NPOINTS=)
	 * @exception JCAMPException The exception description.
	 * @deprecated
	 */
	@Deprecated
	protected double[][] getNTupleXYPoints(JCAMPBlock block,
			JCAMPNTuplePage page) throws JCAMPException {
		JCAMPDataRecord ldrPeaktable = page.getDataRecord("DATATABLE");
		if (ldrPeaktable == null) {
			throw new JCAMPException("missing required label ##DATATABLE=");
		}
		double[][] xy = new double[2][];
		ArrayList<Double> x = new ArrayList<Double>(20);
		ArrayList<Double> y = new ArrayList<Double>(20);
		AFFNTokenizer tokenizer = new AFFNTokenizer(ldrPeaktable);
		while (tokenizer.hasMoreGroups()) {
			AFFNGroup group = tokenizer.nextGroup();
			x.add(new Double(group.getValue(0)));
			y.add(new Double(group.getValue(1)));
		}
		xy[0] = new double[x.size()];
		xy[1] = new double[y.size()];
		for (int i = 0; i < x.size(); i++) {
			xy[0][i] = x.get(i).doubleValue();
			xy[1][i] = y.get(i).doubleValue();
		}
		return xy;
	}

	/**
	 * gets ##ORIGIN= content
	 *
	 * @return java.lang.String
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected String getOrigin(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrOrigin = block.getDataRecord("ORIGIN");
		if (ldrOrigin == null) {
			if (log.isWarnEnabled()) {
				log.warn("missing required label ##ORIGIN=");
			}
			return "UNKNOWN ORIGIN";
		}
		return ldrOrigin.getContent();
	}

	/**
	 * gets ##OWNER= content
	 *
	 * @return java.lang.String
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected String getOwner(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrOwner = block.getDataRecord("OWNER");
		if (ldrOwner == null) {
			if (log.isWarnEnabled()) {
				log.warn("missing required label ##OWNER=");
			}
			return "COPYRIGHT UNKNOWN";
		}
		return ldrOwner.getContent();
	}

	/**
	 * gets ##PEAKTABLE= or ##PEAKASSIGNMENTS= content
	 *
	 * @return Object[] array of Peak[], Pattern[], Assignment[]
	 * @param block JCAMPBlock
	 * @param nPoints int number of data points (from ##NPOINTS=)
	 * @param xFactor double factor to be applied to x values (from ##XFACTOR=)
	 * @param yFactor double factor to be applied to y values (from ##YFACTOR=)
	 * @exception JCAMPException The exception description.
	 */
	protected Object[] getPeaktable(JCAMPBlock block, int nPoints,
			double xFactor, double yFactor) throws JCAMPException {
		JCAMPDataRecord ldrPeaktable = block.getDataRecord("PEAKTABLE");
		if (ldrPeaktable == null) {
			ldrPeaktable = block.getDataRecord("PEAKASSIGNMENTS");
		}
		if (ldrPeaktable == null) {
			ldrPeaktable = block.getDataRecord("XYPOINTS");
		}
		if (ldrPeaktable == null) {
			ldrPeaktable = block.getDataRecord("XYDATA");
		}
		if (ldrPeaktable == null) {
			throw new JCAMPException("missing peak table");
		}
		DatatableTokenizer tokenizer = new DatatableTokenizer(ldrPeaktable);
		if (tokenizer.getType().equals(DataType.XY)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			while (tokenizer.hasMoreGroups()) {
				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				peaks[i] = new Peak1D(x, y);
				i++;
			}
			return new Object[]{peaks};
		} else if (tokenizer.getType().equals(DataType.XYW)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			while (tokenizer.hasMoreGroups()) {

				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				double w = ((Double) group.getValue(2)).doubleValue();
				peaks[i] = new Peak1D(x, y, w);
				i++;
			}
			return new Object[]{peaks};
		} else if (tokenizer.getType().equals(DataType.XYM)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			Pattern[] pattern = new Pattern[nPoints];
			while (tokenizer.hasMoreGroups()) {
				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				Multiplicity m = ((Multiplicity) group.getValue(2));
				peaks[i] = new Peak1D(x, y);
				pattern[i] = new Pattern(x, m);
				i++;
			}
			return new Object[]{peaks, pattern};
		} else if (tokenizer.getType().equals(DataType.XYA)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			Pattern[] pattern = new Pattern[nPoints];
			Assignment[] assigns = new Assignment[nPoints];
			while (tokenizer.hasMoreGroups()) {
				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				String a = (String) group.getValue(2);
				peaks[i] = new Peak1D(x, y);
				IAssignmentTarget[] targets = parseAssignment(a);
				pattern[i] = new Pattern(x, Multiplicity.UNKNOWN,
						new Peak1D[]{peaks[i]});
				assigns[i] = new Assignment(pattern[i], targets);
				i++;
			}
			return new Object[]{peaks, pattern, assigns};
		} else if (tokenizer.getType().equals(DataType.XYMA)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			Pattern[] pattern = new Pattern[nPoints];
			Assignment[] assigns = new Assignment[nPoints];
			while (tokenizer.hasMoreGroups()) {
				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				Multiplicity m = ((Multiplicity) group.getValue(2));
				String a = (String) group.getValue(3);
				IAssignmentTarget[] targets = parseAssignment(a);
				peaks[i] = new Peak1D(x, y);
				pattern[i] = new Pattern(x, m, new Peak1D[]{peaks[i]});
				assigns[i] = new Assignment(pattern[i], targets);
				i++;
			}
			return new Object[]{peaks, pattern, assigns};
		} else if (tokenizer.getType().equals(DataType.XYWA)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			Pattern[] pattern = new Pattern[nPoints];
			Assignment[] assigns = new Assignment[nPoints];
			while (tokenizer.hasMoreGroups()) {
				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				String a = (String) group.getValue(3);
				IAssignmentTarget[] targets = parseAssignment(a);
				peaks[i] = new Peak1D(x, y);
				pattern[i] = new Pattern(x, Multiplicity.UNKNOWN,
						new Peak1D[]{peaks[i]});
				assigns[i] = new Assignment(pattern[i], targets);
				i++;
			}
			return new Object[]{peaks, pattern, assigns};
		} else if (tokenizer.getType().equals(DataType.XYMWA)) {
			int i = 0;
			Peak1D[] peaks = new Peak1D[nPoints];
			Pattern[] pattern = new Pattern[nPoints];
			Assignment[] assigns = new Assignment[nPoints];
			while (tokenizer.hasMoreGroups()) {
				DataGroup group = tokenizer.nextGroup();
				double x = xFactor * ((Double) group.getValue(0)).doubleValue();
				double y = yFactor * ((Double) group.getValue(1)).doubleValue();
				Multiplicity m = ((Multiplicity) group.getValue(2));
				double w = ((Double) group.getValue(3)).doubleValue();
				String a = (String) group.getValue(4);
				IAssignmentTarget[] targets = parseAssignment(a);
				peaks[i] = new Peak1D(x, y, w);
				pattern[i] = new Pattern(x, m, new Peak1D[]{peaks[i]});
				assigns[i] = new Assignment(pattern[i], targets);
				i++;
			}
			return new Object[]{peaks, pattern, assigns};
		} else {
			throw new JCAMPException("unknown peaktable");
		}
	}

	/**
	 * gets ##TITLE= content
	 *
	 * @return java.lang.String
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected String getTitle(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrTitle = block.getDataRecord("TITLE");
		if (ldrTitle == null) { // should never happen here
			throw new JCAMPException("missing required label ##TITLE=");
		}
		return ldrTitle.getContent();
	}

	/**
	 * gets ##XFACTOR= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected double getXFactor(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable x = block.getVariable("X");
		if (x == null || x.getFactor() == null) {
			if (log.isWarnEnabled()) {
				log.warn("missing x factor, assuming 1.0");
			}
			return 1.0;
		}
		return x.getFactor().doubleValue();
	}

	/**
	 * gets ##XUNITS= content
	 *
	 * @return Unit
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected Unit getXUnits(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable x = block.getVariable("X");
		if (x == null || x.getUnit() == null) {
			if (log.isWarnEnabled()) {
				log.warn("missing x unit");
			}
			return CommonUnit.generic;
		}
		return x.getUnit();
	}

	/**
	 * gets ##XYDATA= content fast implementation assumes ASCII encoded text
	 * (required by JCAMP standard)
	 *
	 * @return double[]
	 * @param block JCAMPBlock
	 * @param firstX double starting x value (from ##FIRSTX=)
	 * @param lastX double ending x value (from ##LASTX=)
	 * @param nPoints int number of data points (from ##NPOINTS=)
	 * @param xFactor double factor to be applied to x values (from ##XFACTOR=)
	 * @param yFactor double factor to be applied to y values (from ##YFACTOR=)
	 * @exception JCAMPException The exception description.
	 */
	protected double[] getXYData(JCAMPBlock block, double firstX, double lastX,
			int nPoints, double xFactor, double yFactor) throws JCAMPException {
		JCAMPDataRecord ldrXYData = block.getDataRecord("XYDATA");
		if (ldrXYData == null) {
			throw new JCAMPException("missing required label ##XYDATA=");

		}
		double[] y = block.getASDFDecoder().decode(ldrXYData, firstX, lastX,
				xFactor, nPoints);
		int n = y.length;
		double[] yValues = new double[n];
		for (int i = 0; i < n; i++) {
			yValues[i] = yFactor * y[i];
		}
		return yValues;

	}

	/**
	 * gets ##XYPOINTS= content
	 *
	 * @return double[]
	 * @param block JCAMPBlock
	 * @param nPoints int number of data points (from ##NPOINTS=)
	 * @exception JCAMPException The exception description.
	 */
	protected double[][] getXYPoints(JCAMPBlock block, int nPoints,
			double xFactor, double yFactor) throws JCAMPException {
		class XYPair implements Comparable<XYPair> {

			public double x;
			public double y;

			public XYPair(double x, double y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public int compareTo(XYPair o) {
				XYPair p = o;
				if (this.x < p.x) {
					return -1;
				}
				if (this.x > p.x) {
					return 1;
				}
				return 0;
			}
		}
		;
		JCAMPDataRecord ldrXYPoints = block.getDataRecord("XYPOINTS");
		if (ldrXYPoints == null) {
			throw new JCAMPException("missing required label ##XYPOINTS=");

		}

		int i = 0;
		AFFNTokenizer tokenizer = new AFFNTokenizer(ldrXYPoints);
		TreeSet<XYPair> data = new TreeSet<XYPair>();
		while (tokenizer.hasMoreGroups()) {
			AFFNGroup group = tokenizer.nextGroup();
			data.add(new XYPair(xFactor * group.getValue(0), yFactor
					* group.getValue(1)));
		}
		if (data.size() != nPoints) {
			if (log.isErrorEnabled()) {
				log.error("bad ##NPOINTS= or duplicate X values");
			}
		}
		double[][] xy = new double[2][data.size()];
		for (Iterator<XYPair> it = data.iterator(); it.hasNext();) {
			XYPair p = it.next();
			xy[0][i] = p.x;
			xy[1][i] = p.y;
		}
		return xy;
	}

	/**
	 * gets ##XFACTOR= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected double getYFactor(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable y = block.getVariable("Y");
		if (y == null || y.getFactor() == null) {
			if (log.isWarnEnabled()) {
				log.warn("missing y factor, assuming 1.0");
			}
			return 1.0;
		}
		return y.getFactor().doubleValue();
	}

	/**
	 * gets ##YUNITS= content
	 *
	 * @return Unit
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected Unit getYUnits(JCAMPBlock block) throws JCAMPException {
		JCAMPVariable y = block.getVariable("Y");
		if (y == null || y.getUnit() == null) {
			if (log.isWarnEnabled()) {
				log.warn("missing y unit");
			}
			return CommonUnit.generic;
		}
		return y.getUnit();
	}

	/**
	 * set spectrum note
	 *
	 * @param block JCAMPBlock
	 * @param ldr JCAMPDataRecord
	 * @param spectrum com.creon.chem.spectrum.Spectrum
	 */
	protected void setNote(JCAMPBlock block, JCAMPDataRecord ldr,
			Spectrum spectrum) throws JCAMPException {
		String key = ldr.getKey();
		if (key.length() == 0) // comment
		{
			return;
		}

		NoteDescriptor descr = NoteDescriptorFactory.getInstance()
				.findByJCAMPKey(key);
		if (descr.equals(NoteDescriptor.IGNORE)) {
			return;
		}

		Object content;
		try {
			content = descr.getNoteContentParser().parseContent(
					ldr.getContent(), descr.getNoteContentClass());
			spectrum.setNote(descr, content);
		} catch (org.jcamp.spectrum.notes.BadContentException ex) {
			StringBuilder msg = new StringBuilder("bad ")
					.append(descr.getName()).append(" note:\n")
					.append(ex.getMessage());
			if (log.isWarnEnabled()) {
				log.warn(msg.toString());
			}
		}
	}

	/**
	 * after creation of spectrum, add other notes
	 *
	 * @param block com.creon.chem.jcamp.JCAMPBlock
	 * @param spectrum com.creon.chem.spectrum.Spectrum
	 */
	protected void setNotes(JCAMPBlock block, Spectrum spectrum)
			throws JCAMPException {
		String title = getTitle(block);
		spectrum.setTitle(title);
		/*
		 * String owner = getOwner(block); spectrum.setOwner(owner); String
		 * origin = getOrigin(block); spectrum.setOrigin(origin);
		 */
		int n = block.numDataRecords();
		for (int i = 0; i < n; i++) {
			JCAMPDataRecord ldr = block.getDataRecord(i);
			setNote(block, ldr, spectrum);
		}
	}
}
