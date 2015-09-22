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

/**
 * mass spectrum specific peak picking (Thorstens algo)
 * does not work!
 * 
 * @author Thomas Weber
 */
public class MSPeakPicking
  implements IPeakPicking {
  
  final int MS_SIGNIFICANT_PEAKS = 32;
  
  /**
   * MSPeakPicking constructor comment.
   */
  public MSPeakPicking() {
    super();
  }
  /**
   * calculate method comment.
   */
  public Vector calculate(Spectrum1D spectrum) {
    double amu = 100;
    double rj = 40;
    double rk = 50;
    double rl = 7;
    Vector assigns = new Vector();
    IArray1D x = spectrum.getXData();
    IArray1D y = spectrum.getYData();
    int n = y.getLength();
    Integer[] peaks = new Integer[n];
    for (int i = 0; i < n; i++) {
      peaks[i] = new Integer(i);
    }
    int[] sigPeaks = new int[MS_SIGNIFICANT_PEAKS];
    int nSigs;
    // sort peak by descending weight
    sortPeaksWeighted(spectrum, peaks, 0, n - 1);
    // insert first 16 peaks
    nSigs = Math.min(MS_SIGNIFICANT_PEAKS, n);
    for (int i = 0; i < nSigs; i++)
      sigPeaks[i] = peaks[i].intValue();
    if (n <= MS_SIGNIFICANT_PEAKS) {
      for (int i = 0; i < nSigs; i++)
	assigns.addElement(new Peak1D(spectrum, x.pointAt(sigPeaks[i])));
      return assigns;
    }

    // check if range of masses is lower then <code>amu</code>
    double range = x.getRange1D().getXWidth();
    if (amu > 0.0 && range < amu) {
      double sf = range / amu;
      amu = range;
      rj *= sf;
      rk *= sf;
    }
    // start filtering
    //
    // Pass 1:
    // Each peak is considered in turn to be the center of a mass window
    // at most <code>(amu + 1)</code>  wide.
    // A peak is rejected during pass 1 if there are <code>((rj/amu)*(2*rk+1))</code>
    // larger peaks in its own window.
    // The first peak in the spectrum has a mass window only <code>amu/2</code> wide, so
    // it is rejected if therare <code>((rj/amu)*((amu+1)/2)</code> larger peaks in the
    // <code>amu/2</code> above it. Similar remarks apply to all peaks near the beginning or
    // end of the spectrum.
    // If pass 1 would save more than 200 peaks, the parameters are implicitetly reduced so
    // that 200 or fewer peaks are saved.
    //
    // Pass 2:
    // Most of the noise peaks have now been eliminated, producing for most psectra, a series of
    // relatively separated clusters. The algorithm of pass 1 is repeated, but the mass window
    // is narrowed (default <code>rl = +/- 7*amu</code>).
    // Thus a peak will be rejected during pass 2 if there are
    // <code>(rj/amu*(rl*2+1)</code> in its <code>(rl*2+1)</code> neighborhood.
    // After pass 2, the 16 largest peaks saved are added to the list of peaks if they are not
    // already present.

    double a1;
    double intervalLeft;
    double intervalRight;
    double realLeft;
    double realRight;
    double ratio;
    for (int filter = 0; filter < 2; filter++) {
      int resultDatapoints = 0;
      int[] tmpPeaks = new int[n];
      if (filter == 0)
	a1 = rk;
      else
	a1 = rl;
      // select peak
      for (int i = 0; i < n; i++) {
	int remainPeaks = (int) ((rj / amu) * (2 * a1 + 1));
	intervalLeft = x.pointAt(i) - a1;
	intervalRight = x.pointAt(i) + a1;
	realLeft = Math.max(intervalLeft, x.pointAt(0));
	realRight = Math.min(intervalLeft, x.pointAt(n - 1));
	if ((intervalRight - intervalLeft) == 0.0)
	  ratio = 1.0;
	else
	  ratio = (realRight - realLeft) / (intervalRight - intervalLeft);
	remainPeaks = (int) (remainPeaks * ratio);
	int j = 0;
	for (int k = 0; k < n; k++) {
	  if (x.pointAt(k) >= realLeft && x.pointAt(k) <= realRight && y.pointAt(i) < y.pointAt(k))
	    j++;
	}
	if (j < remainPeaks) {
	  tmpPeaks[resultDatapoints] = i;
	  resultDatapoints++;
	}
      }
      // copy peaks back
      for (int i = 0; i < resultDatapoints; i++)
	peaks[i] = new Integer(tmpPeaks[i]);
      n = resultDatapoints;
    }
    // add most significant peaks	
    int k = 0;
    for (int i = 0; i < nSigs; i++) {
      boolean peakFound = false;
      // check if peak already there
      for (int j = 0; j < n; j++) {
	if (j == sigPeaks[i]) {
	  peakFound = true;
	  break;
	}
      }
      if (!peakFound) {
	peaks[n + k] = new Integer(sigPeaks[i]);
	k++;
      }
    }
    n += k;
    if (n > 0) {
      sortPeaksWeighted(spectrum, peaks, 0, n - 1);
    }
    for (int i = 0; i < n; i++) {
      assigns.addElement(new Peak1D(spectrum, x.pointAt(peaks[i].intValue())));
    }
    return assigns;
  }
  /**
   * sort peaks descending by sqrt(m*i)
   * @param peakIndices java.lang.Integer[]
   */
  private void sortPeaksWeighted(Spectrum1D spectrum, Integer[] peaks, int from, int to) {
    final IArray1D x = spectrum.getXData();
    final IArray1D y = spectrum.getYData();
    java.util.Arrays.sort(peaks, from, to, new java.util.Comparator() {
      public int compare(Object o1, Object o2) {
	int i1 = ((Integer) o1).intValue();
	int i2 = ((Integer) o2).intValue();
	double w1 = Math.sqrt(x.pointAt(i1) * y.pointAt(i1));
	double w2 = Math.sqrt(x.pointAt(i2) * y.pointAt(i2));
	if (w1 < w2)
	  return 1;
	else if (w1 == w2)
	  return 0;
	else
	  return -1;
      }
    });
  }
}
