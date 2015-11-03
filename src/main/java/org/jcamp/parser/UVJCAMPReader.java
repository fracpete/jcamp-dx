/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * ****************************************************************************
 */
package org.jcamp.parser;

import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.UVSpectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * adapter between UV spectrum class and JCAMPReader.
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 * Kerner</a>
 */
public class UVJCAMPReader extends CommonSpectrumJCAMPReader implements
		ISpectrumJCAMPReader {

	private final static Logger log = LoggerFactory
			.getLogger(UVJCAMPReader.class);

	/**
	 * UVJCAMPAdapter constructor comment.
	 */
	protected UVJCAMPReader() {
		super();
	}

	/**
	 * read UV full spectrum.
	 *
	 * @return com.creon.chem.spectrum.UVSpectrum
	 * @param block com.creon.chem.jcamp.JCAMPBlock
	 */
	@Override
	protected UVSpectrum createFS(JCAMPBlock block) throws JCAMPException {
		UVSpectrum spectrum;
		Unit xUnit = getXUnits(block);
		if (xUnit == null) {
			xUnit = CommonUnit.nanometerWavelength;
		}
		Unit yUnit = getYUnits(block);
		if (yUnit == null) {
			yUnit = CommonUnit.absorbance;
		}
		double xFactor = getXFactor(block);
		double yFactor = getYFactor(block);
		int nPoints = getNPoints(block);
		if (block.getDataRecord("XYDATA") != null) {
			double firstX = getFirstX(block);
			double lastX = getLastX(block);
			double[] intensities = getXYData(block, firstX, lastX, nPoints,
					xFactor, yFactor);
			if (intensities.length != nPoints) {
				throw new JCAMPException(
						"incorrect ##NPOINTS= or bad ##XYDATA=");
			}
			IOrderedDataArray1D x = new EquidistantData(firstX, lastX, nPoints,
					xUnit);
			IDataArray1D y = new ArrayData(intensities, yUnit);
			spectrum = new UVSpectrum(x, y, true);
		} else if (block.getDataRecord("XYPOINTS") != null) {
			double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
			IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
			IDataArray1D y = new ArrayData(xy[1], yUnit);
			spectrum = new UVSpectrum(x, y, false);
		} else {
			throw new JCAMPException(
					"missing data: ##XYDATA= or ##XYPOINTS= required.");
		}
		return spectrum;
	}

	/**
	 * create UV peak table (peak spectrum) from JCAMPBlock.
	 *
	 * @return UVSpectrum
	 * @param block JCAMPBlock
	 * @exception JCAMPException exception thrown if parsing fails.
	 */
	@Override
	protected UVSpectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
		Unit xUnit = getXUnits(block);
		if (xUnit == null) {
			xUnit = CommonUnit.nanometerWavelength;
		}
		Unit yUnit = getYUnits(block);
		if (yUnit == null) {
			yUnit = CommonUnit.intensity;
		}
		double xFactor = getXFactor(block);
		double yFactor = getYFactor(block);
		int nPoints = getNPoints(block);
		Object[] tables = getPeaktable(block, nPoints, xFactor, yFactor);
		Peak1D[] peaks = (Peak1D[]) tables[0];
		if (peaks.length != nPoints) {
			if (log.isErrorEnabled()) {
				log.error("incorrect ##NPOINTS=: expected "
						+ Integer.toString(nPoints) + " but got "
						+ Integer.toString(peaks.length));
			}
			nPoints = peaks.length;
		}
		double[][] xy = peakTableToPeakSpectrum(peaks);
		IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
		IDataArray1D y = new ArrayData(xy[1], yUnit);
		UVSpectrum spectrum = new UVSpectrum(x, y, false);
		spectrum.setPeakTable(peaks);
		if (tables.length > 1) {
			spectrum.setPatternTable((Pattern[]) tables[1]);
			if (tables.length > 2) {
				spectrum.setAssignments((Assignment[]) tables[2]);
			}
		}
		return spectrum;
	}

	@Override
	protected int getExpectedSpectrumType() {
		return ISpectrumIdentifier.UV;
	}
}
