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
 * grid with linear grid points.
 * 
 * @author Thomas Weber
 */
public class LinearGrid1D
  extends Grid1D 
  implements Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = -6900058303852024997L;

  private double start;
  
  private double end;
  
  private double step;
  
  private double istep;

  /**
   * default constructor initializes to illegal values.
   */
  public LinearGrid1D() {
    super(0);
    this.start = Double.NaN;
    this.end = Double.NaN;
    this.step = 0;
    this.istep = 0;
  }

  /**
   * linear grid from <code>start</code> to <code>end</code> in  steps of <code>step</code> length.
   * @param double start
   * @param double end
   * @param double step
   */
  public LinearGrid1D(double start, double end, double step) {
    super(0);
    if (end > start && step <= 0 || start > end && step >= 0) {
      throw new IllegalArgumentException("illegal linear grid");
    }
    int length = (int) Math.ceil((end - start) / step) + 1;
    setLength(length);
    this.start = start;
    this.end = start + (length - 1) * step;
    this.step = step;
    this.istep = 1.0 / step;
  }

  /**
   * linear grid from <code>start</code> to <code>end</code> in  <code>length</code> steps.
   * @param length int
   */
  public LinearGrid1D(double start, double end, int length) {
    super(length);
    if (length < 2)
      throw new IllegalArgumentException("grid length lesser than 2");
    this.start = start;
    this.end = end;
    this.step = (end - start) / (length - 1);
    this.istep = 1.0 / this.step;
  }

  /**
   * linear grid from <code>start</code> to <code>end</code> in  <code>length</code> steps.
   * @param length int
   */
  public LinearGrid1D(Range1D range, int length) {
    super(length);
    if (length < 2)
      throw new IllegalArgumentException("grid length lesser than 2");
    this.start = range.getXMinAsDouble();
    this.end = range.getXMaxAsDouble();
    this.step = (end - start) / (length - 1);
    this.istep = 1.0 / this.step;
  }

  /**
   * cloning.
   * @return Object
   */
  @Override
  public Object clone() {
    LinearGrid1D grid = null;
    //	try {
    grid = (LinearGrid1D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    return grid;
  }

  /**
   * conversion of grid coordinates to real world coordinates
   */
  @Override
  public double coordinateAt(double x) {
    double grid;
    grid = (x - start) * istep;
    return grid;
  }

  /**
   * getArray method comment.
   */
  @Override
  public IArray1D getArray(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    if (index == 0)
      return this;
    else
      throw new ArrayIndexOutOfBoundsException();
  }

  /**
   * gets end point of grid.
   * @return double
   */
  public double getEnd() {
    return end;
  }

  /**
   * getRange method comment.
   */
  @Override
  public Range.Double getRange() {
    return new Range1D.Double(start, end);
  }

  /**
   * getRange method comment.
   */
  @Override
  public Range1D.Double getRange1D() {
    return new Range1D.Double(start, end);
  }

  /**
   * gets grid starting point
   * @return double
   */
  public double getStart() {
    return start;
  }

  /**
   * gets grid step.
   * @return double
   */
  public double getStep() {
    return step;
  }

  /**
   * isAscending method comment.
   */
  @Override
  public boolean isAscending() {
    return step > 0;
  }

  /**
   * gets grid value at index.
   */
  @Override
  public double pointAt(int index) {
    return start + index * step;
  }

  /**
   * scale grid points by <code>amount</code>.
   * @param amount double
   */
  public void scale(double amount) {
    start *= amount;
    end *= amount;
    step *= amount;
    istep = 1. / step;
  }

  /**
   * convert grid to double array.
   * 
   * @return double[]
   */
  public double[] toArray() {
    int n = getLength();
    double[] a = new double[n];
    for (int i = 0; i < n; i++)
      a[i] = start + i * step;
    return a;
  }

  /**
   * translate grid points by <code>amount</code>.
   * @param amount double
   */
  public void translate(double amount) {
    start += amount;
    end += amount;
  }

  /**
   * conversion of grid coordinates to real world coordinates
   */
  @Override
  public double valueAt(double g) {
    double value;
    double l = -0.5; // minimum for grid coordinates
    double r = ((double) getLength()) - 0.5; // maximum for grid coordinates
    value = (double) ((l < g && g < r) ? start + g * step : Double.NaN);
    return value;
  }
}
