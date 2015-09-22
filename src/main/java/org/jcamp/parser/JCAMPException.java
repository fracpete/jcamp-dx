/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

/**
 * exceptions thrown at JCAMP-DX parsing.
 * 
 * @author Thomas Weber
 */
public class JCAMPException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = -4773819366174671655L;
  /**
   * JCAMPException constructor comment.
   */
  public JCAMPException() {
    super();
  }
  /**
   * JCAMPException constructor comment.
   * @param s java.lang.String
   */
  public JCAMPException(String s) {
    super(s);
  }
}
