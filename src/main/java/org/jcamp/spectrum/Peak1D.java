/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.Range1D;

/**
 * peak for 1D spectra.
 * 
 * @author Thomas Weber
 */
public class Peak1D extends Peak implements Cloneable {

	/** peak range */
	Range1D.Double range = new Range1D.Double();
	Range1D.Int irange = new Range1D.Int();

	/** number format for peak label */
	static protected java.text.NumberFormat formatPeakPosition = java.text.NumberFormat
			.getInstance();
	static {
		formatPeakPosition.setMaximumFractionDigits(3);
		formatPeakPosition.setMinimumFractionDigits(0);
		formatPeakPosition.setGroupingUsed(false);
	}

	/**
	 * Peak constructor comment.
	 */
	public Peak1D(double x, double y) {
		super(null, new double[] { x, x }, y);
		this.range.set(x, x);
	}

	/**
	 * Peak constructor comment.
	 */
	public Peak1D(double x, double y, double w) {
		super(null, new double[] { x, x }, y);
		this.range.setCenterAndWidth(x, w);
	}

	/**
	 * Peak constructor comment.
	 */
	public Peak1D(Spectrum1D spectrum, double position) {
		super(spectrum, new double[] { position, 1.0 });
		this.range.set(position, position);
	}

	/**
	 * Peak constructor comment.
	 */
	public Peak1D(Spectrum1D spectrum, double position, Range1D.Double range) {
		super(spectrum, new double[] { position });
		setRange(range);
	}

	/**
	 * ctor with spectrum and data point index
	 * 
	 * @param spectrum
	 *            Spectrum1D
	 * @param xIndex
	 *            int
	 */
	public Peak1D(Spectrum1D spectrum, int xIndex) {
		this(spectrum, spectrum.getXData().pointAt(xIndex));
	}

	/**
	 * Peak constructor comment.
	 */
	public Peak1D(Spectrum1D spectrum, int index, Range1D.Double range) {
		this(spectrum, spectrum.getXData().pointAt(index), range);
	}

	/**
	 * cloning.
	 * 
	 * @return java.lang.Object
	 */
	@Override
	public Object clone() {
		Peak1D peak = new Peak1D((Spectrum1D) spectrum, position[0], range);
		return peak;
	}

	@Override
	public int compareTo(ISpectrumLabel obj) {
		double p0 = getPosition()[0];
		double p1 = obj.getPosition()[0];
		if (p0 < p1)
			return -1;
		if (p0 > p1)
			return 1;
		return 0;
	}

	/**
	 * format position label
	 * 
	 * @param x
	 *            double
	 */
	static private String formatPeakPosition(double x) {
		double prec = Math.pow(10.0,
				formatPeakPosition.getMaximumFractionDigits());
		StringBuffer label = new StringBuffer();
		java.text.FieldPosition fp = new java.text.FieldPosition(
				java.text.NumberFormat.INTEGER_FIELD);
		label.setLength(0);
		x = Math.floor(x * prec + .5) / prec;
		formatPeakPosition.format(x, label, fp);
		return label.toString();
	}

	/**
	 * gets range of data point indices.
	 * 
	 * @return Range1D.Int
	 */
	public Range1D.Int getIndexRange() {
		return irange;
	}

	/**
	 * gets label on peak.
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		return formatPeakPosition(position[0]);
	}

	/**
	 * gets peak range.
	 * 
	 * @return Range1D.Double
	 */
	public Range1D.Double getRange() {
		return range;
	}

	/**
	 * sets peak range.
	 * 
	 * @param newRange
	 *            Range1D
	 */
	public void setRange(Range1D newRange) {
		this.range.set(newRange);
		if (spectrum != null) {
			IOrderedDataArray1D xdata = ((Spectrum1D) spectrum).getXData();
			irange = new Range1D.Int(xdata.indexAt(this.range.getXMin()),
					xdata.indexAt(this.range.getXMax()));

		}
	}
}
