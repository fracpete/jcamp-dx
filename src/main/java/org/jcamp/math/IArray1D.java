/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * real interval with discrete data points.
 * 
 * @author Thomas Weber
 */
public interface IArray1D
  extends IInterval1D {
  
  /**
   * number of data points in interval.
   * @return int
   */
  public int getLength();
  
  /**
   * discrete data point at position <code>index</code> in interval.
   * @param index int
   */
  public double pointAt(int index);
  
  /**
   * scale by given amount.
   * @param amount double
   */
  public void scale(double amount);
  
  /**
   * convert data as double array.
   * 
   * @return double[]
   */
  public double[] toArray();
  
  /**
   * translate by given amount.
   * @param amount double
   */
  public void translate(double amount);
}
