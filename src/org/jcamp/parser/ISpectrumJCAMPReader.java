package org.jcamp.parser;

import org.jcamp.spectrum.Spectrum;
/**
 * reader for spectra from JCAMP
 * @author Thomas Weber
 */
public interface ISpectrumJCAMPReader {
    /**
     * create spectrum from JCAMP block.
     * 
     * @return Spectrum
     * @param block JCAMPBlock
     */
    Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException;
}
