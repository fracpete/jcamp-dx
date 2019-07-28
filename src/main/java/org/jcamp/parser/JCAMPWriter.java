package org.jcamp.parser;

import java.util.Hashtable;

import org.jcamp.spectrum.FluorescenceSpectrum;
import org.jcamp.spectrum.IRSpectrum;
import org.jcamp.spectrum.MassSpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.RamanSpectrum;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.UVSpectrum;
/**
 * singleton class that creates JCAMP strings from spectra
 * @author Thomas Weber
 */
public class JCAMPWriter {
    private static JCAMPWriter theInstance = null;
    private static Hashtable adapters = new Hashtable(10);
    /**
     * JCAMPWriter constructor comment.
     */
    private JCAMPWriter() {
    }
    /**
     * find adapter class for spectrum class.
     * 
     * @return ISpectrumJCAMPWriter
     * @param spectrum Spectrum
     */
    private ISpectrumJCAMPWriter findAdapter(Spectrum spectrum) {
        Class spectrumClass = spectrum.getClass();
        ISpectrumJCAMPWriter writer = null;
        while (writer == null && spectrumClass != null) {
            writer = (ISpectrumJCAMPWriter) adapters.get(spectrumClass);
            spectrumClass = spectrumClass.getSuperclass();
        }
        return writer;
    }
    /**
     * singleton accessor method.
     * 
     * @return com.labcontrol.jcamp.reader.JCAMPWriter
     */
    public static JCAMPWriter getInstance() {
	if (theInstance == null) {
            theInstance = new JCAMPWriter();
            initAdapters();
        }
        return theInstance;
    }
    private static void initAdapters() {
        adapters.put(NMRSpectrum.class, new NMRJCAMPWriter());
        adapters.put(IRSpectrum.class, new IRJCAMPWriter());
        adapters.put(RamanSpectrum.class, new RamanJCAMPWriter());
        adapters.put(UVSpectrum.class, new UVJCAMPWriter());
        adapters.put(MassSpectrum.class, new MSJCAMPWriter());
        adapters.put(FluorescenceSpectrum.class, new FluorescenceJCAMPWriter());
    }
    public String toJCAMP(Spectrum spectrum) throws JCAMPException {
	ISpectrumJCAMPWriter adapter = findAdapter(spectrum);
        if (adapter == null)
            throw new JCAMPException("cannot write JCAMP for " + spectrum.getClass());
        else
            return adapter.toJCAMP(spectrum);
    }
    public String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException {
        ISpectrumJCAMPWriter adapter = findAdapter(spectrum);
        if (adapter == null)
            throw new JCAMPException("cannot write JCAMP for " + spectrum.getClass());
        else
            return adapter.toSimpleJCAMP(spectrum);
    }
}
