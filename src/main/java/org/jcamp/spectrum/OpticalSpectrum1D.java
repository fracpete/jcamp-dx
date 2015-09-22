/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.Array1D;
import org.jcamp.math.IArray1D;
import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * base class for all optical spectrum types with 1 independent variable.
 * 
 * @author Thomas Weber
 */
public abstract class OpticalSpectrum1D
  extends Spectrum1D {
  
  /** for serialization. */
  private static final long serialVersionUID = -6989200995078011258L;

  /**
   * default ctor.
   * 
   * @param x org.jcamp.spectrum.IOrderedDataArray1D
   * @param y org.jcamp.spectrum.IDataArray1D
   */
  protected OpticalSpectrum1D(IOrderedDataArray1D x, IDataArray1D y) {
    super(x, y);
  }

  /**
   * default ctor.
   */
  protected OpticalSpectrum1D() {
    super();
  }

  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    OpticalSpectrum1D spectrum = (OpticalSpectrum1D) super.clone();
    spectrum.xAxisMap = new LinearAxisMap(spectrum.xData);
    spectrum.yAxisMap = new LinearAxisMap(spectrum.yData);
    return spectrum;
  }

  /**
   * convert transmittance data to absorbance 
   */
  public void convertToAbsorbance() {

    IDataArray1D newY = convertToAbsorbance(yData);
    if (newY != yData) {
      setData(xData, newY);
      newY.setLabel(null); // force automatic y axis labeling
      adjustFullViewRange();
    }
  }

  /**
   * convert intensities to percent
   */
  public void convertToPercent() {
    IDataArray1D newY = convertToPercent(yData);
    if (newY != yData) {
      setData(xData, newY);
      newY.setLabel(null); // force automatic y axis labeling
      adjustFullViewRange();
    }
  }

  /**
   * convert absorbance to transmittance
   */
  public void convertToTransmittance() {
    IDataArray1D newY = convertToTransmittance(yData);
    if (newY != yData) {
      setData(xData, newY);
      newY.setLabel(null); // force automatic y axis labeling
      adjustFullViewRange();
    }
  }

  /**
   * gets spectrum ID.
   * @return int
   */
  @Override
  public abstract int getIdentifier();

  /**
   * isSameType method comment.
   */
  @Override
  public abstract boolean isSameType(Spectrum otherSpectrum);

  /**
   * heuristical check for transmission spectrum
   * @return boolean
   */
  public boolean isTransmission() {
    Unit yunit = yData.getUnit();
    if (yunit.equals(CommonUnit.percentTransmittance) || yunit.equals(CommonUnit.transmittance))
      return true;
    if (yunit.equals(CommonUnit.percentAbsorbance) || yunit.equals(CommonUnit.absorbance))
      return false;
    return isTransmission(yData);
  }

  /**
   * simple test if spectrum is a transmission spectrum.
   * @return boolean
   */
  private static boolean isTransmission(IArray1D data) {
    Range1D.Double range = data.getRange1D();
    double yhigh = range.getXMax() - 0.1 * range.getXWidth();
    double ylow = range.getXMin() + 0.1 * range.getXWidth();
    int nlow = 0;
    int nhigh = 0;
    for (int i = 0; i < data.getLength(); i++) {
      double y = data.pointAt(i);
      if (y > yhigh)
	nhigh++;
      else if (y < ylow)
	nlow++;
    }
    if (nhigh > nlow)
      return true;
    else
      return false;
  }

  /**
   * default ctor.
   * 
   * @param x org.jcamp.spectrum.IOrderedDataArray1D
   * @param y org.jcamp.spectrum.IDataArray1D
   * @param fullSpectrum boolean
   */
  protected OpticalSpectrum1D(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
    super(x, y, fullSpectrum);
    if (isTransmission()) {
      convertToAbsorbance();
      adjustFullViewRange();
    }
  }

  private static IDataArray1D convertToAbsorbance(IDataArray1D data) {
    int n = data.getLength();
    double ref;
    if (data.getUnit().equals(CommonUnit.percentTransmittance)) {
      ref = 100.;
    } else if (data.getUnit().equals(CommonUnit.transmittance)) {
      ref = 1.;
    } else
      return data;

    double[] y = new double[n];
    for (int i = 0; i < n; i++) {
      double t = data.pointAt(i);
      if (t < 0)
	t = 0;
      if (t > ref)
	t = ref;
      if (t > 0)
	y[i] = Math.max(0., Math.log(ref / t) / Math.log(10.));
      else
	y[i] = 1;
    }
    return new ArrayData(new Array1D(y, false), CommonUnit.absorbance);
  }

  /**
   * convertToAbsorbance method comment.
   */
   private static IDataArray1D convertToPercent(IDataArray1D data) {
    int n = data.getLength();
    if (data.getUnit().equals(CommonUnit.transmittance)) {
      double max = data.getRange1D().getXMax();
      double[] y = new double[n];
      for (int i = 0; i < n; i++) {
	y[i] = 100.0 * (data.pointAt(i) / max);
      }
      return new ArrayData(new Array1D(y, false), CommonUnit.percentTransmittance);
    }
    return data;
   }

   /**
    * convertToAbsorbance method comment.
    */
   private static IDataArray1D convertToTransmittance(IDataArray1D data) {
     int n = data.getLength();
     if (data.getUnit().equals(CommonUnit.absorbance)) {
       double[] y = new double[n];
       for (int i = 0; i < n; i++) {
	 double a = data.pointAt(i);
	 if (a > 1)
	   a = 1;
	 if (a < 0)
	   a = 0;
	 y[i] = 100.0 / Math.pow(10, a);
       }
       return new ArrayData(new Array1D(y, false), CommonUnit.percentTransmittance);
     } else
       return data;
   }
}
