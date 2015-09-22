/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * 2 dimensional rectangular grid.
 * 
 * @author Thomas Weber
 */
public class RectangularGrid2D
  extends Grid2D {
  
  /** for serialization. */
  private static final long serialVersionUID = -4455195927183629550L;

  private Grid1D x;
  
  private Grid1D y;
  
  /**
   * default constructor uses a NullGrid1D on both axes.
   */
  public RectangularGrid2D() {
    super();
    this.x = new NullGrid1D();
    this.y = new NullGrid1D();
  }
  
  /**
   * construct RectangularGrid2D from grid on x-axis and y-axis.
   * @param x Grid1D
   * @param y Grid1D
   */
  public RectangularGrid2D(Grid1D x, Grid1D y) {
    super(x.getLength(), y.getLength());
    this.x = (Grid1D) x.clone();
    this.y = (Grid1D) y.clone();
  }
  
  /**
   * cloning.
   * @return Object
   */
  @Override
  public Object clone() {
    RectangularGrid2D grid = null;
    //	try {
    grid = (RectangularGrid2D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    grid.x = (Grid1D) this.x.clone();
    grid.y = (Grid1D) this.y.clone();

    return grid;
  }
  
  /**
   * gets coordinates at x/y for grid.
   */
  @Override
  public double[] coordinateAt(double x, double y) {
    double gridX = this.x.coordinateAt(x);
    double gridY = this.y.coordinateAt(y);
    double[] grid = new double[] { gridX, gridY };
    return grid;
  }
  
  /**
   * gets coordinates for x/y values.
   */
  @Override
  public double[][] coordinatesAt(double[] xarr, double[] yarr) {
    double[] gridX = x.coordinatesAt(xarr);
    double[] gridY = y.coordinatesAt(yarr);
    double[][] grid = new double[2][];
    grid[0] = gridX;
    grid[1] = gridY;
    return grid;
  }
  
  /**
   * gets grid array on dimension <code>index</code>.
   */
  @Override
  public IArray1D getArray(int index) throws java.lang.ArrayIndexOutOfBoundsException {
    switch (index) {
      case 0 :
	return x;
      case 1 :
	return y;
      default :
	throw new ArrayIndexOutOfBoundsException();
    }
  }
  
  /**
   * gets 2D range of grid.
   * @return Range.Double
   */
  @Override
  public Range.Double getRange() {
    return new Range2D.Double(x.getRange1D(), y.getRange1D());
  }
  
  /**
   * gets grid range.
   * @return Range2D.Double
   */
  @Override
  public Range2D.Double getRange2D() {
    return new Range2D.Double(x.getRange1D(), y.getRange1D());
  }
  
  /**
   * gets grid on x-axis.
   * @return Grid1D
   */
  public IArray1D getXArray() {
    return x;
  }
  
  /**
   * gets grid on x-axis.
   * @return Grid1D
   */
  public Grid1D getXGrid() {
    return x;
  }
  
  /**
   * gets grid on y-axis.
   * @return Grid1D
   */
  public IArray1D getYArray() {
    return y;
  }
  
  /**
   * gets grid on y-axis.
   * @return Grid1D
   */
  public Grid1D getYGrid() {
    return y;
  }
  
  /**
   * gets grid point at index <code>index</code>.
   */
  @Override
  public double[] gridPointAt(int index) {
    int indexX = index % getXLength();
    int indexY = index / getXLength();
    double valueX = x.pointAt(indexX);
    double valueY = y.pointAt(indexY);
    double[] values = new double[] { valueX, valueY };
    return values;
  }
  
  /**
   * gets grid points at indices.
   */
  @Override
  public double[][] gridPointsAt(int[] index) {
    int n = index.length;
    int lengthX = getXLength();
    int length = getLength();
    int[] indexX = new int[n];
    int[] indexY = new int[n];
    for (int i = 0; i < n; i++) {
      if (0 <= index[i] && index[i] < length) {
	indexX[i] = index[i] % lengthX;
	indexY[i] = index[i] / lengthX;
      } else {
	indexX[i] = -1;
	indexY[i] = -1;
      }
    }
    double[] valuesX = x.pointsAt(indexX);
    double[] valuesY = y.pointsAt(indexY);
    double[][] values = new double[2][n];
    values[0] = valuesX;
    values[1] = valuesY;
    return values;
  }
  
  /**
   * sets grid on x-axis to <code>newX</code>.
   * @param newX Grid1D
   */
  public void setXGrid(Grid1D newX) {
    this.x = newX;
    setXLength(newX.getLength());
    setLength(getXLength() * getYLength());
  }
  
  /**
   * sets grid on y-axis to <code>newY</code>.
   * @param newY Grid1D
   */
  public void setYGrid(Grid1D newY) {
    this.y = newY;
    setYLength(newY.getLength());
    setLength(getXLength() * getYLength());
  }
  
  /**
   * gets value at grid coordinates <code>gridx, gridy</code>.
   */
  @Override
  public double[] valueAt(double gridx, double gridy) {
    double valueX = x.valueAt(gridx);
    double valueY = y.valueAt(gridy);
    double[] value = new double[2];
    value[0] = valueX;
    value[1] = valueY;
    return value;
  }
  
  /**
   * gets values for grid coordinates <code> gridx, gridy</code>.
   */
  @Override
  public double[][] valuesAt(double[] gridx, double[] gridy) {
    double[] valueX = x.valuesAt(gridx);
    double[] valueY = y.valuesAt(gridy);
    double[][] value = new double[2][];
    value[0] = valueX;
    value[1] = valueY;
    return value;
  }
}
