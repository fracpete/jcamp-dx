/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import java.util.Vector;

import org.jcamp.math.Integral;

/**
 * peak picking algorithm extending the MinHeightPeakPicking algorithm which 
 * uses the area under the peak as an additional criteria.
 * 
 * @author Thomas Weber
 * @see MinHeightPeakPicking
 */
public class MinAreaPeakPicking
  extends MinHeightPeakPicking {
  
  /**
   * minimum area (integral) under a peak 
   */
  private double minArea;
  /**
   * MinAreaPeakPicking constructor comment.
   * @param minHeight double
   * @param noise double
   */
  public MinAreaPeakPicking(double minArea, double minHeight, double noise) {
    super(minHeight, noise);
    this.minArea = minArea;
  }
  /**
   * @return java.util.Vector
   * @param spectrum Spectrum1D
   */
  @Override
  public Vector calculate(Spectrum1D spectrum) {
    Vector filteredPeaks = new Vector();
    if (/*!*/ spectrum.isFullSpectrum()) {
      Vector peaks = super.calculate(spectrum);
      for (int i = 0; i < peaks.size(); i++) {
	Peak1D peak = (Peak1D) peaks.elementAt(i);
	Integral integral = new Spectrum1DIntegral(spectrum, peak.getRange());
	if (integral.getArea() > minArea)
	  filteredPeaks.addElement(peak);
      }
    }
    return filteredPeaks;
  }
  /**
   * Insert the method's description here.
   * Creation date: (30.06.00 18:05:11)
   * @return double
   */
  public double getMinArea() {
    return this.minArea;
  }
  /**
   * Insert the method's description here.
   * Creation date: (30.06.00 18:05:11)
   * @param newMinArea double
   */
  public void setMinArea(double newMinArea) {
    this.minArea = newMinArea;
  }
}
