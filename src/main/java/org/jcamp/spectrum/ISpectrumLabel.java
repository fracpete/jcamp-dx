package org.jcamp.spectrum;

/**
 * spectrum labels (peak labels, pattern labels, assignments etc.).
 * 
 * @author Thomas Weber
 */
public interface ISpectrumLabel
  extends Comparable {
  
  /**
   * returns the label text
   * @return String label text 
   */
  String getLabel();
  
  /**
   * returns current position in spectrum coordinates
   * @return double[] coordinates of the position 
   */
  double[] getPosition();
  
  /**
   * translate the label the translation vector <code>amount</code>
   * @param amount double[] translation vector
   */
  void translate(double[] amount);
}
