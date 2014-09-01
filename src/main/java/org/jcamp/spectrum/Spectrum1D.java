package org.jcamp.spectrum;
import java.io.Serializable;
import java.util.Arrays;

import org.jcamp.math.AxisMap;
import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.Range1D;
import org.jcamp.math.Range2D;
/**
 * 1D spectra
 * @author Thomas Weber
 */
public abstract class Spectrum1D extends Spectrum implements Cloneable, Serializable {
    IOrderedDataArray1D xData;
    IDataArray1D yData;
    /**
     * flag indicating a full spectrum 
     */
    protected boolean fullSpectrum;
    /**
     * axis mapping for x Axis
     */
    protected AxisMap xAxisMap = null;
    /**
     * axis mapping for y Axis
     */
    protected AxisMap yAxisMap = null;
    /**
     * peak table (array of peaks) 
     */
    protected Peak1D[] peakTable = null;

    /**
     * pattern table (array of peak patterns (singulets, doublets, multiplets)) 
     */
    protected Pattern[] patternTable = null;

    /**
     * array of assignments (peak (range) to atom number) 
     */
    protected Assignment[] assignments = null;

    /**
     * Spectrum1D constructor comment.
     */
    public Spectrum1D() {
        super();
    }

    /**
     * standard ctor.
     * 
     * @param x com.creon.chem.spectrum.IOrderedDataArray1D
     * @param y com.creon.chem.spectrum.IDataArray1D
     */
    public Spectrum1D(IOrderedDataArray1D x, IDataArray1D y) {
        this(x, y, !(x instanceof EquidistantData));
    }

    /**
     * standard ctor.
     * 
     * @param x com.creon.chem.spectrum.IOrderedDataArray1D
     * @param y com.creon.chem.spectrum.IDataArray1D
     * @param fullSpectrum boolean indicates a full spectrum vs. peak spectrum
     */
    public Spectrum1D(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
        this.xData = x;
        this.yData = y;
        this.fullSpectrum = fullSpectrum;
        this.xAxisMap = new LinearAxisMap(x);
        this.yAxisMap = new LinearAxisMap(y);
    }

    /**
     * adjusts full view to a pretty range.
     */
    protected void adjustFullViewRange() {
        double w;
        if (isFullSpectrum()) {
            Range1D.Double xrange = xData.getRange1D();
            w = xrange.getXWidth();
            xrange.set(xrange.getXMin() - .05 * w, xrange.getXMax() + .05 * w);
            setXFullViewRange(xrange);
            Range1D.Double yrange = yData.getRange1D();
            w = yrange.getXWidth();
            yrange.set(yrange.getXMin() - .05 * w, yrange.getXMax() + .05 * w);
            setYFullViewRange(yrange);
        } else {
            /* include 0.0 in y-range and add 5% on to all borders */
            int n = xData.getLength();
            if (n > 1) {
                Range1D.Double xrange = xData.getRange1D();
                w = xrange.getXWidth();
                xrange.set(xrange.getXMin() - .05 * w, xrange.getXMax() + .05 * w);
                setXFullViewRange(xrange);
            } else if (n == 1) {
                double x = xData.pointAt(0);
                setXFullViewRange(new Range1D.Double(x - 50, x + 50));
            } else {
                setXFullViewRange(new Range1D.Double(0., 300.));
            }
            if (n > 1) {
                Range1D.Double yrange = yData.getRange1D();
                yrange.set(Math.min(yrange.getXMin(), 0.0), Math.max(yrange.getXMax(), 0.0));
                w = yrange.getXWidth();
                if (yrange.getXMin() == 0.0) {
                    yrange.set(yrange.getXMin(), yrange.getXMax() + .05 * w);
                } else {
                    yrange.set(yrange.getXMin() - .05 * w, yrange.getXMax());
                }
                setYFullViewRange(yrange);
            } else {
                setYFullViewRange(new Range1D.Double(0., 100.));
            }
        }
    }

    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        Spectrum1D spectrum = null;
        spectrum = (Spectrum1D) super.clone();
        spectrum.setData((IOrderedDataArray1D) xData.clone(), (IDataArray1D) yData.clone());
        spectrum.copyPeakTable(this.peakTable);
        spectrum.copyPatternTable(this.patternTable);
        spectrum.copyAssignments(this.assignments);

