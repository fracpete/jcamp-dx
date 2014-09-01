package org.jcamp.math;

/**
 * interface for coordinate axes.
 * @author Thomas Weber
 */
public interface IAxis {
    int X_AXIS = 0;
    int Y_AXIS = 1;
    int Z_AXIS = 2;

    /**
     * gets data mapping to axis.
     * @return AxisMap
     */
    AxisMap getAxisMap();
    /**
     * gets direction id.
     * @return int
     */
    int getDirection();
    /**
     * get axis label.
     * @return String
     */
    String getLabel();
    /**
     * sets data mapping for axis.
     * @param map AxisMap
     */
    void setAxisMap(AxisMap map);
}
