package org.jcamp.math;

import java.io.Serializable;

/**
 * abstract base class for 1-dimensional grids
 * 
 * @author Thomas Weber
 */
public abstract class Grid1D
  extends Grid
  implements IOrderedArray1D, Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = 6773857584138997622L;

  /**
   * Grid1D constructor comment.
   */
  public Grid1D() {
    super();
  }

  /**
   * Grid1D constructor comment.
   */
  public Grid1D(int length) {
    super(length);
  }

  /**
   * return array of grid points.
   * @return double[]
   */
  public double[] asArray() {
    double[] g = new double[getLengthX()];
    for (int i = 0; i < getLengthX(); i++)
      g[i] = pointAt(i);
    return g;
  }

  /**
   * cloning
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    Grid1D grid = null;
    //	try {
    grid = (Grid1D) super.clone();
    //	} catch( CloneNotSupportedException e) {}
    return grid;
  }

  /**
   * convert value into grid coordinate.
   * Creation date: (01/12/00 10:55:42)
   * @return double
   * @param value double
   */
  @Override
  public double[] coordinateAt(double[] value) {
    double grid = coordinateAt(value[0]);
    return new double[] { grid };
  }

  /**
   * convert value into grid coordinate.
   * @return double
   * @param value double
   */
  public abstract double coordinateAt(double value);

  /**
   * convert value into grid coordinate.
   * Creation date: (01/12/00 10:55:42)
   * @return double[][]
   * @param values double[][]
   */
  @Override
  public double[][] coordinatesAt(double[][] values) {
    double[][] grid = new double[1][];
    grid[0] = coordinatesAt(values[0]);
    return grid;
  }

  /**
   * convert value into grid coordinate.
   * Creation date: (01/12/00 10:55:42)
   * @return double
   * @param value double
   */
  public double[] coordinatesAt(double[] values) {
    int n = values.length;
    double[] grid = new double[n];
    for (int i = 0; i < n; i++)
      grid[i] = coordinateAt(values[i]);
    return grid;
  }

  /**
   * getDimension method comment.
   */
  @Override
  public final int getDimension() {
    return 1;
  }

  /**
   * length of grid.
   * @return int
   */
  public int getLengthX() {
    return getLength();
  }

  /**
   * gets range of grid
   * @return Range.Double
   */
  public abstract Range.Double getRange();

  /**
   * get range of grid
   * @return Range1D.Double
   */
  public abstract Range1D.Double getRange1D();

  /**
   * return 1 dim. array of grid value at index.
   * @return double[]
   * @param index int
   */
  @Override
  public double[] gridPointAt(int index) {
    return new double[] { pointAt(index)};
  }

  /**
   * gets grid points at indices <code>indices</code>.
   * @return double
   * @param index int
   */
  @Override
  public double[][] gridPointsAt(int[] indices) {
    double[] grid = pointsAt(indices);
    return new double[][] { grid };
  }

  /**
   * convert 1-dim. value into integral grid coordinate
   * (inherited from Grid).
   * Creation date: (01/12/00 10:55:42)
   * @return int
   * @param value double
   */
  @Override
  public int indexAt(double[] value) {
    return (int) (coordinateAt(value[0]) + 0.5);
  }

  /**
   * convert value into integral grid coordinate.
   * Creation date: (01/12/00 10:55:42)
   * @return int
   * @param value double
   */
  public int indexAt(double value) {
    return (int) (coordinateAt(value) + 0.5);
  }

  /**
   * convert value into integral grid coordinate.
   * Creation date: (01/12/00 10:55:42)
   * @return int
   * @param value double
   */
  @Override
  public int[] indicesAt(double[][] values) {
    int[] indices = new int[values[0].length];
    for (int i = 0; i < values[0].length; i++)
      indices[i] = indexAt(values[0][i]);
    return indices;
  }

  /**
   * Insert the method's description here.
   * Creation date: (2/24/00 5:05:23 PM)
   * @return boolean
   */
  public abstract boolean isAscending();

  /**
   * convert value into grid coordinate.
   * Creation date: (01/12/00 10:55:42)
   * @return double
   * @param index int
   */
  public abstract double pointAt(int index);

  /**
   * Insert the method's description here.
   * Creation date: (28.03.00 10:44:58)
   * @return double
   * @param index int
   */
  public double[] pointsAt(int[] indices) {
    int n = indices.length;
    double[] grid = new double[n];
    for (int i = 0; i < n; i++)
      grid[i] = pointAt(indices[i]);
    return grid;
  }

  /**
   * convert grid coordinate into value
   * Creation date: (01/12/00 10:56:08)
   * @return double
   * @param grid double
   */
  @Override
  public double[] valueAt(double[] grid) {
    double value = valueAt(grid[0]);
    return new double[] { value };
  }

  /**
   * convert grid coordinate into value
   * Creation date: (01/12/00 10:56:08)
   * @return double
   * @param grid double
   */
  public abstract double valueAt(double grid);

  /**
   * convert grid coordinate into value
   * Creation date: (01/12/00 10:56:08)
   * @return double
   * @param grid double
   */
  @Override
  public double[][] valuesAt(double[][] grid) {
    double[] value = valuesAt(grid[0]);
    return new double[][] { value };
  }

  /**
   * convert grid coordinate into value
   * Creation date: (01/12/00 10:56:08)
   * @return double
   * @param grid double
   */
  public double[] valuesAt(double[] grid) {
    int n = grid.length;
    double[] values = new double[n];
    for (int i = 0; i < n; i++)
      values[i] = valueAt(grid[i]);
    return values;
  }
}