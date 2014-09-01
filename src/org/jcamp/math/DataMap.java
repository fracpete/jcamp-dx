package org.jcamp.math;

/**
 * abstract base class for data mapping.
 * @author Thomas Weber
 */
public abstract class DataMap {
    /**
     * DataMap constructor comment.
     */
    protected DataMap() {
        super();
    }
    /**
     * range of data mapping.
     * @return Range1D.Double
     */
    public abstract Range1D.Double getMapRange();
    /**
     * map real world data
     * @param world double
     * @return double
     */
    public abstract double[] map(double[] world);
    /**
     * map single world data point.
     * @param world double
     * @return double
     */
    public abstract double map(double world);
    /**
     * reverse map back to world data
     * @param world double
     * @return double[]
     */
    public abstract double[] reverseMap(double[] vpdata);
    /**
     * reverse map to world data.
     * @param world double
     * @return double
     */
    public abstract double reverseMap(double vpdata);
}
