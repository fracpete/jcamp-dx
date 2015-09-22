/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import java.util.Vector;

import org.jcamp.math.IArray1D;
import org.jcamp.math.IOrderedArray1D;
import org.jcamp.math.Range1D;

/**
 * simple peak picking algorithm using a minimum height and a noise range.
 * 
 * @author Thomas Weber
 */
public class MinHeightPeakPicking
  implements IPeakPicking {
  
  /**
   * minimum height for a peak 
   */
  private double minHeight;

  /**
   * noise factor 
   */
  private double noise;

  /**
   * maximum number of peaks allowed 
   */
  private int maxPeaks = 40;
  /**
   * MinHeightPeakPicking constructor comment.
   */
  public MinHeightPeakPicking(double minHeight, double noise) {    
    super();
    this.minHeight = minHeight;
    this.noise = noise;
  }
  /**
   * calc peak range starting from data point at <code>index</code>.
   */
  private int calcPeakRange(Peak1D peak, int index, double noise) {
    Spectrum1D spectrum = (Spectrum1D) peak.getSpectrum();
    IOrderedArray1D xdata = (IOrderedArray1D) spectrum.getXData();
    IArray1D idata = spectrum.getYData();
    int lindex = index;
    int rindex = index;
    double int0, int1;
    lindex = -1;
    for (int j = index; j >= 1; --j) {
      int1 = idata.pointAt(j);
      int0 = idata.pointAt(j - 1);
      if (int0 > int1 && Math.abs(int1 - int0) < noise) {
	lindex = j;
	break;
      }
    }
    lindex = Math.max(lindex, 0);
    rindex = xdata.getLength();
    for (int j = index + 1; j <= xdata.getLength() - 2; j++) {
      int1 = idata.pointAt(j + 1);
      int0 = idata.pointAt(j);
      if (int1 > int0 && Math.abs(int1 - int0) < noise) {
	rindex = j;
	break;
      }
    }
    rindex = Math.min(xdata.getLength() - 1, rindex);
    Range1D.Double range = new Range1D.Double(xdata.pointAt(lindex), xdata.pointAt(rindex));
    peak.setRange(range);
    int0 = idata.pointAt(lindex);
    index = lindex;
    for (int i = lindex; i <= rindex; i++) {
      int1 = idata.pointAt(i);
      if (int1 > int0) {
	index = i;
	int0 = int1;
      }
    }
    return index;
  }
  public Vector calculate(Spectrum1D spectrum) {
    Vector peakVector;
    if (spectrum.isFullSpectrum()) {
      double currentNoise = getNoise();
      do {
	peakVector = pickPeaksFS(spectrum, currentNoise);
	currentNoise *= 2;
      } while (peakVector.size() > maxPeaks);
      return peakVector;
    } else { // simple for peak table
      return pickPeaksPT(spectrum);
    }
  }
  /**
   * gets the maximum number of peaks allowed.
   * @return int
   */
  public int getMaxPeaks() {
    return this.maxPeaks;
  }
  /**
   * gets the minimum height parameter.
   * @return double
   */
  public double getMinHeight() {
    return this.minHeight;
  }
  /**
   * gets the noise parameter.
   * @return double
   */
  public double getNoise() {
    return noise;
  }
  /**
   * peak picking for full spectrum
   */
  private Vector pickPeaksFS(Spectrum1D spectrum, double noise) {
    Vector peaks = new Vector();
    IOrderedArray1D xdata = spectrum.getXData();
    IArray1D idata = spectrum.getYData();
    int n = xdata.getLength();
    int imax;
    double int0, int1;
    double base = 0;
    for (int i = 0; i < n; i++) {
      int0 = idata.pointAt(i);
      imax = i;
      if (Math.abs(int0 - base) > noise) {
	for (int j = i + 1; j < n; j++) {
	  int0 = idata.pointAt(j - 1);
	  int1 = idata.pointAt(j);
	  if (int1 >= int0) {
	    imax = j;
	  } else {
	    break;
	  }
	}
	//Peak1D peak = new Peak1D(spectrum, xdata.pointAt(imax));
	Peak1D peak = new Peak1D(xdata.pointAt(imax), idata.pointAt(imax));
	peak.setSpectrum(spectrum);
	imax = calcPeakRange(peak, imax, noise);
	int0 = idata.pointAt(imax);
	if (int0 > minHeight) {
	  peaks.addElement(peak);
	  i = Math.max(i, peak.getIndexRange().getXMax());
	}
      }
    }
    return peaks;
  }
  /**
   * peak picking for peak table
   */
  private Vector pickPeaksPT(Spectrum1D spectrum) {
    Vector peaks = new Vector();
    IOrderedArray1D xdata = spectrum.getXData();
    IArray1D idata = spectrum.getYData();
    int n = xdata.getLength();
    double int0;
    for (int i = 0; i < n; i++) {
      int0 = idata.pointAt(i);
      if (int0 > minHeight) {
	Peak1D peak = new Peak1D(spectrum, xdata.pointAt(i));
	peaks.addElement(peak);
      }
    }
    return peaks;
  }
  /**
   * sets the maximum number of peaks allowed.
   * Creation date: (30.06.00 17:27:03)
   * @param newMaxPeaks int
   */
  public void setMaxPeaks(int newMaxPeaks) {
    this.maxPeaks = newMaxPeaks;
  }
  /**
   * minimum height for a peak to be recognized
   * @param newMinHeight double
   */
  public void setMinHeight(double newMinHeight) {
    this.minHeight = newMinHeight;
  }
  /**
   * sets the noise parameter.
   * @param newNoise double
   */
  public void setNoise(double newNoise) {
    this.noise = newNoise;
  }
}
