package org.jcamp.spectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
/**
 * Fluorescence spectra
 * @author Thomas Weber
 */
public class FluorescenceSpectrum extends OpticalSpectrum1D {
    public final static Unit DEFAULT_XUNIT = CommonUnit.nanometerWavelength;
    public final static Unit DEFAULT_YUNIT = CommonUnit.intensity;

    /**
     */
    private FluorescenceSpectrum() {
        super();
    }

    /**
     * standard ctor.
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param y org.jcamp.spectrum.IDataArray1D
     */
    public FluorescenceSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
        super(x, y);
    }

    /**
     * standard ctor.
     * 
     * @param x org.jcamp.spectrum.IOrderedDataArray1D
     * @param y org.jcamp.spectrum.IDataArray1D
     * @param fullSpectrum boolean
     */
    public FluorescenceSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
        super(x, y, fullSpectrum);
    }

    /**
     * gets spectrum ID.
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.FLUORESCENCE;
    }

    /**
     * isSameType method comment.
     */
    public boolean isSameType(Spectrum otherSpectrum) {
        if (otherSpectrum instanceof FluorescenceSpectrum)
            return true;
        return false;
    }
}