package org.jcamp.spectrum;

import org.jcamp.math.Array1D;
import org.jcamp.math.IArray1D;
import org.jcamp.units.CommonUnit;
/**
 * GC/MS spectra
 * GC/MS are usually represented as 1D TIC-chromatogram with a 1D MS-spectrum corresponding to
 * each chromatogram data point.
 * @author Thomas Weber
 */
public class GCMSSpectrum extends Chromatogram implements IMasterSpectrum {
    protected MassSpectrum[] massSpectra;
    /**
     * GCMSSpectrum
     * @param times IOrderedDataArray1D array of retention time values
     * @param ms MassSpectrum[] array of mass spectra at above retention times
     */
    public GCMSSpectrum(IOrderedDataArray1D times, MassSpectrum[] ms) {
        super(times, calcTICC(ms));
        this.massSpectra = ms;
        for (int i = 0; i < ms.length; i++) {
            ms[i].setMasterSpectrum(this);
        }
    }
    /**
     * GCMSSpectrum
     * @param times IOrderedDataArray1D array of retention time values
     * @param tic IDataArray1D array of total ion counts time values
     * @param ms MassSpectrum[] array of mass spectra at above retention times
     */
    public GCMSSpectrum(IOrderedDataArray1D times, IDataArray1D tic, MassSpectrum[] ms) {
        super(times, tic);
        this.massSpectra = ms;
        for (int i = 0; i < ms.length; i++) {
            ms[i].setMasterSpectrum(this);
        }
    }
    /**
     * recalulate TIC
     * @return IDataArray1D
     * @param ms com.labcontrol.spectrum.MassSpectrum[]
     */
    static IDataArray1D calcTICC(MassSpectrum[] ms) {
        int n = ms.length;
        double[] ticc = new double[n];
        for (int i = 0; i < n; i++) {
            ticc[i] = 0.0;
            MassSpectrum spectrum = ms[i];
            IArray1D intensities = spectrum.getYData();
            for (int j = 0; j < intensities.getLength(); j++) {
                ticc[i] += intensities.pointAt(j);
            }
        }
        return new ArrayData(new Array1D(ticc, false), CommonUnit.intensity);
    }
    /**
     * cloning of the spectrum.
     * @return java.lang.Object
     */
    public Object clone() {
        int n = massSpectra.length;
        MassSpectrum[] ms = new MassSpectrum[n];
        for (int i = 0; i < n; i++) {
            ms[i] = (MassSpectrum) massSpectra[i].clone();
        }
        GCMSSpectrum spectrum = null;
        spectrum = (GCMSSpectrum) super.clone();
        spectrum.massSpectra = ms;
        return spectrum;
    }
    /**
     * gets spectrum ID
     * @return int
     */
    public int getIdentifier() {
        return ISpectrumIdentifier.GCMS;
    }
    /**
     * getSlaveSpectrumFromIndex method comment.
     */
    public Spectrum1D getSlaveSpectrumFromXIndex(int ix) {
        return massSpectra[ix];
    }
    /**
     * getSlaveSpectrumFromPosition method comment.
     */
    public Spectrum1D getSlaveSpectrumFromXPosition(double x) {
        int index = getXData().indexAt(x);
        return massSpectra[index];
    }
    /**
     * getSlaveSpectrumFromIndex method comment.
     */
    public Spectrum1D getSlaveSpectrumFromYIndex(int iy) {
        return null;
    }
    /**
     * getSlaveSpectrumFromPosition method comment.
     */
    public Spectrum1D getSlaveSpectrumFromYPosition(double y) {
        return null;
    }
}
