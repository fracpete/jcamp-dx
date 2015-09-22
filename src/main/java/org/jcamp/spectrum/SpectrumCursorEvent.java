/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

/**
 * This is the event class to support the 
 * com.creon.chem.spectrum.SpectrumCursorListener interface.
 */
public class SpectrumCursorEvent
  extends java.util.EventObject {
  
  /** for serialization. */
  private static final long serialVersionUID = -465785802527658497L;

  /**
   * SpectrumCursorEvent constructor comment.
   * @param source java.lang.Object
   */
  public SpectrumCursorEvent(java.lang.Object source) {
    super(source);
  }
}
