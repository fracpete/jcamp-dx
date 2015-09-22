/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import java.io.Serializable;

/**
 * null value implementation.
 * 
 * @author Thomas Weber
 */
public final class NullGrid1D
  extends Grid1D
  implements Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = 5117763411029918975L;
  
  private final static Range1D.Double NULLRANGE = new Range1D.Double();

  /**
   * NullGrid1D constructor comment.
   */
  public NullGrid1D() {
    super();
  }

  /**
   * clone method comment.
   */
  @Override
  public Object clone() {
    NullGrid1D grid = null;
    //	try {
    grid = (NullGrid1D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    return grid;
  }

  /**
   * coordinateAt method comment.
   */
  @Override
  public double[] coordinateAt(double[] value) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * coordinateAt method comment.
   */
  @Override
  public double coordinateAt(double value) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * coordinatesAt method comment.
   */
  @Override
  public double[][] coordinatesAt(double[][] values) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * elementAt method comment.
   */
  @Override
  public double[] elementAt(int index) {
    throw new ArrayIndexOutOfBoundsException();
  }

  /**
   * getArray method comment.
   */
  @Override
  public IArray1D getArray(int index) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * gets grid range.
   */
  @Override
  public Range.Double getRange() {
    return NULLRANGE;
  }

  /**
   * gets grid range.
   * @return Range1D.Double
   */
  @Override
  public Range1D.Double getRange1D() {
    return NULLRANGE;
  }

  /**
   * gridPointAt method comment.
   */
  @Override
  public double[] gridPointAt(int index) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * gridPointsAt method comment.
   */
  @Override
  public double[][] gridPointsAt(int[] indices) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * indexAt method comment.
   */
  @Override
  public int indexAt(double[] position) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * indicesAt method comment.
   */
  @Override
  public int[] indicesAt(double[][] positions) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * isAscending method comment.
   */
  @Override
  public boolean isAscending() {
    return false;
  }

  /**
   * pointAt method comment.
   */
  @Override
  public double pointAt(int index) {
    throw new ArrayIndexOutOfBoundsException("null grid");
  }

  /**
   * scale method comment.
   */
  public void scale(double amount) {
  }

  public double[] toArray() {
    return new double[] {
    };
  }

  /**
   * translate method comment.
   */
  public void translate(double amount) {
  }

  /**
   * valueAt method comment.
   */
  @Override
  public double[] valueAt(double[] grid) {
    return null;
  }

  /**
   * valueAt method comment.
   */
  @Override
  public double valueAt(double grid) {
    return 0;
  }

  /**
   * valuesAt method comment.
   */
  @Override
  public double[][] valuesAt(double[][] grid) {
    return null;
  }
}
