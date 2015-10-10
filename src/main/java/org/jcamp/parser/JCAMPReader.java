/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.Spectrum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for the JCAMP-DX spectrum format.
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public class JCAMPReader {

	private static Hashtable<Integer, ISpectrumJCAMPReader> adapters = new Hashtable<Integer, ISpectrumJCAMPReader>(
			20);

	private final static IErrorHandler DEFAULT_ERROR_HANDLER = new ErrorHandlerAdapter() {
		@Override
		public void error(String msg) throws JCAMPException {
			log.error(msg);
			throw new JCAMPException("ERROR! " + msg);
		}

		@Override
		public void fatal(String msg) throws JCAMPException {
			log.fatal(msg);
			throw new JCAMPException("FATAL ERROR! " + msg);
		}

		@Override
		public void warn(String msg) throws JCAMPException {
			log.warn(msg);
		}
	};
	private static boolean isValidating = true;
	private final static Logger lg = LoggerFactory.getLogger(JCAMPReader.class);
	private static Log log = LogFactory.getLog(JCAMPReader.class);
	private static String mode = JCAMPReader.STRICT;
	public final static String RELAXED = "relaxed";
	/**
	 * these are flags for initialising the reader as strict or relaxed.
	 * Differences in behaviour are: - STRICT uses the SHIFT REFERENCE for
	 * calculating ppm in NMR, relaxed ignores them. We found many files have
	 * the SHIFT REFERENCE but are actually already shifted.
	 */
	public final static String STRICT = "strict";
	private static JCAMPReader theInstance = null;

	/**
	 * access method for JCAMPReader singleton instance. gives a validating
	 * instance
	 *
	 * @return JCAMPReader
	 */
	public static JCAMPReader getInstance() {
		if (theInstance == null || JCAMPReader.isValidating != true
				|| JCAMPReader.mode != JCAMPReader.STRICT) {
			theInstance = new JCAMPReader(true, JCAMPReader.STRICT);
			initAdapters();
		}
		return theInstance;
	}

	/**
	 * access method for JCAMPReader singleton instance.
	 *
	 * @param isValidating
	 *            should the parser be validating? often files are not strictly
	 *            correct
	 * @return JCAMPReader
	 */
	public static JCAMPReader getInstance(boolean isValidating, String mode) {
		if (theInstance == null || JCAMPReader.isValidating != isValidating
				|| JCAMPReader.mode != mode) {
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
		adapters.put(new Integer(ISpectrumIdentifier.NMR), new NMRJCAMPReader(
				mode));
		adapters.put(new Integer(ISpectrumIdentifier.IR), new IRJCAMPReader());
		adapters.put(new Integer(ISpectrumIdentifier.UV), new UVJCAMPReader());
		adapters.put(new Integer(ISpectrumIdentifier.MS), new MSJCAMPReader());
		adapters.put(new Integer(ISpectrumIdentifier.RAMAN),
				new RamanJCAMPReader());
		adapters.put(new Integer(ISpectrumIdentifier.CHROMATOGRAM),
				new ChromatogramJCAMPReader());
		adapters.put(new Integer(ISpectrumIdentifier.FLUORESCENCE),
				new FluorescenceJCAMPReader());
	}

	private IErrorHandler errorHandler = DEFAULT_ERROR_HANDLER;

	private int idoffirstspectrum = -1;

	private JCAMPBlock rootblock = null;

	/**
	 * JCAMPReader constructor. gives a validating reader.
	 */
	private JCAMPReader() {
		super();
	}

	/**
	 * JCAMPReader constructor comment.
	 *
	 * @param isValidating
	 *            should the parser be validating? often files are not strictly
	 *            correct
	 */
	private JCAMPReader(boolean isValidating, String mode) {
		super();
		JCAMPReader.isValidating = isValidating;
		JCAMPReader.mode = mode;
	}

	public Spectrum createSpectrum(File file) throws IOException,
	JCAMPException {
		return createSpectrum(new FileReader(file));

	}

	/**
	 * Create {@link Spectrum} from {@link JCAMPBlock}.
	 *
	 * @return {@link Spectrum}
	 * @throws JCAMPException
	 */
	public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
		if (block.isLinkBlock()) {
			if (lg.isWarnEnabled()) {
				lg.warn("compound JCAMP encountered: using first spectrum block");
			}
			rootblock = block;
			block = findFirstSpectrumBlock(block);
		}
		ISpectrumJCAMPReader reader = findAdapter(block.getSpectrumID());
		if (reader == null) {
			throw new IllegalArgumentException("spectrum type not implemented");
		}
		errorHandler.fatal("spectrum type not implemented");
		return reader.createSpectrum(block);
	}

	/**
	 * create spectrum from JCAMPBlock.
	 *
	 * @return Spectrum
	 * @param block
	 *            JCAMPBlock
	 * @param blockID
	 *            int
	 */
	public Spectrum createSpectrum(JCAMPBlock block, int blockID)
			throws JCAMPException {
		return createSpectrum(block.getBlock(blockID));
	}

	/**
	 * Create spectrum from {@link Reader reader}. Caller must close reader.
	 *
	 * @return {@link Spectrum}
	 * @param reader
	 *            {@link Reader reader}
	 * @throws JCAMPException
	 * @throws IOException
	 */
	public Spectrum createSpectrum(Reader reader) throws IOException,
	JCAMPException {
		StringBuilder fileData = new StringBuilder();
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		return createSpectrum(fileData.toString());
	}

	/**
	 * Create {@link Spectrum} from JCAMP-DX string.
	 *
	 * @return {@link Spectrum}
	 * @param jcamp
	 *            JCAMP-DX string JCAMP-DX source
	 * @throws JCAMPException
	 *
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
	 * @param spectrumID
	 *            int
	 */
	private ISpectrumJCAMPReader findAdapter(int spectrumID) {
		ISpectrumJCAMPReader adapter = adapters.get(new Integer(spectrumID));
		return adapter;
	}

	/**
	 * find first spectrum block within a compound JCAMP link block, first tries
	 * to find a full spectrum block, otherwise uses first block encountered
	 *
	 * @return com.creon.chem.jcamp.JCAMPBlock
	 * @param block
	 *            com.creon.chem.jcamp.JCAMPBlock
	 */
	private JCAMPBlock findFirstSpectrumBlock(JCAMPBlock block)
			throws JCAMPException {
		// first try, returning only full spectra
		Collection<JCAMPBlock> blocks = block.getChildBlocks();
		Iterator<JCAMPBlock> it = blocks.iterator();
		if (blocks != null) {
			while (it.hasNext()) {
				JCAMPBlock b = it.next();
				if (b.isStructureBlock() || b.isLinkBlock())
					continue;
				JCAMPDataRecord dataTypeLDR = b.getDataRecord("DATATYPE");
				if (dataTypeLDR == null)
					continue; // bad block
				String dataType = dataTypeLDR.getContent().toUpperCase();
				if (dataType.endsWith("TABLE")
						|| dataType.endsWith("ASSIGNMENTS"))
					continue;
				idoffirstspectrum = b.getID();
				return b;
			}
		}
		// second try, returning any spectrum
		blocks = block.getChildBlocks();
		Iterator<JCAMPBlock> it2 = blocks.iterator();
		if (blocks != null) {
			while (it2.hasNext()) {
				JCAMPBlock b = it2.next();
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
	 * shk3: This method gives you the id of the first child block in case of
	 * multi block files. If a JCAMP file contains several blocks (blocks means
	 * basically spectra in JCAMP), the spectrum returned by create spectrum is
	 * the first subblock. In order to filter this out, you can get its id here.
	 *
	 * @return The id of the first child block.
	 */
	public int getIdoffirstspectrum() {
		return idoffirstspectrum;
	}

	/**
	 * shk3: This method gives you the root block in order to retrieve other
	 * blocks in case of multi block files. If a JCAMP file contains several
	 * blocks (blocks means basically spectra in JCAMP), the spectrum returned
	 * by create spectrum is the first subblock.
	 *
	 * @return The root block.
	 */
	public JCAMPBlock getRootblock() {
		return rootblock;
	}

	/**
	 * sets the error handler.
	 *
	 * @param newErrorHandler
	 *            com.creon.chem.jcamp.IErrorHandler
	 */
	public void setErrorHandler(IErrorHandler newErrorHandler) {
		errorHandler = newErrorHandler;
	}
}
