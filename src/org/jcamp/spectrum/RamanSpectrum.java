package org.jcamp.spectrum;

import org.jcamp.math.IInterval1D;
import org.jcamp.math.IrregularAxisMap;
import org.jcamp.math.IrregularGrid1D;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
/**
 * Raman spectra.
 * @author Thomas Weber
 */
public class RamanSpectrum extends OpticalSpectrum1D {
    private final static double[] DEFAULT_WAVENUMBERS =
        new double[] { 4000.0, 3500.0, 3000.0, 2500.0, 2000.0, 1800, 1600, 1400, 1200, 1000, 800, 600, 400 };
    private final static IrregularGrid1D DEFAULT_XGRID = new IrregularGrid1D(DEFAULT_WAVENUMBERS);
    private final static Range1D.Double DEFAULT_YRANGE = new Range1D.Double(0., 1.);
    private final static Unit DEFAULT_XUNIT = CommonUnit.perCM;
    private final static Unit DEFAULT_YUNIT = CommonUnit.intensity;
    /**
     */
    protected RamanSpectrum() {
        super();
    }
    /**
     * standard ctor.
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param y org.jcamp.spectrum.IDataArray1D
     */
    public RamanSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
        super(x, y);
    }
    /**
     * standard ctor.
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param y org.jcamp.spectrum.IDataArray1D
     * @param fullSpectrum boolean
     */
    public RamanSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
        super(x, y, fullSpectrum);
    }
    /**
     * gets spectrum ID.
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.RAMAN;
    }
    /**
     * isSameType method comment.
     */
    public boolean isSameType(Spectrum otherSpectrum) {
        if (otherSpectrum instanceof RamanSpectrum)
            return true;
        return false;
    }
    /**
     * sets full view range on x axis.
     */
    public void setXFullViewRange(Range1D.Double dataRange) {
        IrregularGrid1D newGrid = new IrregularGrid1D(DEFAULT_XGRID, dataRange);
        this.xAxisMap = new IrregularAxisMap((IInterval1D) getXData(), newGrid);
    }
}
