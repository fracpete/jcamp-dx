package org.jcamp.spectrum;

import java.util.Vector;
/**
 * peak picking algorithm interface.
 * @author Thomas Weber
 */
public interface IPeakPicking {
    /**
     * perform the peak picking on the 1D spectrum.
     * @param Spectrum1D the spectrum
     * @return Vector vector of peaks*/
    public Vector calculate(Spectrum1D spectrum);
}
