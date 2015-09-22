/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.Integral;
import org.jcamp.math.Range1D;
/**
 * integral over a 1D spectrum.
 * @author Thomas Weber
 */
public class Spectrum1DIntegral extends Integral {
    private Spectrum1D spectrum;
    /**
     * SpectrumIntegral constructor comment.
     * @param spectrum Spectrum1D
     * @param start double
     * @param end double
     */
    public Spectrum1DIntegral(Spectrum1D spectrum, double start, double end) {
        super(spectrum.getXData(), spectrum.getYData(), start, end);
        this.spectrum = spectrum;
    }
    /**
     * SpectrumIntegral constructor comment.
     * @param spectrum Spectrum1D
     * @param start double
     * @param end double
     * @param threshold double
     */
    public Spectrum1DIntegral(Spectrum1D spectrum, double start, double end, double threshold) {
        super(spectrum.getXData(), spectrum.getYData(), start, end, threshold);
        this.spectrum = spectrum;
    }
    /**
     * SpectrumIntegral constructor comment.
     * @param spectrum Spectrum1D
     * @param range Range1D.Double
     */
    public Spectrum1DIntegral(Spectrum1D spectrum, Range1D.Double range) {
        super(spectrum.getXData(), spectrum.getYData(), range);
        this.spectrum = spectrum;
    }
    /**
     * SpectrumIntegral constructor comment.
     * @param spectrum Spectrum1D
     * @param range Range1D.Double
     * @param threshold double
     */
    public Spectrum1DIntegral(Spectrum1D spectrum, Range1D.Double range, double threshold) {
        super(spectrum.getXData(), spectrum.getYData(), range, threshold);
        this.spectrum = spectrum;
    }
    /**
     * special integration routine that checks for peak spectra.
     */
    public void integrate() {
        if (!spectrum.isFullSpectrum()) {
            area = 0.0;
            integral = new double[0];
            integrationDone = true;
            return;
        } else
            super.integrate();
    }
}
