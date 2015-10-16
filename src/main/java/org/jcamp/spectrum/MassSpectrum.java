/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.LinearAxisMap;

/**
 * mass spectrum.
 * 
 * @author Thomas Weber
 */
public class MassSpectrum
  extends Spectrum1D {
  
  /** for serialization. */
  private static final long serialVersionUID = -5738819181800698120L;

  /**
   * standard ctor.
   * 
   * @param x org.jcamp.spectrum.IOrderedDataArray1D
   * @param y org.jcamp.spectrum.IDataArray1D
   */
  public MassSpectrum() {
    super();
  }
  
  public MassSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
    super(x, y, false);
  }

  /**
   * standard ctor.
   * 
   * @param x org.jcamp.spectrum.IOrderedDataArray1D
   * @param y org.jcamp.spectrum.IDataArray1D
   * @param fullSpectrum boolean
   */
  public MassSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
    super(x, y, fullSpectrum);
  }

  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    MassSpectrum spectrum = (MassSpectrum) super.clone();
    spectrum.xData = (IOrderedDataArray1D) this.xData.clone();
    spectrum.yData = (IDataArray1D) this.yData.clone();
    spectrum.xAxisMap = new LinearAxisMap(spectrum.xData);
    spectrum.yAxisMap = new LinearAxisMap(spectrum.yData);
    return spectrum;
  }

  /**
   * gets spectrum ID.
   * @return int
   */
  @Override
  public int getIdentifier() {
    return ISpectrumIdentifier.MS;
  }
}
