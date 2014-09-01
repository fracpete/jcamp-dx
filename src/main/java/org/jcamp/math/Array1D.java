package org.jcamp.math;
import java.io.Serializable;
/**
 * 1-dimensional numerical data arrays.
 * @author Thomas Weber
 */
public class Array1D extends Array implements IArray1D, Serializable, Cloneable {
    private double[] array;
    private Range1D.Double range;

    /**
     */
    public Array1D() {
        super(0);
        this.array = new double[] {
        };
        this.range = new Range1D.Double();
    }

    /**
     * create data array from double array.
     * @param values double[] 
     */
    public Array1D(double[] values) {
        this(values, true);
    }

    /**
     * create data array from double array.
     * @param values double[]
     * @param copy boolean indicates if array should be copied
     */
    public Array1D(double[] values, boolean copy) {
        super(values.length);
        if (copy) {
            array = (double[]) values.clone();
        } else
            array = values;
        calcRange();
    }

    /**
     * ctor from other IArray1D
     * 
     * @param from com.creon.math.IArray1D
     */
    public Array1D(IArray1D from) {
        super(from.getLength());
        this.array = new double[from.getLength()];
        for (int i = 0; i < this.array.length; i++)
            this.array[i] = from.pointAt(i);
        this.range = from.getRange1D();
    }

    /**
     * calculate maximum and minimum.
     */
    private void calcRange() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < getLength(); i++) {
            if (array[i] < min)
                min = array[i];
            if (array[i] > max)
                max = array[i];
        }
        range = new Range1D.Double(min, max);
    }

    /**
     * std cloning.
     * @return java.lang.Object
     */
    public Object clone() {
        Array1D array = null;
        //	try  {
        array = (Array1D) super.clone();
        //	} catch (CloneNotSupportedException e) {}
        array.range = (Range1D.Double) this.range.clone();
        array.array = (double[]) this.array.clone();
        return array;
    }

    /**
     * gets data value at index.
     * @param index int
     * @return double
     */
    public double[] elementAt(int index) {
        return new double[] { array[index] };
    }

    /**
     * gets data values in double array.
     * @param index int[] positions to read
     * @return double[][] data values at index positions
     */
    public double[][] elementsAt(int[] index) {
        double[][] values = new double[1][index.length];
        for (int i = 0; i < index.length; i++) {
            int index_i = index[i];
            values[0][i] = array[index_i];
        }
        return values;
    }

    /**
     * get values as array.
     */
    public IArray1D getArray(int index) throws ArrayIndexOutOfBoundsException {
        if (index != 0)
            throw new ArrayIndexOutOfBoundsException();
        return this;
    }

    /**
     * dimension is 1.
     */
    public final int getDimension() {
        return 1;
    }

    /**
     * gets range of data in array
     * @return Range.Double
     */
    public Range.Double getRange() {
        return range;
    }

    /**
     * gets range of data in array
     * @return Range1D.Double
     */
    public Range1D.Double getRange1D() {
        return range;
    }

    /**
     * gets data value at index.
     * @param index int
     * @return double
     */
    public double pointAt(int index) {
        return array[index];
    }

    /**
     * scale all array values by <code>amount</code>
     * @param amount double
     */
    public void scale(double amount) {
        for (int i = 0; i < array.length; i++)
            array[i] *= amount;
        this.range = new Range1D.Double(this.range.getXMin() * amount, this.range.getXMax() * amount);
    }

    /**
     * convert to double array.
     * 
     * @return double[]
     */
    public double[] toArray() {
        double[] a = new double[array.length];
        System.arraycopy(array, 0, a, 0, array.length);
        return a;
    }

    /**
     * translate all array values by <code>amount</code>
     * @param amount double
     */
    public void translate(double amount) {
        for (int i = 0; i < array.length; i++)
            array[i] += amount;
        this.range.translate(amount);
    }
}