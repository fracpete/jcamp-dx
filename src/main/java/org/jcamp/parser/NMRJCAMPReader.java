/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * ****************************************************************************
 */
package org.jcamp.parser;

import java.util.StringTokenizer;

import org.jcamp.math.IArray1D;
import org.jcamp.math.IArray2D;
import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.INMRSpectrum;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrum;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.NMR2DSpectrum2;
import org.jcamp.spectrum.NMRFIDSpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter between NMR spectrum class and JCAMPReader.
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 * Kerner</a>
 */
public class NMRJCAMPReader extends CommonSpectrumJCAMPReader implements
		ISpectrumJCAMPReader {

	private final static Logger log = LoggerFactory
			.getLogger(NMRJCAMPReader.class);

	private String mode;

	protected NMRJCAMPReader(String mode) {
		super();
		this.mode = mode;
	}

	/**
	 * create NMR FID spectrum from JCAMPBlock.
	 *
	 * @return {@link NMRSpectrum}
	 * @param block JCAMPBlock
	 * @exception JCAMPException if parsing fails
	 */
	private NMRSpectrum createFID(JCAMPBlock block) throws JCAMPException {
		NMRSpectrum spectrum = null;
		String nucleus = getNucleus(block);
		double reference = getShiftReference(block);
		double freq = getFrequency(block);
		if (!block.isNTupleBlock()) {
			if (log.isWarnEnabled()) {
				log.warn("illegal FID, assuming y values are real FID part");
			}

			// this should never be happen, let's assume we have only real part
			// of FID
			Unit xUnit = getXUnits(block);
			if (xUnit == null) {
				xUnit = CommonUnit.hertz;
			}
			Unit yUnit = getYUnits(block);
			if (yUnit == null) {
				yUnit = CommonUnit.intensity;
			}
			double xFactor = getXFactor(block);
			double yFactor = getYFactor(block);
			int nPoints = getNPoints(block);
			IOrderedDataArray1D x;
			IDataArray1D y;
			if (block.getDataRecord("XYDATA") != null) {
				double firstX = getFirstX(block);
				double lastX = getLastX(block);
				double[] intensities = getXYData(block, firstX, lastX, nPoints,
						xFactor, yFactor);
				if (intensities.length != nPoints) {
					throw new JCAMPException(
							"incorrect ##NPOINTS= or bad ##XYDATA=");
				}
				x = new EquidistantData(firstX, lastX, nPoints, xUnit);
				y = new ArrayData(intensities, yUnit);
			} else if (block.getDataRecord("XYPOINTS") != null) {
				double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
				x = new OrderedArrayData(xy[0], xUnit);
				y = new ArrayData(xy[1], yUnit);
			} else {
				throw new JCAMPException(
						"missing data: ##XYDATA= or ##XYPOINTS= required.");
			}
			spectrum = new NMRFIDSpectrum(x, y, nucleus, freq, reference);
		} else {
			JCAMPVariable x = block.getVariable("X");
			JCAMPVariable r = block.getVariable("R");
			JCAMPVariable i = block.getVariable("I");
			if (x == null) {
				throw new JCAMPException("missing X variable");
			}
			if (r == null) {
				throw new JCAMPException("missing real fid");
			}
			if (i == null) {
				throw new JCAMPException("missing imaginary fid");
			}
			Double firstX = x.getFirst();
			if (firstX == null) {
				throw new JCAMPException("missing required ##FIRST= for X");
			}
			Double lastX = x.getLast();
			if (lastX == null) {
				throw new JCAMPException("missing required ##LAST= for X");
			}
			if (x.getFactor() == null && log.isErrorEnabled()) {
				log.error("missing required ##FACTOR= for X");
			}

			if (r.getFactor() == null && log.isErrorEnabled()) {
				log.error("missing required ##FACTOR= for R");
			}
			if (i.getFactor() == null && log.isErrorEnabled()) {
				log.error("missing required ##FACTOR= for I");
			}

			Integer dim = x.getDimension();
			if (dim == null) {
				throw new JCAMPException("missing required ##VARDIM= for X");
			}
			Unit xUnit = x.getUnit();
			Unit rUnit = r.getUnit();
			JCAMPNTuplePage page0 = block.getNTuple().getPage(0);
			JCAMPNTuplePage page1 = block.getNTuple().getPage(1);
			if (page0.getDatatableVariableSymbols()[1].equalsIgnoreCase("I")) { // swap
				// pages,
				// imaginary
				// is
				// first
				JCAMPNTuplePage tmp = page0;
				page0 = page1;
				page1 = tmp;
			}
			IArray1D reals = page0.getXYData().getYArray();
			IArray1D imags = page1.getXYData().getYArray();
			IOrderedDataArray1D xData = new EquidistantData(
					firstX, lastX, dim, xUnit);
			IDataArray1D rData = new ArrayData(reals, rUnit);
			IDataArray1D iData = new ArrayData(imags, rUnit);
			spectrum = new NMRFIDSpectrum(xData, rData, iData, nucleus, freq,
					reference);
		}
		return spectrum;
	}

	/**
	 * create NMR full spectrum from JCAMPBlock.
	 *
	 * @return NMRSpectrum
	 * @param block JCAMPBlock
	 * @exception JCAMPException exception thrown if parsing fails.
	 */
	@Override
	protected NMRSpectrum createFS(JCAMPBlock block) throws JCAMPException {
		String nucleus = getNucleus(block);
		if (!block.isNTupleBlock()) { // standard JCAMP
			Unit xUnit = getXUnits(block);
			if (xUnit == null) {
				xUnit = CommonUnit.hertz;
			}
			Unit yUnit = getYUnits(block);
			if (yUnit == null) {
				yUnit = CommonUnit.intensity;
			}
			double xFactor = getXFactor(block);
			double yFactor = getYFactor(block);
			int nPoints = getNPoints(block);
			IOrderedDataArray1D x = null;
			IDataArray1D y = null;
			if (block.getDataRecord("XYDATA") != null) {
				double firstX = getFirstX(block);
				double lastX = getLastX(block);
				double[] intensities = getXYData(block, firstX, lastX, nPoints,
						xFactor, yFactor);
				if (intensities.length != nPoints) {
					/*
					 * throw new JCAMPException(
					 * "incorrect ##NPOINTS= or bad ##XYDATA=\n" + "found " +
					 * intensities.length + ", but ##NPOINTS= " + nPoints);
					 */
					log.warn("incorrect ##NPOINTS= or bad ##XYDATA=\n"
							+ "found " + intensities.length
							+ ", but ##NPOINTS= " + nPoints
							+ ", setting NPOINTS to " + intensities.length);
					nPoints = intensities.length;
				}
				x = new EquidistantData(firstX, lastX, nPoints, xUnit);
				y = new ArrayData(intensities, yUnit);
			} else if (block.getDataRecord("XYPOINTS") != null) {
				double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
				x = new OrderedArrayData(xy[0], xUnit);
				y = new ArrayData(xy[1], yUnit);
			} else {
				if (log.isErrorEnabled()) {
					log.error("missing data: ##XYDATA= or ##XYPOINTS= required.");
				}
			}

			double reference;
			try {
				int refPoint = getShiftReferencePoint(block);
				reference = x.pointAt(refPoint);
			} catch (JCAMPException e) {
				reference = getShiftReference(block);
			}
			double freq = getFrequency(block);
			return new NMRSpectrum(x, y, nucleus, freq, reference, true, mode);
		} else {
			// workaround for Bruker WinNMR real+imag 1D NMR spectra
			JCAMPVariable x = block.getVariable("X");
			JCAMPVariable r = block.getVariable("R");
			if (x == null) {
				throw new JCAMPException("missing X variable");
			}
			if (r == null) {
				throw new JCAMPException("missing real part");
			}
			Double firstX = x.getFirst();
			if (firstX == null) {
				throw new JCAMPException("missing required ##FIRST= for X");
			}
			Double lastX = x.getLast();
			if (lastX == null) {
				throw new JCAMPException("missing required ##LAST= for X");
			}

			if (x.getFactor() == null && log.isErrorEnabled()) {
				log.error("missing required ##FACTOR= for X");
			}
			if (r.getFactor() == null && log.isErrorEnabled()) {
				log.error("missing required ##FACTOR= for R");
			}

			Integer dim = x.getDimension();
			if (dim == null) {
				throw new JCAMPException("missing required ##VARDIM= for X");
			}
			Unit xUnit = x.getUnit();
			Unit rUnit = r.getUnit();
			// first page are reals (TODO: check)
			JCAMPNTuplePage page = block.getNTuple().getPage(0);
			if (page.getDatatableVariableSymbols()[1].equalsIgnoreCase("I")) {
				page = block.getNTuple().getPage(1);
			}
			IArray1D reals = page.getXYData().getYArray();
			IOrderedDataArray1D xData = new EquidistantData(
					firstX, lastX, dim, xUnit);
			IDataArray1D rData = new ArrayData(reals, rUnit);
			double reference;
			try {
				int refPoint = getShiftReferencePoint(block);
				reference = xData.pointAt(refPoint);
			} catch (JCAMPException e) {
				reference = getShiftReference(block);
			}
			return new NMRSpectrum(xData, rData, nucleus, getFrequency(block), reference,
					true, mode);
		}
	}

	/**
	 * create NMR peak table (peak spectrum) from JCAMPBlock.
	 *
	 * @return NMRSpectrum
	 * @param block JCAMPBlock
	 * @exception JCAMPException exception thrown if parsing fails.
	 */
	@Override
	protected NMRSpectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
		String nucleus = getNucleus(block);
		Unit xUnit = getXUnits(block);
		if (xUnit == null) {
			xUnit = CommonUnit.hertz;
		}
		Unit yUnit = getYUnits(block);
		if (yUnit == null) {
			yUnit = CommonUnit.intensity;
		}
		double xFactor = getXFactor(block);
		double yFactor = getYFactor(block);
		int refPoint;
		double reference;
		double freq = Double.NaN;
		int nPoints = getNPoints(block);
		Object[] tables = getPeaktable(block, nPoints, xFactor, yFactor);
		Peak1D[] peaks = (Peak1D[]) tables[0];
		double[][] xy = peakTableToPeakSpectrum(peaks);
		IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
		IDataArray1D y = new ArrayData(xy[1], yUnit);
		try {
			refPoint = getShiftReferencePoint(block);
			reference = x.pointAt(refPoint);
		} catch (JCAMPException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getLocalizedMessage(), e);
			}
			// often solventreference is missing in peak tables
			reference = getShiftReference(block);
		}
		try {
			freq = getFrequency(block);
		} catch (JCAMPException e) {
			// often observe frequency is missing in peak tables TODO: make
			// separate exception for that, don't catch all errors here

			if (log.isErrorEnabled()) {
				log.error(e.getLocalizedMessage());
			}
		}
		NMRSpectrum spectrum = new NMRSpectrum(x, y, nucleus, freq, reference, false, mode);
		spectrum.setPeakTable(peaks);
		if (tables.length > 1) {
			spectrum.setPatternTable((Pattern[]) tables[1]);
			if (tables.length > 2) {
				spectrum.setAssignments((Assignment[]) tables[2]);
			}
		}
		return spectrum;
	}

	/**
	 * createSpectrum method comment.
	 */
	@Override
	public ISpectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
		if (block.getSpectrumType() != ISpectrumIdentifier.NMR) {
			throw new JCAMPException("adapter missmatch");
		}
		boolean isFID = false;
		JCAMPDataRecord ldrDataType = block.getDataRecord("DATATYPE");
		String dataType = ldrDataType.getContent().toUpperCase();
		if (dataType.indexOf("FID") >= 0) {
			isFID = true;
		}
		// currently many NMR with NTUPLES are FIDs, should generate an error
		// when 2D NMR is available
		//
		// Bruker writes X->R/I NMR spectra with WinNMR
		//
		INMRSpectrum spectrum = null;
		if (!isFID && block.isNTupleBlock()) {
			JCAMPNTuple ntuple = block.getNTuple();
			JCAMPVariable x = ntuple.getVariable("X");
			if (x != null) {
				if (x.getUnit().equals(CommonUnit.second)) {
					if (log.isWarnEnabled()) {
						log.warn("NMR FID without NMR FID data type");
					}
					isFID = true;
				}
				if (log.isWarnEnabled()) {
					log.warn("nD NMR or NMR spectra with real and imaginary part");
				}
			} else {
				// might be 2D
				JCAMPVariable f1 = ntuple.getVariable("F1"),
						f2 = ntuple.getVariable("F2");
				NMR2DSpectrum2 spectrum2D = new NMR2DSpectrum2(new JCAMPVariable[]{f1, f2},
						getNucleus2D(block), getFrequency2D(block), mode);
				// values ranges for x,y,z
				int nPages = ntuple.numPages();
				for (int i = 0; i < nPages; i++) {
					JCAMPNTuplePage page = ntuple.getPage(i);
					IArray2D xyData = page.getXYData();
					spectrum2D.addDataRow(xyData.getYArray());
				}
				spectrum = spectrum2D;
			}
		}
		if (spectrum == null) {
			if (isFID) {
				spectrum = createFID(block);
			} else {
				BlockType type = block.getBlockType();
				if (type.equals(BlockType.FULLSPECTRUM)) {
					spectrum = createFS(block);
				} else if (type.equals(BlockType.PEAKTABLE)) {
					spectrum = createPeakTable(block);
				} else if (type.equals(BlockType.ASSIGNMENT)) {
					spectrum = createPeakTable(block);
				} else // never reached
				{
					throw new JCAMPException("illegal block type");
				}
			}
		}

		// info about measurement
		String solvent = getSolvent(block);
		spectrum.setSolvent(solvent);
		spectrum.setTemperature(getTemperatureKelvin(block));

		if (spectrum instanceof NMRSpectrum) {
			NMRSpectrum spectrum1D = (NMRSpectrum) spectrum;
			spectrum1D.setSolventReference(getSolventReference(block, solvent));
			setNotes(block, spectrum1D);
			getLinkedAssignments(block, spectrum1D);
		}

		return spectrum;
	}

	/**
	 * gets ##$OFFSET= content (Bruker specific)
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	private double getBrukerOffset(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrOffset = block.getDataRecord("$OFFSET");
		if (ldrOffset == null) {
			throw new JCAMPException();
		}
		String os = ldrOffset.getContent();
		return Double.valueOf(os);
	}

	/**
	 * Gets ##$SF= content (Bruker specific)
	 *
	 */
	private double getBrukerSF(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrSF = block.getDataRecord("$SF");
		if (ldrSF == null) {
			throw new JCAMPException("No data record for $SF");
		}
		String sf = ldrSF.getContent();
		return Double.valueOf(sf);
	}

	/**
	 * gets ##$SW= content (Bruker specific)
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	private double getBrukerSW(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrSW = block.getDataRecord("$SW");
		if (ldrSW == null) {
			throw new JCAMPException();
		}
		String sw = ldrSW.getContent();
		return Double.valueOf(sw);
	}

	/**
	 * gets ##.OBSERVEFREQUENCY= content
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	private double getFrequency(JCAMPBlock block) throws JCAMPException {
		double freq = Double.NaN;
		JCAMPDataRecord ldrFrequency = block.getDataRecord(".OBSERVEFREQUENCY");
		if (ldrFrequency == null) {
			// first try Bruker labels before quitting
			try {
				freq = getBrukerSF(block);
			} catch (JCAMPException n) {
				// again an error, throw original exception
				throw new JCAMPException(
						"missing required label ##.OBSERVEFREQUENCY=");
			}
			if (log.isWarnEnabled()) {
				log.warn("missing required label ##.OBSERVEFREQUENCY=, using Bruker custom labels");
			}
			return freq;
		} else {
			freq = Double.valueOf(ldrFrequency.getContent());
			return freq;
		}
	}

	/**
	 * find linked peak tables and assignments.
	 *
	 * NOTE: works only with SpecInfo convention
	 *
	 * @param block com.creon.chem.jcamp.JCAMPBlock
	 * @param spectrum com.creon.chem.spectrum.NMRSpectrum
	 */
	private void getLinkedAssignments(JCAMPBlock block, NMRSpectrum spectrum)
			throws JCAMPException {
		JCAMPBlock[] linked = block.getReferences();
		for (int i = 0; i < linked.length; i++) {
			if (linked[i].isStructureBlock() || linked[i].isLinkBlock()) {
				continue;
			}
			BlockType type = linked[i].getBlockType();
			if (linked[i].getSpectrumType() != ISpectrumIdentifier.NMR) {
				continue;
			}
			if (type.equals(BlockType.PEAKTABLE)
					|| type.equals(BlockType.ASSIGNMENT)) {
				NMRSpectrum linkSpectrum = createPeakTable(linked[i]);
				// check if nucleus, frequency, and solvent reference are equal
				if (!linkSpectrum.getNucleus().equals(spectrum.getNucleus())) {
					continue;
				}
				if (linkSpectrum.getFrequency() != linkSpectrum.getFrequency()) // Double.NaN
				{
					linkSpectrum.setFrequency(spectrum.getFrequency());
				}
				if (linkSpectrum.getReference() != linkSpectrum.getReference()) // Double.NaN
				{
					linkSpectrum.setReference(spectrum.getReference());
				}
				if (linkSpectrum.hasPeakTable()) {
					spectrum.setPeakTable(linkSpectrum.getPeakTable());
				}
				if (linkSpectrum.hasPatternTable()) {
					spectrum.setPatternTable(linkSpectrum.getPatternTable());
				}
				if (linkSpectrum.hasAssignments()) {
					spectrum.setAssignments(linkSpectrum.getAssignments());
				}
			}
		}
	}

	/**
	 * gets ##.OBSERVENUCLEUS= content
	 *
	 * @return String
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	private String getNucleus(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrNucleus = block.getDataRecord(".OBSERVENUCLEUS");
		if (ldrNucleus == null) {
			throw new JCAMPException(
					"missing required label: ##.OBSERVENUCLEUS=");
		}
		return ldrNucleus.getContent();
	}

	private String[] getNucleus2D(JCAMPBlock block) throws JCAMPException {
		String[] byIndexes = new String[2];
		JCAMPDataRecord ldrNuc = block.getDataRecord("$NUC1");
		if (ldrNuc != null) {
			int i = 0;
			JCAMPDataRecord.Iterator it = ldrNuc.iterator();
			do {
				byIndexes[i] = clean(ldrNuc.getContent());
				i++;
				if (i >= byIndexes.length) {
					break;
				}
				ldrNuc = it.next();
			} while (it.hasNext());
		}

		JCAMPDataRecord ldrNucleus = block.getDataRecord(".NUCLEUS");
		if (ldrNucleus == null) {
			log.warn("missing required label: ##.NUCLEUS=");
			return byIndexes;
		}

		// double-check
		String[] nucleus = ldrNucleus.getContent().split(",");
		for (int i = 0, iMax = Math.min(byIndexes.length, nucleus.length); i < iMax; i++) {
			nucleus[i] = nucleus[i].trim();
			if (byIndexes[i] != null && !byIndexes[i].equals(nucleus[i])) {
				log.warn("ambigous nuclues information, " + nucleus[i] + " vs. " + byIndexes[i]);
			}
		}
		return nucleus;
	}

	private double[] getFrequency2D(JCAMPBlock block) throws JCAMPException {
		double[] byIndexes = new double[2];

		JCAMPDataRecord ldrFreq = block.getDataRecord("$BF1");
		if (ldrFreq != null) {
			int i = 0;
			JCAMPDataRecord.Iterator it = ldrFreq.iterator();
			do {
				try {
					byIndexes[i] = Double.parseDouble(ldrFreq.getContent());
				} catch (NumberFormatException ex) {
				}
				i++;
				if (i >= byIndexes.length) {
					break;
				}
				ldrFreq = it.next();
			} while (it.hasNext());
		}
		return byIndexes;
	}

	/**
	 * gets ##.SHIFTREFERENCE= content NOTE: this is a open issue in the JCAMP
	 * spec. With standard JCAMP 5.00 it is not possible to specify a reference
	 * point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE0
	 * is marked as optional instead of required.
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	private double getShiftReference(JCAMPBlock block) throws JCAMPException {
		// first try Bruker custom labels
		try {
			double freq = getBrukerSF(block),
					reference = freq * (getBrukerSW(block) - getBrukerOffset(block));
			if (log.isWarnEnabled()) {
				log.warn("missing ##.SHIFTREFERENCE=, using Bruker custom labels");
			}
			return reference;
		} catch (JCAMPException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getLocalizedMessage(), e);
			}
			JCAMPDataRecord ldrReferencePoint = block
					.getDataRecord("$REFERENCEPOINT");
			if (ldrReferencePoint == null) {
				if (log.isWarnEnabled()) {
					log.warn("missing shift reference: assuming 0.0 Hz");
				}
				return 0.0;
			} else {
				if (log.isWarnEnabled()) {
					log.warn("missing ##.SHIFTREFERENCE=, using SpecInfo ##$REFERENCEPOINT=");
				}
				return Double.valueOf(ldrReferencePoint.getContent());
			}
		}
	}

	/**
	 * gets ##.SHIFTREFERENCE= content NOTE: this is a open issue in the JCAMP
	 * spec. With standard JCAMP 5.00 it is not possible to specify a reference
	 * point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE0
	 * is marked as optional instead of required.
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	private int getShiftReferencePoint(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldrShiftReference = block
				.getDataRecord(".SHIFTREFERENCE");
		if (ldrShiftReference == null) {
			if (log.isWarnEnabled()) {
				log.warn("Missing data record .SHIFTREFERENCE, assuming 0");
			}
			return 0;

		} else {
			String shiftRef = ldrShiftReference.getContent();
			StringTokenizer commaTokenizer = new java.util.StringTokenizer(
					shiftRef, ",");
			commaTokenizer.nextToken(); // skip INTERNAL/EXTERNAL
			commaTokenizer.nextToken(); // skip solvent
			try {
				int shiftPoint = Integer.parseInt(commaTokenizer.nextToken()
						.trim());
				return shiftPoint;
			} catch (NumberFormatException ex) {
				// shk3: looking at the code where the method is used, it looks
				// like
				// there is a fallback, but the fallback only works if
				// a JCAMPException is thrown
				throw new JCAMPException(ex.getMessage());
			}
		}
	}

	/**
	 * gets ##.SHIFTREFERENCE= content NOTE: this is a open issue in the JCAMP
	 * spec. With standard JCAMP 5.00 it is not possible to specify a reference
	 * point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE0
	 * is marked as optional instead of required.
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @exception JCAMPException The exception description.
	 */
	protected String getSolvent(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldr = block.getDataRecord(".SHIFTREFERENCE");
		if (ldr == null) {
			ldr = block.getDataRecord("$SOLVENT"); // Bruker??? or SpecInfo???
			if (ldr == null) {
				return "TMS";
			} else {
				return clean(ldr.getContent());
			}
		} else {
			String shiftRef = ldr.getContent();
			StringTokenizer commaTokenizer = new java.util.StringTokenizer(
					shiftRef, ",");
			commaTokenizer.nextToken(); // skip INTERNAL/EXTERNAL
			return commaTokenizer.nextToken();
		}
	}

	protected Double getTemperatureKelvin(JCAMPBlock block) throws JCAMPException {
		JCAMPDataRecord ldr = block.getDataRecord("$TE");
		if (ldr != null) {
			try {
				return Double.parseDouble(ldr.getContent());
			} catch (NumberFormatException ex) {
			}
		}
		return null;
	}

	/**
	 * gets solvent reference in ppm from either ##.SHIFTREFERENCE= or
	 * ##.SOLVENTREFERENCE= content NOTE: this is a open issue in the JCAMP
	 * spec. With standard JCAMP 5.00 it is not possible to specify a reference
	 * point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE=
	 * is marked as optional instead of required.
	 *
	 * @return double
	 * @param block JCAMPBlock
	 * @param solvent the solvent
	 * @exception JCAMPException The exception description.
	 */
	protected double getSolventReference(JCAMPBlock block, String solvent)
			throws JCAMPException {
		JCAMPDataRecord ldr = block.getDataRecord(".SHIFTREFERENCE");
		if (ldr == null) {
			ldr = block.getDataRecord(".SOLVENTREFERENCE");
			if (ldr == null) {
				if (solvent.equals("TMS")) {
					return 0.0;
				} else {
					if (log.isWarnEnabled()) {
						log.warn("missing required label for non-TMS solvent: ##.SOLVENTREFERENCE=, setting reference to 0.0");
					}
					return 0.0;
				}
			} else {
				// fix for ##.SHIFT REFERENCE= (INTERNAL, CDCl3, 0, 17.4877)
				return Double.parseDouble(ldr.getContent().replaceAll("[\\(\\)]", ""));
			}

		} else {
			String shiftRef = ldr.getContent();
			String[] tokens = shiftRef.split(",", -1);
			// shk3 changed this to using split since the StringTokenizer
			// ignores empty fields
			// and the .SHIFTREFERENCE may look like ,TMS,0,0.0
			// StringTokenizer commaTokenizer = new
			// java.util.StringTokenizer(shiftRef, ",");
			// commaTokenizer.nextToken(); // skip INTERNAL/EXTERNAL
			// commaTokenizer.nextToken(); // skip solvent
			// commaTokenizer.nextToken(); // skip shiftPoint
			// double solvRef =
			// Double.parseDouble(commaTokenizer.nextToken().trim());
			double solvRef = Double.parseDouble(tokens[3]);
			return solvRef;
		}
	}
}
