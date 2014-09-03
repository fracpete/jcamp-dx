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
