package org.jcamp.parser;

import org.jcamp.spectrum.Spectrum;
/**
 * writer for spectrums as JCAMP
 * @author Thomas Weber
 */
public interface ISpectrumJCAMPWriter {
    String toJCAMP(Spectrum spectrum) throws JCAMPException;
    String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException;
}
