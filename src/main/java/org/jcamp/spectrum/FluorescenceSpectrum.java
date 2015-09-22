/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * Fluorescence spectra.
 * 
 * @author Thomas Weber
 */
public class FluorescenceSpectrum
  extends OpticalSpectrum1D {
  
  /** for serialization. */
  private static final long serialVersionUID = 5497963911561409122L;

  public final static Unit DEFAULT_XUNIT = CommonUnit.nanometerWavelength;
  
  public final static Unit DEFAULT_YUNIT = CommonUnit.intensity;

  /**
   * standard ctor.
   * 
   * @param x org.jcamp.spectrum.IOrderedDataArray1D
   * @param y org.jcamp.spectrum.IDataArray1D
   */
  public FluorescenceSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
    super(x, y);
  }

  /**
   * standard ctor.
   * 
   * @param x org.jcamp.spectrum.IOrderedDataArray1D
   * @param y org.jcamp.spectrum.IDataArray1D
   * @param fullSpectrum boolean
   */
  public FluorescenceSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
    super(x, y, fullSpectrum);
  }

  /**
   * gets spectrum ID.
   * @return int
   */
  @Override
  public int getIdentifier() {
    return ISpectrumIdentifier.FLUORESCENCE;
  }

  /**
   * isSameType method comment.
   */
  @Override
  public boolean isSameType(Spectrum otherSpectrum) {
    if (otherSpectrum instanceof FluorescenceSpectrum)
      return true;
    return false;
  }
}
