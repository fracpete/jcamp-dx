/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import org.jcamp.math.IArray2D;
import org.jcamp.math.IOrderedArray1D;
import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.GCMSSpectrum;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.MassSpectrum;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between MS spectrum class and JCAMPWriter.
 *
 * @author Thomas Weber
 */
public class MSJCAMPReader extends CommonSpectrumJCAMPReader implements
		ISpectrumJCAMPReader {

	/**
	 * MSJCAMPAdapter constructor comment.
	 */
	protected MSJCAMPReader() {
		super();
	}

	/**
	 * gets mass spectrum.
	 * 
	 * @return com.creon.chem.spectrum.MassSpectrum
	 * @param block
	 *            com.creon.chem.jcamp.JCAMPBlock
	 */
	private MassSpectrum createFS(JCAMPBlock block) throws JCAMPException {
		MassSpectrum spectrum;
		Unit xUnit = getXUnits(block);
		if (xUnit == null)
			xUnit = CommonUnit.mz;
		Unit yUnit = getYUnits(block);
		if (yUnit == null)
			yUnit = CommonUnit.percentIntensity;
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
			if (intensities.length != nPoints)
				throw new JCAMPException(
						"incorrect ##NPOINTS= or bad ##XYDATA=");
			x = new EquidistantData(firstX, lastX, nPoints, xUnit);
			y = new ArrayData(intensities, yUnit);
		} else if (block.getDataRecord("XYPOINTS") != null) {
			double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
			x = new OrderedArrayData(xy[0], xUnit);
			y = new ArrayData(xy[1], yUnit);
		} else
			throw new JCAMPException(
					"missing data: ##XYDATA= or ##XYPOINTS= required.");
		spectrum = new MassSpectrum(x, y, true);
		return spectrum;
	}

	/**
	 * NTUPLE mass spectra.
	 * 
	 * @return com.creon.chem.spectrum.GCMSSpectrum
	 * @param block
	 *            com.creon.chem.jcamp.JCAMPBlock
	 */
	private GCMSSpectrum createGCMS(JCAMPBlock block) throws JCAMPException {
		GCMSSpectrum spectrum;
		String title = getTitle(block);
		JCAMPNTuple ntuple = block.getNTuple();
		int nPages = ntuple.numPages();
		MassSpectrum[] ms = new MassSpectrum[nPages];

		double[] times = getRetentionTimes(ntuple);
		double[] tics = getTICs(ntuple);
		Unit xUnit = getXUnits(block);
		if (xUnit == null)
			xUnit = CommonUnit.mz;
		Unit yUnit = getYUnits(block);
		if (yUnit == null)
			yUnit = CommonUnit.relativeAbundance;
		for (int i = 0; i < nPages; i++) {
			JCAMPNTuplePage page = block.getNTuple().getPage(i);
			IArray2D msxy = page.getXYData();
			/*
			 * double[][] msxy = getNTupleXYPoints(block, page); for (int j = 0;
			 * j < msxy[0].length; j++) { msxy[0][j] *= xFactor; msxy[1][j] *=
			 * yFactor; }
			 */
			IOrderedDataArray1D x = new OrderedArrayData(
					(IOrderedArray1D) msxy.getXArray(), xUnit);
			IDataArray1D y = new ArrayData(msxy.getYArray(), yUnit);

			ms[i] = new MassSpectrum(x, y, false);
			if (Double.isNaN(times[i]))
				ms[i].setTitle(title + "(mass spectrum [" + i + "]");
			else
				ms[i].setTitle(title + "(mass spectrum [" + i + "]: t="
						+ times[i] + ")");
		}
		IOrderedDataArray1D x = new OrderedArrayData(times, CommonUnit.second);
		if (tics != null) {
			IDataArray1D y = new ArrayData(tics, CommonUnit.intensity);
			spectrum = new GCMSSpectrum(x, y, ms);
		} else
			spectrum = new GCMSSpectrum(x, ms);
		return spectrum;
	}

	/**
	 * create mass spectrum peak spectrum from JCAMPBlock.
	 * 
	 * @return MassSpectrum
	 * @param block
	 *            JCAMPBlock
	 * @exception JCAMPException
	 *                exception thrown if parsing fails.
	 */
	private MassSpectrum createPeakTable(JCAMPBlock block)
			throws JCAMPException {
		MassSpectrum spectrum = null;
		String title = getTitle(block);
		Unit xUnit = null;
		try {
			xUnit = getXUnits(block);
		} catch (JCAMPException e) {
			xUnit = CommonUnit.mz;
		}
		Unit yUnit = null;
		try {
			yUnit = getYUnits(block);
		} catch (JCAMPException e) {
			yUnit = CommonUnit.intensity;
		}
		double xFactor;
		try {
			xFactor = getXFactor(block);
		} catch (JCAMPException e) {
			xFactor = 1.0;
		}
		double yFactor;
		try {
			yFactor = getYFactor(block);
		} catch (JCAMPException e) {
			yFactor = 1.0;
		}
		int nPoints = getNPoints(block);
		Object[] tables = getPeaktable(block, nPoints, xFactor, yFactor);
		if (tables == null)
			throw new JCAMPException("missing data table");
		Peak1D[] peaks = (Peak1D[]) tables[0];
		double[][] xy = peakTableToPeakSpectrum(peaks);
		IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
		IDataArray1D y = new ArrayData(xy[1], yUnit);
		spectrum = new MassSpectrum(x, y, false);
		// shk3: removed this condition, since I see not reason for it
		// and it breaks reading files with more than 50 peaks.
		// if (peaks.length < 50) // save check
		spectrum.setPeakTable(peaks);
		if (tables.length > 1) {
			Pattern[] pattern = (Pattern[]) tables[1];
			if (pattern != null && pattern.length < 50)
				spectrum.setPatternTable(pattern);
			if (tables.length > 2) {
				Assignment[] assigns = (Assignment[]) tables[2];
				if (assigns != null && assigns.length < 50)
					spectrum.setAssignments(assigns);
			}
		}
		spectrum.setTitle(title);
		try {
			String owner = getOwner(block);
			spectrum.setOwner(owner);
		} catch (JCAMPException e) {
			// label is normally required, but often it is missing
		}
		try {
			String origin = getOrigin(block);
			spectrum.setOrigin(origin);
		} catch (JCAMPException e) {
			// label is normally required, but often it is missing
		}
		return spectrum;
	}

	/**
	 * createSpectrum method comment.
	 */
	@Override
	public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
		if (block.getSpectrumID() != ISpectrumIdentifier.MS)
			block.getErrorHandler().fatal("JCAMP reader adapter missmatch");
		if (block.isNTupleBlock()) { // chromatogram!
			GCMSSpectrum spectrum = createGCMS(block);
			setNotes(block, spectrum);
			return spectrum;
		} else {
			MassSpectrum spectrum = null;
			Type type = block.getType();
			if (type.equals(Type.FULLSPECTRUM))
				spectrum = createFS(block);
			else if (type.equals(Type.PEAKTABLE))
				spectrum = createPeakTable(block);
			else if (type.equals(Type.ASSIGNMENT))
				spectrum = createPeakTable(block);
			else
				// never reached
				block.getErrorHandler().fatal("illegal block type");
			setNotes(block, spectrum);
			return spectrum;
		}
	}

	/**
	 * get retention times for GC/MS
	 * 
	 * @return double[]
	 */
	private double[] getRetentionTimes(JCAMPNTuple ntuple) {
		int nPages = ntuple.numPages();
		JCAMPVariable vTime = ntuple.getVariableByName("RETENTIONTIME");
		String[] timeValues = ntuple.getVariableValues(".RETENTIONTIME", vTime);
		double[] times = new double[nPages];
		double timeFactor = 1.0;
		double firstTime = Double.NaN;
		double lastTime = Double.NaN;
		double deltaTime = 0.;
		if (vTime != null) {
			if (vTime.getFactor() != null)
				timeFactor = vTime.getFactor().doubleValue();
			if (vTime.getFirst() != null)
				firstTime = vTime.getFirst().doubleValue();
			if (vTime.getLast() != null)
				lastTime = vTime.getLast().doubleValue();
			deltaTime = (lastTime - firstTime) / nPages;
		}

		for (int i = 0; i < times.length; i++) {
			if (timeValues[i] != null)
				times[i] = Double.parseDouble(timeValues[i]) * timeFactor;
			else
				times[i] = firstTime + i * deltaTime;
		}

		return times;
	}

	/**
	 * get total ion counts for GC/MS
	 * 
	 * @return double[]
	 */
	private double[] getTICs(JCAMPNTuple ntuple) {
		int nPages = ntuple.numPages();
		JCAMPVariable vtic = ntuple.getVariableByName("RIC");
		String[] ticValues = ntuple.getVariableValues(".RIC", vtic);
		double[] tics = new double[nPages];
		double ticFactor = 1.0;
		boolean hasTIC = false;
		if (vtic != null) {
			if (vtic.getFactor() != null)
				ticFactor = vtic.getFactor().doubleValue();
		}
		for (int i = 0; i < tics.length; i++) {
			if (ticValues[i] != null) {
				hasTIC = true;
				tics[i] = Double.parseDouble(ticValues[i]) * ticFactor;
			} else
				tics[i] = Double.NaN;
		}
		if (hasTIC)
			return tics;
		else
			return null;
	}
}
