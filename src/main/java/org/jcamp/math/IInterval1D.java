package org.jcamp.math;

/**
 * interface for data that is bounded by an interval range.
 * 
 * @author Thomas Weber
 */
public interface IInterval1D
  extends IInterval {
  
  public Range1D.Double getRange1D();
}
