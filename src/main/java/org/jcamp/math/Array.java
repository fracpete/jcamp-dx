package org.jcamp.math;

import java.io.Serializable;

/**
 * abstract base class for numerical data arrays. 
 * 
 * @author Thomas Weber
 */
public abstract class Array
  implements Serializable, Cloneable {
  
  /** for serialization. */
  private static final long serialVersionUID = 8863214988341386318L;
  
  /** the length. */
  private int length = 0;

  /**
   * Grid constructor comment.
   */
  public Array() {
    super();
  }

  /**
   * Grid constructor comment.
   */
  public Array(int length) {
    super();
    this.length = length;
  }

  /**
   * cloning.
   * @return Object
   */
  @Override
  public Object clone() {
    Array array = null;
    try {
      array = (Array) super.clone();
    } catch (CloneNotSupportedException e) {
    }
    array.length = this.length;
    return array;
  }

  /**
   * return m-dim. data array at index <code>index</code>.
   * @param index
   * @return double
   */
  public abstract double[] elementAt(int index);

  /**
   * return n,m- dim. array of double data for m indices <code>indices</code>.
   * default implementation is a double loop over array and should be overwritten
   * for performance.
   * @param indices
   * @return double[][]
   */
  public double[][] elementsAt(int[] indices) {
    double[][] value = new double[getDimension()][indices.length];
    for (int i = 0; i < indices.length; i++) {
      double[] value_i = elementAt(indices[i]);
      for (int j = 0; j < getDimension(); j++)
	value[j][i] = value_i[j];
    }
    return value;
  }

  /**
   * gets discrete data points.
   * @param index int
   * @return IArray1D
   */
  public abstract IArray1D getArray(int index) throws ArrayIndexOutOfBoundsException;

  /**
   * dimension of data point
   * @return int
   */
  public abstract int getDimension();

  /**
   * number of data points
   * @return int
   */
  public int getLength() {
    return length;
  }

  /**
   * sets length of array.
   * 
   * @param newLength int
   */
  protected void setLength(int newLength) {
    length = newLength;
  }

  /**
   * gets the size of the array
   * @return int size of array 
   */
  public int size() {
    return this.length;
  }
  
  /**
   * Returns a representation of the array.
   * 
   * @return		the representation
   */
  @Override
  public String toString() {
    StringBuilder	result;
    int			i;
    int			n;
    double[]		values;
    
    result = new StringBuilder();
    for (i = 0; i < size(); i++) {
      if (i > 0)
	result.append(",");
      values = elementAt(i);
      for (n = 0; n < values.length; n++) {
	if (n > 0)
	  result.append(":");
	result.append(values[n]);
      }
    }
    
    return result.toString();
  }
}