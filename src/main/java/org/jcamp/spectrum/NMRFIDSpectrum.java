package org.jcamp.spectrum;
import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.Range1D;
/**
 * NMR FID spectrum.
 * @author Thomas Weber
 */
public class NMRFIDSpectrum extends NMRSpectrum {
    protected IDataArray1D rData;
    protected IDataArray1D iData;

    protected NMRFIDSpectrum() {
        super();
    }

    /**
     * x->r,i ctor
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param r org.jcamp.spectrum.IDataArray1D
     * @param i org.jcamp.spectrum.IDataArray1D
     * @param nucleus String
     * @param freq double observe frequency
     * @param ref double reference frequency
     */
    public NMRFIDSpectrum(
        IOrderedDataArray1D x,
        IDataArray1D r,
        IDataArray1D i,
        String nucleus,
        double freq,
        double ref) {
        super(x, r, nucleus, freq, ref);
        this.rData = r;
        this.iData = i;
        mapRealFID();

    }

    /**
     * x->r ctor
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param r org.jcamp.spectrum.IDataArray1D
     * @param nucleus String
     * @param freq double observe frequency
     * @param ref double reference frequency
     */
    public NMRFIDSpectrum(IOrderedDataArray1D x, IDataArray1D r, String nucleus, double freq, double ref) {
        this(x, r, null, nucleus, freq, ref);
    }

    /**
     * clone method comment.
     */
    public java.lang.Object clone() {
        NMRFIDSpectrum spectrum = null;
        spectrum = (NMRFIDSpectrum) super.clone();
        spectrum.iData = (IDataArray1D) this.iData.clone();
        spectrum.rData = (IDataArray1D) this.rData.clone();
        mapRealFID();
        return spectrum;
    }

    /**
     * gets spectrum ID
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.NMRFID;
    }

    public boolean isFID() {
        return true;
    }

    /**
     * map imaginary FID part to intensities.
     */
    public void mapImaginaryFID() {
        if (iData != null) {
            yData = iData;
            adjustFullViewRange();
        }
    }

    /**
     * map real FID part to intensities.
     */
    public void mapRealFID() {
        yData = rData;
        adjustFullViewRange();
    }

    public void setData(IOrderedDataArray1D x, IDataArray1D y) {
        this.xData = x;
        this.yData = y;
        this.rData = y;
        mapRealFID();
    }

    /**
     * sets range in x axis for full view
     */
    public void setXFullViewRange(Range1D.Double dataRange) {
        this.xAxisMap = new LinearAxisMap(xData, dataRange);
    }

    /**
     * sets range in x axis for full view
     */
    public void setYFullViewRange(Range1D.Double dataRange) {
        this.yAxisMap = new LinearAxisMap(yData, dataRange);
    }
}