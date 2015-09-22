/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import javax.vecmath.Point2d;

/**
 * real interval with discrete data points.
 * 
 * @author Thomas Weber
 */
public interface IArray2D
  extends IInterval2D {
  
  /**
   * gets array of first dimension
   * @return IArray1D
   */
  public IArray1D getXArray();
  
  /**
   * gets array of second dimension
   * @return IArray1D
   */
  public IArray1D getYArray();
  

  /**
   * number of data points in interval.
   * @return int
   */
  public int getLength();
  

  /**
   * discrete data point at position <code>index</code> in interval.
   * @param index int
   */
  public Point2d pointAt(int index);
}
