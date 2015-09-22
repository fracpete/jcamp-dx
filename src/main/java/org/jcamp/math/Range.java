/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import java.io.Serializable;

/**
 * abstract base class for numerical ranges.
 * 
 * @author Thomas Weber
 */
public abstract class Range
  implements Serializable, Cloneable {

  /** for serialization. */
  private static final long serialVersionUID = -1916411598896570330L;

  final static double DOUBLE_EPS = java.lang.Double.MIN_VALUE * 2;

  final static double FLOAT_EPS = java.lang.Float.MIN_VALUE * 2;

  private final int dimension;

  public interface Int {
    public int getMin(int dim);
    public int getMax(int dim);
    public int getWidth(int dim);
    public int getCenter(int dim);
    public Range1D.Int getRange(int dim);
  }

  public interface Float {
    public float getMin(int dim);
    public float getMax(int dim);
    public float getWidth(int dim);
    public Range1D.Float getRange(int dim);
    public float getCenter(int dim);
  }

  public interface Double {
    public double getMin(int dim);
    public double getMax(int dim);
    public double getCenter(int dim);
    public double getWidth(int dim);
    public Range1D.Double getRange(int dim);
  }

  /**
   * Range constructor comment.
   */
  protected Range(int dim) {
    super();
    this.dimension = dim;
  }

  public int getDimension() {
    return dimension;
  }
  
  /**
   * Returns a string representation of the range.
   * 
   * @return		the string representation
   */
  @Override
  public abstract String toString();
}
