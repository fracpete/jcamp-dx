/*******************************************************************************
* The JCAMP-DX project is the reference implemention of the IUPAC JCAMP-DX spectroscopy data standard.
* 
*   Copyright (C) 2019 Thomas Weber
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Library General Public
*    License as published by the Free Software Foundation; either
*    version 2 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Library General Public License for more details.
*
* Contributors:
* Thomas Weber - initial API and implementation
* Christoph Läubrich - implement custom Point2D class
*******************************************************************************/
package org.jcamp.math;

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

  public Point2D pointAt(int i) {
    return new Point2D(this.x.pointAt(i), this.y.pointAt(i));
  }
}