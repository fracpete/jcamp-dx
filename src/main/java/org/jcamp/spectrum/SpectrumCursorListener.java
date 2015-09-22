/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

/**
 * The event set listener interface for the spectrumCursor feature.
 */
public interface SpectrumCursorListener extends java.util.EventListener {
  
  /**
   * 
   * @param event SpectrumCursorEvent
   */
  void cursorPositionChanged(SpectrumCursorEvent event);
}
