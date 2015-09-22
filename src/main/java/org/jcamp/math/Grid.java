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
 * abstract base class for grid topologies.
 * (grid values must be strict monotone in all dimensions).
 * grids define an coordinate system that makes conversions of 
 * real world values into grid coordinates possible.
 * used for independend data in spectra and data mappings to
 * axes.
 * 
 * @author Thomas Weber
 */
public abstract class Grid
  extends OrderedArray 
  implements Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = -2843269058848877668L;

  /**
   * Grid constructor comment.
   */
  public Grid() {
    super();
  }

  /**
   * Grid constructor comment.
   */
  public Grid(int length) {
    super(length);
  }

  /**
   * cloning
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    Grid grid = null;
    //	try {
    grid = (Grid) super.clone();
    //	} catch( CloneNotSupportedException e) {}
    return grid;
  }

  /**
   * convert n-dimensional real world value into
   * n-dimension grid coordinate
   * @param value double []
   * @return double []
   */
  public abstract double[] coordinateAt(double[] value);

  /**
   * convert 1- or 2-dimensional real world value array into
   * grid coordinate array
   * @param values double [][]
   * @return double [][]
   */
  public abstract double[][] coordinatesAt(double[][] values);

  /**
   * return grid point at index
   * n-dimension grid coordinate
   * @param value double []
   * @return double []
   */
  @Override
  public double[] elementAt(int index) {
    return gridPointAt(index);
  }

  /**
   * return grid points at indices
   * @param value double [][]
   * @return double [][]
   */
  @Override
  public double[][] elementsAt(int[] indices) {
    return gridPointsAt(indices);
  }

  /**
   * return grid point at index
   * n-dimension grid coordinate
   * @param value double []
   * @return double []
   */
  public abstract double[] gridPointAt(int index);

  /**
   * return grid points at indices
   * @param value double [][]
   * @return double [][]
   */
  public abstract double[][] gridPointsAt(int[] indices);

  /**
   * return index of nearest grid point at position <code>position</code>.
   */
  public abstract int indexAt(double[] position);

  /**
   * return indices of nearest grid points at positions <code>positions</code>.
   */
  public abstract int[] indicesAt(double[][] positions);

  /**
   * convert n-dimensional grid coordinate into real world coordinate
   * @param grid double []
   * @return double []
   */
  public abstract double[] valueAt(double[] grid);

  /**
   * convert n-dimensional grid coordinate into real world coordinate
   * real world data array
   * @param grid double [][]
   * @return double [][]
   */
  public abstract double[][] valuesAt(double[][] grid);
}
