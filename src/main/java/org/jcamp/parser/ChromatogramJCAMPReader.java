/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.Chromatogram;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * adapter between chromatography class and JCAMPReader.
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public class ChromatogramJCAMPReader extends CommonSpectrumJCAMPReader
implements ISpectrumJCAMPReader {

	private final static Logger log = LoggerFactory
			.getLogger(ChromatogramJCAMPReader.class);

	protected ChromatogramJCAMPReader() {
		super();
	}

	/**
	 * read chromatogram full spectrum.
	 *
	 * @return Chromatogram
	 * @param block
	 *            JCAMPBlock
	 */
	private Chromatogram createFS(JCAMPBlock block) throws JCAMPException {
		Chromatogram spectrum;
		Unit xUnit = getXUnits(block);
		if (xUnit == null)
			xUnit = CommonUnit.second;
		Unit yUnit = getYUnits(block);
		if (yUnit == null)
			yUnit = CommonUnit.intensity;
		double xFactor = getXFactor(block);
		double yFactor = getYFactor(block);
		double firstX = getFirstX(block);
		double lastX = getLastX(block);
		int nPoints = getNPoints(block);
		double[] intensities = getXYData(block, firstX, lastX, nPoints,
				xFactor, yFactor);
		if (intensities.length != nPoints) {
			if (log.isErrorEnabled()) {
				log.error("incorrect ##NPOINTS=: expected "
						+ Integer.toString(nPoints) + " but got "
						+ Integer.toString(intensities.length));
			}
			nPoints = intensities.length;
		}
		IOrderedDataArray1D x = new EquidistantData(firstX, lastX, nPoints,
				xUnit);
		IDataArray1D y = new ArrayData(intensities, yUnit);
		spectrum = new Chromatogram(x, y, true);
		return spectrum;
	}

	/**
	 * create chromatogram peak table (peak spectrum) from JCAMPBlock.
	 *
	 * @return chromatogram
	 * @param block
	 *            JCAMPBlock
	 * @exception JCAMPException
	 *                exception thrown if parsing fails.
	 */
	private Chromatogram createPeakTable(JCAMPBlock block)
			throws JCAMPException {
		Chromatogram spectrum = null;
		Unit xUnit = getXUnits(block);
		if (xUnit == null)
			xUnit = CommonUnit.second;
		Unit yUnit = getYUnits(block);
		if (yUnit == null)
			yUnit = CommonUnit.intensity;
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
		spectrum = new Chromatogram(x, y, false);
		spectrum.setPeakTable(peaks);
		if (tables.length > 1) {
			spectrum.setPatternTable((Pattern[]) tables[1]);
			if (tables.length > 2)
				spectrum.setAssignments((Assignment[]) tables[2]);
		}
		return spectrum;
	}

	/**
	 * createSpectrum method comment.
	 */
	@Override
	public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
		if (block.getSpectrumType() != ISpectrumIdentifier.CHROMATOGRAM)
			throw new JCAMPException("JCAMP reader adapter missmatch");
		Chromatogram spectrum = null;
		BlockType type = block.getBlockType();
		if (type.equals(BlockType.FULLSPECTRUM))
			spectrum = createFS(block);
		else if (type.equals(BlockType.PEAKTABLE))
			spectrum = createPeakTable(block);
		else if (type.equals(BlockType.ASSIGNMENT))
			spectrum = createPeakTable(block);
		else
			// never reached
			throw new JCAMPException("illegal block type");
		setNotes(block, spectrum);
		return spectrum;
	}
}
