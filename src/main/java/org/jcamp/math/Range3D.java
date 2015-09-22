/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * base class for 3D data ranges.
 * 
 * @author Thomas Weber
 */
public abstract class Range3D
  extends Range {
  
  /** for serialization. */
  private static final long serialVersionUID = -7893792822902181226L;

  /**
   * 3D data range.
   * 
   * @author Thomas Weber
   */
  public static class Int
    extends Range3D
    implements Range.Int, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = 286208478600748675L;

    /** minimum x value */
    private int xmin;

    /** maximum x value */
    private int xmax;

    /** minimum y value */
    private int ymin;

    /** maximum y value */
    private int ymax;

    /** minimum z value */
    private int zmin;

    /** maximum z value */
    private int zmax;
    
    /**
     * default constructor.
     */
    public Int() {
      super();
      this.xmin = 0;
      this.xmax = 0;
      this.ymin = 0;
      this.ymax = 0;
      this.zmin = 0;
      this.zmax = 0;
    }
    
    /**
     * copy constructor.
     */
    public Int(Range3D.Int r) {
      super();
      this.xmin = r.xmin;
      this.xmax = r.xmax;
      this.ymin = r.ymin;
      this.ymax = r.ymax;
      this.zmin = r.zmin;
      this.zmax = r.zmax;
    }
    
    /**
     * create range
     * @param xrange Range1D.Int
     * @param yrange Range1D.Int
     * @param zrange Range1D.Int
     */
    public Int(Range1D.Int xrange, Range1D.Int yrange, Range1D.Int zrange) {
      super();
      this.xmin = xrange.getXMin();
      this.xmax = xrange.getXMax();
      this.ymin = yrange.getXMin();
      this.ymax = yrange.getXMax();
      this.zmin = zrange.getXMin();
      this.zmax = zrange.getXMax();
    }
    
    /**
     * set range
     * @param xrange Range1D.Int
     * @param yrange Range1D.Int
     * @param zrange Range1D.Int
     */
    public void set(Range1D.Int xrange, Range1D.Int yrange, Range1D.Int zrange) {
      this.xmin = xrange.getXMin();
      this.xmax = xrange.getXMax();
      this.ymin = yrange.getXMin();
      this.ymax = yrange.getXMax();
      this.zmin = zrange.getXMin();
      this.zmax = zrange.getXMax();
    }
    
    /**
     * create range 
     *[min(<code>x0,x1</code>), max(<code>x0,x1</code>)]
     *[min(<code>y0,y1</code>), max(<code>y0,y1</code>)]
     *[min(<code>z0,z1</code>), max(<code>z0,z1</code>)]
     * @param x0 int 
     * @param x1 int
     * @param y0 int 
     * @param y1 int
     * @param z0 int 
     * @param z1 int
     */
    public Int(int x0, int x1, int y0, int y1, int z0, int z1) {
      super();
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
      if (y0 < y1) {
	this.ymin = y0;
	this.ymax = y1;
      } else {
	this.ymin = y1;
	this.ymax = y0;
      }
      if (z0 < z1) {
	this.zmin = z0;
	this.zmax = z1;
      } else {
	this.zmin = z1;
	this.zmax = z0;
      }
    }
    
    /**
     * check if range contains value <code>(x,y,z)</code>.
     * @param x int
     * @param y int
     * @param z int 
     * @return boolean
     */
    public boolean contains(int x, int y, int z) {
      return (x >= xmin && x <= xmax && y >= ymin && y <= ymax && z >= zmin && z <= zmax);
    }
    
    /**
     * clip range <code>x0,x1,y0,y1,z0,z1</code>. return <code>null</code> if range is outside or on border
     * @param x0 int
     * @param x1 int
     * @param y0 int
     * @param y1 int
     * @param z0 int
     * @param z1 int
     * @return Range3D.Int
     */
    public Range3D.Int clip(int x0, int x1, int y0, int y1, int z0, int z1) {
      Range3D.Int nr = new Int(x0, x1, y0, y1, z0, z1);
      if (nr.xmin < xmin) {
	if (nr.xmax <= xmin)
	  return null;
	else
	  nr.xmin = xmin;
      }
      if (nr.xmax > xmax) {
	if (nr.xmin >= xmax)
	  return null;
	else
	  nr.xmax = xmax;
      }
      if (nr.ymin < ymin) {
	if (nr.ymax <= ymin)
	  return null;
	else
	  nr.ymin = ymin;
      }
      if (nr.ymax > ymax) {
	if (nr.ymin >= ymax)
	  return null;
	else
	  nr.ymax = ymax;
      }
      if (nr.zmin < zmin) {
	if (nr.zmax <= zmin)
	  return null;
	else
	  nr.zmin = zmin;
      }
      if (nr.zmax > zmax) {
	if (nr.zmin >= zmax)
	  return null;
	else
	  nr.zmax = zmax;
      }
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @return Range3D.Int
     * @param r Range3D.Int
     */
    public Range3D.Int clip(Range3D.Int r) {
      Range3D.Int nr = new Range3D.Int(r);
      if (nr.xmin < xmin) {
	if (nr.xmax <= xmin)
	  return null;
	else
	  nr.xmin = xmin;
      }
      if (nr.xmax > xmax) {
	if (nr.xmin >= xmax)
	  return null;
	else
	  nr.xmax = xmax;
      }
      if (nr.ymin < ymin) {
	if (nr.ymax <= ymin)
	  return null;
	else
	  nr.ymin = ymin;
      }
      if (nr.ymax > ymax) {
	if (nr.ymin >= ymax)
	  return null;
	else
	  nr.ymax = ymax;
      }
      if (nr.zmin < zmin) {
	if (nr.zmax <= zmin)
	  return null;
	else
	  nr.zmin = zmin;
      }
      if (nr.zmax > zmax) {
	if (nr.zmin >= zmax)
	  return null;
	else
	  nr.zmax = zmax;
      }
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range3D.Int range = null;
      try {
	range = (Range3D.Int) super.clone();
      } catch (CloneNotSupportedException e) {
      }
      return range;
    }
    
    /**
     * compare ranges for equality.
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Range3D.Int) {
	Range3D.Int r = (Range3D.Int) obj;
	if (this == r
	    || ((r.xmin == xmin)
		&& (r.xmax == xmax)
		&& (r.ymin == ymin)
		&& (r.ymax == ymax)
		&& (r.zmin == zmin)
		&& (r.zmax == zmax)))
	  return true;
      }
      return false;
    }
    
    /**
     * extend range by <code>xamount,yamount,zamount</code> on both sides.
     * @param xamount int
     * @param yamount int
     * @param zamount int
     */
    public void extend(int xamount, int yamount, int zamount) {
      this.xmin -= xamount;
      this.xmax += xamount;
      this.ymin -= yamount;
      this.ymax += yamount;
      this.zmin -= zamount;
      this.zmax += zamount;
    }
    
    /**
     * gets the max x value of the range as a double
     * @return double
     */
    @Override
    public double getXMaxAsDouble() {
      return xmax;
    }
    
    /**
     * gets the max z value of the range as a double
     * @return double
     */
    @Override
    public double getZMaxAsDouble() {
      return zmax;
    }
    
    /**
     * gets the max y value of the range as a double
     * @return double
     */
    @Override
    public double getYMaxAsDouble() {
      return ymax;
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getCenter(int dim) {
      if (dim == 0)
	return (xmax + xmin) / 2;
      else if (dim == 1)
	return (ymax + ymin) / 2;
      else if (dim == 2)
	return (zmax + zmin) / 2;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the range in dimension <code>dim</code>.
     * @param dim int
     * @return Range1D.Int
     */
    public Range1D.Int getRange(int dim) {
      if (dim == 0)
	return new Range1D.Int(xmin, xmax);
      else if (dim == 1)
	return new Range1D.Int(ymin, ymax);
      else if (dim == 2)
	return new Range1D.Int(zmin, zmax);
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the maximum in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getMax(int dim) {
      if (dim == 0)
	return xmax;
      else if (dim == 1)
	return ymax;
      else if (dim == 2)
	return zmax;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the min x value of the range as a double
     * @return double
     */
    @Override
    public double getXMinAsDouble() {
      return xmin;
    }
    
    /**
     * gets the min z value of the range as a double
     * @return double
     */
    @Override
    public double getZMinAsDouble() {
      return zmin;
    }
    
    /**
     * gets the min y value of the range as a double
     * @return double
     */
    @Override
    public double getYMinAsDouble() {
      return ymin;
    }
    
    /**
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getWidth(int dim) {
      if (dim == 0)
	return (xmax - xmin);
      else if (dim == 1)
	return (ymax - ymin);
      else if (dim == 2)
	return (zmax - zmin);
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the minimum in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getMin(int dim) {
      if (dim == 0)
	return xmin;
      else if (dim == 1)
	return ymin;
      else if (dim == 2)
	return zmin;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the max x value of the range
     * @return int
     */
    public int getXMax() {
      return xmax;
    }
    
    /**
     * gets the max z value of the range
     * @return int
     */
    public int getZMax() {
      return zmax;
    }
    
    /**
     * gets the max y value of the range
     * @return int
     */
    public int getYMax() {
      return ymax;
    }
    
    /**
     * gets the min x value of the range
     * @return int
     */
    public int getXMin() {
      return xmin;
    }
    
    /**
     * gets the x range
     * @return Range1D.Int
     */
    public Range1D.Int getXRange() {
      return new Range1D.Int(xmin, xmax);
    }
    
    /**
     * gets the z range
     * @return Range1D.Int
     */
    public Range1D.Int getZRange() {
      return new Range1D.Int(zmin, zmax);
    }
    
    /**
     * gets the y range
     * @return Range1D.Int
     */
    public Range1D.Int getYRange() {
      return new Range1D.Int(ymin, ymax);
    }
    
    /**
     * gets the min z value of the range
     * @return int
     */
    public int getZMin() {
      return zmin;
    }
    
    /**
     * gets the min y value of the range
     * @return int
     */
    public int getYMin() {
      return ymin;
    }
    
    /**
     * gets the size of the x interval.
     * @return int
     */
    public int getXWidth() {
      return xmax - xmin;
    }
    
    /**
     * gets the center of the x interval.
     * @return int
     */
    public int getXCenter() {
      return (xmax + xmin) / 2;
    }
    
    /**
     * gets the center of the z interval.
     * @return int
     */
    public int getZCenter() {
      return (zmax + zmin) / 2;
    }
    
    /**
     * gets the center of the y interval.
     * @return int
     */
    public int getYCenter() {
      return (ymax + ymin) / 2;
    }
    
    /**
     * gets the size of the z interval.
     * @return int
     */
    public int getZWidth() {
      return zmax - zmin;
    }
    
    /**
     * gets the size of the y interval.
     * @return int
     */
    public int getYWidth() {
      return ymax - ymin;
    }
    
    /**
     * include <code>(x,y,z)</code> in range.
     * @param x int
     * @param y int
     * @param z int
     * @return Range3D.Int (this)
     */
    public Range3D.Int include(int x, int y, int z) {
      if (x < this.xmin)
	this.xmin = x;
      if (x > this.xmax)
	this.xmax = x;
      if (y < this.ymin)
	this.ymin = y;
      if (y > this.ymax)
	this.ymax = y;
      if (z < this.zmin)
	this.zmin = z;
      if (z > this.zmax)
	this.zmax = z;
      return this;
    }
    
    /**
     * include [<code>xmin,xmax</code> ],[<code>ymin,ymax</code> ],[<code>zmin,zmax</code> ]  in range.
     * @param xmin int
     * @param xmax int
     * @param ymin int
     * @param ymax int
     * @param zmin int
     * @param zmax int
     * @return Range3D.Int (this)
     */
    public Range3D.Int include(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      if (ymin < this.ymin)
	this.ymin = ymin;
      if (ymax > this.ymax)
	this.ymax = ymax;
      if (zmin < this.zmin)
	this.zmin = zmin;
      if (zmax > this.zmax)
	this.zmax = zmax;
      return this;
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range3D.Int
     * @return Range3D.Int
     */
    public Range3D.Int include(Range3D.Int range) {
      return include(
	  range.getXMin(),
	  range.getXMax(),
	  range.getYMin(),
	  range.getYMax(),
	  range.getZMin(),
	  range.getZMax());
    }
    
    /**
     * set range to 
     *[min(<code>x0,x1</code>), max(<code>x0,x1</code>)]
     *[min(<code>y0,y1</code>), max(<code>y0,y1</code>)]
     *[min(<code>z0,y1</code>), max(<code>y0,y1</code>)]
     * @param x0 int 
     * @param x1 int
     * @param y0 int 
     * @param y1 int
     * @param z0 int 
     * @param z1 int
     */
    public void set(int x0, int x1, int y0, int y1, int z0, int z1) {
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
      if (y0 < y1) {
	this.ymin = y0;
	this.ymax = y1;
      } else {
	this.ymin = y1;
	this.ymax = y0;
      }
      if (z0 < z1) {
	this.zmin = z0;
	this.zmax = z1;
      } else {
	this.zmin = z1;
	this.zmax = z0;
      }
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range3D
     */
    public void set(Range3D.Int r) {
      this.xmin = r.getXMin();
      this.xmax = r.getXMax();
      this.ymin = r.getYMin();
      this.ymax = r.getYMax();
      this.zmin = r.getZMin();
      this.zmax = r.getZMax();
    }
    
    /**
     * sets range to center point and width.
     * @param xcenter int
     * @param xwidth int
     * @param ycenter int
     * @param ywidth int
     * @param zcenter int
     * @param zwidth int
     */
    public void setCenterAndWidth(int xcenter, int xwidth, int ycenter, int ywidth, int zcenter, int zwidth) {
      this.xmin = xcenter - xwidth / 2;
      this.xmax = xcenter + xwidth / 2;
      this.ymin = ycenter - ywidth / 2;
      this.ymax = ycenter + ywidth / 2;
      this.zmin = zcenter - zwidth / 2;
      this.zmax = zcenter + zwidth / 2;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return new StringBuilder()
      .append('[')
      .append(xmin)
      .append(',')
      .append(xmax)
      .append("],[")
      .append(ymin)
      .append(',')
      .append(ymax)
      .append("],[")
      .append(zmin)
      .append(',')
      .append(zmax)
      .append(']')
      .toString();
    }
    
    /**
     * translate range by <code>xamount,yamount,zamount</code>
     * @param xamount int
     * @param yamount int
     * @param zamount int
     */
    public void translate(int xamount, int yamount, int zamount) {
      this.xmin += xamount;
      this.xmax += xamount;
      this.ymin += yamount;
      this.ymax += yamount;
      this.zmin += zamount;
      this.zmax += zamount;
    }
    
    /**
     * gets union of <code>(x,y,z)</code> with <code>this</code> range.
     * @param xvalue int
     * @param yvalue int
     * @param zvalue int
     * @return Range3D.Int
     */
    public Range3D.Int union(int xvalue, int yvalue, int zvalue) {
      Range3D.Int range = (Range3D.Int) this.clone();
      if (xvalue < range.xmin)
	range.xmin = xvalue;
      if (xvalue > range.xmax)
	range.xmax = xvalue;
      if (yvalue < range.ymin)
	range.ymin = yvalue;
      if (yvalue > range.ymax)
	range.ymax = yvalue;
      if (zvalue < range.zmin)
	range.zmin = zvalue;
      if (zvalue > range.zmax)
	range.zmax = zvalue;
      return range;
    }
    
    /**
     * gets union of [<code>xmin,xmax,ymin,ymax,zmin,zmax</code> ] with <code>this</code> range.
     * @param xmin int
     * @param xmax int
     * @param ymin int
     * @param ymax int
     * @param zmin int
     * @param zmax int
     * @return Range3D.Int
     */
    public Range3D.Int union(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
      Range3D.Int range = (Range3D.Int) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      if (ymin < range.ymin)
	range.ymin = ymin;
      if (ymax > range.ymax)
	range.ymax = ymax;
      if (zmin < range.zmin)
	range.zmin = zmin;
      if (zmax > range.zmax)
	range.zmax = zmax;
      return range;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range3D.Int
     * @return Range3D.Int
     */
    public Range3D.Int union(Range3D.Int range) {
      return union(
	  range.getXMin(),
	  range.getXMax(),
	  range.getYMin(),
	  range.getYMax(),
	  range.getZMin(),
	  range.getZMax());
    }
  }
  
  /**
   * 3D data range.
   * 
   * @author Thomas Weber
   */
  public static class Double
    extends Range3D
    implements Range.Double, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = 2246452030290172973L;

    /** x minimum value */
    private double xmin;

    /** x maximum value */
    private double xmax;

    /** y minimum value */
    private double ymin;

    /** y maximum value */
    private double ymax;

    /** y minimum value */
    private double zmin;

    /** y maximum value */
    private double zmax;

    /**
     * default constructor.
     */
    public Double() {
      super();
      this.xmin = java.lang.Double.NaN;
      this.xmax = java.lang.Double.NaN;
      this.ymin = java.lang.Double.NaN;
      this.ymax = java.lang.Double.NaN;
      this.zmin = java.lang.Double.NaN;
      this.zmax = java.lang.Double.NaN;
    }
    
    /**
     * copy constructor.
     */
    public Double(Range3D r) {
      super();
      this.xmin = r.getXMinAsDouble();
      this.xmax = r.getXMaxAsDouble();
      this.ymin = r.getYMinAsDouble();
      this.ymax = r.getYMaxAsDouble();
      this.zmin = r.getZMinAsDouble();
      this.zmax = r.getZMaxAsDouble();
    }
    
    /**
     * create range.
     * @param xrange Range1D
     * @param yrange Range1D
     * @param zrange Range1D
     */
    public Double(Range1D xrange, Range1D yrange, Range1D zrange) {
      super();
      this.xmin = xrange.getXMinAsDouble();
      this.xmax = xrange.getXMaxAsDouble();
      this.ymin = yrange.getXMinAsDouble();
      this.ymax = yrange.getXMaxAsDouble();
      this.zmin = zrange.getXMinAsDouble();
      this.zmax = zrange.getXMaxAsDouble();
    }
    
    /**
     * set range.
     * @param xrange Range1D
     * @param yrange Range1D
     * @param zrange Range1D
     */
    public void set(Range1D xrange, Range1D yrange, Range1D zrange) {
      this.xmin = xrange.getXMinAsDouble();
      this.xmax = xrange.getXMaxAsDouble();
      this.ymin = yrange.getXMinAsDouble();
      this.ymax = yrange.getXMaxAsDouble();
      this.zmin = zrange.getXMinAsDouble();
      this.zmax = zrange.getXMaxAsDouble();
    }
    
    /**
     * create range 
     * [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)],
     * [min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * [min(<code>z0</code>,<code>z1</code>), max(<code>z0</code>,<code>z1</code>)]
     * @param x0 double 
     * @param x1 double
     * @param y0 double 
     * @param y1 double
     * @param z0 double 
     * @param z1 double
     */
    public Double(double x0, double x1, double y0, double y1, double z0, double z1) {
      super();
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
      if (y0 < y1) {
	this.ymin = y0;
	this.ymax = y1;
      } else {
	this.ymin = y1;
	this.ymax = y0;
      }
      if (z0 < z1) {
	this.zmin = z0;
	this.zmax = z1;
      } else {
	this.zmin = z1;
	this.zmax = z0;
      }
    }
    
    /**
     * clip range <code>x0,x1,y0,y1,z0,z1</code>. return <code>null</code> if range is outside or on border
     * @param x0 double
     * @param x1 double
     * @param y0 double
     * @param y1 double
     * @param z0 double
     * @param z1 double
     * @return Range3D.Double
     */
    public Range3D.Double clip(double x0, double x1, double y0, double y1, double z0, double z1) {
      Range3D.Double nr = new Double(x0, x1, y0, y1, z0, z1);
      if (nr.xmin < xmin) {
	if (nr.xmax <= xmin)
	  return null;
	else
	  nr.xmin = xmin;
      }
      if (nr.xmax > xmax) {
	if (nr.xmin >= xmax)
	  return null;
	else
	  nr.xmax = xmax;
      }
      if (nr.ymin < ymin) {
	if (nr.ymax <= ymin)
	  return null;
	else
	  nr.ymin = ymin;
      }
      if (nr.ymax > ymax) {
	if (nr.ymin >= ymax)
	  return null;
	else
	  nr.ymax = ymax;
      }
      if (nr.zmin < zmin) {
	if (nr.zmax <= zmin)
	  return null;
	else
	  nr.zmin = zmin;
      }
      if (nr.zmax > zmax) {
	if (nr.ymin >= zmax)
	  return null;
	else
	  nr.zmax = zmax;
      }
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @param r Range3D
     * @return Range3D.Double
     */
    public Range3D.Double clip(Range3D r) {
      Range3D.Double nr = new Range3D.Double(r);
      if (nr.xmin < xmin) {
	if (nr.xmax <= xmin)
	  return null;
	else
	  nr.xmin = xmin;
      }
      if (nr.xmax > xmax) {
	if (nr.xmin >= xmax)
	  return null;
	else
	  nr.xmax = xmax;
      }
      if (nr.ymin < ymin) {
	if (nr.ymax <= ymin)
	  return null;
	else
	  nr.ymin = ymin;
      }
      if (nr.ymax > ymax) {
	if (nr.ymin >= ymax)
	  return null;
	else
	  nr.ymax = ymax;
      }
      if (nr.zmin < zmin) {
	if (nr.zmax <= zmin)
	  return null;
	else
	  nr.zmin = zmin;
      }
      if (nr.zmax > zmax) {
	if (nr.zmin >= zmax)
	  return null;
	else
	  nr.zmax = zmax;
      }
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range3D.Double range = null;
      try {
	range = (Range3D.Double) super.clone();
      } catch (CloneNotSupportedException e) {
      }
      return range;
    }
    
    /**
     * compare ranges for equality.
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Range3D) {
	Range3D r = (Range3D) obj;
	if (this == r
	    || (Math.abs(r.getXMinAsDouble() - xmin) < DOUBLE_EPS
		&& Math.abs(r.getXMaxAsDouble() - xmax) < DOUBLE_EPS
		&& Math.abs(r.getYMinAsDouble() - ymin) < DOUBLE_EPS
		&& Math.abs(r.getYMaxAsDouble() - ymax) < DOUBLE_EPS
		&& Math.abs(r.getZMinAsDouble() - zmin) < DOUBLE_EPS
		&& Math.abs(r.getZMaxAsDouble() - zmax) < DOUBLE_EPS))
	  return true;
      }
      return false;
    }
    
    /**
     * extend range by <code>xamount, yamount, zamount</code> on both sides.
     * @param xamount double
     * @param yamount double
     * @param zamount double
     */
    public void extend(double xamount, double yamount, double zamount) {
      this.xmin -= xamount;
      this.xmax += xamount;
      this.ymin -= yamount;
      this.ymax += yamount;
      this.zmin -= zamount;
      this.zmax += zamount;
    }
    
    /**
     * gets the max x value of the range as a double
     * @return double
     */
    @Override
    public double getXMaxAsDouble() {
      return xmax;
    }
    
    /**
     * gets the max z value of the range as a double
     * @return double
     */
    @Override
    public double getZMaxAsDouble() {
      return zmax;
    }
    
    /**
     * gets the max y value of the range as a double
     * @return double
     */
    @Override
    public double getYMaxAsDouble() {
      return ymax;
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getCenter(int dim) {
      if (dim == 0)
	return 0.5 * (xmax + xmin);
      else if (dim == 1)
	return 0.5 * (ymax + ymin);
      else if (dim == 2)
	return 0.5 * (zmax + zmin);
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return Range1D.Double
     */
    public Range1D.Double getRange(int dim) {
      if (dim == 0)
	return new Range1D.Double(xmin, xmax);
      else if (dim == 1)
	return new Range1D.Double(ymin, ymax);
      else if (dim == 2)
	return new Range1D.Double(zmin, zmax);
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the maximum in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getMax(int dim) {
      if (dim == 0)
	return xmax;
      else if (dim == 1)
	return ymax;
      else if (dim == 2)
	return zmax;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the min x value of the range as a double
     * @return double
     */
    @Override
    public double getXMinAsDouble() {
      return xmin;
    }
    
    /**
     * gets the min z value of the range as a double
     * @return double
     */
    @Override
    public double getZMinAsDouble() {
      return zmin;
    }
    
    /**
     * gets the min y value of the range as a double
     * @return double
     */
    @Override
    public double getYMinAsDouble() {
      return ymin;
    }
    
    /**
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getWidth(int dim) {
      if (dim == 0)
	return xmax - xmin;
      else if (dim == 1)
	return ymax - ymin;
      else if (dim == 2)
	return zmax - zmin;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the minimum in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getMin(int dim) {
      if (dim == 0)
	return xmin;
      else if (dim == 1)
	return ymin;
      else if (dim == 2)
	return zmin;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the max x value of the range
     * @return double
     */
    public double getXMax() {
      return xmax;
    }
    
    /**
     * gets the max z value of the range
     * @return double
     */
    public double getZMax() {
      return zmax;
    }
    
    /**
     * gets the max y value of the range
     * @return double
     */
    public double getYMax() {
      return ymax;
    }
    
    /**
     * gets the min x value of the range
     * @return double
     */
    public double getXMin() {
      return xmin;
    }
    
    /**
     * gets the x range
     * @return Range1D.Double
     */
    public Range1D.Double getXRange() {
      return new Range1D.Double(xmin, xmax);
    }
    
    /**
     * gets the z range
     * @return Range1D.Double
     */
    public Range1D.Double getZRange() {
      return new Range1D.Double(zmin, zmax);
    }
    
    /**
     * gets the y range
     * @return Range1D.Double
     */
    public Range1D.Double getYRange() {
      return new Range1D.Double(ymin, ymax);
    }
    
    /**
     * gets the min z value of the range
     * @return double
     */
    public double getZMin() {
      return zmin;
    }
    
    /**
     * gets the min y value of the range
     * @return double
     */
    public double getYMin() {
      return ymin;
    }
    
    /**
     * gets the size of the x interval.
     * @return double
     */
    public double getXWidth() {
      return xmax - xmin;
    }
    
    /**
     * gets the center of the x interval.
     * @return double
     */
    public double getXCenter() {
      return 0.5 * (xmax + xmin);
    }
    
    /**
     * gets the center of the z interval.
     * @return double
     */
    public double getZCenter() {
      return 0.5 * (zmax + zmin);
    }
    
    /**
     * gets the center of the y interval.
     * @return double
     */
    public double getYCenter() {
      return 0.5 * (ymax + ymin);
    }
    
    /**
     * gets the size of the z interval.
     * @return double
     */
    public double getZWidth() {
      return zmax - zmin;
    }
    
    /**
     * gets the size of the y interval.
     * @return double
     */
    public double getYWidth() {
      return ymax - ymin;
    }
    
    /**
     * include (<code>x,y,z</code>) in range.
     * @param x double
     * @param y double
     * @param z double
     * @return Range3D.Double (this)
     */
    public Range3D.Double include(double x, double y, double z) {
      if (x < this.xmin)
	this.xmin = x;
      if (x > this.xmax)
	this.xmax = x;
      if (y < this.ymin)
	this.ymin = y;
      if (y > this.ymax)
	this.ymax = y;
      if (z < this.zmin)
	this.zmin = z;
      if (z > this.zmax)
	this.zmax = z;
      return this;
    }
    
    /**
     * include point p in range.
     * @param p Point3d
     * @return Range3D.Double (this)
     */
    public Range3D.Double include(Point3d p) {
      return include(p.x, p.y, p.z);
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range3D
     * @return Range3D.Double
     */
    public Range3D.Double include(Range3D range) {
      return include(
	  range.getXMinAsDouble(),
	  range.getXMaxAsDouble(),
	  range.getYMinAsDouble(),
	  range.getYMaxAsDouble(),
	  range.getZMinAsDouble(),
	  range.getZMaxAsDouble());
    }
    
    /**
     * check for NaNs
     * @return boolean
     */
    public boolean isValid() {
      return (xmin == xmin && xmax == xmax && ymin == ymin && ymax == ymax && zmin == zmin && zmax == zmax);
    }
    
    /**
     * set range to
     * [min(<code>x0,x1</code>), max(<code>x0,x1</code>)],
     * [min(<code>y0,y1</code>), max(<code>y0,y1</code>)]
     * [min(<code>z0,z1</code>), max(<code>z0,z1</code>)]
     * @param x0 double 
     * @param x1 double
     * @param y0 double 
     * @param y1 double
     * @param z0 double 
     * @param z1 double
     */
    public void set(double x0, double x1, double y0, double y1, double z0, double z1) {
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
      if (y0 < y1) {
	this.ymin = y0;
	this.ymax = y1;
      } else {
	this.ymin = y1;
	this.ymax = y0;
      }
      if (z0 < z1) {
	this.zmin = z0;
	this.zmax = z1;
      } else {
	this.zmin = z1;
	this.zmax = z0;
      }
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range3D
     */
    public void set(Range3D r) {
      this.xmin = r.getXMinAsDouble();
      this.xmax = r.getXMaxAsDouble();
      this.ymin = r.getYMinAsDouble();
      this.ymax = r.getYMaxAsDouble();
      this.zmin = r.getZMinAsDouble();
      this.zmax = r.getZMaxAsDouble();
    }
    
    /**
     * sets range to center point and width.
     * @param xcenter double
     * @param xwidth double
     * @param ycenter double
     * @param ywidth double
     * @param zcenter double
     * @param zwidth double
     */
    public void setCenterAndWidth(
	double xcenter,
	double xwidth,
	double ycenter,
	double ywidth,
	double zcenter,
	double zwidth) {
      this.xmin = xcenter - 0.5 * xwidth;
      this.xmax = xcenter + 0.5 * xwidth;
      this.ymin = ycenter - 0.5 * ywidth;
      this.ymax = ycenter + 0.5 * ywidth;
      this.zmin = zcenter - 0.5 * zwidth;
      this.zmax = zcenter + 0.5 * zwidth;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return new StringBuilder()
      .append('[')
      .append(xmin)
      .append(',')
      .append(xmax)
      .append("],[")
      .append(ymin)
      .append(',')
      .append(ymax)
      .append("],[")
      .append(zmin)
      .append(',')
      .append(zmax)
      .append(']')
      .toString();
    }
    
    /**
     * translate range by <code>xamount,yamount,zamount</code>
     * @param xamount double
     * @param yamount double
     * @param zamount double
     */
    public void translate(double xamount, double yamount, double zamount) {
      this.xmin += xamount;
      this.xmax += xamount;
      this.ymin += yamount;
      this.ymax += yamount;
      this.zmin += zamount;
      this.zmax += zamount;
    }
    
    /**
     * translate range by vector <code>v</code>
     * @param v Vector3d
     */
    public void translate(Vector3d v) {
      this.xmin += v.x;
      this.xmax += v.x;
      this.ymin += v.y;
      this.ymax += v.y;
      this.zmin += v.z;
      this.zmax += v.z;
    }
    
    /**
     * gets union of <code>(xvalue,yvalue,zvalue)</code> with <code>this</code> range.
     * @param xvalue double
     * @param yvalue double
     * @param zvalue double
     * @return Range3D.Double
     */
    public Range3D.Double union(double xvalue, double yvalue, double zvalue) {
      Range3D.Double range = (Range3D.Double) clone();
      if (xvalue < range.xmin)
	range.xmin = xvalue;
      if (xvalue > range.xmax)
	range.xmax = xvalue;
      if (yvalue < range.ymin)
	range.ymin = yvalue;
      if (yvalue > range.ymax)
	range.ymax = yvalue;
      if (zvalue < range.zmin)
	range.zmin = zvalue;
      if (zvalue > range.zmax)
	range.zmax = zvalue;
      return range;
    }
    
    /**
     * gets union of point <code>p</code> with <code>this</code> range.
     * @param p Point3d
     * @return Range3D.Double
     */
    public Range3D.Double union(Point3d p) {
      return union(p.x, p.y, p.z);
    }
    
    /**
     * gets union of [<code>xmin,xmax,ymin,ymax,zmin,zmax</code> ] with <code>this</code> range.
     * @param xmin double
     * @param xmax double
     * @param ymin double
     * @param ymax double
     * @param zmin double
     * @param zmax double
     * @return Range3D.Double
     */
    public Range3D.Double union(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
      Range3D.Double range = (Range3D.Double) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      if (ymin < range.ymin)
	range.ymin = ymin;
      if (ymax > range.ymax)
	range.ymax = ymax;
      if (zmin < range.zmin)
	range.zmin = zmin;
      if (zmax > range.zmax)
	range.zmax = zmax;
      return range;
    }
    
    /**
     * includes [<code>xmin,xmax,ymin,ymax,zmin,zmax</code> ] in range.
     * @param xmin double
     * @param xmax double
     * @param ymin double
     * @param ymax double
     * @param zmin double
     * @param zmax double
     * @return Range3D.Double (this)
     */
    public Range3D.Double include(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      if (ymin < this.ymin)
	this.ymin = ymin;
      if (ymax > this.ymax)
	this.ymax = ymax;
      if (zmin < this.zmin)
	this.zmin = zmin;
      if (zmax > this.zmax)
	this.zmax = zmax;
      return this;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range3D
     * @return Range3D.Double
     */
    public Range3D.Double union(Range3D range) {
      return union(
	  range.getXMinAsDouble(),
	  range.getXMaxAsDouble(),
	  range.getYMinAsDouble(),
	  range.getYMaxAsDouble(),
	  range.getZMinAsDouble(),
	  range.getZMaxAsDouble());
    }
  }
  
  /**
   * 3D data range.
   * 
   * @author Thomas Weber
   */
  public static class Float
    extends Range3D
    implements Range.Float, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = -5479043455846444112L;

    /** x minimum value */
    private float xmin;

    /** x maximum value */
    private float xmax;

    /** y minimum value */
    private float ymin;

    /** y maximum value */
    private float ymax;

    /** z minimum value */
    private float zmin;

    /** z maximum value */
    private float zmax;

    /**
     * default constructor.
     */
    public Float() {
      super();
      this.xmin = java.lang.Float.NaN;
      this.xmax = java.lang.Float.NaN;
      this.ymin = java.lang.Float.NaN;
      this.ymax = java.lang.Float.NaN;
      this.zmin = java.lang.Float.NaN;
      this.zmax = java.lang.Float.NaN;
    }
    
    /**
     * copy constructor.
     */
    public Float(Range3D r) {
      super();
      this.xmin = (float) r.getXMinAsDouble();
      this.xmax = (float) r.getXMaxAsDouble();
      this.ymin = (float) r.getYMinAsDouble();
      this.ymax = (float) r.getYMaxAsDouble();
      this.zmin = (float) r.getZMinAsDouble();
      this.zmax = (float) r.getZMaxAsDouble();
    }
    
    /**
     * create range.
     * @param xrange Range1D
     * @param yrange Range1D
     * @param zrange Range1D
     */
    public Float(Range1D xrange, Range1D yrange, Range1D zrange) {
      super();
      this.xmin = (float) xrange.getXMinAsDouble();
      this.xmax = (float) xrange.getXMaxAsDouble();
      this.ymin = (float) yrange.getXMinAsDouble();
      this.ymax = (float) yrange.getXMaxAsDouble();
      this.zmin = (float) zrange.getXMinAsDouble();
      this.zmax = (float) zrange.getXMaxAsDouble();
    }
    
    /**
     * set range.
     * @param xrange Range1D
     * @param yrange Range1D
     * @param zrange Range1D
     */
    public void set(Range1D xrange, Range1D yrange, Range1D zrange) {
      this.xmin = (float) xrange.getXMinAsDouble();
      this.xmax = (float) xrange.getXMaxAsDouble();
      this.ymin = (float) yrange.getXMinAsDouble();
      this.ymax = (float) yrange.getXMaxAsDouble();
      this.zmin = (float) zrange.getXMinAsDouble();
      this.zmax = (float) zrange.getXMaxAsDouble();
    }
    
    /**
     * create range.
     * @param xrange Range1D.Float
     * @param yrange Range1D.Float
     * @param zrange Range1D.Float
     */
    public Float(Range1D.Float xrange, Range1D.Float yrange, Range1D.Float zrange) {
      super();
      this.xmin = xrange.getXMin();
      this.xmax = xrange.getXMax();
      this.ymin = yrange.getXMin();
      this.ymax = yrange.getXMax();
      this.zmin = zrange.getXMin();
      this.zmax = zrange.getXMax();
    }
    
    /**
     * set range.
     * @param xrange Range1D.Float
     * @param yrange Range1D.Float
     * @param zrange Range1D.Float
     */
    public void set(Range1D.Float xrange, Range1D.Float yrange, Range1D.Float zrange) {
      this.xmin = xrange.getXMin();
      this.xmax = xrange.getXMax();
      this.ymin = yrange.getXMin();
      this.ymax = yrange.getXMax();
      this.zmin = zrange.getXMin();
      this.zmax = zrange.getXMax();
    }
    
    /**
     * copy constructor
     */
    public Float(Range3D.Float r) {
      super();
      this.xmin = r.xmin;
      this.xmax = r.xmax;
      this.ymin = r.ymin;
      this.ymax = r.ymax;
      this.zmin = r.zmin;
      this.zmax = r.zmax;
    }
    
    /**
     * create range 
     * [min(<code>x0,x1</code>), max(<code>x0,x1</code>)],
     * [min(<code>y0,y1</code>), max(<code>y0,y1</code>)]
     * [min(<code>z0,z1</code>), max(<code>z0,z1</code>)]
     * @param x0 float 
     * @param x1 float
     * @param y0 float 
     * @param y1 float
     * @param z0 float 
     * @param z1 float
     */
    public Float(float x0, float x1, float y0, float y1, float z0, float z1) {
      super();
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
      if (y0 < y1) {
	this.ymin = y0;
	this.ymax = y1;
      } else {
	this.ymin = y1;
	this.ymax = y0;
      }
      if (z0 < z1) {
	this.zmin = z0;
	this.zmax = z1;
      } else {
	this.zmin = z1;
	this.zmax = y0;
      }
    }
    
    /**
     * clip range <code>x0,x1,y0,y1,z0,z1</code>. return <code>null</code> if range is outside or on border
     * @param x0 float
     * @param x1 float
     * @param y0 float
     * @param y1 float
     * @param z0 float
     * @param z1 float
     * @return Range3D.Float
     */
    public Range3D.Float clip(float x0, float x1, float y0, float y1, float z0, float z1) {
      Range3D.Float nr = new Float(x0, x1, y0, y1, z0, z1);
      if (nr.xmin < xmin) {
	if (nr.xmax <= xmin)
	  return null;
	else
	  nr.xmin = xmin;
      }
      if (nr.xmax > xmax) {
	if (nr.xmin >= xmax)
	  return null;
	else
	  nr.xmax = xmax;
      }
      if (nr.ymin < ymin) {
	if (nr.ymax <= ymin)
	  return null;
	else
	  nr.ymin = ymin;
      }
      if (nr.ymax > ymax) {
	if (nr.ymin >= ymax)
	  return null;
	else
	  nr.ymax = ymax;
      }
      if (nr.zmin < zmin) {
	if (nr.zmax <= zmin)
	  return null;
	else
	  nr.zmin = zmin;
      }
      if (nr.zmax > zmax) {
	if (nr.zmin >= zmax)
	  return null;
	else
	  nr.zmax = zmax;
      }
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @param r Range3D
     * @return Range3D.Float
     */
    public Range3D.Float clip(Range3D r) {
      Range3D.Float nr = new Range3D.Float(r);
      if (nr.xmin < xmin) {
	if (nr.xmax <= xmin)
	  return null;
	else
	  nr.xmin = xmin;
      }
      if (nr.xmax > xmax) {
	if (nr.xmin >= xmax)
	  return null;
	else
	  nr.xmax = xmax;
      }
      if (nr.ymin < ymin) {
	if (nr.ymax <= ymin)
	  return null;
	else
	  nr.ymin = ymin;
      }
      if (nr.ymax > ymax) {
	if (nr.ymin >= ymax)
	  return null;
	else
	  nr.ymax = ymax;
      }
      if (nr.zmin < zmin) {
	if (nr.zmax <= zmin)
	  return null;
	else
	  nr.zmin = zmin;
      }
      if (nr.zmax > zmax) {
	if (nr.zmin >= zmax)
	  return null;
	else
	  nr.zmax = zmax;
      }
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range3D.Float range = null;
      try {
	range = (Range3D.Float) super.clone();
      } catch (CloneNotSupportedException e) {
      }
      return range;
    }
    
    /**
     * compare ranges for equality.
     * @param obj Object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Range3D) {
	Range3D r = (Range3D) obj;
	if (this == r
	    || (Math.abs(r.getXMinAsDouble() - xmin) < FLOAT_EPS
		&& Math.abs(r.getXMaxAsDouble() - xmax) < FLOAT_EPS
		&& Math.abs(r.getYMinAsDouble() - ymin) < FLOAT_EPS
		&& Math.abs(r.getYMaxAsDouble() - ymax) < FLOAT_EPS
		&& Math.abs(r.getZMinAsDouble() - zmin) < FLOAT_EPS
		&& Math.abs(r.getZMaxAsDouble() - zmax) < FLOAT_EPS))
	  return true;
      }
      return false;
    }
    
    /**
     * compare ranges for equality.
     * @param r Range3D.Float
     * @return boolean
     */
    public boolean equals(Range3D.Float r) {
      if (this == r
	  || (Math.abs(r.xmin - xmin) < FLOAT_EPS
	      && Math.abs(r.xmax - xmax) < FLOAT_EPS
	      && Math.abs(r.ymin - ymin) < FLOAT_EPS
	      && Math.abs(r.ymax - ymax) < FLOAT_EPS
	      && Math.abs(r.zmin - zmin) < FLOAT_EPS
	      && Math.abs(r.zmax - zmax) < FLOAT_EPS))
	return true;
      return false;
    }
    
    /**
     * extend range by <code>xamount, yamount, zamount</code> on both sides.
     * @param xamount float
     * @param yamount float
     * @param zamount float
     */
    public void extend(float xamount, float yamount, float zamount) {
      this.xmin -= xamount;
      this.xmax += xamount;
      this.ymin -= yamount;
      this.ymax += yamount;
      this.zmin -= zamount;
      this.zmax += zamount;
    }
    
    /**
     * gets the max x value of the range as a double
     * @return double
     */
    @Override
    public double getXMaxAsDouble() {
      return xmax;
    }
    
    /**
     * gets the max z value of the range as a double
     * @return double
     */
    @Override
    public double getZMaxAsDouble() {
      return zmax;
    }
    
    /**
     * gets the max y value of the range as a double
     * @return double
     */
    @Override
    public double getYMaxAsDouble() {
      return ymax;
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getCenter(int dim) {
      if (dim == 0)
	return 0.5f * (xmax + xmin);
      else if (dim == 1)
	return 0.5f * (ymax + ymin);
      else if (dim == 2)
	return 0.5f * (zmax + zmin);
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return Range1D.Float
     */
    public Range1D.Float getRange(int dim) {
      if (dim == 0)
	return new Range1D.Float(xmin, xmax);
      else if (dim == 1)
	return new Range1D.Float(ymin, ymax);
      else if (dim == 2)
	return new Range1D.Float(zmin, zmax);
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the maximum in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getMax(int dim) {
      if (dim == 0)
	return xmax;
      else if (dim == 1)
	return ymax;
      else if (dim == 2)
	return zmax;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the min x value of the range as a double
     * @return double
     */
    @Override
    public double getXMinAsDouble() {
      return xmin;
    }
    
    /**
     * gets the min y value of the range as a double
     * @return double
     */
    @Override
    public double getYMinAsDouble() {
      return ymin;
    }
    
    /**
     * gets the min z value of the range as a double
     * @return double
     */
    @Override
    public double getZMinAsDouble() {
      return zmin;
    }
    
    /**
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getWidth(int dim) {
      if (dim == 0)
	return xmax - xmin;
      else if (dim == 1)
	return ymax - ymin;
      else if (dim == 2)
	return zmax - zmin;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the minimum in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getMin(int dim) {
      if (dim == 0)
	return xmin;
      else if (dim == 1)
	return ymin;
      else if (dim == 2)
	return zmin;
      else
	throw new IndexOutOfBoundsException();
    }
    
    /**
     * gets the max x value of the range
     * @return float
     */
    public float getXMax() {
      return xmax;
    }
    
    /**
     * gets the max z value of the range
     * @return float
     */
    public float getZMax() {
      return zmax;
    }
    
    /**
     * gets the max y value of the range
     * @return float
     */
    public float getYMax() {
      return ymax;
    }
    
    /**
     * gets the min x value of the range
     * @return float
     */
    public float getXMin() {
      return xmin;
    }
    
    /**
     * gets the x range
     * @return Range1D.Float
     */
    public Range1D.Float getXRange() {
      return new Range1D.Float(xmin, xmax);
    }
    
    /**
     * gets the z range
     * @return Range1D.Float
     */
    public Range1D.Float getZRange() {
      return new Range1D.Float(zmin, zmax);
    }
    
    /**
     * gets the y range
     * @return Range1D.Float
     */
    public Range1D.Float getYRange() {
      return new Range1D.Float(ymin, ymax);
    }
    
    /**
     * gets the min z value of the range
     * @return float
     */
    public float getZMin() {
      return zmin;
    }
    
    /**
     * gets the min y value of the range
     * @return float
     */
    public float getYMin() {
      return ymin;
    }
    
    /**
     * gets the size of the x interval.
     * @return float
     */
    public float getXWidth() {
      return xmax - xmin;
    }
    
    /**
     * gets the size of the x interval.
     * @return float
     */
    public float getXCenter() {
      return 0.5f * (xmax + xmin);
    }
    
    /**
     * gets the size of the z interval.
     * @return float
     */
    public float getZCenter() {
      return 0.5f * (zmax + zmin);
    }
    
    /**
     * gets the size of the y interval.
     * @return float
     */
    public float getYCenter() {
      return 0.5f * (ymax + ymin);
    }
    
    /**
     * gets the size of the z interval.
     * @return float
     */
    public float getZWidth() {
      return zmax - zmin;
    }
    
    /**
     * gets the size of the y interval.
     * @return float
     */
    public float getYWidth() {
      return ymax - ymin;
    }
    
    /**
     * include (<code>x,y,z</code>) in range.
     * @param x float
     * @param y float
     * @param z float
     * @return Range3D.Float (this)
     */
    public Range3D.Float include(float x, float y, float z) {
      if (x < this.xmin)
	this.xmin = x;
      if (x > this.xmax)
	this.xmax = x;
      if (y < this.ymin)
	this.ymin = y;
      if (y > this.ymax)
	this.ymax = y;
      if (z < this.zmin)
	this.zmin = z;
      if (z > this.zmax)
	this.zmax = z;
      return this;
    }
    
    /**
     * include (<code>p</code>) in range.
     * @param p Point3f
     * @return Range3D.Float (this)
     */
    public Range3D.Float include(Point3f p) {
      return include(p.x, p.y, p.z);
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range3D
     * @return Range3D.Float
     */
    public Range3D.Float include(Range3D range) {
      return include(
	  (float) range.getXMinAsDouble(),
	  (float) range.getXMaxAsDouble(),
	  (float) range.getYMinAsDouble(),
	  (float) range.getYMaxAsDouble(),
	  (float) range.getZMinAsDouble(),
	  (float) range.getZMaxAsDouble());
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range3D.Float
     * @return Range3D.Float
     */
    public Range3D.Float include(Range3D.Float range) {
      return include(
	  range.getXMin(),
	  range.getXMax(),
	  range.getYMin(),
	  range.getYMax(),
	  range.getZMin(),
	  range.getZMax());
    }
    
    /**
     * check for NaNs
     * @return boolean
     */
    public boolean isValid() {
      return (xmin == xmin && xmax == xmax && ymin == ymin && ymax == ymax && zmin == zmin && zmax == zmax);
    }
    
    /**
     * set range to
     * [min(<code>x0,x1</code>), max(<code>x0,x1</code>)],
     * [min(<code>y0,y1</code>), max(<code>y0,y1</code>)]
     * [min(<code>z0,z1</code>), max(<code>z0,z1</code>)]
     * @param x0 float 
     * @param x1 float
     * @param y0 float 
     * @param y1 float
     * @param z0 float 
     * @param z1 float
     */
    public void set(float x0, float x1, float y0, float y1, float z0, float z1) {
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
      if (y0 < y1) {
	this.ymin = y0;
	this.ymax = y1;
      } else {
	this.ymin = y1;
	this.ymax = y0;
      }
      if (z0 < z1) {
	this.zmin = z0;
	this.zmax = z1;
      } else {
	this.zmin = z1;
	this.zmax = z0;
      }
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range3D.Float
     */
    public void set(Range3D.Float r) {
      this.xmin = r.getXMin();
      this.xmax = r.getXMax();
      this.ymin = r.getYMin();
      this.ymax = r.getYMax();
      this.zmin = r.getZMin();
      this.zmax = r.getZMax();
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range3D
     */
    public void set(Range3D r) {
      this.xmin = (float) r.getXMinAsDouble();
      this.xmax = (float) r.getXMaxAsDouble();
      this.ymin = (float) r.getYMinAsDouble();
      this.ymax = (float) r.getYMaxAsDouble();
      this.zmin = (float) r.getZMinAsDouble();
      this.zmax = (float) r.getZMaxAsDouble();
    }
    
    /**
     * sets range to center point and width.
     * @param xcenter float
     * @param xwidth float
     * @param ycenter float
     * @param ywidth float
     * @param zcenter float
     * @param zwidth float
     */
    public void setCenterAndWidth(
	float xcenter,
	float xwidth,
	float ycenter,
	float ywidth,
	float zcenter,
	float zwidth) {
      this.xmin = xcenter - 0.5f * xwidth;
      this.xmax = xcenter + 0.5f * xwidth;
      this.ymin = ycenter - 0.5f * ywidth;
      this.ymax = ycenter + 0.5f * ywidth;
      this.zmin = zcenter - 0.5f * zwidth;
      this.zmax = zcenter + 0.5f * zwidth;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return new StringBuilder()
      .append('[')
      .append(xmin)
      .append(',')
      .append(xmax)
      .append("],[")
      .append(ymin)
      .append(',')
      .append(ymax)
      .append("],[")
      .append(zmin)
      .append(',')
      .append(zmax)
      .append(']')
      .toString();
    }
    
    /**
     * translate range by <code>xamount,yamount,zamount</code>
     * @param xamount float
     * @param yamount float
     * @param zamount float
     */
    public void translate(float xamount, float yamount, float zamount) {
      xmin += xamount;
      xmax += xamount;
      ymin += yamount;
      ymax += yamount;
      zmin += zamount;
      zmax += zamount;
    }
    
    /**
     * translate range by vector <code>v</code>
     * @param v Vector3f
     */
    public void translate(Vector3f v) {
      this.xmin += v.x;
      this.xmax += v.x;
      this.ymin += v.y;
      this.ymax += v.y;
      this.zmin += v.z;
      this.zmax += v.z;
    }
    
    /**
     * gets union of <code>(xvalue,yvalue,zvalue)</code> with <code>this</code> range.
     * @param xvalue float
     * @param yvalue float
     * @param zvalue float
     * @return Range3D.Float
     */
    public Range3D.Float union(float xvalue, float yvalue, float zvalue) {
      Range3D.Float range = (Range3D.Float) clone();
      if (xvalue < range.xmin)
	range.xmin = xvalue;
      if (xvalue > range.xmax)
	range.xmax = xvalue;
      if (yvalue < range.ymin)
	range.ymin = yvalue;
      if (yvalue > range.ymax)
	range.ymax = yvalue;
      if (zvalue < range.zmin)
	range.zmin = zvalue;
      if (zvalue > range.zmax)
	range.zmax = zvalue;
      return range;
    }
    
    /**
     * gets union of point <code>p</code> with <code>this</code> range.
     * @param p Point3f
     * @return Range3D.Float
     */
    public Range3D.Float union(Point3f p) {
      return union(p.x, p.y, p.z);
    }
    
    /**
     * gets union of [<code>xmin,xmax,ymin,ymax,zmin,zmax</code> ] with <code>this</code> range.
     * @param xmin float
     * @param xmax float
     * @param ymin float
     * @param ymax float
     * @param zmin float
     * @param zmax float
     * @return Range3D.Float
     */
    public Range3D.Float union(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax) {
      Range3D.Float range = (Range3D.Float) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      if (ymin < range.ymin)
	range.ymin = ymin;
      if (ymax > range.ymax)
	range.ymax = ymax;
      if (zmin < range.zmin)
	range.zmin = zmin;
      if (zmax > range.zmax)
	range.zmax = zmax;
      return range;
    }
    
    /**
     * includes [<code>xmin,xmax,ymin,ymax,zmin,zmax</code> ] in range.
     * @param xmin float
     * @param xmax float
     * @param ymin float
     * @param ymax float
     * @param zmin float
     * @param zmax float
     * @return Range3D.Float (this)
     */
    public Range3D.Float include(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      if (ymin < this.ymin)
	this.ymin = ymin;
      if (ymax > this.ymax)
	this.ymax = ymax;
      if (zmin < this.zmin)
	this.zmin = zmin;
      if (zmax > this.zmax)
	this.zmax = zmax;
      return this;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range3D
     * @return Range3D.Float
     */
    public Range3D.Float union(Range3D range) {
      return union(
	  (float) range.getXMinAsDouble(),
	  (float) range.getXMaxAsDouble(),
	  (float) range.getYMinAsDouble(),
	  (float) range.getYMaxAsDouble(),
	  (float) range.getZMinAsDouble(),
	  (float) range.getZMaxAsDouble());
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range3D.Float
     * @return Range3D.Float
     */
    public Range3D.Float union(Range3D.Float range) {
      return union(
	  range.getXMin(),
	  range.getXMax(),
	  range.getYMin(),
	  range.getYMax(),
	  range.getZMin(),
	  range.getZMax());
    }
  }

  /**
   * Range1D constructor comment.
   */
  protected Range3D() {
    super(2);
  }

  /**
   * check if range contains fully another range <code>r</code>.
   * @param r Range3D
   * @return boolean
   */
  public boolean contains(Range3D r) {
    return (
	r.getXMinAsDouble() >= getXMinAsDouble()
	&& r.getXMaxAsDouble() <= getXMaxAsDouble()
	&& r.getYMinAsDouble() >= getYMinAsDouble()
	&& r.getYMaxAsDouble() <= getYMaxAsDouble()
	&& r.getZMinAsDouble() >= getZMinAsDouble()
	&& r.getZMaxAsDouble() <= getZMaxAsDouble());
  }

  /**
   * check if range contains point <code>p</code>.
   * @param p Point3d
   * @return boolean
   */
  public boolean contains(Point3d p) {
    return (
	p.x >= getXMinAsDouble()
	&& p.x <= getXMaxAsDouble()
	&& p.y >= getYMinAsDouble()
	&& p.y <= getYMaxAsDouble()
	&& p.z >= getZMinAsDouble()
	&& p.z <= getZMaxAsDouble());
  }

  /**
   * gets the max x value of the range as a double
   * @return double
   */
  public abstract double getXMaxAsDouble();

  /**
   * gets the min x value of the range as a double
   * @return double
   */
  public abstract double getXMinAsDouble();

  /**
   * gets the max y value of the range as a double
   * @return double
   */
  public abstract double getYMaxAsDouble();

  /**
   * gets the min y value of the range as a double
   * @return double
   */
  public abstract double getYMinAsDouble();

  /**
   * gets the max z value of the range as a double
   * @return double
   */
  public abstract double getZMaxAsDouble();

  /**
   * gets the min z value of the range as a double
   * @return double
   */
  public abstract double getZMinAsDouble();
}
