/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.DataException;
import org.jcamp.math.IOrderedArray1D;
import org.jcamp.math.OrderedArray1D;
import org.jcamp.math.Range;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
import org.jcamp.units.UnitException;

/**
 * combination of a discrete interval with a physical unit.
 * 
 * @author Thomas Weber
 */
public class OrderedArrayData
  implements IOrderedDataArray1D {
  
  /** for serialization. */
  private static final long serialVersionUID = 351561040610523139L;
  
  private OrderedArray1D data;
  private Unit unit;
  private String label;
  private boolean ascending = true;
  
  /**
   * 
   */
  public OrderedArrayData(double[] data, Unit unit) {
    this.data = new OrderedArray1D(data, false, true);
    if (unit != null)
      this.unit = unit;
    else
      this.unit = CommonUnit.generic;
    if (data.length > 1)
      this.ascending = (data[0] < data[1]);
  }
  /**
   */
  public OrderedArrayData(IOrderedArray1D data) {
    this(data, CommonUnit.generic);
  }
  /**
   */
  public OrderedArrayData(IOrderedArray1D data, Unit unit) {
    this.data = new OrderedArray1D(data);
    if (unit != null)
      this.unit = unit;
    else
      this.unit = CommonUnit.generic;
  }
  /**
   * binary search for bounding indices
   * 
   * @return int left index
   * @param position double
   */
  private int binsearchIndex(double position) {
    int left = 0;
    int right = getLength() - 1;
    int mid;
    if (isAscending()) {
      while (right - left > 1) {
	mid = (right + left) / 2;
	double sposition = data.pointAt(mid);
	if (position > sposition)
	  left = mid;
	else if (position < sposition)
	  right = mid;
	else {
	  left = mid;
	  break;
	}
      }
      return left;
    } else {
      while (right - left > 1) {
	mid = (right + left) / 2;
	double sposition = data.pointAt(mid);
	if (position < sposition)
	  left = mid;
	else if (position > sposition)
	  right = mid;
	else {
	  left = mid;
	  break;
	}
      }
      return left;
    }
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public int[] boundIndices(double position) throws org.jcamp.math.DataException {
    Range1D.Double r = data.getRange1D();
    int max = data.getLength() - 1;
    if (!r.contains(position))
      throw new DataException("data point out of data range");
    if (isAscending()) {
      int c0 = Math.max(0, binsearchIndex(position));
      return new int[] { c0, c0 + 1 };
    } else {
      int c0 = Math.min(max, binsearchIndex(position) + 1);
      return new int[] { c0, c0 - 1 };
    }
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public org.jcamp.math.Range1D.Double bounds(double position) throws org.jcamp.math.DataException {
    int[] bi = boundIndices(position);
    return new Range1D.Double(data.pointAt(bi[0]), data.pointAt(bi[1]));
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public double ceil(double position) throws org.jcamp.math.DataException {
    return data.pointAt(ceilIndex(position));
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public int ceilIndex(double position) throws org.jcamp.math.DataException {

    int n = data.getLength();
    // first check for degenerated data
    if (n == 0)
      throw new DataException("empty data set");
    else if (n == 1) {
      if (position < data.pointAt(0))
	return 0;
      else
	throw new DataException("no ceiling data point for position");
    }
    Range1D.Double r = data.getRange1D();
    if (position > r.getXMax())
      throw new DataException("no ceiling data point for position");
    int max = n - 1;
    if (isAscending()) {
      if (position < r.getXMin())
	return 0;
      else
	return Math.min(max, binsearchIndex(position) + 1);
    } else {
      if (position < r.getXMin())
	return max;
      else
	return Math.max(0, binsearchIndex(position));
    }
  }
  /**
   * cloning
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    OrderedArrayData o = null;
    try {
      o = (OrderedArrayData) super.clone();
    } catch (CloneNotSupportedException e) {
    }
    o.data = (OrderedArray1D) this.data.clone();
    o.unit = (Unit) this.unit.clone();
    return o;
  }
  /**
   * convert data to new unit.
   * @param newUnit Unit
   * NOTE: original data is lost and new data is an expensive array.
   */
  public void convertToUnit(Unit newUnit) throws UnitException {
    double[] array = new double[data.getLength()];
    for (int i = 0; i < data.getLength(); i++)
      array[i] = newUnit.convertFrom(data.pointAt(i), unit);
    data = new OrderedArray1D(array, false);
    setUnit(newUnit);
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public double floor(double position) throws org.jcamp.math.DataException {
    return data.pointAt(floorIndex(position));
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public int floorIndex(double position) throws org.jcamp.math.DataException {
    int n = data.getLength();
    // first check empty or degenerated data
    if (n == 0)
      throw new DataException("empty data set");
    else if (n == 1) {
      if (position > data.pointAt(0))
	return 0;
      else
	throw new DataException("no floor data point for position");
    }

    Range1D.Double r = data.getRange1D();
    int max = n - 1;
    if (position < r.getXMin())
      throw new DataException("no floor data point for position");

    if (isAscending()) {
      if (position > r.getXMax())
	return max;
      else
	return Math.max(0, binsearchIndex(position));
    } else {
      if (position > r.getXMax())
	return 0;
      else
	return Math.min(max, binsearchIndex(position) + 1);
    }

  }
  /**
   * gets data label
   * 
   * @return String
   */
  public String getLabel() {
    if (label == null)
      return unit.toString();
    return label;
  }
  /**
   * gets number of data points.
   */
  public int getLength() {
    return data.getLength();
  }
  /**
   * get data range.
   * @return Range.Double
   */
  public Range.Double getRange() {
    return data.getRange();
  }
  /**
   * gets data range.
   * @return Range1D.Double
   */
  public Range1D.Double getRange1D() {
    return data.getRange1D();
  }
  /**
   * gets the data unit.
   * @return Unit
   */
  public Unit getUnit() {
    return unit;
  }
  /**
   * @see com.creon.math.IOrderedArray1D
   */
  public int indexAt(double p) {
    return data.indexAt(p);
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public boolean isAscending() {
    return ascending;
  }
  /**
   * gets data point at index.
   */
  public double pointAt(int index) {
    return data.pointAt(index);
  }
  /**
   * @see com.creon.math.IArray1D
   */
  public void scale(double amount) {
    data.scale(amount);
  }
  /**
   * Insert the method's description here.
   * 
   * @param newLabel String
   */
  public void setLabel(String newLabel) {
    label = newLabel;
  }
  /**
   * sets the unit for the data.
   * @param newUnit Unit
   */
  public void setUnit(Unit newUnit) {
    unit = newUnit;
  }
  /**
   * toArray method comment.
   */
  public double[] toArray() {
    return data.toArray();
  }
  public void translate(double amount) {
    data.translate(amount);
  }
}
