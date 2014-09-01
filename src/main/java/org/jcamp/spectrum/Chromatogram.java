package org.jcamp.spectrum;
import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
/**
 * a Spectrum1D class representing a chromatogram.
 * @author Thomas Weber
 */
public class Chromatogram extends Spectrum1D {
    public final static Unit DEFAULT_XUNIT = CommonUnit.second;
    public final static Unit DEFAULT_YUNIT = CommonUnit.intensity;

    /**
     * Chromatogram constructor comment.
     */
    protected Chromatogram() {
        super();

    }

    /**
     * standard ctor.
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param y org.jcamp.spectrum.IDataArray1D
     */
    public Chromatogram(IOrderedDataArray1D x, IDataArray1D y) {
        super(x, y);
    }

    /**
     * standard ctor.
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param y org.jcamp.spectrum.IDataArray1D
     * @param fullSpectrum boolean
     */
    public Chromatogram(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
        super(x, y, fullSpectrum);
    }

    /**
     * adjust full range for pretty views.
     */
    protected void adjustFullViewRange() {
        Range1D.Double xrange = getXData().getRange1D();
        double w = xrange.getXWidth();
        xrange.set(xrange.getXMin() - .05 * w, xrange.getXMax() + .05 * w);
        setXFullViewRange(xrange);
        Range1D.Double yrange = getYData().getRange1D();
        w = yrange.getXWidth();
        yrange.set(yrange.getXMin() - .05 * w, yrange.getXMax() + .05 * w);
        setYFullViewRange(yrange);
    }

    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        Chromatogram spectrum = (Chromatogram) super.clone();
        spectrum.xAxisMap = new LinearAxisMap(xData);
        spectrum.yAxisMap = new LinearAxisMap(yData);

        return spectrum;
    }

    /**
     * gets spectrum ID.
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.CHROMATOGRAM;
    }

    /**
     * isSameType method comment.
     */
    public boolean isSameType(Spectrum otherSpectrum) {
        if (otherSpectrum instanceof Chromatogram)
            return true;
        return false;
    }
}