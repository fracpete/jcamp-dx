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
 * 2 dimensional linear and rectangular grid.
 * 
 * @author Thomas Weber 
 */
public class LinearGrid2D
  extends RectangularGrid2D
  implements Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = -4627615487641799717L;

  /**
   * default constructor.
   */
  public LinearGrid2D() {
    super();
  }

  /**
   * LinearGrid2D constructor comment.
   */
  public LinearGrid2D(double startX, double endX, double stepX, double startY, double endY, double stepY) {
    super(new LinearGrid1D(startX, endX, stepX), new LinearGrid1D(startY, endY, stepY));
  }

  /**
   * LinearGrid2D constructor comment.
   * @param lengthX int
   * @param lengthY int
   */
  public LinearGrid2D(double startX, double endX, int lengthX, double startY, double endY, int lengthY) {
    super(new LinearGrid1D(startX, endX, lengthX), new LinearGrid1D(startY, endY, lengthY));
  }

  /**
   * LinearGrid2D constructor comment.
   * @param xgrid LinearGrid1D
   * @param ygrid LienarGrid1D
   */
  public LinearGrid2D(LinearGrid1D xgrid, LinearGrid1D ygrid) {
    super(xgrid, ygrid);
  }

  /**
   * cloning.
   * @return Object
   */
  @Override
  public Object clone() {
    LinearGrid2D grid = null;
    //	try {
    grid = (LinearGrid2D) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    return grid;
  }

  /**
   * gets end point in x-direction.
   * @return double
   */
  public double getXEnd() {
    return ((LinearGrid1D) getXGrid()).getEnd();
  }

  /**
   * gets starting point in x-direction.
   * @return double
   */
  public double getXStart() {
    return ((LinearGrid1D) getXGrid()).getStart();
  }

  /**
   * gets step size in x-direction
   * @return double
   */
  public double getXStep() {
    return ((LinearGrid1D) getXGrid()).getStep();
  }

  /**
   * gets end point in y-direction.
   * @return double
   */
  public double getYEnd() {
    return ((LinearGrid1D) getYGrid()).getEnd();
  }

  /**
   * gets starting value in y-direction.
   * @return double
   */
  public double getYStart() {
    return ((LinearGrid1D) getYGrid()).getStart();
  }

  /**
   * gets step size in y-direction.
   * @return double
   */
  public double getYStep() {
    return ((LinearGrid1D) getYGrid()).getStep();
  }

  /**
   * interpolate z-value for given spectrum xy coordinate.
   * @return double     interpolated height
   * @param xy double[] position in xy-plane
   */
  public double[] interpolate(RectangularGrid2D dataGrid, double[] data) {
    Grid1D x = dataGrid.getXGrid();
    Grid1D y = dataGrid.getYGrid();
    int lengthX = this.getXLength();
    int lengthY = this.getYLength();
    double[] interpolated = new double[this.getLength()];
    for (int index = 0; index < getLength(); index++) {
      double[] xy = gridPointAt(index);
      double gx = x.coordinateAt(xy[0]);
      double gy = y.coordinateAt(xy[1]);
      //		System.out.println("interpolating at " + xy[0] + "," + xy[1]);
      //		System.out.println("grid coordinates " + gx + "," + gy);
      if (gx < -0.5 || gx > lengthX - 0.5 || gy < -0.5 || gy > lengthY - 0.5) { // out of bounds
	interpolated[index] = Double.NaN;
	continue;
      }
      // find nearest integer lesser than g
      int gx0 = (int) Math.floor(gx + 0.5);
      if (gx0 < 0)
	gx0 = 0;
      if (gx0 > lengthX - 2)
	gx0 = lengthX - 2;
      int gy0 = (int) Math.floor(gy + 0.5);
      if (gy0 < 0)
	gy0 = 0;
      if (gy0 > lengthY - 2)
	gy0 = lengthY - 2;
      // bounding rectangle:
      int lb = gy0 * lengthX + gx0;
      int rb = gy0 * lengthX + gx0 + 1;
      int lt = (gy0 + 1) * lengthX + gx0;
      int rt = (gy0 + 1) * lengthX + gx0 + 1;
      double dx = Math.max(0.0, Math.min(1.0, gx - gx0));
      double dy = Math.max(0.0, Math.min(1.0, gy - gy0));
      double zlb = data[lb];
      double zrb = data[rb];
      double zlt = data[lt];
      double zrt = data[rt];
      double z;
      if (dx + dy - 1 < 0) {
	z = zlb + dx * (zrb - zlb) + dy * (zlt - zlb);
      } else {
	dx = 1. - dx;
	dy = 1. - dy;
	z = zrt + dx * (zlt - zrt) + dy * (zrb - zrt);
      }
      interpolated[index] = z;
    }
    return interpolated;
  }
}
