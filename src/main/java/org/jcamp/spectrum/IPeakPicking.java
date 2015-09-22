/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import java.util.Vector;

/**
 * peak picking algorithm interface.
 * 
 * @author Thomas Weber
 */
public interface IPeakPicking {
  
  /**
   * perform the peak picking on the 1D spectrum.
   * @param Spectrum1D the spectrum
   * @return Vector vector of peaks*/
  public Vector calculate(Spectrum1D spectrum);
}
