package org.jcamp.parser;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.Spectrum;
/**
 * reader for JCAMP-DX spectrum format.
 * @author Thomas Weber
 */
public class JCAMPReader {
    private static Log log = LogFactory.getLog(JCAMPReader.class);
    private final static IErrorHandler DEFAULT_ERROR_HANDLER = new ErrorHandlerAdapter() {
        public void fatal(String msg) throws JCAMPException {
            log.fatal(msg);
            throw new JCAMPException("FATAL ERROR! " + msg);
        }
        public void error(String msg) throws JCAMPException {
            log.error(msg);
            throw new JCAMPException("ERROR! " + msg);
        }
        public void warn(String msg) throws JCAMPException {
            log.warn(msg);
        }
    };
    private IErrorHandler errorHandler = DEFAULT_ERROR_HANDLER;
    private static JCAMPReader theInstance = null;
    private static Hashtable adapters = new Hashtable(20);
    private static boolean isValidating=true;
    private static String mode=JCAMPReader.STRICT;
    private JCAMPBlock rootblock=null;
	private int idoffirstspectrum=-1;
	/**
     *these are flags for initialising the reader as strict or relaxed.
     *Differences in behaviour are:
     *- STRICT uses the SHIFT REFERENCE for calculating ppm in NMR, relaxed ignores them.
     *We found many files have the SHIFT REFERENCE but are actually already shifted.
	 */
	public final static String STRICT="strict";
	public final static String RELAXED="relaxed";


    /**
     * shk3: This method gives you the root block in order to retrieve other blocks in 
     * case of multi block files. If a JCAMP file contains several blocks (blocks means basically spectra
     * in JCAMP), the spectrum returned by create spectrum is the first subblock.
     * 
     * @return The root block.
     */
    public JCAMPBlock getRootblock() {
		return rootblock;
	}

    /**
     * shk3: This method gives you the id of the first child block in case of multi 
     * block files. If a JCAMP file contains several blocks (blocks means basically spectra
     * in JCAMP), the spectrum returned by create spectrum is the first subblock. In order
     * to filter this out, you can get its id here.
     * 
     * @return The id of the first child block.
     */
    public int getIdoffirstspectrum() {
		return idoffirstspectrum;
	}

    /**
     * JCAMPReader constructor comment.
     * @param isValidating should the parser be validating? often files are not strictly correct
     */
    private JCAMPReader(boolean isValidating, String mode) {
        super();
    	JCAMPReader.isValidating=isValidating;
    	JCAMPReader.mode=mode;
    }

    
    /**
     * JCAMPReader constructor. gives a validating reader.
     */
    private JCAMPReader() {
        super();
    }

