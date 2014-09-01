package org.jcamp.spectrum;
import org.jcamp.math.AxisMap;
import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.Range1D;
import org.jcamp.math.ReversedLinearAxisMap;
/**
 * 2D nmr spectrum.
 * @author Thomas Weber 
 */
public class NMR2DSpectrum extends Spectrum2D {
    private final static Range1D.Double DEFAULT_ZRANGE = new Range1D.Double(0., 20.);
    private final static Range1D.Double[] DEFAULT_XYRANGE =
        new Range1D.Double[] { new Range1D.Double(-2., 14.), new Range1D.Double(0., 200.)};
    String[] nucleus = new String[] { "^13C", "^13C" };
    double[] frequency = new double[] { 300.0, 300.0 };
    double[] reference = new double[] { 0.0, 0.0 };
    boolean fullSpectrum = true;

    /**
     * NMR2DSpectrum 
     */
    public NMR2DSpectrum(
        IOrderedDataArray1D x,
        IOrderedDataArray1D y,
        IDataArray1D z,
        String[] nucleus,
        double[] freq,
        double[] ref) {
        super(x, y, z);
        this.nucleus = nucleus;
        this.frequency = freq;
        this.reference = ref;
        adjustFullViewRange();
    }

    /**
     * adjust data ranges for pretty full spectrum view
     */
    private void adjustFullViewRange() {
        if (isFullSpectrum()) {
            Range1D.Double xrange = getXData().getRange1D();
            double w = xrange.getXWidth();
            xrange.set(xrange.getXMin() - .05 * w, xrange.getXMax() + .05 * w);
            setXFullViewRange(xrange);
            Range1D.Double yrange = getYData().getRange1D();
            w = yrange.getXWidth();
            yrange.set(yrange.getXMin() - .05 * w, yrange.getXMax() + .05 * w);
            setYFullViewRange(yrange);
            Range1D.Double zrange = getZData().getRange1D();
            w = zrange.getXWidth();
            zrange.set(zrange.getXMin() - .05 * w, zrange.getXMax() + .05 * w);
            setZFullViewRange(zrange);
        } else {
            /* include 0.0 in y-range and add 5% on to all borders */
            /*
            Range1D xrange = getXData().getRange();
            double w = xrange.getWidth();
            xrange.left -= 0.05 * w;
            xrange.right += 0.05 * w;
            setFullViewRangeX(xrange);
            Range1D yrange = getYData().getRange();
            yrange.left = Math.min(yrange.left, 0.0);
            yrange.right = Math.max(yrange.right, 0.0);
            w = yrange.getWidth();
            if (yrange.left == 0.0) {
            yrange.right += 0.05 * w;
            } else {
            yrange.left -= 0.05 * w;
            }
            setFullViewRangeY(yrange);
            */
        }
    }

    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        NMR2DSpectrum o = (NMR2DSpectrum) super.clone();
        o.nucleus = new String[] { this.nucleus[0], this.nucleus[1] };
        o.frequency = new double[] { this.frequency[0], this.frequency[1] };
        o.reference = new double[] { this.reference[0], this.reference[1] };
        return o;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/27/00 1:19:03 PM)
     */
    private static double[] convertToPPM(double[] hz, double freq, double ref) {
        int n = hz.length;
        double[] ppm = new double[n];
        for (int i = 0; i < n; i++) {
            ppm[i] = (hz[i] - ref) / freq;
        }
        return ppm;
    }

    /**
     * gets spectrum ID
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.NMR2D;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/18/00 17:22:51)
     * @return java.lang.String
     */
    public java.lang.String getXNucleus() {
        return nucleus[0];
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/27/00 1:13:28 PM)
     * @return double
     */
    public double getXReference() {
        return reference[0];
    }

    /**
     * getYAxisMap method comment.
     */
    public AxisMap getYAxisMap() {
        return yAxisMap;
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/19/00 20:12:58)
     * @return double
     */
    public double getYFrequency() {
        return frequency[1];
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/18/00 17:22:51)
     * @return java.lang.String
     */
    public java.lang.String getYNucleus() {
        return nucleus[1];
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/27/00 1:13:28 PM)
     * @return double
     */
    public double getYReference() {
        return reference[1];
    }

    /**
     * @return boolean
     */
    public boolean isFullSpectrum() {
        return fullSpectrum;
    }

    /**
     * isSameType method comment.
     */
    public boolean isSameType(Spectrum otherSpectrum) {
        if (otherSpectrum instanceof NMR2DSpectrum)
            return true;
        return false;
    }

    /**
     * sets full view range on x axis.
     */
    public void setXFullViewRange(Range1D.Double dataRange) {
        xAxisMap = new ReversedLinearAxisMap(getXData(), dataRange);
    }

    /**
     * sets full view range on y axis.
     */
    public void setYFullViewRange(Range1D.Double dataRange) {
        yAxisMap = new ReversedLinearAxisMap(getYData(), dataRange);
    }

    /**
     * sets full view range on z axis.
     */
    public void setZFullViewRange(Range1D.Double dataRange) {
        zAxisMap = new LinearAxisMap(zData, dataRange);
    }
}