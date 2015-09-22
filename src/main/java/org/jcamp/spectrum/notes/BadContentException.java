/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum.notes;

/**
 * Insert the type's description here.
 * 
 * @author Thomas Weber
 */
public class BadContentException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = 1625864303430463264L;

  /**
   * BadContentException constructor comment.
   */
  public BadContentException() {
    super();
  }
  
  /**
   * BadContentException constructor comment.
   * @param s java.lang.String
   */
  public BadContentException(String s) {
    super(s);
  }
}
