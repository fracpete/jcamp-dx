package org.jcamp.math;

import java.io.Serializable;

/**
 * n-dimensional numerical data arrays.
 * @author Thomas Weber
 */
public class ArrayND 
  extends Array
  implements Serializable, Cloneable {
  
  /** for serialization. */
  private static final long serialVersionUID = 6575500697734808219L;
  
  private Array1D[] arrays;

  /**
   * default constructor (empty array).
   */
  public ArrayND() {
    super(0);
    this.arrays = new Array1D[]{};
  }

  /**
   * constructor from coordinate arrays in N dimensions (values are copied).
   * @param double[][] values
   */
  public ArrayND(double[][] values) {
    this(values, true);
  }

  /**
   * constructor from coordinate arrays in N dimensions.
   * @param double[][] values
   * @param boolean copy Copy flag
   */
  public ArrayND(double[][] values, boolean copy) {
    super(values[0].length);
    arrays = new Array1D[values.length];
    for (int i = 0; i < arrays.length; i++) {
      arrays[i] = new Array1D(values[i], copy);
    }
  }

  /**
   * constructor from coordinate arrays in N dimensions (values are copied).
   * @param Array1D[] values
   */
  public ArrayND(Array1D[] values) {
    this(values, true);
  }

  /**
   * constructor from coordinate arrays in N dimensions.
   * @param Array1D[] values
   * @param boolean copy Copy flag
   */
  public ArrayND(Array1D[] values, boolean copy) {
    super(values[0].getLength());
    arrays = new Array1D[values.length];
    for (int i = 0; i < arrays.length; i++) {
      arrays[i] = values[i];
      if (values[i].getLength() != this.getLength())
	throw new IllegalArgumentException("dimensions of coordinate array different");
    }
  }

  /**
   * cloning
   * @return Object
   */
  @Override
  public Object clone() {
    ArrayND array = null;
    //    try {
    array = (ArrayND) super.clone();
    //    } catch (CloneNotSupportedException e) {}
    for (int i = 0; i < this.arrays.length; i++) {
      array.arrays[i] = (Array1D) this.arrays[i].clone();
    }
    return array;
  }

  /**
   * gets value at index <code>index</code>.
   */
  @Override
  public double[] elementAt(int index) {
    double[] values = new double[getDimension()];
    for (int i = 0; i < getDimension(); i++) {
      values[i] = arrays[i].pointAt(index);
    }
    return values;
  }

  /**
   * gets values at indices <code>index</code>.
   */
  @Override
  public double[][] elementsAt(int[] index) {
    int n = arrays.length;
    double[][] values = new double[n][index.length];
    for (int i = 0; i < index.length; i++) {
      int index_i = index[i];
      for (int j = 0; j < n; j++) {
	values[j][i] = arrays[j].pointAt(index_i);
      }
    }
    return values;
  }

  /**
   * gets coordinate array in dimension <code>index</code>.
   */
  @Override
  public IArray1D getArray(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    if (index < 0 || index >= arrays.length)
      throw new ArrayIndexOutOfBoundsException();
    return arrays[index];
  }

  /**
   * gets dimension of array.
   */
  @Override
  public int getDimension() {
    return arrays.length;
  }
}