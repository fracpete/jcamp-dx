/*******************************************************************************
* The JCAMP-DX project is the reference implemention of the IUPAC JCAMP-DX spectroscopy data standard.
* 
*   Copyright (C) 2019 Thomas Weber
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Library General Public
*    License as published by the Free Software Foundation; either
*    version 2 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Library General Public License for more details.
*
* Contributors:
* Thomas Weber - initial API and implementation
* Christoph Läubrich - implement custom Point2D class
*******************************************************************************/
package org.jcamp.math;

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
  public Point2D pointAt(int index);
}