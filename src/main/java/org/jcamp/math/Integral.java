/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * class representing an integral over an discrete interval.
 * 
 * @author Thomas Weber
 */
public class Integral
  implements IInterval1D {
  
  private double istart;
  
  private double iend;
  
  private IOrderedArray1D xData;
  
  private IArray1D yData;
  
  private double threshold;
  
  protected double area;
  
  private int length;
  
  private int start;
  
  private int end;
  
  protected boolean integrationDone = false;
  
  protected double[] integral;
  
  /**
   * Integral constructor comment.
   */
  public Integral(IOrderedArray1D xData, IArray1D yData, double start, double end) {
    this(xData, yData, start, end, 1.0);
  }
  
  /**
   * Integral constructor comment.
   */
  public Integral(IOrderedArray1D xData, IArray1D yData, double start, double end, double threshold) {
    super();
    this.xData = xData;
    this.yData = yData;
    this.istart = start;
    this.iend = end;
    this.integral = new double[0];
    this.length = 0;
    this.area = 0.0;
    this.threshold = threshold;
  }
  
  /**
   * Integral constructor comment.
   */
  public Integral(IOrderedArray1D xData, IArray1D yData, Range1D.Double range) {
    this(xData, yData, range.getXMin(), range.getXMax(), 1.0);
  }
  
  /**
   * Integral constructor comment.
   */
  public Integral(IOrderedArray1D xData, IArray1D yData, Range1D.Double range, double threshold) {
    this(xData, yData, range.getXMin(), range.getXMax(), threshold);
  }
  
  /**
   * return integral value.
   * @return double
   */
  public double getArea() {
    if (!integrationDone)
      integrate();
    return Math.abs(area);
  }
  
  /**
   * Insert the method's description here.
   * Creation date: (1/26/00 4:34:51 PM)
   * @return int
   */
  public int getEndIndex() {
    return end;
  }
  
  /**
   * gets integral points over interval.
   * @return double[]
   */
  public double[] getIntegral() {
    if (!integrationDone)
      integrate();
    return integral;
  }
  
  /**
   * gets number of data points.
   * @return int
   */
  public int getLength() {
    return length;
  }
  
  /**
   * gets interval range.
   * @return Range.Double
   */
  public Range.Double getRange() {
    return new Range1D.Double(istart, iend);
  }
  
  /**
   * gets integral range.
   * @return Range1D.Double
   */
  public Range1D.Double getRange1D() {
    return new Range1D.Double(istart, iend);
  }
  
  /**
   * index of start
   * @return double
   */
  public int getStartIndex() {
    return start;
  }
  
  /**
   * gets integration threshold.
   * @return double
   */
  public double getThreshold() {
    return threshold;
  }
  
  /**
   * integrate spectrum, use threshold of <code>threshold</code> percent.
   */
  public void integrate() {
    if (threshold <= 0.0)
      threshold = 1;
    boolean negative = false;
    start = (int) Math.floor(xData.indexAt(istart) + 0.5);
    end = (int) Math.floor(xData.indexAt(iend) + 0.5);
    istart = xData.pointAt(start);
    iend = xData.pointAt(end);
    if (start > end) {
      int tmp = start;
      start = end;
      end = tmp;
      negative = true;
    } else if (start == end) {
      area = 0.0;
      integral = new double[0];
    }
    start = Math.max(start, 0);
    end = Math.min(end, xData.getLength() - 1);
    double x0, x1, y0, y1;
    length = end - start + 1;
    integral = new double[length];
    double t = yData.getRange1D().getXWidth() * threshold / 100;
    x0 = xData.pointAt(start);
    y0 = yData.pointAt(start);
    if (y0 < t)
      y0 = 0;
    integral[0] = 0;
    for (int i = 1; i < length; i++) {
      x1 = xData.pointAt(start + i);
      y1 = yData.pointAt(start + i);
      double dx = x1 - x0;
      if (y1 < t)
	y1 = 0.0;
      //			{
	//				integral[i] = integral[i-1];
	//			} else {	
	  integral[i] = integral[i - 1] + 0.5 * dx * (y1 + y0);
	  x0 = x1;
	  y0 = y1;
	  //			}
    }
    area = integral[length - 1];
    for (int i = 0; i < length; i++)
      integral[i] /= area;
    if (negative)
      area = -area;
    integrationDone = true;
  }
  
  /**
   * set threshold for integration.
   * @param newThreshold double
   */
  public void setThreshold(double newThreshold) {
    threshold = newThreshold;
  }
}
