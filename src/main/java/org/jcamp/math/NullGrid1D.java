package org.jcamp.math;
import java.io.Serializable;
/**
 * null value implementation.
 * @author Thomas Weber
 */
public final class NullGrid1D extends Grid1D implements Cloneable, Serializable {
    private final static Range1D.Double NULLRANGE = new Range1D.Double();

    /**
     * NullGrid1D constructor comment.
     */
    public NullGrid1D() {
        super();
    }

    /**
     * clone method comment.
     */
    public Object clone() {
        NullGrid1D grid = null;
        //	try {
        grid = (NullGrid1D) super.clone();
        //	} catch (CloneNotSupportedException e) {}
        return grid;
    }

    /**
     * coordinateAt method comment.
     */
    public double[] coordinateAt(double[] value) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * coordinateAt method comment.
     */
    public double coordinateAt(double value) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * coordinatesAt method comment.
     */
    public double[][] coordinatesAt(double[][] values) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * elementAt method comment.
     */
    public double[] elementAt(int index) {
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * getArray method comment.
     */
    public IArray1D getArray(int index) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * gets grid range.
     */
    public Range.Double getRange() {
        return NULLRANGE;
    }

    /**
     * gets grid range.
     * @return Range1D.Double
     */
    public Range1D.Double getRange1D() {
        return NULLRANGE;
    }

    /**
     * gridPointAt method comment.
     */
    public double[] gridPointAt(int index) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * gridPointsAt method comment.
     */
    public double[][] gridPointsAt(int[] indices) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * indexAt method comment.
     */
    public int indexAt(double[] position) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * indicesAt method comment.
     */
    public int[] indicesAt(double[][] positions) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * isAscending method comment.
     */
    public boolean isAscending() {
        return false;
    }

    /**
     * pointAt method comment.
     */
    public double pointAt(int index) {
        throw new ArrayIndexOutOfBoundsException("null grid");
    }

    /**
     * scale method comment.
     */
    public void scale(double amount) {
    }

    public double[] toArray() {
        return new double[] {
        };
    }

    /**
     * translate method comment.
     */
    public void translate(double amount) {
    }

    /**
     * valueAt method comment.
     */
    public double[] valueAt(double[] grid) {
        return null;
    }

    /**
     * valueAt method comment.
     */
    public double valueAt(double grid) {
        return 0;
    }

    /**
     * valuesAt method comment.
     */
    public double[][] valuesAt(double[][] grid) {
        return null;
    }
}