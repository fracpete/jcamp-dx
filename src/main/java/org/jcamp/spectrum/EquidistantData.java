package org.jcamp.spectrum;

import org.jcamp.math.DataException;
import org.jcamp.math.LinearGrid1D;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * implementation of IOrderedDataArray1D for equidistant incremental data
 * 
 * @author Thomas Weber
 */
public class EquidistantData
  implements IOrderedDataArray1D, IEquidistant {
  
  /** for serialization. */
  private static final long serialVersionUID = 293967635222847967L;

  private LinearGrid1D data;
  
  private Unit unit;
  
  private String label;
  
  /**
   * LinearIncrData1D constructor comment.
   */
  public EquidistantData(double first, double last, int n, Unit unit) {
    super();
    this.data = new LinearGrid1D(first, last, n);
    if (unit != null)
      this.unit = unit;
    else
      this.unit = CommonUnit.generic;
  }
  /**
   * LinearIncrData1D constructor comment.
   */
  public EquidistantData(LinearGrid1D data) {
    this(data, CommonUnit.generic);
  }
  /**
   * LinearIncrData1D constructor comment.
   */
  public EquidistantData(LinearGrid1D data, Unit unit) {
    super();
    this.data = data;
    if (unit != null)
      this.unit = unit;
    else
      this.unit = CommonUnit.generic;
  }
  /**
   * @see com.creon.chem.spectrum.IOrderedDataArray1D
   */
  public int[] boundIndices(double position) throws org.jcamp.math.DataException {
    Range1D.Double r = data.getRange1D();
    int max = data.getLength() - 1;
    if (!r.contains(position))
      throw new DataException("data point out of data range");
    double c = data.coordinateAt(position);
    if (isAscending()) {
      int c0 = Math.max(0, (int) Math.floor(c));
      int c1 = Math.min(max, (int) Math.ceil(c));
      return new int[] { c0, c1 };
    } else {
      int c0 = Math.min(max, (int) Math.ceil(c));
      int c1 = Math.max(0, (int) Math.floor(c));
      return new int[] { c0, c1 };
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
    double c = data.coordinateAt(position);
    Range1D.Double r = data.getRange1D();
    if (position > r.getXMax())
      throw new DataException("no ceiling data point for position");
    int max = data.getLength() - 1;
    if (isAscending()) {
      if (position < r.getXMin())
	return 0;
      else
	return Math.min(max, (int) Math.ceil(c));
    } else {
      if (position < r.getXMin())
	return max;
      else
	return Math.max(0, (int) Math.floor(c));
    }
  }
  /**
   * cloning
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    EquidistantData o = null;
    try {
      o = (EquidistantData) super.clone();
    } catch (CloneNotSupportedException e) {
    }
    o.data = (LinearGrid1D) this.data.clone();
    o.unit = (Unit) this.unit.clone();
    return o;
  }
  /**
   * @see org.jcamp.units.IMeasurable
   */
  public void convertToUnit(org.jcamp.units.Unit newUnit) throws org.jcamp.units.UnitException {
    double newStart = newUnit.convertFrom(this.data.getStart(), this.unit);
    double newEnd = newUnit.convertFrom(this.data.getEnd(), this.unit);
    this.data = new LinearGrid1D(newStart, newEnd, data.getLength());
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
    double c = data.coordinateAt(position);
    Range1D.Double r = data.getRange1D();
    int max = data.getLength() - 1;
    if (position < r.getXMin())
      throw new DataException("no floor data point for position");
    if (isAscending()) {
      if (position > r.getXMax())
	return max;
      else
	return Math.max(0, (int) Math.floor(c));
    } else {
      if (position > r.getXMax())
	return 0;
      else
	return Math.min(max, (int) Math.ceil(c));
    }
  }
  /**
   * see IEquidistant
   * 
   * @return com.creon.math.LinearGrid1D
   */
  public LinearGrid1D getDataGrid() {
    return data;
  }
  /**
   * gets data label.
   * 
   * @return java.lang.String
   */
  public java.lang.String getLabel() {
    if (label == null)
      return unit.toString();
    return label;
  }
  /**
   * @see com.creon.math.IArray1D
   */
  public int getLength() {
    return data.getLength();
  }
  /**
   * @see com.creon.math.IInterval
   */
  public org.jcamp.math.Range.Double getRange() {
    return data.getRange();
  }
  /**
   * @see com.creon.math.IInterval1D
   */
  public org.jcamp.math.Range1D.Double getRange1D() {
    return data.getRange1D();
  }
  /**
   * @see org.jcamp.units.IMeasurable
   */
  public org.jcamp.units.Unit getUnit() {
    return this.unit;
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
    return data.isAscending();
  }
  /**
   * @see com.creon.math.IArray1D
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
   * sets data label.
   * 
   * @param newLabel java.lang.String
   */
  public void setLabel(java.lang.String newLabel) {
    label = newLabel;
  }
  /**
   * Insert the method's description here.
   * 
   * @param newUnit org.jcamp.units.Unit
   */
  public void setUnit(org.jcamp.units.Unit newUnit) {
    unit = newUnit;
  }
  /**
   * @see com.creon.math.IArray1D
   */
  public double[] toArray() {
    return data.toArray();
  }
  /**
   * @see com.creon.math.IArray1D
   */
  public void translate(double amount) {
    data.translate(amount);
  }
}
