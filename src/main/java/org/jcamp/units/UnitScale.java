/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;

import java.io.Serializable;

/**
 * scaling factor for units.
 * SI scaling factors are predefined.
 * 
 * @author Thomas Weber
 */
public class UnitScale
  implements Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = -3443058899890094395L;

  double factor;
  
  String name = null;
  
  String prefix = null;

  /**
   * UnitScale constructor comment.
   */
  public UnitScale(double factor) {
    super();
    this.factor = factor;
  }

  /**
   * UnitScale constructor comment.
   */
  public UnitScale(double factor, String name, String prefix) {
    super();
    this.factor = factor;
    this.name = name;
    this.prefix = prefix;
  }

  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    UnitScale scale = null;
    try {
      scale = (UnitScale) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return scale;
  }

  /**
   * equality check.
   * 
   * @return boolean
   * @param o java.lang.Object
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof UnitScale) {
      if (Math.abs(((UnitScale) o).getFactor() - this.factor) < 2 * Double.MIN_VALUE)
	return true;
    } else if (o instanceof Number) {
      if (Math.abs(((Number) o).doubleValue() - this.factor) < 2 * Double.MIN_VALUE)
	return true;
    }
    return false;
  }

  /**
   * gets factor of scale
   * @return double
   */
  public double getFactor() {
    return factor;
  }

  /**
   * gets name of scale (e.g. micro, kilo, ...).
   * @return String
   */
  public String getName() {
    return name;
  }

  /**
   * gets scale prefix (e.g. k,m,G).
   * @return String
   */
  public String getPrefix() {
    return prefix;
  }
}
