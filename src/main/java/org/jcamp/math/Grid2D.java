package org.jcamp.math;

import java.io.Serializable;

import javax.vecmath.Point2d;

/**
 * abstract base class for 2-dimensional grids.
 * 
 * @author Thomas Weber
 */
public abstract class Grid2D
  extends Grid
  implements IOrderedArray2D, Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = -5585688358617134086L;

  private int lengthY;
  
  private int lengthX;

  /**
   * default constructor initializes to an empty grid
   */
  public Grid2D() {
    super();
    this.lengthX = this.lengthY = 0;
  }

  /**
   * Grid2D constructor comment.
   */
  public Grid2D(int lengthX, int lengthY) {
    super(lengthX * lengthY);
    this.lengthX = lengthX;
    this.lengthY = lengthY;
  }

  /**
   * cloning
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    Grid2D grid = null;
    //	try {
    grid = (Grid2D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    return grid;
  }

  /**
   * getGridFromValue method comment.
   */
  @Override
  public double[] coordinateAt(double[] value) {
    return coordinateAt(value[0], value[1]);
  }

  /**
   * Die Beschreibung der Methode hier eingeben.
   * Erstellungsdatum: (31.03.00 13:58:04)
   * @return double[]
   * @param x double
   * @param y double
   */
  public abstract double[] coordinateAt(double x, double y);

  /**
   * getGridFromValue method comment.
   */
  @Override
  public double[][] coordinatesAt(double[][] value) {
    return coordinatesAt(value[0], value[1]);
  }

  /**
   * get grid coordinates at positions x/y.
   * @return double[]
   * @param x double
   * @param y double
   */
  public abstract double[][] coordinatesAt(double[] x, double[] y);

  /**
   * getDimension method comment.
   */
  @Override
  public final int getDimension() {
    return 2;
  }

  /**
   * 2-dimensional range of grid.
   * @return Range.Double
   */
  public abstract Range.Double getRange();

  /**
   * 2-dimensional range of grid.
   * @return Range2D.Double
   */
  public abstract Range2D.Double getRange2D();

  /**
   * length in x-direction
   */
  public int getXLength() {
    return lengthX;
  }

  /**
   * length in y-direction.
   * @return int
   */
  public int getYLength() {
    return lengthY;
  }

  /**
   * get grid point at index.
   * @return double[]
   * @param index int
   */
  @Override
  public abstract double[] gridPointAt(int index);

  /**
   * getValueFromIndex method comment.
   */
  @Override
  public abstract double[][] gridPointsAt(int[] index);

  /**
   * gets grid index at position.
   * @return int
   * @param x double
   * @param y double
   */
  @Override
  public int indexAt(double[] position) {
    double g[] = coordinateAt(position);
    int ix = (int) (g[0] + .5);
    int iy = (int) (g[1] + .5);
    return iy * lengthX + ix;
  }

  /**
   * get index of grid point at position x/y.
   * @return int
   * @param x double
   * @param y double
   */
  public int indexAt(double x, double y) {
    double g[] = coordinateAt(x, y);
    int ix = (int) (g[0] + .5);
    int iy = (int) (g[1] + .5);
    return iy * lengthX + ix;
  }

  /**
   * gets index at point.
   * @param p Point2d
   * @return int
   */
  public int indexAt(Point2d p) {
    double g[] = coordinateAt(p.x, p.y);
    int ix = (int) (g[0] + .5);
    int iy = (int) (g[1] + .5);
    return iy * lengthX + ix;
  }

  /**
   * convert value into integral grid coordinate.
   * @return int
   * @param value double
   */
  @Override
  public int[] indicesAt(double[][] values) {
    int[] indices = new int[values[0].length];
    for (int i = 0; i < values[0].length; i++)
      indices[i] = indexAt(values[0][i], values[1][i]);
    return indices;
  }

  /**
   * return grid point at index.
   * @return Point2d
   * @param index int
   */
  public Point2d pointAt(int index) {
    double[] c = gridPointAt(index);
    return new Point2d(c[0], c[1]);
  }

  /**
   * sets for x length
   * 
   * @param newLengthX int
   */
  protected void setXLength(int newLengthX) {
    lengthX = newLengthX;
  }

  /**
   * sets for y length.
   * 
   * @param newLengthY int
   */
  protected void setYLength(int newLengthY) {
    lengthY = newLengthY;
  }

  /**
   * getValueFromGrid method comment.
   */
  @Override
  public double[] valueAt(double[] grid) {
    return valueAt(grid[0], grid[1]);
  }

  /**
   */
  public abstract double[] valueAt(double gridx, double gridy);

  /**
   * getValueFromGrid method comment.
   */
  @Override
  public double[][] valuesAt(double[][] grid) {
    return valuesAt(grid[0], grid[1]);
  }

  /**
   */
  public abstract double[][] valuesAt(double[] gridx, double[] gridy);
}