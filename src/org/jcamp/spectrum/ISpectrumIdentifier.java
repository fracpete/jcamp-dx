package org.jcamp.spectrum;

/**
 * IDs for spectrum classes
 * @author Thomas Weber
 */
public interface ISpectrumIdentifier {
    public final static int UNKNOWN = 0;
    public final static int INFRARED = 2 << 0;
    public final static int IR = 2 << 0;
    public final static int RAMAN = 2 << 1;
    public final static int ULTRAVIOLET = 2 << 2;
    public final static int UV = 2 << 2;
    public final static int FLUORESCENCE = 2 << 3;
    public final static int NMR = 2 << 4;
    public final static int MASS = 2 << 5;
    public final static int MS = 2 << 5;
    public final static int CHROMATOGRAM = 2 << 16;
    public final static int GC = 2 << 17 | CHROMATOGRAM;
    public final static int LC = 2 << 18 | CHROMATOGRAM;
    public final static int FID = 2 << 23;
    public final static int SPEC2D = 2 << 24;
    public final static int NMRFID = NMR | FID;
    public final static int NMR2D = NMR | SPEC2D;
    public final static int FLUORESCENCE2D = FLUORESCENCE | SPEC2D;
    public final static int GCMS = MS | GC;
    public final static int LCMS = MS | LC;
}
