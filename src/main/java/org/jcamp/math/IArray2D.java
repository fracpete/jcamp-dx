package org.jcamp.math;
import javax.vecmath.Point2d;
/**
 * real interval with discrete data points.
 * @author Thomas Weber
 */
public interface IArray2D extends IInterval2D {
    /**
     * gets array of first dimension
     * @return IArray1D
     */
    IArray1D getXArray();
    /**
     * gets array of second dimension
     * @return IArray1D
     */
    IArray1D getYArray();

    /**
     * number of data points in interval.
     * @return int
     */
    int getLength();

    /**
     * discrete data point at position <code>index</code> in interval.
     * @param index int
     */
    Point2d pointAt(int index);
}