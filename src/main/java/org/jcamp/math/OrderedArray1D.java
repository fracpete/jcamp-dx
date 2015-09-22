/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 1-dimensional numerical data arrays.
 * 
 * @author Thomas Weber
 */
public class OrderedArray1D
  extends OrderedArray
  implements IOrderedArray1D, Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = 2757300467105104953L;

  private double[] array;
  
  private Range1D.Double range;

  /**
   * create data array from double array.
   * @param values double[] 
   */
  public OrderedArray1D(double[] values) {
    this(values, true);
  }

  /**
   * OrderedArray1D constructor comment.
   * @param values double[] 
   * @param copy boolean perform copy of data
   */
  public OrderedArray1D(double[] values, boolean copy) {
    this(values, copy, false);
  }

  /**
   * ctor from possibly unsorted double array.
   * @param values double[] array of data values
   * @param copy boolean copy data values
   * @param sort boolean sort data values
   */
  public OrderedArray1D(double[] values, boolean copy, boolean sort) {
    super(values.length);
    if (copy) {
      array = (double[]) values.clone();
    } else
      array = values;
    if (sort)
      Arrays.sort(array);
    calcRange();
  }

  /**
   * ctor 
   * @param from IOrderedArray1D array to copy from
   * @param copy boolean copy data values
   * @param sort boolean sort data values
   */
  public OrderedArray1D(IOrderedArray1D from) {
    super(from.getLength());
    int n = from.getLength();
    this.array = new double[n];
    for (int i = 0; i < n; i++)
      array[i] = from.pointAt(i);
    this.range = from.getRange1D();
  }

  /**
   * calculate maximum and minimum.
   */
  private void calcRange() {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    int length = getLength();
    for (int i = 0; i < length; i++) {
      if (array[i] < min)
	min = array[i];
      if (array[i] > max)
	max = array[i];
    }
    range = new Range1D.Double(min, max);
  }

  /**
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    OrderedArray1D array = null;
    //	try {
    array = (OrderedArray1D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    array.array = (double[]) this.array.clone();
    array.range = (Range1D.Double) this.range.clone();
    return array;
  }

  /**
   * return data value at index.
   * @return double
   * @param index int
   */
  @Override
  public double[] elementAt(int index) {
    return new double[] { array[index] };
  }

  /**
   * return data values in double array.
   */
  @Override
  public double[][] elementsAt(int[] index) {
    double[][] values = new double[1][index.length];
    for (int i = 0; i < index.length; i++) {
      int index_i = index[i];
      values[0][i] = array[index_i];
    }
    return values;
  }

  /**
   * getArray method comment.
   */
  @Override
  public IArray1D getArray(int index) throws ArrayIndexOutOfBoundsException {
    if (index != 0)
      throw new ArrayIndexOutOfBoundsException();
    return this;
  }

  /**
   * dimension is 1.
   */
  @Override
  public final int getDimension() {
    return 1;
  }

  /**
   * gets range of data in array
   * @return Range
   */
  public Range.Double getRange() {
    return (Range1D.Double) range.clone();
  }

  /**
   * gets range of data in array
   * @return Range1D.Double
   */
  public Range1D.Double getRange1D() {
    return (Range1D.Double) range.clone();
  }

  /**
   * indexAt method comment.
   */
  public int indexAt(double p) {
    int il = 0;
    int ir = array.length - 1;
    int index = (il + ir) / 2;
    double pi;
    double pl = pointAt(il);
    double pr = pointAt(ir);
    if (p < pl)
      return il;
    if (p > pr)
      return ir;
    while (ir - il > 1) {
      pi = pointAt(index);
      if (p > pi) {
	il = index;
	pl = pi;
      } else if (Math.abs(p - pi) < 2.0 * Double.MIN_VALUE) {
	return index;
      } else {
	ir = index;
	pr = pi;
      }
      index = (il + ir) / 2;
    }
    if (p < .5 * (pl + pr))
      return il;
    else
      return ir;
  }

  /**
   * return data value at index.
   * @return double
   * @param index int
   */
  public double pointAt(int index) {
    return array[index];
  }

  public void scale(double amount) {
    for (int i = 0; i < array.length; i++)
      array[i] *= amount;
    range = new Range1D.Double(range.getXMin() * amount, range.getXMax() * amount);
  }

  /**
   * convert to double array.
   * 
   * @return double[]
   */
  public double[] toArray() {
    double[] a = new double[array.length];
    System.arraycopy(array, 0, a, 0, array.length);
    return a;
  }

  public void translate(double amount) {
    for (int i = 0; i < array.length; i++)
      array[i] += amount;
    range.translate(amount);
  }
}