    /**
     * create spectrum from JCAMPBlock.
     * 
     * @return Spectrum
     * @param blockID int
     */
    public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
        if (block.isLinkBlock()) {
            errorHandler.warn("compound JCAMP encountered: using first spectrum block");
            rootblock=block;
            block = findFirstSpectrumBlock(block);
        }
        ISpectrumJCAMPReader reader = findAdapter(block.getSpectrumID());
        if (reader == null)
            errorHandler.fatal("spectrum type not implemented");
        return reader.createSpectrum(block);
    }

    /**
     * create spectrum from JCAMPBlock.
     * 
     * @return Spectrum
     * @param block JCAMPBlock
     * @param blockID int
     */
    public Spectrum createSpectrum(JCAMPBlock block, int blockID) throws JCAMPException {
        return createSpectrum(block.getBlock(blockID));
    }

    /**
     * create spectrum from JCAMP-DX string.
     * 
     * @return Spectrum
     * @param jcamp JCAMP-DX source
     * @exception JCAMPException The exception description.
     */
    public Spectrum createSpectrum(String jcamp) throws JCAMPException {
        JCAMPBlock block = new JCAMPBlock(jcamp, errorHandler);
        block.setValidating(JCAMPReader.isValidating);
        return createSpectrum(block);
    }

    /**
     * find JCAMPAdapter for specific spectrum ID.
     * 
     * @return ISpectrumJCAMPReader
     * @param spectrumID int
     */
    private ISpectrumJCAMPReader findAdapter(int spectrumID) {
        ISpectrumJCAMPReader adapter = (ISpectrumJCAMPReader) adapters.get(new Integer(spectrumID));
        return adapter;
    }

    /**
     * find first spectrum block within a compound JCAMP link block,
     * first tries to find a full spectrum block, otherwise uses first block encountered
     * 
     * @return com.creon.chem.jcamp.JCAMPBlock
     * @param block com.creon.chem.jcamp.JCAMPBlock
     */
    private JCAMPBlock findFirstSpectrumBlock(JCAMPBlock block) throws JCAMPException {
        // first try, returning only full spectra
        Enumeration blocks = block.getBlocks();
        if (blocks != null) {
            while (blocks.hasMoreElements()) {
                JCAMPBlock b = (JCAMPBlock) blocks.nextElement();
                if (b.isStructureBlock() || b.isLinkBlock())
                    continue;
                JCAMPDataRecord dataTypeLDR = b.getDataRecord("DATATYPE");
                if (dataTypeLDR == null)
                    continue; //bad block
                String dataType = dataTypeLDR.getContent().toUpperCase();
                if (dataType.endsWith("TABLE") || dataType.endsWith("ASSIGNMENTS"))
                    continue;
                idoffirstspectrum=b.getID();
                return b;
            }
        }
        // second try, returning any spectrum
        blocks = block.getBlocks();
        if (blocks != null) {
            while (blocks.hasMoreElements()) {
                JCAMPBlock b = (JCAMPBlock) blocks.nextElement();
                if (b.isStructureBlock() || b.isLinkBlock())
                    continue;
                return b;
            }
        }
        errorHandler.fatal("link block contains no spectrum block");
        return null;
    }

    /**
     * gets current error handler.
     * 
     * @return com.creon.chem.jcamp.IErrorHandler
     */
    public IErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * access method for JCAMPReader singleton instance. gives a validating instance
     * 
     * @return JCAMPReader
     */
    public static JCAMPReader getInstance() {
        if (theInstance == null || JCAMPReader.isValidating!=true || JCAMPReader.mode!=JCAMPReader.STRICT) {
            theInstance = new JCAMPReader(true, JCAMPReader.STRICT);
            initAdapters();
        }
        return theInstance;
    }

    /**
     * access method for JCAMPReader singleton instance.
     * 
     * @param isValidating should the parser be validating? often files are not strictly correct
     * @return JCAMPReader
     */
    public static JCAMPReader getInstance(boolean isValidating, String mode) {
        if (theInstance == null || JCAMPReader.isValidating!=isValidating || JCAMPReader.mode!=mode) {
            theInstance = new JCAMPReader(isValidating, mode);
            initAdapters();
        }
        return theInstance;
    }
    
    /**
     * initialize adapter map
     * 
     */
    private static void initAdapters() {
        adapters.put(new Integer(ISpectrumIdentifier.NMR), new NMRJCAMPReader(mode));
        adapters.put(new Integer(ISpectrumIdentifier.IR), new IRJCAMPReader());
        adapters.put(new Integer(ISpectrumIdentifier.UV), new UVJCAMPReader());
        adapters.put(new Integer(ISpectrumIdentifier.MS), new MSJCAMPReader());
        adapters.put(new Integer(ISpectrumIdentifier.RAMAN), new RamanJCAMPReader());
        adapters.put(new Integer(ISpectrumIdentifier.CHROMATOGRAM), new ChromatogramJCAMPReader());
        adapters.put(new Integer(ISpectrumIdentifier.FLUORESCENCE), new FluorescenceJCAMPReader());
    }

    /**
     * sets the error handler.
     * 
     * @param newErrorHandler com.creon.chem.jcamp.IErrorHandler
     */
    public void setErrorHandler(IErrorHandler newErrorHandler) {
        errorHandler = newErrorHandler;
    }
}