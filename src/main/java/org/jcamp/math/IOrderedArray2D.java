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
 * real interval with monotone ordered discrete points.
 * allows assignment of a single discrete point/index to a position within the interval. 
 * 
 * @author Thomas Weber
 */
public interface IOrderedArray2D
  extends IArray2D {
  
  /**
   * @return int[]
   * @param x double
   * @param y double
   */
  public int indexAt(double x, double y);
  
  /**
   * get index of point.
   * @return int[]
   * @param p Point2d
   */
  public int indexAt(Point2d p);
}