        return spectrum;
    }

    /**
     * deep copy of assignment table.
     * @param assignments com.labcontrol.spectrum.Assignment[]
     */
    protected void copyAssignments(Assignment[] assignments) {
        if (assignments == null) {
            this.assignments = null;
            return;
        }
        this.assignments = new Assignment[assignments.length];
        for (int i = 0; i < this.assignments.length; i++) {
            this.assignments[i] = (Assignment) assignments[i].clone();
            this.assignments[i].setSpectrum(this);
        }
    }

    /**
     * deep copy of pattern table.
     * @param patternTable Pattern[]
     */
    protected void copyPatternTable(Pattern[] patternTable) {
        if (patternTable == null) {
            this.patternTable = null;
            return;
        }
        this.patternTable = new Pattern[patternTable.length];
        for (int i = 0; i < patternTable.length; i++) {
            this.patternTable[i] = (Pattern) patternTable[i].clone();
            this.patternTable[i].setSpectrum(this);
        }
    }

    /**
     * deep copy of peak table.
     * @param peakTable Peak1D[]
     */
    protected void copyPeakTable(Peak1D[] peakTable) {
        if (peakTable == null) {
            this.peakTable = null;
            return;
        }
        this.peakTable = new Peak1D[peakTable.length];
        for (int i = 0; i < peakTable.length; i++) {
            this.peakTable[i] = (Peak1D) peakTable[i].clone();
            this.peakTable[i].setSpectrum(this);
        }
    }

    /**
     * find index of label nearest to position.
     * @return ISpectrumLabel
     * @param ISpectrumLabel[]
     * @param pos double
     */
    private static int findLabelIndexNearestTo(ISpectrumLabel[] array, double pos) {
        class SearchKey implements ISpectrumLabel {
            double[] position = new double[1];
            SearchKey(double pos) {
                position[0] = pos;
            }
            public double[] getPosition() {
                return position;
            }
            public String getLabel() {
                return null;
            }
            public int compareTo(Object o) {
                double p0 = position[0];
                double p1 = ((ISpectrumLabel) o).getPosition()[0];
                if (p0 < p1)
                    return -1;
                if (p1 > p0)
                    return 1;
                return 0;
            }
            public void translate(double[] a) {
            }
        }
        SearchKey key = new SearchKey(pos);
        int index = Arrays.binarySearch(array, key);
        if (index < 0)
            index = -index - 1;
        if (index >= array.length)
            return array.length - 1;
        else {
            if (index <= 0) {
                return 0;
            } else {
                if (pos - array[index - 1].getPosition()[0] < array[index].getPosition()[0] - pos)
                    return index - 1;
                else
                    return index;
            }
        }
    }

    /**
     * find spectrum label (peak, pattern, assignment) nearest to
     * position <code>pos</code>.
     * @return ISpectrumLabel
     * @param array ISpectrumLabel[]
     * @param pos double
     */
    public ISpectrumLabel findNearestLabel(ISpectrumLabel[] array, double pos) {
        int index = findLabelIndexNearestTo(array, pos);
        return array[index];
    }

    /**
     * return assignment nearest to position <code>pos</code>.
     * @return Assignment
     * @param pos double
     */
    public Assignment getAssignmentNearestTo(double pos) {
        if (assignments == null)
            return null;
        return (Assignment) findNearestLabel(assignments, pos);
    }

    /**
     * return assignment table.
     * @return Assignment[]
     */
    public Assignment[] getAssignments() {
        return assignments;
    }

    /**
     * gets data range.
     * @return Range2D.Double
     */
    public Range2D.Double getDataRange() {
        return new Range2D.Double(xAxisMap.getDataRange(), yAxisMap.getDataRange());
    }

    /**
     * return range of mapped xy-data in full view
     * @return Range2D.Double
     */
    public Range2D.Double getFullViewRange() {
        return new Range2D.Double(getXFullViewRange(), getYFullViewRange());
    }

    /**
     * returns pattern nearest to postion <code>pos</code>.
     * @return Assignment
     * @param pos double
     */
    public Pattern getPatternNearestTo(double pos) {
        if (patternTable == null)
            return null;
        return (Pattern) findNearestLabel(patternTable, pos);
    }

    /**
     *@return com.labcontrol.spectrum.Pattern[]
     */
    public Pattern[] getPatternTable() {
        return patternTable;
    }

    /**
     * return peak nearest to position <code>pos</code>.
     * @return Assignment
     * @param pos double
     */
    public Peak1D getPeakNearestTo(double pos) {
        if (peakTable == null)
            return null;
        return (Peak1D) findNearestLabel(peakTable, pos);
    }

    /**
     * return peak table.
     * @return Peak1D[]
     */
    public Peak1D[] getPeakTable() {
        return peakTable;
    }

    /**
     * gets label on x-axis.
     * 
     * @return java.lang.String
     */
    public String getXAxisLabel() {
        if (xData != null)
            return xData.getLabel();
        else
            return "";
    }

    /**
     * @return AxisMap
     * @see com.creon.chem.spectrum.Spectrum
     */
    public AxisMap getXAxisMap() {
        return xAxisMap;
    }

    /**
     * Insert the method's description here.
     * 
     * @return com.creon.chem.spectrum.IOrderedDataArray1D
     */
    public IOrderedDataArray1D getXData() {
        return xData;
    }

    /**
     * gets x range in full view
     * @see com.creon.chem.spectrum.Spectrum1D
     */
    public Range1D.Double getXFullViewRange() {
        return new Range1D.Double(xAxisMap.getFullViewRange());
    }

    /**
     * gets label on y-axis.
     * 
     * @return java.lang.String
     */
    public String getYAxisLabel() {
        if (yData != null)
            return yData.getLabel();
        else
            return "";
    }

    /**
     * @return AxisMap
     * @see com.creon.chem.spectrum.Spectrum
     */
    public org.jcamp.math.AxisMap getYAxisMap() {
        return yAxisMap;
    }

    /**
     * Insert the method's description here.
     * 
     * @return com.creon.chem.spectrum.IDataArray1D
     */
    public IDataArray1D getYData() {
        return yData;
    }

    /**
     * return an integer array of the y data (intensities) e.g. for writing to JCAMP
     * 
     * @param int[] integer array
     * @return double y factor
     */
    public double getYDataAs16BitIntArray(int[] y) {
        int n = yData.getLength();
        double y0 = yData.pointAt(0);
        double y1 = yData.pointAt(n - 1);
        Range1D.Double yRange = yData.getRange1D();
        double yWidth = Math.max(yRange.getXMax(), -yRange.getXMin());
        double shortIntWidth = (double) (2 << 14); // JCAMP requires 16bit signed int precision?
        double yf = shortIntWidth / yWidth;
        for (int i = 0; i < n; i++) {
            y[i] = (int) (yData.pointAt(i) * yf + .5);
        }
        return yf;
    }

    /**
     * return an integer array of the y data (intensities) e.g. for writing to JCAMP (using non-std. 32 bit precision)
     * 
     * @param int[] integer array
     * @return double y factor
     */
    public double getYDataAs32BitIntArray(int[] y) {
        int n = yData.getLength();
        double y0 = yData.pointAt(0);
        double y1 = yData.pointAt(n - 1);
        Range1D.Double yRange = yData.getRange1D();
        double yWidth = Math.max(yRange.getXMax(), -yRange.getXMin());
        double shortIntWidth = (double) (2 << 30);
        double yf = shortIntWidth / yWidth;
        for (int i = 0; i < n; i++) {
            y[i] = (int) (yData.pointAt(i) * yf + .5);
        }
        return yf;
    }

    /**
     * @see com.creon.chem.spectrum.Spectrum1D
     */
    public Range1D.Double getYFullViewRange() {
        return new Range1D.Double(yAxisMap.getFullViewRange());
    }

    /**
     * flag indicating assignments.
     * @return boolean
     */
    public boolean hasAssignments() {
        return assignments != null;
    }

    /**
     * flag indictating a pattern table.
     * @return boolean
     */
    public boolean hasPatternTable() {
        return patternTable != null;
    }

    /**
     * flag indicating a peak table.
     * @return boolean
     */
    public boolean hasPeakTable() {
        return peakTable != null;
    }

    /**
     * flag indicating a full spectrum.
     * @return boolean
     */
    public boolean isFullSpectrum() {
        return fullSpectrum;
    }

    /**
     * remove assignment nearest to position <code>pos</code>.
     * @param pos double
     */
    public void removeAssignmentAt(double pos) {
        if (this.assignments != null && this.assignments.length > 0) {
            int index = findLabelIndexNearestTo(this.assignments, pos);
            Assignment[] newTable = new Assignment[this.assignments.length - 1];
            System.arraycopy(this.assignments, 0, newTable, 0, index);
            System.arraycopy(this.assignments, index + 1, newTable, index, this.assignments.length - index - 1);
            setAssignments(newTable);
        }
    }

    /**
     * remove pattern nearest to position <code>pos</code>.
     * @param pos double
     */
    public void removePatternAt(double pos) {
        if (this.patternTable != null && this.patternTable.length > 0) {
            int index = findLabelIndexNearestTo(this.patternTable, pos);
            Pattern[] newTable = new Pattern[this.patternTable.length - 1];
            System.arraycopy(this.patternTable, 0, newTable, 0, index);
            System.arraycopy(this.patternTable, index + 1, newTable, index, this.patternTable.length - index - 1);
            setPatternTable(newTable);
        }
    }

    /**
     * remove peak nearest to position <code>pos</code>.
     * @param pos double
     */
    public void removePeakAt(double pos) {
        if (this.peakTable != null && this.peakTable.length > 0) {
            int index = findLabelIndexNearestTo(this.peakTable, pos);
            Peak1D[] newTable = new Peak1D[this.peakTable.length - 1];
            System.arraycopy(this.peakTable, 0, newTable, 0, index);
            System.arraycopy(this.peakTable, index + 1, newTable, index, this.peakTable.length - index - 1);
            setPeakTable(newTable);
        }
    }

    /**
     * sets assignment array.
     * @param newAssignments Assignment[]
     */
    public void setAssignments(Assignment[] newAssignments) {
        if (assignments != newAssignments) {
            assignments = newAssignments;
            if (assignments != null) {
                for (int i = 0; i < assignments.length; i++)
                    assignments[i].setSpectrum(this);
                Arrays.sort(newAssignments);
            }
        }
    }

    /**
     * sets data arrays.
     * 
     * @param x com.creon.math.IOrderedDataArray1D
     * @param y com.creon.math.IDataArray1D
     */
    public void setData(IOrderedDataArray1D x, IDataArray1D y) {
        this.xData = x;
        this.yData = y;
    }

    /**
     * set full spectrum flag
     * @param newFullSpectrum boolean
     */
    protected void setFullSpectrum(boolean newFullSpectrum) {
        fullSpectrum = newFullSpectrum;
    }

    /**
     * set full view data range in mapping
     * should be overloaded
     * @param range Range2D.Double
     */
    public void setFullViewRange(Range2D.Double range) {
        setXFullViewRange(range.getXRange());
        setYFullViewRange(range.getYRange());
    }

    /**
     * sets a new pattern table.
     * @param newPatternTable Pattern[]
     */
    public void setPatternTable(Pattern[] newPatternTable) {
        if (newPatternTable != patternTable) {
            patternTable = newPatternTable;
            if (patternTable != null)
                for (int i = 0; i < patternTable.length; i++)
                    patternTable[i].setSpectrum(this);
        }
    }

    /**
     * sets a new peak table.
     * @param newPeakTable Peak1D[]
     */
    public void setPeakTable(Peak1D[] newPeakTable) {
        if (newPeakTable != peakTable) {
            peakTable = newPeakTable;
            if (peakTable != null)
                for (int i = 0; i < peakTable.length; i++)
                    peakTable[i].setSpectrum(this);
        }
    }

    /**
     * Insert the method's description here.
     * 
     * @param newXData com.creon.chem.spectrum.IOrderedDataArray1D
     */
    private void setXData(IOrderedDataArray1D newXData) {
        xData = newXData;
    }

    /**
     * set xy-mapping with a data range of <code>dataRange</code>.
     * mappings are linear mapping in <code>dataRange</code> bounds.
     * mapping: 
     * x := indepData 
     * @param dataRange Range1D.Double
     */
    public void setXFullViewRange(Range1D.Double dataRange) {
        this.xAxisMap = new LinearAxisMap(xData, dataRange);
    }

    /**
     * Insert the method's description here.
     * 
     * @param newYData com.creon.chem.spectrum.IDataArray1D
     */
    private void setYData(IDataArray1D newYData) {
        yData = newYData;
    }

    /**
     * sets full view range on y axis.
     */
    public void setYFullViewRange(Range1D.Double dataRange) {
        this.yAxisMap = new LinearAxisMap(yData, dataRange);
    }

    /**
     * insertion sort for peak tables (assuming most peak tables are already sorted)
     * sort by first dimension
     * @param array double[][]
     */
    static void sort(double[][] array) {
        int i, j, n;
        double x, y;
        n = array[0].length;
        for (i = 1; i < n; i++) {
            x = array[0][i];
            y = array[1][i];
            j = i;
            while (j > 0 && array[0][j - 1] > x) {
                array[0][j] = array[0][j - 1];
                array[1][j] = array[1][j - 1];
                j--;
            }
            array[0][j] = x;
            array[1][j] = y;

        }
    }

    /**
     * translate x values by <code>amount</code> together with all associated data e.g. for calibration
     * @param amount double
     */
    public void translate(double amount) {
        double[] tvec = new double[2];
        tvec[0] = amount;
        tvec[1] = 0.0;
        xData.translate(amount);
        if (peakTable != null) {
            for (int i = 0; i < peakTable.length; i++) {
                peakTable[i].translate(tvec);
            }
        }
        if (patternTable != null) {
            for (int i = 0; i < patternTable.length; i++) {
                patternTable[i].translate(tvec);
            }
        }
        if (assignments != null) {
            for (int i = 0; i < assignments.length; i++) {
                assignments[i].translate(tvec);
            }
        }
        adjustFullViewRange();
    }
}