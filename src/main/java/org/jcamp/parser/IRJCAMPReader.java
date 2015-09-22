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
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.IRSpectrum;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between IR spectrum class and JCAMPReader.
 * 
 * @author Thomas Weber
 */
public class IRJCAMPReader
  extends CommonSpectrumJCAMPReader
  implements ISpectrumJCAMPReader {

  /**
   * IRJCAMPAdapter constructor comment.
   */
  protected IRJCAMPReader() {
    super();
  }

  /**
   * read IR full spectrum.
   * 
   * @return com.creon.chem.spectrum.IRSpectrum
   * @param block com.creon.chem.jcamp.JCAMPBlock
   */
  private IRSpectrum createFS(JCAMPBlock block) throws JCAMPException {
    IRSpectrum spectrum;
    Unit xUnit = null;
    try {
      xUnit = getXUnits(block);
    } catch (JCAMPException e) {
      xUnit = CommonUnit.perCM;
    }
    Unit yUnit = null;
    try {
      yUnit = getYUnits(block);
    } catch (JCAMPException e) {
      yUnit = CommonUnit.absorbance;
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
    if (block.getDataRecord("XYDATA") != null) {
      double firstX = getFirstX(block);
      double lastX = getLastX(block);
      double[] intensities = getXYData(block, firstX, lastX, nPoints, xFactor, yFactor);
      if (intensities.length != nPoints)
	throw new JCAMPException("incorrect ##NPOINTS= or bad ##XYDATA=");
      IOrderedDataArray1D x = new EquidistantData(firstX, lastX, nPoints, xUnit);
      IDataArray1D y = new ArrayData(intensities, yUnit);
      spectrum = new IRSpectrum(x, y, true);
    } else if (block.getDataRecord("XYPOINTS") != null) {
      double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
      IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
      IDataArray1D y = new ArrayData(xy[1], yUnit);
      spectrum = new IRSpectrum(x, y, false);
    } else
      throw new JCAMPException("missing data: ##XYDATA= or ##XYPOINTS= required.");
    return spectrum;
  }

  /**
   * create IR peak table (peak spectrum) from JCAMPBlock.
   * 
   * @return IRSpectrum
   * @param block JCAMPBlock
   * @exception JCAMPException exception thrown if parsing fails.
   */
  private IRSpectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
    IRSpectrum spectrum = null;
    Unit xUnit = getXUnits(block);
    if (xUnit == null)
      xUnit = CommonUnit.hertz;
    Unit yUnit = getYUnits(block);
    if (yUnit == null)
      yUnit = CommonUnit.intensity;
    double xFactor = getXFactor(block);
    double yFactor = getYFactor(block);
    int nPoints = getNPoints(block);
    Object[] tables = getPeaktable(block, nPoints, xFactor, yFactor);
    Peak1D[] peaks = (Peak1D[]) tables[0];
    if (nPoints != peaks.length) {
      block.getErrorHandler().error(
	  "incorrect ##NPOINTS=: expected "
	      + Integer.toString(nPoints)
	      + " but got "
	      + Integer.toString(peaks.length));
      nPoints = peaks.length;
    }
    double[][] xy = peakTableToPeakSpectrum(peaks);
    IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
    IDataArray1D y = new ArrayData(xy[1], yUnit);
    spectrum = new IRSpectrum(x, y, false);
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
    if (block.getSpectrumID() != ISpectrumIdentifier.IR)
      block.getErrorHandler().fatal("JCAMP reader adapter missmatch");
    IRSpectrum spectrum = null;
    JCAMPBlock.Type type = block.getType();
    if (type.equals(JCAMPBlock.Type.FULLSPECTRUM))
      spectrum = createFS(block);
    else if (type.equals(JCAMPBlock.Type.PEAKTABLE))
      spectrum = createPeakTable(block);
    else if (type.equals(JCAMPBlock.Type.ASSIGNMENT))
      spectrum = createPeakTable(block);
    else // never reached
      block.getErrorHandler().fatal("illegal block type");
    setNotes(block, spectrum);
    return spectrum;
  }
}
