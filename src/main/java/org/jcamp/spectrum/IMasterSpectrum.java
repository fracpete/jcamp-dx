/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

/**
 * interface for spectra that are composite types e.g. a chromatogram which 
 * has at each retention time a seperate mass spectrum.
 * 
 * @author Thomas Weber
 */
public interface IMasterSpectrum {
  
  /**
   * returns the 1D spectrum at the x index position <code>ix</code>
   * @return Spectrum1D
   * @param ix int
   */
  Spectrum1D getSlaveSpectrumFromXIndex(int ix);
  /**
   * returns the 1D spectrum nearest to the x position <code>x</code>
   * @return Spectrum1D
   * @param x double
   */
  Spectrum1D getSlaveSpectrumFromXPosition(double x);
  /**
   * returns the 1D spectrum at the y index position <code>iy</code>
   * @return Spectrum1D
   * @param iy int
   */
  Spectrum1D getSlaveSpectrumFromYIndex(int iy);
  /**
   * returns the 1D spectrum nearest to the y position <code>y</code>
   * @return Spectrum1D
   * @param y double
   */
  Spectrum1D getSlaveSpectrumFromYPosition(double y);
}
