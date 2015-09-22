/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;

/**
 * exception raised if units are not convertible into each other.
 * 
 * @author Thomas Weber
 */
public class UnitException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = 3848518000407284705L;

  /**
   * UnitException constructor comment.
   */
  public UnitException() {
    super();
  }
  
  /**
   * UnitException constructor comment.
   * @param s java.lang.String
   */
  public UnitException(String s) {
    super(s);
  }
}
