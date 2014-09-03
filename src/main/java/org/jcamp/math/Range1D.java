package org.jcamp.math;

/**
 * base class for 1D data ranges.
 * 
 * @author Thomas Weber
 */
public abstract class Range1D
  extends Range {
  
  /** for serialization. */
  private static final long serialVersionUID = -1174175881057971793L;

  /**
   * 1D data range.
   * 
   * @author Thomas Weber
   */
  public static class Int
    extends Range1D
    implements Range.Int {
    
    /** for serialization. */
    private static final long serialVersionUID = -5096845094034133121L;

    /** minimum value */
    private int xmin;

    /** maximum value */
    private int xmax;

    /**
     * default constructor.
     */
    public Int() {
      super();
      this.xmin = 0;
      this.xmax = 0;
    }
    
    /**
     * copy constructor.
     */
    public Int(Range1D.Int r) {
      super();
      this.xmin = r.xmin;
      this.xmax = r.xmax;
    }
    
    /**
     * create range [min(<code>l</code>,<code>r</code>), max(<code>l</code>,<code>r</code>)]
     * @param l int 
     * @param r int
     */
    public Int(int l, int r) {
      super();
      if (l < r) {
	this.xmin = l;
	this.xmax = r;
      } else {
	this.xmin = r;
	this.xmax = l;
      }
    }
    
    /**
     * check if range contains value <code>v</code>.
     * @return boolean
     * @param v int
     */
    public boolean contains(int v) {
      return (v >= xmin && v <= xmax);
    }
    
    /**
     * clip range <code>x0,x1</code>. return <code>null</code> if range is outside or on border
     * @param x0 int
     * @param x1 int
     * @return Range1D.Int
     */
    public Range1D.Int clip(int x0, int x1) {
      Range1D.Int nr = new Int(x0, x1);
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
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @return Range1D.Int
     * @param r Range1D.Int
     */
    public Range1D.Int clip(Range1D.Int r) {
      Range1D.Int nr = new Range1D.Int(r);
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
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range1D.Int range = null;
      try {
	range = (Range1D.Int) super.clone();
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
      if (obj instanceof Range1D.Int) {
	Range1D.Int r = (Range1D.Int) obj;
	if (this == r || ((r.xmin == xmin) && (r.xmax == xmax)))
	  return true;
      }
      return false;
    }
    
    /**
     * extend range by <code>amount</code> on both sides.
     * @param amount int
     */
    public void extend(int amount) {
      xmin -= amount;
      xmax += amount;
    }
    
    /**
     * gets the max value of the range as a double
     * @return double
     */
    @Override
    public double getXMaxAsDouble() {
      return xmax;
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getCenter(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return (xmax + xmin) / 2;
    }
    
    /**
     * gets the range in dimension <code>dim</code>.
     * @param dim int
     * @return Range1D.Int
     */
    public Range1D.Int getRange(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return (Range1D.Int) this.clone();
    }
    
    /**
     * gets the maximum in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getMax(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmax;
    }
    
    /**
     * gets the min value of the range as a double
     * @return double
     */
    @Override
    public double getXMinAsDouble() {
      return xmin;
    }
    
    /**
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getWidth(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmax - xmin;
    }
    
    /**
     * gets the minimum in dimension <code>dim</code>.
     * @param dim int
     * @return int
     */
    public int getMin(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmin;
    }
    
    /**
     * gets the max value of the range
     * @return int
     */
    public int getXMax() {
      return xmax;
    }
    
    /**
     * gets the min value of the range
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
      return (Range1D.Int) this.clone();
    }
    
    /**
     * gets the size of the interval.
     * @return int
     */
    public int getXWidth() {
      return xmax - xmin;
    }
    
    /**
     * gets the center of the interval.
     * @return int
     */
    public int getXCenter() {
      return (xmax + xmin) / 2;
    }
    
    /**
     * include <code>value</code> in range.
     * @param value int
     * @return Range1D.Int (this)
     */
    public Range1D.Int include(int value) {
      if (value < xmin)
	xmin = value;
      if (value > xmax)
	xmax = value;
      return this;
    }
    
    /**
     * include [<code>xmin</code>,<code>xmax</code> ] in range.
     * @param xmin int
     * @param xmax int
     * @return Range1D.Int (this)
     */
    public Range1D.Int include(int xmin, int xmax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      return this;
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range1D
     * @return Range1D.Int
     */
    public Range1D.Int include(Range1D.Int range) {
      return include(range.getXMin(), range.getXMax());
    }
    
    /**
     * set range to interval [min(<code>l</code>,<code>r</code>), max(<code>l</code>,<code>r</code>)]
     * @param l int 
     * @param r int
     */
    public void set(int l, int r) {
      if (l < r) {
	this.xmin = l;
	this.xmax = r;
      } else {
	this.xmin = r;
	this.xmax = l;
      }
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range1D
     */
    public void set(Range1D.Int r) {
      this.xmin = r.getXMin();
      this.xmax = r.getXMax();
    }
    
    /**
     * sets range to center point and width.
     * @param center int
     * @param width int
     */
    public void setCenterAndWidth(int center, int width) {
      this.xmin = center - width / 2;
      this.xmax = center + width / 2;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return "[" + xmin + "," + xmax + "]";
    }
    
    /**
     * translate range by <code>amount</code>
     * @param amount int
     */
    public void translate(int amount) {
      xmin += amount;
      xmax += amount;
    }
    
    /**
     * gets union of <code>value</code> with <code>this</code> range.
     * @param value int
     * @return Range1D.Int
     */
    public Range1D.Int union(int value) {
      Range1D.Int range = (Range1D.Int) this.clone();
      if (value < range.xmin)
	range.xmin = value;
      if (value > range.xmax)
	range.xmax = value;
      return range;
    }
    
    /**
     * gets union of [<code>xmin</code>,<code>xmax</code> ] with <code>this</code> range.
     * @param xmin int
     * @param xmax int
     * @return Range1D.Int
     */
    public Range1D.Int union(int xmin, int xmax) {
      Range1D.Int range = (Range1D.Int) this.clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      return range;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range1D.Int
     * @return Range1D.Int
     */
    public Range1D.Int union(Range1D.Int range) {
      return union(range.getXMin(), range.getXMax());
    }
  }
  
  /**
   * 1D data range
   * @author Thomas Weber
   */
  public static class Double
    extends Range1D
    implements Range.Double, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = -6191572071758320201L;

    /** minimum value */
    private double xmin;

    /** maximum value */
    private double xmax;

    /**
     * default constructor.
     */
    public Double() {
      super();
      this.xmin = java.lang.Double.NaN;
      this.xmax = java.lang.Double.NaN;
    }
    
    /**
     * copy constructor.
     */
    public Double(Range1D r) {
      super();
      this.xmin = r.getXMinAsDouble();
      this.xmax = r.getXMaxAsDouble();
    }
    
    /**
     * create range [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)]
     * @param x0 double 
     * @param x1 double
     */
    public Double(double x0, double x1) {
      super();
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
    }
    
    /**
     * clip range to <code>x0,x1</code>. return <code>null</code> if range is outside or on border
     * @param x0 double
     * @param x1 double
     * @return Range1D.Double
     */
    public Range1D.Double clip(double x0, double x1) {
      Range1D.Double nr = new Double(x0, x1);
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
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @param r Range1D
     * @return Range1D.Double
     */
    public Range1D.Double clip(Range1D r) {
      Range1D.Double nr = new Range1D.Double(r);
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
      return nr;
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range1D.Double range = null;
      try {
	range = (Range1D.Double) super.clone();
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
      if (obj instanceof Range1D) {
	Range1D r = (Range1D) obj;
	if (this == r
	    || (Math.abs(r.getXMinAsDouble() - xmin) < DOUBLE_EPS
		&& Math.abs(r.getXMaxAsDouble() - xmax) < DOUBLE_EPS))
	  return true;
      }
      return false;
    }
    
    /**
     * extend range by <code>amount</code> on both sides.
     * @param amount double
     */
    public void extend(double amount) {
      xmin -= amount;
      xmax += amount;
    }
    
    /**
     * gets the max value of the range as a double
     * @return double
     */
    @Override
    public double getXMaxAsDouble() {
      return xmax;
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getCenter(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return 0.5 * (xmax + xmin);
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return Range1D.Double
     */
    public Range1D.Double getRange(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return (Range1D.Double) this.clone();
    }
    
    /**
     * gets the maximum in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getMax(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmax;
    }
    
    /**
     * gets the min value of the range as a double
     * @return double
     */
    @Override
    public double getXMinAsDouble() {
      return xmin;
    }
    
    /**
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getWidth(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmax - xmin;
    }
    
    /**
     * gets the minimum in dimension <code>dim</code>.
     * @param dim int
     * @return double
     */
    public double getMin(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmin;
    }
    
    /**
     * gets the max value of the range
     * @return double
     */
    public double getXMax() {
      return xmax;
    }
    
    /**
     * gets the min value of the range
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
      return (Range1D.Double) this.clone();
    }
    
    /**
     * gets the size of the interval.
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
     * include <code>value</code> in range.
     * @param value double
     * @return Range1D.Double (this)
     */
    public Range1D.Double include(double value) {
      if (value < xmin)
	xmin = value;
      if (value > xmax)
	xmax = value;
      return this;
    }
    
    /**
     * include [<code>xmin</code>,<code>xmax</code> ] in range.
     * @param xmin double
     * @param xmax double
     * @return Range1D.Double (this)
     */
    public Range1D.Double include(double xmin, double xmax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      return this;
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range1D
     * @return Range1D.Double
     */
    public Range1D.Double include(Range1D range) {
      return include(range.getXMinAsDouble(), range.getXMaxAsDouble());
    }
    
    /**
     * check for NaNs
     * @return boolean
     */
    public boolean isValid() {
      return (xmin == xmin && xmax == xmax);
    }
    
    /**
     * set range to interval [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)]
     * @param x0 double 
     * @param x1 double
     */
    public void set(double x0, double x1) {
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range1D
     */
    public void set(Range1D r) {
      this.xmin = r.getXMinAsDouble();
      this.xmax = r.getXMaxAsDouble();
    }
    
    /**
     * sets range to center point and width.
     * @param center double
     * @param width double
     */
    public void setCenterAndWidth(double center, double width) {
      xmin = center - 0.5 * width;
      xmax = center + 0.5 * width;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return "[" + xmin + "," + xmax + "]";
    }
    
    /**
     * translate range by <code>amount</code>
     * @param amount double
     */
    public void translate(double amount) {
      xmin += amount;
      xmax += amount;
    }
    
    /**
     * gets union of <code>value</code> with <code>this</code> range.
     * @param value double
     * @return Range1D.Double
     */
    public Range1D.Double union(double value) {
      Range1D.Double range = (Range1D.Double) clone();
      if (value < range.xmin)
	range.xmin = value;
      if (value > range.xmax)
	range.xmax = value;
      return range;
    }
    
    /**
     * gets union of [<code>xmin</code>,<code>xmax</code> ] with <code>this</code> range.
     * @param xmin double
     * @param xmax double
     * @return Range1D.Double
     */
    public Range1D.Double union(double xmin, double xmax) {
      Range1D.Double range = (Range1D.Double) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      return range;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range1D
     * @return Range1D.Double
     */
    public Range1D.Double union(Range1D range) {
      return union(range.getXMinAsDouble(), range.getXMaxAsDouble());
    }
  }

  /**
   * 1D data range.
   * 
   * @author Thomas Weber
   */
  public static class Float
    extends Range1D
    implements Range.Float, Cloneable {
    
    /** for serialization. */
    private static final long serialVersionUID = -4164264992802959334L;

    /** minimum value */
    private float xmin;

    /** maximum value */
    private float xmax;

    /**
     * default constructor.
     */
    public Float() {
      super();
      this.xmin = java.lang.Float.NaN;
      this.xmax = java.lang.Float.NaN;
    }
    
    /**
     * copy constructor.
     */
    public Float(Range1D r) {
      super();
      this.xmin = (float) r.getXMinAsDouble();
      this.xmax = (float) r.getXMaxAsDouble();
    }
    
    /**
     * copy constructor.
     */
    public Float(Range1D.Float r) {
      super();
      this.xmin = r.xmin;
      this.xmax = r.xmax;
    }
    
    /**
     * create range [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)]
     * @param x0 double 
     * @param x1 double
     */
    public Float(float x0, float x1) {
      super();
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
    }
    
    /**
     * check if range contains value <code>v</code>.
     * @return boolean
     * @param v float
     */
    public boolean contains(float v) {
      return (v >= xmin && v <= xmax);
    }

    /**
     * clip range <code>x0,x1</code>. return <code>null</code> if range is outside or on border
     * @return Range1D.Float
     * @param x0 float
     * @param x1 float
     */
    public Range1D.Float clip(float x0, float x1) {
      Range1D.Float nr = new Float(x0, x1);
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
      return nr;
    }
    
    /**
     * clip range <code>r</code>. return <code>null</code> if range is outside or on border
     * @param r Range1D
     * @return Range1D.Float
     */
    public Range1D.Float clip(Range1D r) {
      return clip((float) r.getXMinAsDouble(), (float) r.getXMinAsDouble());
    }
    
    /**
     * std. cloning.
     * @return Object
     */
    @Override
    public Object clone() {
      Range1D.Float range = null;
      try {
	range = (Range1D.Float) super.clone();
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
      if (obj instanceof Range1D) {
	Range1D r = (Range1D) obj;
	if (this == r
	    || (Math.abs(r.getXMinAsDouble() - xmin) < FLOAT_EPS
		&& Math.abs(r.getXMaxAsDouble() - xmax) < FLOAT_EPS))
	  return true;
      }
      return false;
    }
    
    /**
     * compare ranges for equality.
     * @return boolean
     * @param r Range1D.Float
     */
    public boolean equals(Range1D.Float r) {
      if (this == r || (Math.abs(r.xmin - xmin) < FLOAT_EPS && Math.abs(r.xmax - xmax) < FLOAT_EPS))
	return true;
      return false;
    }
    
    /**
     * extend range by <code>amount</code> on both sides.
     * @param amount float
     */
    public void extend(float amount) {
      this.xmin -= amount;
      this.xmax += amount;
    }
    
    /**
     * gets the max value of the range as a double
     * @return double
     */
    @Override
    public double getXMaxAsDouble() {
      return xmax;
    }
    
    /**
     * gets the maximum in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getMax(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmax;
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getCenter(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return 0.5f * (xmax + xmin);
    }
    
    /**
     * gets the center in dimension <code>dim</code>.
     * @param dim int
     * @return Range1D.Float
     */
    public Range1D.Float getRange(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return (Range1D.Float) this.clone();
    }
    
    /**
     * gets the minimum in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getMin(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmin;
    }
    
    /**
     * gets the min value of the range
     * @return double
     */
    @Override
    public double getXMinAsDouble() {
      return xmin;
    }
    
    /**
     * gets the width in dimension <code>dim</code>.
     * @param dim int
     * @return float
     */
    public float getWidth(int dim) {
      if (dim != 0)
	throw new IndexOutOfBoundsException();
      else
	return xmax - xmin;
    }
    
    /**
     * gets the max value of the range
     * @return float
     */
    public float getXMax() {
      return xmax;
    }
    
    /**
     * gets the min value of the range
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
      return (Range1D.Float) this.clone();
    }
    
    /**
     * gets the size of the interval.
     * @return float
     */
    public float getXWidth() {
      return xmax - xmin;
    }
    
    /**
     * gets the center of the interval.
     * @return float
     */
    public float getXCenter() {
      return 0.5f * (xmax + xmin);
    }
    
    /**
     * include <code>value</code> in range.
     * @param value float
     * @return Range1D.Float (this)
     */
    public Range1D.Float include(float value) {
      if (value < xmin)
	xmin = value;
      if (value > xmax)
	xmax = value;
      return this;
    }
    
    /**
     * include [<code>xmin</code>,<code>xmax</code> ] in range.
     * @param xmin float
     * @param xmax float
     * @return Range1D.Float (this)
     */
    public Range1D.Float include(float xmin, float xmax) {
      if (xmin < this.xmin)
	this.xmin = xmin;
      if (xmax > this.xmax)
	this.xmax = xmax;
      return this;
    }
    
    /**
     * include <code>range</code> in range.
     * @param range Range1D
     * @return Range1D.Float
     */
    public Range1D.Float include(Range1D range) {
      return include((float) range.getXMinAsDouble(), (float) range.getXMaxAsDouble());
    }
    
    /**
     * check for NaNs
     * @return boolean
     */
    public boolean isValid() {
      return (xmin == xmin && xmax == xmax);
    }
    
    /**
     * set range to interval [min(<code>x0</code>,<code>x1</code>), max(<code>x0</code>,<code>x1</code>)]
     * @param x0 double 
     * @param x1 double
     */
    public void set(float x0, float x1) {
      if (x0 < x1) {
	this.xmin = x0;
	this.xmax = x1;
      } else {
	this.xmin = x1;
	this.xmax = x0;
      }
    }
    
    /**
     * set range from range <code>r</code>.
     * @param r Range1D
     */
    public void set(Range1D r) {
      this.xmin = (float) r.getXMinAsDouble();
      this.xmax = (float) r.getXMaxAsDouble();
    }
    
    /**
     * sets range to center point and width.
     * @param center float
     * @param width float
     */
    public void setCenterAndWidth(float center, float width) {
      xmin = center - 0.5f * width;
      xmax = center + 0.5f * width;
    }
    
    /**
     * gets String representation.
     * @return java.lang.String
     */
    @Override
    public String toString() {
      return "[" + xmin + "," + xmax + "]";
    }
    
    /**
     * translate range by <code>amount</code>
     * @param amount float
     */
    public void translate(float amount) {
      xmin += amount;
      xmax += amount;
    }
    
    /**
     * gets union of <code>value</code> with <code>this</code> range.
     * @param value float
     * @return Range1D.Float
     */
    public Range1D.Float union(float value) {
      Range1D.Float range = (Range1D.Float) clone();
      if (value < range.xmin)
	range.xmin = value;
      if (value > range.xmax)
	range.xmax = value;
      return range;
    }
    
    /**
     * gets union of [<code>xmin</code>,<code>xmax</code> ] with <code>this</code> range.
     * @param xmin float
     * @param xmax float
     */
    public Range1D.Float union(float xmin, float xmax) {
      Range1D.Float range = (Range1D.Float) clone();
      if (xmin < range.xmin)
	range.xmin = xmin;
      if (xmax > range.xmax)
	range.xmax = xmax;
      return range;
    }
    
    /**
     * gets union of <code>range</code> with <code>this</code> range.
     * @param range Range1D
     * @return Range1D.Float
     */
    public Range1D.Float union(Range1D range) {
      return union((float) range.getXMinAsDouble(), (float) range.getXMaxAsDouble());
    }
  }

  /**
   * Range1D constructor comment.
   */
  protected Range1D() {
    super(1);
  }

  /**
   * check if range contains value <code>v</code>.
   * @return boolean
   * @param v double
   */
  public boolean contains(double v) {
    return (v >= getXMinAsDouble() && v <= getXMaxAsDouble());
  }

  /**
   * check if range contains fully another range <code>r</code>.
   * @param r Range1D
   * @return boolean
   */
  public boolean contains(Range1D r) {
    return (r.getXMinAsDouble() >= getXMinAsDouble() && r.getXMaxAsDouble() <= getXMaxAsDouble());
  }

  public abstract double getXMaxAsDouble();

  public abstract double getXMinAsDouble();
}