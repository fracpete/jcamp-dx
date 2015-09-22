/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import javax.vecmath.Point2d;
import javax.vecmath.Point2f;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;

/**
 * base class for 2D data ranges.
 * 
 * @author Thomas Weber
 */
public abstract class Range2D
  extends Range {
  
  /** for serialization. */
  private static final long serialVersionUID = 2927117694454510932L;

  /**
   * 2D data range
   * @author Thomas Weber
   */
  public static class Int
    extends Range2D
    implements Range.Int, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = -3771688259177686692L;

    /** minimum x value */
    private int xmin;

    /** maximum x value */
    private int xmax;

    /** minimum y value */
    private int ymin;

    /** maximum y value */
    private int ymax;

    /**
     * default constructor.
     */
    public Int() {
      super();
      this.xmin = 0;
      this.xmax = 0;
      this.ymin = 0;
      this.ymax = 0;
    }
    
    /**
     * copy constructor.
     */
    public Int(Range2D.Int r) {
      super();
      this.xmin = r.xmin;
      this.xmax = r.xmax;
      this.ymin = r.ymin;
      this.ymax = r.ymax;
    }
    
    /**
     * create range
     * @param xrange Range1D.Int
     * @param yrange Range1D.Int
     */
    public Int(Range1D.Int xrange, Range1D.Int yrange) {
      super();
      this.xmin = xrange.getXMin();
      this.xmax = xrange.getXMax();
      this.ymin = yrange.getXMin();
      this.ymax = yrange.getXMax();
    }
    
    /**
     * set range
     * @param xrange Range1D.Int
     * @param yrange Range1D.Int
     */
    public void set(Range1D.Int xrange, Range1D.Int yrange) {
      this.xmin = xrange.getXMin();
      this.xmax = xrange.getXMax();
      this.ymin = yrange.getXMin();
      this.ymax = yrange.getXMax();
    }
    
    /**
     * create range 
     *	[min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)]
     *	[min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * @param x0 int 
     * @param x1 int
     * @param y0 int 
     * @param y1 int
     */
    public Int(int x0, int x1, int y0, int y1) {
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
    }
    
    /**
     * check if range contains value <code>(x,y)</code>.
     * @param x int
     * @param y int
     * @return boolean
     */
    public boolean contains(int x, int y) {
      return (x >= xmin && x <= xmax && y >= ymin && y <= ymax);
    }
    
    /**
     * clip range <code>x0,x1,y0,y1</code>. return <code>null</code> if range is outside or on border
     * @param x0 int
     * @param x1 int
     * @param y0 int
     * @param y1 int
     * @return Range2D.Int
     */
    public Range2D.Int clip(int x0, int x1, int y0, int y1) {
      Range2D.Int nr = new Int(x0, x1, y0, y1);
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
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @return Range2D.Int
     * @param r Range2D.Int
     */
    public Range2D.Int clip(Range2D.Int r) {
      Range2D.Int nr = new Range2D.Int(r);
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
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range2D.Int range = null;
      try {
	range = (Range2D.Int) super.clone();
      } catch (CloneNotSupportedException e) {
      }
      return range;
    }
    
    /**
     * compare ranges for equality.
     * @return boolean
     * @param obj Object
     */
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Range2D.Int) {
	Range2D.Int r = (Range2D.Int) obj;
	if (this == r || ((r.xmin == xmin) && (r.xmax == xmax) && (r.ymin == ymin) && (r.ymax == ymax)))
	  return true;
      }
      return false;
    }
    
    /**
     * extend range by <code>xamount,yamount</code> on both sides.
     * @param xamount int
     * @param yamount int
     */
    public void extend(int xamount, int yamount) {
      this.xmin -= xamount;
      this.xmax += xamount;
      this.ymin -= yamount;
      this.ymax += yamount;
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
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getWidth(int dim) {
      if (dim == 0)
	return (xmax - xmin);
      else if (dim == 1)
	return (ymax - ymin);
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
     * gets the y range
     * @return Range1D.Int
     */
    public Range1D.Int getYRange() {
      return new Range1D.Int(ymin, ymax);
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
     * gets the size of the x interval.
     * @return int
     */
    public int getXCenter() {
      return (xmax + xmin) / 2;
    }
    
    /**
     * gets the size of the y interval.
     * @return int
     */
    public int getYCenter() {
      return (ymax + ymin) / 2;
    }
    
    /**
     * gets the size of the y interval.
     * @return int
     */
    public int getYWidth() {
      return ymax - ymin;
    }
    
    /**
     * include <code>(x,y)</code> in range.
     * @param x int
     * @param y int
     * @return Range2D.Int (this)
     */
    public Range2D.Int include(int x, int y) {
      if (x < this.xmin)
	this.xmin = x;
      if (x > this.xmax)
	this.xmax = x;
      if (y < this.ymin)
	this.ymin = y;
      if (y > this.ymax)
	this.ymax = y;
      return this;
    }
    
    /**
     * include [<code>xmin,xmax</code> ],[<code>ymin,ymax</code> ] in range.
     * @param xmin int
     * @param xmax int
     * @param ymin int
     * @param ymax int
     * @return Range2D.Int (this)
     */
    public Range2D.Int include(int xmin, int xmax, int ymin, int ymax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      if (ymin < this.ymin)
	this.ymin = ymin;
      if (ymax > this.ymax)
	this.ymax = ymax;
      return this;
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range2D.Int
     * @return Range2D.Int
     */
    public Range2D.Int include(Range2D.Int range) {
      return include(range.getXMin(), range.getXMax(), range.getYMin(), range.getYMax());
    }
    
    /**
     * set range to 
     *	[min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)]
     *	[min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * @param x0 int 
     * @param x1 int
     * @param y0 int 
     * @param y1 int
     */
    public void set(int x0, int x1, int y0, int y1) {
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
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range2D
     */
    public void set(Range2D.Int r) {
      this.xmin = r.getXMin();
      this.xmax = r.getXMax();
      this.ymin = r.getYMin();
      this.ymax = r.getYMax();
    }
    
    /**
     * sets range to center point and width.
     * @param xcenter int
     * @param xwidth int
     * @param ycenter int
     * @param ywidth int
     */
    public void setCenterAndWidth(int xcenter, int xwidth, int ycenter, int ywidth) {
      this.xmin = xcenter - xwidth / 2;
      this.xmax = xcenter + xwidth / 2;
      this.ymin = ycenter - ywidth / 2;
      this.ymax = ycenter + ywidth / 2;
    }
    /**
     * 
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return "[" + xmin + "," + xmax + "],[" + ymin + "," + ymax + "]";
    }
    
    /**
     * translate range by <code>xamount,yamount</code>
     * @param xamount int
     * @param yamount int
     */
    public void translate(int xamount, int yamount) {
      this.xmin += xamount;
      this.xmax += xamount;
      this.ymin += yamount;
      this.ymax += yamount;
    }
    
    /**
     * gets union of <code>(x,y)</code> with <code>this</code> range.
     * @param xvalue int
     * @param yvalue int
     * @return Range2D.Int
     */
    public Range2D.Int union(int xvalue, int yvalue) {
      Range2D.Int range = (Range2D.Int) this.clone();
      if (xvalue < range.xmin)
	range.xmin = xvalue;
      if (xvalue > range.xmax)
	range.xmax = xvalue;
      if (yvalue < range.ymin)
	range.ymin = yvalue;
      if (yvalue > range.ymax)
	range.ymax = yvalue;
      return range;
    }
    
    /**
     * gets union of [<code>xmin,xmax,ymin,ymax</code> ] with <code>this</code> range.
     * @param xmin int
     * @param xmax int
     * @param ymin int
     * @param ymax int
     * @return Range2D.Int
     */
    public Range2D.Int union(int xmin, int xmax, int ymin, int ymax) {
      Range2D.Int range = (Range2D.Int) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      if (ymin < range.ymin)
	range.ymin = ymin;
      if (ymax > range.ymax)
	range.ymax = ymax;
      return range;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range2D.Int
     * @return Range2D.Int
     */
    public Range2D.Int union(Range2D.Int range) {
      return union(range.getXMin(), range.getXMax(), range.getYMin(), range.getYMax());
    }
  }
  
  /**
   * 2D data range.
   * 
   * @author Thomas Weber
   */
  public static class Double
    extends Range2D
    implements Range.Double, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = 3369803771308467437L;

    /** x minimum value */
    private double xmin;

    /** x maximum value */
    private double xmax;

    /** y minimum value */
    private double ymin;

    /** y maximum value */
    private double ymax;

    /**
     * default constructor.
     */
    public Double() {
      super();
      this.xmin = java.lang.Double.NaN;
      this.xmax = java.lang.Double.NaN;
      this.ymin = java.lang.Double.NaN;
      this.ymax = java.lang.Double.NaN;
    }
    
    /**
     * copy constructor.
     */
    public Double(Range2D r) {
      super();
      this.xmin = r.getXMinAsDouble();
      this.xmax = r.getXMaxAsDouble();
      this.ymin = r.getYMinAsDouble();
      this.ymax = r.getYMaxAsDouble();
    }
    
    /**
     * create range 
     * [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)],
     * [min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * @param x0 double 
     * @param x1 double
     * @param y0 double 
     * @param y1 double
     */
    public Double(double x0, double x1, double y0, double y1) {
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
    }
    
    /**
     * create range 
     * @param xrange Range1D
     * @param yrange Range1D
     */
    public Double(Range1D xrange, Range1D yrange) {
      this(
	  xrange.getXMinAsDouble(),
	  xrange.getXMaxAsDouble(),
	  yrange.getXMinAsDouble(),
	  yrange.getXMaxAsDouble());
    }
    
    /**
     * set range 
     * @param xrange Range1D
     * @param yrange Range1D
     */
    public void set(Range1D xrange, Range1D yrange) {
      set(xrange.getXMinAsDouble(), xrange.getXMaxAsDouble(), yrange.getXMinAsDouble(), yrange.getXMaxAsDouble());
    }
    
    /**
     * clip range <code>x0,x1,y0,y1</code>. return <code>null</code> if range is outside or on border
     * @param x0 double
     * @param x1 double
     * @param y0 double
     * @param y1 double
     * @return Range2D.Double
     */
    public Range2D.Double clip(double x0, double x1, double y0, double y1) {
      Range2D.Double nr = new Double(x0, x1, y0, y1);
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
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @param r Range2D
     * @return Range2D.Double
     */
    public Range2D.Double clip(Range2D r) {
      Range2D.Double nr = new Range2D.Double(r);
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
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range2D.Double range = null;
      try {
	range = (Range2D.Double) super.clone();
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
      if (obj instanceof Range2D) {
	Range2D r = (Range2D) obj;
	if (this == r
	    || (Math.abs(r.getXMinAsDouble() - xmin) < DOUBLE_EPS
		&& Math.abs(r.getXMaxAsDouble() - xmax) < DOUBLE_EPS
		&& Math.abs(r.getYMinAsDouble() - ymin) < DOUBLE_EPS
		&& Math.abs(r.getYMaxAsDouble() - ymax) < DOUBLE_EPS))
	  return true;
      }
      return false;
    }
    
    /**
     * extend range by <code>xamount,yamount</code> on both sides.
     * @param xamount double
     * @param yamount double
     */
    public void extend(double xamount, double yamount) {
      this.xmin -= xamount;
      this.xmax += xamount;
      this.ymin -= yamount;
      this.ymax += yamount;
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
     * gets the x range
     * @return Range1D.Double
     */
    public Range1D.Double getXRange() {
      return new Range1D.Double(xmin, xmax);
    }
    
    /**
     * gets the y range
     * @return Range1D.Double
     */
    public Range1D.Double getYRange() {
      return new Range1D.Double(ymin, ymax);
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
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getWidth(int dim) {
      if (dim == 0)
	return xmax - xmin;
      else if (dim == 1)
	return ymax - ymin;
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
     * gets the center of the y interval.
     * @return double
     */
    public double getYCenter() {
      return 0.5 * (ymax + ymin);
    }
    
    /**
     * gets the size of the y interval.
     * @return double
     */
    public double getYWidth() {
      return ymax - ymin;
    }
    
    /**
     * include (<code>x,y</code>) in range.
     * @param x double
     * @param y double
     * @return Range2D.Double (this)
     */
    public Range2D.Double include(double x, double y) {
      if (x < this.xmin)
	this.xmin = x;
      if (x > this.xmax)
	this.xmax = x;
      if (y < this.ymin)
	this.ymin = y;
      if (y > this.ymax)
	this.ymax = y;
      return this;
    }
    
    /**
     * include point p in range.
     * @param p Point2d
     * @return Range2D.Double (this)
     */
    public Range2D.Double include(Point2d p) {
      return include(p.x, p.y);
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range2D
     * @return Range2D.Double
     */
    public Range2D.Double include(Range2D range) {
      return include(
	  range.getXMinAsDouble(),
	  range.getXMaxAsDouble(),
	  range.getYMinAsDouble(),
	  range.getYMaxAsDouble());
    }
    
    /**
     * check for NaNs
     * @return boolean
     */
    public boolean isValid() {
      return (xmin == xmin && xmax == xmax && ymin == ymin && ymax == ymax);
    }
    
    /**
     * set range to
     * [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)],
     * [min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * @param x0 double 
     * @param x1 double
     * @param y0 double 
     * @param y1 double
     */
    public void set(double x0, double x1, double y0, double y1) {
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
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range2D
     */
    public void set(Range2D r) {
      this.xmin = r.getXMinAsDouble();
      this.xmax = r.getXMaxAsDouble();
      this.ymin = r.getYMinAsDouble();
      this.ymax = r.getYMaxAsDouble();
    }
    
    /**
     * sets range to center point and width.
     * @param xcenter double
     * @param xwidth double
     * @param ycenter double
     * @param ywidth double
     */
    public void setCenterAndWidth(double xcenter, double xwidth, double ycenter, double ywidth) {
      this.xmin = xcenter - 0.5 * xwidth;
      this.xmax = xcenter + 0.5 * xwidth;
      this.ymin = ycenter - 0.5 * ywidth;
      this.ymax = ycenter + 0.5 * ywidth;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return "[" + xmin + "," + xmax + "],[" + ymin + "," + ymax + "]";
    }
    
    /**
     * translate range by <code>xamount,yamount</code>
     * @param xamount double
     * @param yamount double
     */
    public void translate(double xamount, double yamount) {
      this.xmin += xamount;
      this.xmax += xamount;
      this.ymin += yamount;
      this.ymax += yamount;
    }
    
    /**
     * translate range by vector <code>v</code>
     * @param v Vector2d
     */
    public void translate(Vector2d v) {
      this.xmin += v.x;
      this.xmax += v.x;
      this.ymin += v.y;
      this.ymax += v.y;
    }
    
    /**
     * gets union of <code>(xvalue,yvalue)</code> with <code>this</code> range.
     * @param xvalue double
     * @param yvalue double
     * @return Range2D.Double
     */
    public Range2D.Double union(double xvalue, double yvalue) {
      Range2D.Double range = (Range2D.Double) clone();
      if (xvalue < range.xmin)
	range.xmin = xvalue;
      if (xvalue > range.xmax)
	range.xmax = xvalue;
      if (yvalue < range.ymin)
	range.ymin = yvalue;
      if (yvalue > range.ymax)
	range.ymax = yvalue;
      return range;
    }
    
    /**
     * gets union of point <code>p</code> with <code>this</code> range.
     * @param p Point2d
     * @return Range2D.Double
     */
    public Range2D.Double union(Point2d p) {
      return union(p.x, p.y);
    }
    
    /**
     * gets union of [<code>xmin,xmax,ymin,ymax</code> ] with <code>this</code> range.
     * @param xmin double
     * @param xmax double
     * @param ymin double
     * @param ymax double
     * @return Range2D.Double
     */
    public Range2D.Double union(double xmin, double xmax, double ymin, double ymax) {
      Range2D.Double range = (Range2D.Double) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      if (ymin < range.ymin)
	range.ymin = ymin;
      if (ymax > range.ymax)
	range.ymax = ymax;
      return range;
    }
    
    /**
     * includes [<code>xmin,xmax,ymin,ymax</code> ] in range.
     * @param xmin double
     * @param xmax double
     * @param ymin double
     * @param ymax double
     * @return Range2D.Double (this)
     */
    public Range2D.Double include(double xmin, double xmax, double ymin, double ymax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      if (ymin < this.ymin)
	this.ymin = ymin;
      if (ymax > this.ymax)
	this.ymax = ymax;
      return this;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range2D
     * @return Range2D.Double
     */
    public Range2D.Double union(Range2D range) {
      return union(
	  range.getXMinAsDouble(),
	  range.getXMaxAsDouble(),
	  range.getYMinAsDouble(),
	  range.getYMaxAsDouble());
    }
  }
  
  /**
   * 2D data range.
   * 
   * @author Thomas Weber
   */
  public static class Float
    extends Range2D
    implements Range.Float, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = 7906304104729707229L;

    /** x minimum value */
    private float xmin;

    /** x maximum value */
    private float xmax;

    /** y minimum value */
    private float ymin;

    /** y maximum value */
    private float ymax;

    /**
     * default constructor.
     */
    public Float() {
      super();
      this.xmin = java.lang.Float.NaN;
      this.xmax = java.lang.Float.NaN;
      this.ymin = java.lang.Float.NaN;
      this.ymax = java.lang.Float.NaN;
    }
    
    /**
     * copy constructor.
     */
    public Float(Range2D r) {
      super();
      this.xmin = (float) r.getXMinAsDouble();
      this.xmax = (float) r.getXMaxAsDouble();
      this.ymin = (float) r.getYMinAsDouble();
      this.ymax = (float) r.getYMaxAsDouble();
    }
    
    /**
     * create range 
     * @param xrange Range1D
     * @param yrange Range1D
     */
    public Float(Range1D xrange, Range1D yrange) {
      this(
	  (float) xrange.getXMinAsDouble(),
	  (float) xrange.getXMaxAsDouble(),
	  (float) yrange.getXMinAsDouble(),
	  (float) yrange.getXMaxAsDouble());
    }
    
    /**
     * set range 
     * @param xrange Range1D
     * @param yrange Range1D
     */
    public void set(Range1D xrange, Range1D yrange) {
      set(
	  (float) xrange.getXMinAsDouble(),
	  (float) xrange.getXMaxAsDouble(),
	  (float) yrange.getXMinAsDouble(),
	  (float) yrange.getXMaxAsDouble());
    }
    
    /**
     * create range 
     * @param xrange Range1D.Float
     * @param yrange Range1D.Float
     */
    public Float(Range1D.Float xrange, Range1D.Float yrange) {
      this(xrange.getXMin(), xrange.getXMax(), yrange.getXMin(), yrange.getXMax());
    }
    
    /**
     * set range 
     * @param xrange Range1D.Float
     * @param yrange Range1D.Float
     */
    public void set(Range1D.Float xrange, Range1D.Float yrange) {
      set(xrange.getXMin(), xrange.getXMax(), yrange.getXMin(), yrange.getXMax());
    }

    /**
     * copy constructor.
     */
    public Float(Range2D.Float r) {
      super();
      this.xmin = r.xmin;
      this.xmax = r.xmax;
      this.ymin = r.ymin;
      this.ymax = r.ymax;
    }
    
    /**
     * create range 
     * [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)],
     * [min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * @param x0 float 
     * @param x1 float
     * @param y0 float 
     * @param y1 float
     */
    public Float(float x0, float x1, float y0, float y1) {
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
    }
    
    /**
     * clip range <code>x0,x1,y0,y1</code>. return <code>null</code> if range is outside or on border
     * @param x0 float
     * @param x1 float
     * @param y0 float
     * @param y1 float
     * @return Range2D.Float
     */
    public Range2D.Float clip(float x0, float x1, float y0, float y1) {
      Range2D.Float nr = new Float(x0, x1, y0, y1);
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
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @param r Range2D
     * @return Range2D.Float
     */
    public Range2D.Float clip(Range2D r) {
      Range2D.Float nr = new Range2D.Float(r);
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
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range2D.Float range = null;
      try {
	range = (Range2D.Float) super.clone();
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
      if (obj instanceof Range2D) {
	Range2D r = (Range2D) obj;
	if (this == r
	    || (Math.abs(r.getXMinAsDouble() - xmin) < FLOAT_EPS
		&& Math.abs(r.getXMaxAsDouble() - xmax) < FLOAT_EPS
		&& Math.abs(r.getYMinAsDouble() - ymin) < FLOAT_EPS
		&& Math.abs(r.getYMaxAsDouble() - ymax) < FLOAT_EPS))
	  return true;
      }
      return false;
    }
    
    /**
     * compare ranges for equality.
     * @param r Range2D.Float
     * @return boolean
     */
    public boolean equals(Range2D.Float r) {
      if (this == r
	  || (Math.abs(r.xmin - xmin) < FLOAT_EPS
	      && Math.abs(r.xmax - xmax) < FLOAT_EPS
	      && Math.abs(r.ymin - ymin) < FLOAT_EPS
	      && Math.abs(r.ymax - ymax) < FLOAT_EPS))
	return true;
      return false;
    }
    
    /**
     * extend range by <code>amount</code> on both sides.
     * @param xamount float
     * @param yamount float
     */
    public void extend(float xamount, float yamount) {
      this.xmin -= xamount;
      this.xmax += xamount;
      this.ymin -= yamount;
      this.ymax += yamount;
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
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getWidth(int dim) {
      if (dim == 0)
	return xmax - xmin;
      else if (dim == 1)
	return ymax - ymin;
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
     * gets the y range
     * @return Range1D.Float
     */
    public Range1D.Float getYRange() {
      return new Range1D.Float(ymin, ymax);
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
     * gets the size of the y interval.
     * @return float
     */
    public float getYCenter() {
      return 0.5f * (ymax + ymin);
    }
    
    /**
     * gets the size of the y interval.
     * @return float
     */
    public float getYWidth() {
      return ymax - ymin;
    }
    
    /**
     * include (<code>x,y</code>) in range.
     * @param x float
     * @param y float
     * @return Range2D.Float (this)
     */
    public Range2D.Float include(float x, float y) {
      if (x < this.xmin)
	this.xmin = x;
      if (x > this.xmax)
	this.xmax = x;
      if (y < this.ymin)
	this.ymin = y;
      if (y > this.ymax)
	this.ymax = y;
      return this;
    }
    
    /**
     * include point <code>p</code> in range.
     * @param p Point2f
     * @return Range2D.Float (this)
     */
    public Range2D.Float include(Point2f p) {
      return include(p.x, p.y);
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range2D
     * @return Range2D.Float
     */
    public Range2D.Float include(Range2D range) {
      return include(
	  (float) range.getXMinAsDouble(),
	  (float) range.getXMaxAsDouble(),
	  (float) range.getYMinAsDouble(),
	  (float) range.getYMaxAsDouble());
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range2D.Float
     * @return Range2D.Float
     */
    public Range2D.Float include(Range2D.Float range) {
      return include(range.getXMin(), range.getXMax(), range.getYMin(), range.getYMax());
    }
    
    /**
     * check for NaNs
     * @return boolean
     */
    public boolean isValid() {
      return (xmin == xmin && xmax == xmax && ymin == ymin && ymax == ymax);
    }
    
    /**
     * set range to
     * [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)],
     * [min(<code>y0</code>,<code>y1</code>), max(<code>y0</code>,<code>y1</code>)]
     * @param x0 float 
     * @param x1 float
     * @param y0 float 
     * @param y1 float
     */
    public void set(float x0, float x1, float y0, float y1) {
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
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range2D
     */
    public void set(Range2D r) {
      this.xmin = (float) r.getXMinAsDouble();
      this.xmax = (float) r.getXMaxAsDouble();
      this.ymin = (float) r.getYMinAsDouble();
      this.ymax = (float) r.getYMaxAsDouble();
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range2D.Float
     */
    public void set(Range2D.Float r) {
      this.xmin = r.getXMin();
      this.xmax = r.getXMax();
      this.ymin = r.getYMin();
      this.ymax = r.getYMax();
    }
    
    /**
     * sets range to center point and width.
     * @param xcenter float
     * @param xwidth float
     * @param ycenter float
     * @param ywidth float
     */
    public void setCenterAndWidth(float xcenter, float xwidth, float ycenter, float ywidth) {
      this.xmin = xcenter - 0.5f * xwidth;
      this.xmax = xcenter + 0.5f * xwidth;
      this.ymin = ycenter - 0.5f * ywidth;
      this.ymax = ycenter + 0.5f * ywidth;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return "[" + xmin + "," + xmax + "],[" + ymin + "," + ymax + "]";
    }
    
    /**
     * translate range by <code>xamount,yamount</code>
     * @param xamount float
     * @param yamount float
     */
    public void translate(float xamount, float yamount) {
      xmin += xamount;
      xmax += xamount;
      ymin += yamount;
      ymax += yamount;
    }
    
    /**
     * translate range by vector <code>v</code>
     * @param v Vector2f
     */
    public void translate(Vector2f v) {
      xmin += v.x;
      xmax += v.x;
      ymin += v.y;
      ymax += v.y;
    }
    
    /**
     * gets union of <code>(xvalue,yvalue)</code> with <code>this</code> range.
     * @param xvalue float
     * @param yvalue float
     * @return Range2D.Float
     */
    public Range2D.Float union(float xvalue, float yvalue) {
      Range2D.Float range = (Range2D.Float) clone();
      if (xvalue < range.xmin)
	range.xmin = xvalue;
      if (xvalue > range.xmax)
	range.xmax = xvalue;
      if (yvalue < range.ymin)
	range.ymin = yvalue;
      if (yvalue > range.ymax)
	range.ymax = yvalue;
      return range;
    }
    
    /**
     * gets union of point <code>p</code> with <code>this</code> range.
     * @param p Point2f
     * @return Range2D.Float
     */
    public Range2D.Float union(Point2f p) {
      return union(p.x, p.y);
    }
    
    /**
     * gets union of [<code>xmin,xmax,ymin,ymax</code> ] with <code>this</code> range.
     * @param xmin float
     * @param xmax float
     * @param ymin float
     * @param ymax float
     * @return Range2D.Float
     */
    public Range2D.Float union(float xmin, float xmax, float ymin, float ymax) {
      Range2D.Float range = (Range2D.Float) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      if (ymin < range.ymin)
	range.ymin = ymin;
      if (ymax > range.ymax)
	range.ymax = ymax;
      return range;
    }
    
    /**
     * includes [<code>xmin,xmax,ymin,ymax</code> ] in range.
     * @param xmin float
     * @param xmax float
     * @param ymin float
     * @param ymax float
     * @return Range2D.Float (this)
     */
    public Range2D.Float include(float xmin, float xmax, float ymin, float ymax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      if (ymin < this.ymin)
	this.ymin = ymin;
      if (ymax > this.ymax)
	this.ymax = ymax;
      return this;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range2D
     * @return Range2D.Float
     */
    public Range2D.Float union(Range2D range) {
      return union(
	  (float) range.getXMinAsDouble(),
	  (float) range.getXMaxAsDouble(),
	  (float) range.getYMinAsDouble(),
	  (float) range.getYMaxAsDouble());
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range2D.Float
     * @return Range2D.Float
     */
    public Range2D.Float union(Range2D.Float range) {
      return union(range.getXMin(), range.getXMax(), range.getYMin(), range.getYMax());
    }
  }

  /**
   * Range1D constructor comment.
   */
  protected Range2D() {
    super(2);
  }

  /**
   * check if range contains fully another range <code>r</code>.
   * @param r Range2D
   * @return boolean
   */
  public boolean contains(Range2D r) {
    return (
	r.getXMinAsDouble() >= getXMinAsDouble()
	&& r.getXMaxAsDouble() <= getXMaxAsDouble()
	&& r.getYMinAsDouble() >= getYMinAsDouble()
	&& r.getYMaxAsDouble() <= getYMaxAsDouble());
  }

  /**
   * check if range contains point <code>p</code>.
   * @param p Point2d
   * @return boolean
   */
  public boolean contains(Point2d p) {
    return (
	p.x >= getXMinAsDouble()
	&& p.x <= getXMaxAsDouble()
	&& p.y >= getYMinAsDouble()
	&& p.y <= getYMaxAsDouble());
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
}
