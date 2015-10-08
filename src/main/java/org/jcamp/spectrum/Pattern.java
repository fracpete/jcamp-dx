/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import java.util.Enumeration;
import java.util.Vector;

/**
 * peak pattern.
 * 
 * @author Thomas Weber
 */
public class Pattern implements ISpectrumLabel, Cloneable {

	private Multiplicity multiplicity = Multiplicity.UNKNOWN;
	private double[] position = new double[2];
	{
		this.position[0] = 0.0;
		this.position[1] = 1.0;
	}
	private Spectrum1D spectrum = null;
	private Peak1D[] peaks = null;

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(double position, Multiplicity multi) {
		super();
		this.position[0] = position;
		this.spectrum = null;
		this.multiplicity = multi;
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(double position, Multiplicity multi, Peak1D[] peaks) {
		this(null, position, multi, peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(double position, String multi) {
		this(position, Multiplicity.multiplicityOf(multi));
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Multiplicity multi, Peak1D[] peaks) {
		super();
		double position = 0.0;
		this.spectrum = null;
		this.multiplicity = multi;
		if (peaks == null || peaks.length == 0)
			throw new IllegalArgumentException(
					"pattern without position or peaks");
		setPeaks(peaks);
		for (int i = 0; i < peaks.length; i++) {
			position += this.peaks[i].getPosition()[0];
		}
		this.position[0] = position / this.peaks.length;
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, Peak1D[] peaks) {
		this(spectrum, Multiplicity.multiplicityOf(peaks.length), peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, double position, Multiplicity multi) {
		super();
		this.position[0] = position;
		this.spectrum = spectrum;
		this.multiplicity = multi;
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, double position, Multiplicity multi,
			Peak1D[] peaks) {
		super();
		this.position[0] = position;
		this.spectrum = spectrum;
		this.multiplicity = multi;
		setPeaks(peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, double position, Multiplicity multi,
			Vector peaks) {
		super();
		this.position[0] = position;
		this.spectrum = spectrum;
		this.multiplicity = multi;
		setPeaks(peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, double position, String multi) {
		this(spectrum, position, Multiplicity.multiplicityOf(multi));
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, double position, String multi,
			Peak1D[] peaks) {
		this(spectrum, position, Multiplicity.multiplicityOf(multi), peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, double position, String multi,
			Vector peaks) {
		this(spectrum, position, Multiplicity.multiplicityOf(multi), peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, Multiplicity multi, Peak1D[] peaks) {
		super();
		double position = 0.0;
		this.spectrum = spectrum;
		this.multiplicity = multi;
		if (peaks == null || peaks.length == 0)
			throw new IllegalArgumentException(
					"pattern without position or peaks");
		setPeaks(peaks);
		for (int i = 0; i < peaks.length; i++) {
			position += this.peaks[i].getPosition()[0];
		}
		this.position[0] = position / this.peaks.length;
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, Multiplicity multi, Vector peaks) {
		super();
		double position = 0.0;
		this.spectrum = spectrum;
		this.multiplicity = multi;
		if (peaks == null || peaks.size() == 0)
			throw new IllegalArgumentException(
					"pattern without position or peaks");
		this.peaks = new Peak1D[peaks.size()];
		for (int i = 0; i < peaks.size(); i++) {
			this.peaks[i] = (Peak1D) ((Peak1D) peaks.elementAt(i)).clone();
			position += this.peaks[i].getPosition()[0];
		}
		this.position[0] = position / this.peaks.length;
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, String multi, Peak1D[] peaks) {
		this(spectrum, Multiplicity.multiplicityOf(multi), peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, String multi, Vector peaks) {
		this(spectrum, Multiplicity.multiplicityOf(multi), peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(Spectrum1D spectrum, Vector peaks) {
		this(spectrum, Multiplicity.multiplicityOf(peaks.size()), peaks);
	}

	/**
	 * PeakPattern constructor comment.
	 */
	public Pattern(String multi, Peak1D[] peaks) {
		this(Multiplicity.multiplicityOf(multi), peaks);
	}

	/**
	 * cloning.
	 * 
	 * @return java.lang.Object
	 */
	@Override
	public Object clone() {
		Pattern pattern = new Pattern(spectrum, position[0], multiplicity,
				peaks);
		return pattern;
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
	 * gets the label (multiplicity).
	 */
	@Override
	public java.lang.String getLabel() {
		return multiplicity.getSymbol();
	}

	/**
	 * gets multiplicity.
	 * 
	 * @return int
	 */
	public Multiplicity getMultiplicity() {
		return multiplicity;
	}

	/**
	 * returns array of peaks.
	 * 
	 * @return Peak1D[]
	 */
	public Peak1D[] getPeaks() {
		return peaks;
	}

	/**
	 * gets position of pattern (center of pattern).
	 * 
	 * @return double[]
	 */
	@Override
	public double[] getPosition() {
		return position;
	}

	/**
	 * gets the spectrum the pattern belongs to.
	 * 
	 * @return Spectrum1D
	 */
	public Spectrum1D getSpectrum() {
		return spectrum;
	}

	/**
	 * gets peaks of the pattern as an enumeration
	 * 
	 * @return Vector
	 */
	public Enumeration peaks() {
		return new Enumeration() {
			int i = 0;

			@Override
			public Object nextElement() {
				if (peaks != null && i < peaks.length) {
					Peak1D peak = peaks[i];
					i++;
					return peak;
				}
				return null;
			}

			@Override
			public boolean hasMoreElements() {
				if (peaks != null && i < peaks.length - 1)
					return true;
				return false;
			}
		};
	}

	/**
	 * sets the multiplicity.
	 * 
	 * @param newMultiplicity
	 *            int
	 */
	public void setMultiplicity(Multiplicity newMultiplicity) {
		multiplicity = newMultiplicity;
	}

	/**
	 * sets peaks by copying a peak array.
	 */
	public void setPeaks(Peak1D[] peaks) {
		this.peaks = null;
		if (peaks != null) {
			this.peaks = new Peak1D[peaks.length];
			for (int i = 0; i < peaks.length; i++)
				this.peaks[i] = (Peak1D) peaks[i].clone();
		}
	}

	/**
	 * sets peaks by copying from a vector of peaks.
	 */
	public void setPeaks(Vector peaks) {
		this.peaks = null;
		if (peaks != null) {
			this.peaks = new Peak1D[peaks.size()];
			for (int i = 0; i < peaks.size(); i++) {
				this.peaks[i] = (Peak1D) ((Peak1D) peaks.elementAt(i)).clone();
			}
		}
	}

	/**
	 * sets pattern to new position.
	 * 
	 * @param newPosition
	 *            double[]
	 */
	public void setPosition(double[] newPosition) {
		position[0] = newPosition[0];
		position[1] = newPosition[1];
	}

	/**
	 * assign spectrum to pattern.
	 * 
	 * @param newSpectrum
	 *            Spectrum1D
	 */
	public void setSpectrum(Spectrum1D newSpectrum) {
		if (this.spectrum != newSpectrum) {
			this.spectrum = newSpectrum;
			if (peaks != null) {
				for (int i = 0; i < this.peaks.length; i++) {
					peaks[i].setSpectrum(this.spectrum);
				}
			}
		}
	}

	/**
	 * write debug info for label.
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String toString() {
		StringBuilder label = new StringBuilder("Pattern[");
		for (int i = 0; i < position.length; i++) {
			label.append(position[i]);
			if (i < position.length - 1)
				label.append(",");
		}
		label.append("]: ").append(getLabel());
		return label.toString();
	}

	/**
	 * translate the pattern with all its peaks by <code>amount</code>.
	 */
	@Override
	public void translate(double[] amount) {
		position[0] += amount[0];
		position[1] += amount[1];
		for (int i = 0; i < this.peaks.length; i++)
			this.peaks[i].translate(amount);
	}
}
