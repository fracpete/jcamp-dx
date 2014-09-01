package org.jcamp.spectrum;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
/**
 * UV/VIS spectra.
 * @author Thomas Weber
 */
public class UVSpectrum extends OpticalSpectrum1D {
    private final static Range1D.Double DEFAULT_XRANGE = new Range1D.Double(200., 800.);
    private final static Range1D.Double DEFAULT_YRANGE = new Range1D.Double(0., 1.);
    public final static Unit DEFAULT_XUNIT = CommonUnit.nanometerWavelength;
    public final static Unit DEFAULT_YUNIT = CommonUnit.intensity;

    /**
     */
    protected UVSpectrum() {
        super();
    }

    /**
     * standard ctor.
     * 
     * @param x isFrozen.spectrum.IOrderedDataArray1D
     * @param y isFrozen.spectrum.IDataArray1D
     */
    public UVSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
        super(x, y);
    }

    /**
     * standard ctor.
     * 
     * @param x isFrozen.spectrum.IOrderedDataArray1D
     * @param y isFrozen.spectrum.IDataArray1D
     * @param fullSpectrum boolean
     */
    public UVSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
        super(x, y, fullSpectrum);
    }

    /**
     * gets spectrum ID.
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.UV;
    }

    /**
     * isSameType method comment.
     */
    public boolean isSameType(Spectrum otherSpectrum) {
        if (otherSpectrum instanceof UVSpectrum)
            return true;
        return false;
    }
}