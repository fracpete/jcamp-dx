package org.jcamp.math;

/**
 * real interval with monotone ordered discrete points.
 * allows assignment of a single discrete point/index to a position within the interval. 
 * 
 * @author Thomas Weber
 */
public interface IOrderedArray1D
  extends IArray1D {
  
  public int indexAt(double p);
}
