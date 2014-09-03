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
