/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * special exception in data handling.
 * 
 * @author Thomas Weber
 */
public class DataException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = -1506747424253191086L;
  
  /**
   * DataException constructor comment.
   */
  public DataException() {
    super();
  }
  
  /**
   * DataException constructor comment.
   * @param s String
   */
  public DataException(String s) {
    super(s);
  }
}
