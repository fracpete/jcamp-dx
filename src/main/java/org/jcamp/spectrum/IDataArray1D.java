/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import java.io.Serializable;

import org.jcamp.math.IArray1D;
import org.jcamp.units.IMeasurable;

/**
 * 1D data arrays extended by a unit and label.
 * 
 * @author Thomas Weber
 */
public interface IDataArray1D
  extends IArray1D, IMeasurable, Cloneable, Serializable {
  
  /**
   * sets data label
   * 
   * @param label java.lang.String
   */
  void setLabel(String label);

  public Object clone();

  /**
   * get data label.
   * 
   * @return java.lang.String
   */
  String getLabel();
}
