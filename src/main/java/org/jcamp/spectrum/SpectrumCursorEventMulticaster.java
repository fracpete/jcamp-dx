/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

/**
 * This is the event multicaster class to support the 
 * com.creon.chem.spectrum.SpectrumCursorListenerinterface.
 */
public class SpectrumCursorEventMulticaster
  extends java.awt.AWTEventMulticaster implements SpectrumCursorListener {
  
  /**
   * Constructor to support multicast events.
   * @param a java.util.EventListener
   * @param b java.util.EventListener
   */
  protected SpectrumCursorEventMulticaster(java.util.EventListener a, java.util.EventListener b) {
    super(a, b);
  }
  
  /**
   * Add new listener to support multicast events.
   * @return SpectrumCursorListener
   * @param a SpectrumCursorListener
   * @param b SpectrumCursorListener
   */
  public static SpectrumCursorListener add(SpectrumCursorListener a, SpectrumCursorListener b) {
    return (SpectrumCursorListener) addInternal(a, b);
  }
  
  /**
   * Add new listener to support multicast events.
   * @return java.util.EventListener
   * @param a java.util.EventListener
   * @param b java.util.EventListener
   */
  protected static java.util.EventListener addInternal(java.util.EventListener a, java.util.EventListener b) {
    if (a == null)
      return b;
    if (b == null)
      return a;
    return new SpectrumCursorEventMulticaster(a, b);
  }
  
  /**
   * 
   * @param event com.creon.chem.spectrum.SpectrumCursorEvent
   */
  public void cursorPositionChanged(org.jcamp.spectrum.SpectrumCursorEvent event) {
    ((org.jcamp.spectrum.SpectrumCursorListener) a).cursorPositionChanged(event);
    ((org.jcamp.spectrum.SpectrumCursorListener) b).cursorPositionChanged(event);
  }
  
  /**
   * 
   * @return java.util.EventListener
   * @param oldl com.creon.chem.spectrum.SpectrumCursorListener
   */
  protected java.util.EventListener remove(org.jcamp.spectrum.SpectrumCursorListener oldl) {
    if (oldl == a)
      return b;
    if (oldl == b)
      return a;
    java.util.EventListener a2 = removeInternal(a, oldl);
    java.util.EventListener b2 = removeInternal(b, oldl);
    if (a2 == a && b2 == b)
      return this;
    return addInternal(a2, b2);
  }
  
  /**
   * Remove listener to support multicast events.
   * @return SpectrumCursorListener
   * @param l SpectrumCursorListener
   * @param oldl SpectrumCursorListener
   */
  public static SpectrumCursorListener remove(SpectrumCursorListener l, SpectrumCursorListener oldl) {
    if (l == oldl || l == null)
      return null;
    if (l instanceof SpectrumCursorEventMulticaster)
      return (SpectrumCursorListener) ((SpectrumCursorEventMulticaster) l).remove(oldl);
    return l;
  }
}
