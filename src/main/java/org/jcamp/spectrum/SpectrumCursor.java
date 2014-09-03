package org.jcamp.spectrum;

import org.jcamp.math.IAxis;

/**
 * iterator over spectrum data points.
 * 
 * @author Thomas Weber
 */
public abstract class SpectrumCursor {
  
  protected transient SpectrumCursorListener aSpectrumCursorListener;
  
  /**
   * the controlled spectrum 
   */
  Spectrum spectrum;

  /**
   * the direction of the cursor (X or Y) 
   */
  int direction;

  /**
   * current index position of the cursor 
   */
  int index = 0;

  /**
   * inner class for cursors in X direction 
   */
  public static class X extends SpectrumCursor {
    public X(Spectrum spectrum) {
      super(spectrum, IAxis.X_AXIS, 0);
    }
    @Override
    public void next() {
      if (index < ((Spectrum1D) spectrum).getXData().getLength() - 1) {
	index++;
	fireCursorPositionChanged(new SpectrumCursorEvent(this));
      }
    }
    @Override
    public void previous() {
      if (index > 0) {
	index--;
	fireCursorPositionChanged(new SpectrumCursorEvent(this));
      }
    }
    @Override
    public void toPosition(double x) {
      index = ((Spectrum1D) spectrum).getXData().indexAt(x);
      if (index < 0)
	index = 0;
      if (index > ((Spectrum1D) spectrum).getYData().getLength() - 1)
	index = ((Spectrum1D) spectrum).getYData().getLength() - 1;
      fireCursorPositionChanged(new SpectrumCursorEvent(this));
    }
    @Override
    public double getPosition() {
      return ((Spectrum1D) spectrum).getXData().pointAt(index);
    }
  }

  /**
   * inner class for cursors  in Y direction 
   */
  public static class Y extends SpectrumCursor {
    public Y(Spectrum2D spectrum) {
      super(spectrum, IAxis.Y_AXIS, 0);
    }
    @Override
    public void next() {
      if (index < ((Spectrum2D) spectrum).getYData().getLength() - 1) {
	index++;
	fireCursorPositionChanged(new SpectrumCursorEvent(this));
      }
    }
    @Override
    public void previous() {
      if (index > 0) {
	index--;
	fireCursorPositionChanged(new SpectrumCursorEvent(this));
      }
    }
    @Override
    public void toPosition(double y) {
      index = ((Spectrum2D) spectrum).getYData().indexAt(y);
      if (index < 0)
	index = 0;
      if (index > ((Spectrum2D) spectrum).getYData().getLength() - 1)
	index = ((Spectrum2D) spectrum).getYData().getLength() - 1;
      fireCursorPositionChanged(new SpectrumCursorEvent(this));
    }
    @Override
    public double getPosition() {
      return ((Spectrum2D) spectrum).getYData().pointAt(index);
    }
  }

  SpectrumCursor(Spectrum spectrum, int direction, int index) {
    this.spectrum = spectrum;
    this.direction = direction;
    this.index = index;
  }

  /**
   * 
   * @param newListener SpectrumCursorListener
   */
  public void addSpectrumCursorListener(SpectrumCursorListener newListener) {
    aSpectrumCursorListener = SpectrumCursorEventMulticaster.add(aSpectrumCursorListener, newListener);
    fireCursorPositionChanged(new SpectrumCursorEvent(this));
    return;
  }

  /**
   * Method to support listener events.
   * @param event SpectrumCursorEvent
   */
  protected void fireCursorPositionChanged(SpectrumCursorEvent event) {
    if (aSpectrumCursorListener == null) {
      return;
    };
    aSpectrumCursorListener.cursorPositionChanged(event);
  }

  /**
   * returns the cursor direction (X or Y)
   * @return int direction indicator
   */
  public int getDirection() {
    return direction;
  }

  /**
   * gets the current index position
   * @return int the index position
   */
  public int getIndex() {
    return index;
  }

  /**
   * returns the current position of the cursor in spectrum coordinates.
   * @return double
   */
  public abstract double getPosition();

  /**
   * gets the spectrum the cursor iterates upon
   * @return Spectrum
   */
  public Spectrum getSpectrum() {
    return spectrum;
  }

  /**
   * increase the index position by one 
   */
  public abstract void next();

  /**
   * goes back one index position
   */
  public abstract void previous();

  /**
   * 
   * @param newListener SpectrumCursorListener
   */
  public void removeSpectrumCursorListener(SpectrumCursorListener newListener) {
    aSpectrumCursorListener = SpectrumCursorEventMulticaster.remove(aSpectrumCursorListener, newListener);
    return;
  }

  /**
   * sets the cursor nearest to the position <code>pos</code> 
   */
  public abstract void toPosition(double pos);
}