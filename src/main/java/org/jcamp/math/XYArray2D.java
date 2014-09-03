package org.jcamp.math;

import javax.vecmath.Point2d;
/**
 * simple IArray2D containing arbitrary IArray1D for x and y.
 * 
 * @author Thomas Weber
 */
public class XYArray2D
  implements IArray2D {
  
  protected IArray1D x;
  
  protected IArray1D y;

  public XYArray2D(IArray1D x, IArray1D y) {
    this.x = x;
    this.y = y;
  }

  public int getLength() {
    return this.x.getLength() * this.y.getLength();
  }

  public Range.Double getRange() {
    return getRange2D();
  }

  public Range2D.Double getRange2D() {
    return new Range2D.Double(this.x.getRange1D(), this.y.getRange1D());
  }

  public IArray1D getXArray() {
    return this.x;
  }

  public IArray1D getYArray() {
    return this.y;
  }

  public Point2d pointAt(int i) {
    return new Point2d(this.x.pointAt(i), this.y.pointAt(i));
  }
}