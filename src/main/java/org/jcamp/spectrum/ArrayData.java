package org.jcamp.spectrum;

import org.jcamp.math.Array1D;
import org.jcamp.math.IArray1D;
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
public class ArrayData
  implements IDataArray1D {
  
  /** for serialization. */
  private static final long serialVersionUID = -4103255291717042724L;

  private Array1D data;
  
  private Unit unit;
  
  private String label;
  
  /**
   * 
   */
  public ArrayData(double[] data, Unit unit) {
    this.data = new Array1D(data, false);
    if (unit != null)
      this.unit = unit;
    else
      this.unit = CommonUnit.generic;
  }
  /**
   */
  public ArrayData(IArray1D data) {
    this(data, CommonUnit.generic);
  }
  /**
   */
  public ArrayData(IArray1D data, Unit unit) {
    this.data = new Array1D(data);
    if (unit != null)
      this.unit = unit;
    else
      this.unit = CommonUnit.generic;
  }
  /**
   * cloning
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    ArrayData o = null;
    try {
      o = (ArrayData) super.clone();
    } catch (CloneNotSupportedException e) {
    }
    o.data = (Array1D) this.data.clone();
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
    setData(new Array1D(array, false));
    setUnit(newUnit);
  }
  /**
   * gets data (measured data is discontinuous).
   * @return IArray1D
   */
  public IArray1D getData() {
    return data;
  }
  /**
   * gets data label
   * 
   * @return java.lang.String
   */
  public java.lang.String getLabel() {
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
   * gets data point at index.
   */
  public double pointAt(int index) {
    return data.pointAt(index);
  }
  /**
   * @see com.creon.math.IArray1D
   */
  public void scale(double amount) {
    this.data.scale(amount);
  }
  /**
   * sets the data.
   * @param newData Array1D
   */
  void setData(Array1D newData) {
    data = newData;
  }
  /**
   * Insert the method's description here.
   * 
   * @param newLabel java.lang.String
   */
  public void setLabel(java.lang.String newLabel) {
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
