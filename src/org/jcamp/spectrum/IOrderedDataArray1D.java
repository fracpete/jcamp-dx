package org.jcamp.spectrum;
import java.io.Serializable;

import org.jcamp.math.DataException;
import org.jcamp.math.IOrderedArray1D;
import org.jcamp.math.Range1D;
import org.jcamp.units.IMeasurable;
/**
 * 1D ordered data arrays extended by unit and label.
 * @author Thomas Weber
 */
public interface IOrderedDataArray1D extends IOrderedArray1D, IMeasurable, Cloneable, Serializable {
    /**
     * gets data point indices around <code>position</code>
     * @param double position to search
     * @return int[] array of {floorIndex, ceilIndex}
     * @exception DataException thrown if position out of data range
     */
    int[] boundIndices(double position) throws org.jcamp.math.DataException;

    /**
     * gets data point values around <code>position</code>
     * @param double position to search
     * @return Range1D.Double
     * @exception DataException thrown if position out of data range
     */
    Range1D.Double bounds(double position) throws org.jcamp.math.DataException;

    /**
     * gets next data point greater than <code>position</code>
     * @param double position to search
     * @return double
     * @exception DataException thrown if position out of data range
     */
    double ceil(double position) throws org.jcamp.math.DataException;

    /**
     * gets next data point index greater than <code>position</code>
     * @param double position to search
     * @return double
     * @exception DataException thrown if position out of data range
     */
    int ceilIndex(double position) throws org.jcamp.math.DataException;

    public Object clone();

    /**
     * gets next data point lesser than <code>position</code>
     * @param double position to search
     * @return double
      * @exception DataException thrown if position out of data range
    */
    double floor(double position) throws org.jcamp.math.DataException;

    /**
     * gets next data point index lesser than <code>position</code>
     * @param double position to search
     * @return double
     * @exception DataException thrown if position out of data range
     */
    int floorIndex(double position) throws org.jcamp.math.DataException;

    /**
     * get data label.
     * 
     * @return java.lang.String
     */
    String getLabel();

    /**
     * indicates ascending data points.
     * 
     * @return boolean
     */
    boolean isAscending();

    /**
     * sets data label
     * 
     * @param label java.lang.String
     */
    void setLabel(String label);
}