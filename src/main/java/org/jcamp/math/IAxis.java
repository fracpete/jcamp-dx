package org.jcamp.math;

/**
 * interface for coordinate axes.
 * 
 * @author Thomas Weber
 */
public interface IAxis {

  public int X_AXIS = 0;

  public int Y_AXIS = 1;
  
  public int Z_AXIS = 2;

  /**
   * gets data mapping to axis.
   * @return AxisMap
   */
  public AxisMap getAxisMap();
  
  /**
   * gets direction id.
   * @return int
   */
  public int getDirection();
  
  /**
   * get axis label.
   * @return String
   */
  public String getLabel();
  
  /**
   * sets data mapping for axis.
   * @param map AxisMap
   */
  public void setAxisMap(AxisMap map);
}
