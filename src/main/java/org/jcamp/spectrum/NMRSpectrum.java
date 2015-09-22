/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.Range1D;
import org.jcamp.math.Range2D;
import org.jcamp.math.ReversedLinearAxisMap;
import org.jcamp.parser.JCAMPReader;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * 1D NMR spectrum.
 * 
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public class NMRSpectrum extends Spectrum1D {

	@Override
	public String toString() {
		return "NMRSpectrum [nucleus=" + nucleus + ", frequency=" + frequency
				+ ", reference=" + reference + ", solventReference="
				+ solventReference + ", fid=" + fid + ", solvent=" + solvent
				+ ", mode=" + mode + "]";
	}

	/** for serialization. */
	private static final long serialVersionUID = -7481239141912664609L;

	public final static Unit DEFAULT_YUNIT = CommonUnit.intensity;
	public final static Unit DEFAULT_XUNIT = CommonUnit.hertz;
	protected String nucleus;
	protected double frequency = 400.0;
	protected double reference = 0;
	protected double solventReference = 0; // solvent reference in ppm
	protected boolean fid = false;
	protected String solvent = "TMS";
	private String mode = JCAMPReader.STRICT;

	/**
   */
	protected NMRSpectrum() {
		super();
	}

	/**
	 * standard ctor.
	 * 
	 * @param x
	 *            org.jcamp.spectrum.IOrderedDataArray1D
	 * @param y
	 *            org.jcamp.spectrum.IDataArray1D
	 * @param nucleus
	 *            String
	 * @param freq
	 *            double observe frequency
	 * @param ref
	 *            double reference frequency
	 */
	public NMRSpectrum(IOrderedDataArray1D x, IDataArray1D y, String nucleus,
			double freq, double ref) {
		super(x, y);
		this.frequency = freq;
		this.reference = ref;
		this.nucleus = nucleus;
	}

	/**
	 * standard ctor.
	 * 
	 * @param x
	 *            org.jcamp.spectrum.IOrderedDataArray1D
	 * @param y
	 *            org.jcamp.spectrum.IDataArray1D
	 * @param nucleus
	 *            String
	 * @param freq
	 *            double observe frequency
	 * @param ref
	 *            double reference frequency
	 * @param fullSpectrum
	 *            boolean
	 */
	public NMRSpectrum(IOrderedDataArray1D x, IDataArray1D y, String nucleus,
			double freq, double ref, boolean fullSpectrum, String mode) {
		super(x, y, fullSpectrum);
		this.frequency = freq;
		this.reference = ref;
		this.nucleus = nucleus;
		this.mode = mode;
		convertToPPM();
		convertToPercent();
		this.xAxisMap = new ReversedLinearAxisMap(xData);
		this.yAxisMap = new LinearAxisMap(yData);
		adjustFullViewRange();
	}

	/**
	 * cloning.
	 * 
	 * @return java.lang.Object
	 */
	@Override
	public Object clone() {
		NMRSpectrum spectrum = (NMRSpectrum) super.clone();

		spectrum.xAxisMap = new ReversedLinearAxisMap(spectrum.xData);
		spectrum.yAxisMap = new LinearAxisMap(spectrum.yData);
		return spectrum;
	}

	/**
	 * convert x data to Hz
	 * 
	 */
	protected void convertToHertz() {
		if (frequency != frequency) // safety check
			return;

		if (xData.getUnit().equals(CommonUnit.ppm)) {
			// hz = freq * ppm + ref;
			this.xData.scale(frequency);
			if (mode == JCAMPReader.STRICT && reference == reference) // we are
																		// notin
																		// a
																		// peak
																		// table
				this.xData.translate(reference);
			this.xData.setUnit(CommonUnit.hertz);
			adjustFullViewRange();
		}
	}

	/**
	 * convert an array of ppm values to hertz.
	 */
	public static double[] convertToHertz(double[] ppm, double freq, double ref) {
		int n = ppm.length;
		double[] hz = new double[n];
		for (int i = 0; i < n; i++) {
			hz[i] = freq * ppm[i] + ref;
		}
		return hz;
	}

	/**
	 * convert a ppm value to hertz.
	 */
	public static double convertToHertz(double ppm, double freq, double ref) {
		double hz = freq * ppm + ref;
		return hz;
	}

	/**
	 * convert y data to percent
	 * 
	 */
	protected void convertToPercent() {
		if (!yData.equals(CommonUnit.percentIntensity)) {
			// rescale intensities to percent of maximum
			double imax = yData.getRange1D().getXMax();
			if (imax != 0.0) {
				imax = 100. / imax;
				yData.scale(imax);
				yData.setUnit(CommonUnit.percentIntensity);
			}
		}
	}

	/**
	 * converts intensities to percentage.
	 * 
	 * @param intensities
	 *            double[]
	 */
	public static void convertToPercent(double[] intensities) {
		// rescale intensities to percent of maximum
		double imax = Math.abs(intensities[0]);
		for (int i = 1; i < intensities.length; i++) {
			double iabs = Math.abs(intensities[i]);
			if (iabs > imax)
				imax = iabs;
		}
		if (imax != 0.0) {
			imax = 100. / imax;
			for (int i = 0; i < intensities.length; i++)
				intensities[i] *= imax;
		}
	}

	/**
	 * convert x data to Hz
	 * 
	 */
	protected void convertToPPM() {
		if (frequency != frequency)
			return;
		if (xData.getUnit().equals(CommonUnit.hertz)) {
			// ppm = (hz - ref) / freq;
			if (mode == JCAMPReader.STRICT && reference == reference)
				xData.translate(-reference);
			xData.scale(1. / frequency);
			xData.setUnit(CommonUnit.ppm);
			adjustFullViewRange();
		}
	}

	/**
	 * convert an array of hertz values to ppms.
	 */
	public static double[] convertToPPM(double[] hz, double freq, double ref) {
		int n = hz.length;
		double[] ppm = new double[n];
		for (int i = 0; i < n; i++) {
			ppm[i] = (hz[i] - ref) / freq;
		}
		return ppm;
	}

	/**
	 * convert a hertz values to ppm.
	 */
	public static double convertToPPM(double hz, double freq, double ref) {
		double ppm = (hz - ref) / freq;
		return ppm;
	}

	/**
	 * getDataRange method comment.
	 */
	@Override
	public Range2D.Double getDataRange() {
		return new Range2D.Double(xAxisMap.getDataRange(),
				yAxisMap.getDataRange());
	}

	/**
	 * gets observe frequency.
	 * 
	 * @return double
	 */
	public double getFrequency() {
		return frequency;
	}

	/**
	 * gets spectrum ID.
	 * 
	 * @return int
	 */
	@Override
	public int getIdentifier() {
		return ISpectrumIdentifier.NMR;
	}

	/**
	 * gets observe nucleus
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNucleus() {
		return nucleus;
	}

	/**
	 * gets shift reference frequency in Hz.
	 * 
	 * @return double
	 */
	public double getReference() {
		return reference;
	}

	/**
	 * gets solvent.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSolvent() {
		return solvent;
	}

	/**
	 * gets solvent reference in ppm.
	 * 
	 * @return double
	 */
	public double getSolventReference() {
		return solventReference;
	}

	/**
	 * gets full view range on x axis.
	 */
	@Override
	public Range1D.Double getXFullViewRange() {
		return new Range1D.Double(this.xAxisMap.getFullViewRange());
	}

	/**
	 * gets full view range on y axis.
	 */
	@Override
	public Range1D.Double getYFullViewRange() {
		return new Range1D.Double(this.yAxisMap.getFullViewRange());
	}

	/**
	 * indicates a FID.
	 * 
	 * @return boolean
	 */
	public boolean isFID() {
		return false;
	}

	/**
	 * isSameType method comment.
	 */
	@Override
	public boolean isSameType(Spectrum otherSpectrum) {
		if (otherSpectrum instanceof NMRSpectrum)
			return true;
		return false;
	}

	/**
	 * sets observe frequency.
	 * 
	 * @param newFrequency
	 *            double
	 */
	public void setFrequency(double newFrequency) {
		if (this.frequency != newFrequency) {
			convertToHertz();
			this.frequency = newFrequency;
			convertToPPM();
		}
	}

	/**
	 * sets observe nucleus.
	 * 
	 * @param newNucleus
	 *            java.lang.String
	 */
	public void setNucleus(java.lang.String newNucleus) {
		nucleus = newNucleus;
	}

	/**
	 * sets shift reference frequency in Hz.
	 * 
	 * @param newReference
	 *            double
	 */
	public void setReference(double newReference) {
		if (this.reference != newReference) {
			convertToHertz();
			this.reference = newReference;
			convertToPPM();
		}
	}

	/**
	 * sets solvent.
	 * 
	 * @param newSolvent
	 *            java.lang.String
	 */
	public void setSolvent(java.lang.String newSolvent) {
		solvent = newSolvent;
	}

	/**
	 * sets solvent reference in ppm.
	 * 
	 * @param newSolventReference
	 *            double
	 */
	public void setSolventReference(double newSolventReference) {
		solventReference = newSolventReference;
	}

	/**
	 * sets full view range on x axis.
	 */
	@Override
	public void setXFullViewRange(Range1D.Double dataRange) {
		if (dataRange.getXWidth() > 0)
			this.xAxisMap = new ReversedLinearAxisMap(xData, dataRange);
	}

	/**
	 * sets full view range on y axis.
	 */
	@Override
	public void setYFullViewRange(Range1D.Double dataRange) {
		this.yAxisMap = new LinearAxisMap(yData, dataRange);
	}

	/**
	 * Tells if the mode is STRICT or RELAXED.
	 * 
	 * @return the current mode.
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * Sets the mode to STRICT or RELAXED.
	 * 
	 * @param mode
	 *            The mode to use.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
}
