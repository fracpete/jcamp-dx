/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

/**
 * spectrum labels (peak labels, pattern labels, assignments etc.).
 * 
 * @author Thomas Weber
 */
public interface ISpectrumLabel extends Comparable<ISpectrumLabel> {

	/**
	 * returns the label text
	 * 
	 * @return String label text
	 */
	String getLabel();

	/**
	 * returns current position in spectrum coordinates
	 * 
	 * @return double[] coordinates of the position
	 */
	double[] getPosition();

	/**
	 * translate the label the translation vector <code>amount</code>
	 * 
	 * @param amount
	 *            double[] translation vector
	 */
	void translate(double[] amount);
}
