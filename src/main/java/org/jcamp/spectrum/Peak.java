/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

/**
 * Prototype implementation for for peaks and bands.
 * 
 * @author Thomas Weber
 * 
 * @see Peak1D
 */
public abstract class Peak implements ISpectrumLabel {

	/** index of data point assigned to this peak */
	protected double[] position = new double[] { 0.0, 1.0 };

	/** height of peak */
	protected double height = 1.0;

	/** spectrum this peak is belonging to */
	protected Spectrum spectrum;

	/**
	 * add a peak to the spectrum at the given position.
	 * 
	 * @param spectrum
	 *            Spectrum
	 * @param position
	 *            double[]
	 */
	public Peak(Spectrum spectrum, double[] position) {
		this.spectrum = spectrum;
		int n = position.length;
		this.position = new double[n];
		for (int i = 0; i < n; i++)
			this.position[i] = position[i];
	}

	/**
	 * add a peak to the spectrum at the given position.
	 * 
	 * @param spectrum
	 *            Spectrum
	 * @param position
	 *            double[]
	 */
	public Peak(Spectrum spectrum, double[] position, double height) {
		this.spectrum = spectrum;
		int n = position.length;
		this.position = new double[n];
		for (int i = 0; i < n; i++)
			this.position[i] = position[i];
		this.height = height;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @return double
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * returns the label displayed.
	 * 
	 * @return java.lang.String
	 */
	@Override
	public abstract String getLabel();

	/**
	 * returns the position.
	 * 
	 * @return double[]
	 */
	@Override
	public double[] getPosition() {
		return position;
	}

	/**
	 * gets the spectrum
	 * 
	 * @return Spectrum
	 */
	public Spectrum getSpectrum() {
		return spectrum;
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @param newHeight
	 *            double
	 */
	public void setHeight(double newHeight) {
		height = newHeight;
	}

	/**
	 * sets the position
	 * 
	 * @param newPosition
	 *            double[]
	 */
	public void setPosition(double[] newPosition) {
		position[0] = newPosition[0];
		position[1] = newPosition[1];
	}

	/**
	 * sets the spectrum this peak belongs to.
	 * 
	 * @param newSpectrum
	 *            Spectrum
	 */
	public void setSpectrum(Spectrum newSpectrum) {
		spectrum = newSpectrum;
	}

	/**
	 * write debug info for label.
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String toString() {
		StringBuilder label = new StringBuilder("Peak[");
		for (int i = 0; i < position.length; i++) {
			label.append(position[i]);
			if (i < position.length - 1)
				label.append(",");
		}
		label.append("]: ").append(getLabel());
		return label.toString();
	}

	/**
	 * translates peak by translation vector <code>amount</code>.
	 * 
	 * @param amount
	 *            double[]
	 */
	@Override
	public void translate(double[] amount) {
		position[0] += amount[0];
	}
}
