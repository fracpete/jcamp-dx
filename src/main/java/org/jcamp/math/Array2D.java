/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import java.io.Serializable;

import javax.vecmath.Point2d;

/**
 * 2-dimensional numerical data arrays.
 * 
 * @author Thomas Weber
 */
public class Array2D 
  extends Array 
  implements IArray2D, Serializable, Cloneable {
  
  /** for serialization. */
  private static final long serialVersionUID = -3660969957159289155L;

  private Array1D arrayX;
  
  private Array1D arrayY;
  
  private Range2D.Double range;

  /**
   * default constructor.
   */
  public Array2D() {
    super(0);
    this.arrayX = new Array1D();
    this.arrayY = new Array1D();
    this.range = new Range2D.Double();
  }

  /**
   * create data array from double array with x/y pairs (values are copied).
   * double[][0] are x-coordinates, double[][1] are y-coordinates
   * @param values double[][] 
   */
  public Array2D(double[][] values) {
    this(values, true);
  }

  /**
   * create data array from double array with x/y pairs.
   * double[][0] are x-coordinates, double[][1] are y-coordinate
   * @param values double[][]
   * @param copy copy flag
   */
  public Array2D(double[][] values, boolean copy) {
    super(values[0].length);
    arrayX = new Array1D(values[0], copy);
    arrayY = new Array1D(values[1], copy);
    calcRange();
  }

  /**
   * constructor with array of x values and array of y values (values are copied).
   * @param x Array1D
   * @param y Array1D
   */
  public Array2D(Array1D x, Array1D y) {
    this(x, y, true);
  }

  /**
   * constructor with array of x values and array of y values.
   * @param x Array1D
   * @param y Array1D
   * @param copy boolean
   */
  public Array2D(Array1D x, Array1D y, boolean copy) {
    super(x.getLength());
    if (x.getLength() != y.getLength())
      throw new IllegalArgumentException("x and y coordinate arrays have different dimensions");
    if (copy) {
      this.arrayX = (Array1D) x.clone();
      this.arrayY = (Array1D) y.clone();
    } else {
      this.arrayX = x;
      this.arrayY = y;
    }
    calcRange();
  }

  /**
   * calculate maximum and minimum.
   */
  private void calcRange() {
    double left = Double.MAX_VALUE;
    double right = Double.MIN_VALUE;
    double bottom = Double.MAX_VALUE;
    double top = Double.MIN_VALUE;
    int length = getLength();
    for (int i = 0; i < length; i++) {
      double x = arrayX.pointAt(i);
      if (x < left)
	left = x;
      if (x > right)
	right = x;
      double y = arrayY.pointAt(i);
      if (y < bottom)
	bottom = y;
      if (y > top)
	top = y;
    }
    range = new Range2D.Double(left, right, bottom, top);
  }

  /**
   * cloning.
   * @return Object
   */
  @Override
  public Object clone() {
    Array2D array = null;
    //	try  {
    array = (Array2D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    array.arrayX = (Array1D) this.arrayX.clone();
    array.arrayY = (Array1D) this.arrayY.clone();
    return array;
  }

  /**
   * gets data value at index.
   * @return double[]
   * @param index int
   */
  @Override
  public double[] elementAt(int index) {
    return new double[] { arrayX.pointAt(index), arrayY.pointAt(index)};
  }

  /**
   * gets data values in double array.
   */
  @Override
  public double[][] elementsAt(int[] index) {
    double[][] values = new double[2][index.length];
    for (int i = 0; i < index.length; i++) {
      int index_i = index[i];
      values[0][i] = arrayX.pointAt(index_i);
      values[1][i] = arrayY.pointAt(index_i);
    }
    return values;
  }

  /**
   * gets array of values in dimension <code>index</code>.
   */
  @Override
  public IArray1D getArray(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    switch (index) {
      case 0 :
	return arrayX;
      case 1 :
	return arrayY;
      default :
	throw new ArrayIndexOutOfBoundsException();
    }
  }

  /**
   * dimension is always 2.
   */
  @Override
  public final int getDimension() {
    return 2;
  }

  /**
   * gets range of data in array
   * @return Range.Double
   */
  public Range.Double getRange() {
    return range;
  }

  /**
   * gets range of data in array
   * @return Range2D.Double
   */
  public Range2D.Double getRange2D() {
    return range;
  }

  /**
   * gets array of x coordinates.
   * @return Array1D
   */
  public IArray1D getXArray() {
    return arrayX;
  }

  /**
   * gets length of x-coordinates.
   */
  public int getXLength() {
    return arrayX.getLength();
  }

  /**
   * gets array of y coordinates.
   * @return Array1D
   */
  public IArray1D getYArray() {
    return arrayY;
  }

  /**
   * gets length of y coordinates.
   */
  public int getYLength() {
    return arrayY.getLength();
  }

  /**
   * gets 2D point at index <code>index</code>.
   */
  public Point2d pointAt(int index) {
    return new Point2d(arrayX.pointAt(index), arrayY.pointAt(index));
  }
}
