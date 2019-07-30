/*******************************************************************************
* The JCAMP-DX project is the reference implemention of the IUPAC JCAMP-DX spectroscopy data standard.
* 
*   Copyright (C) 2019 Thomas Weber
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Library General Public
*    License as published by the Free Software Foundation; either
*    version 2 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Library General Public License for more details.
*
* Contributors:
* Thomas Weber - initial API and implementation
* Christoph Läubrich - replace log by error handler
*******************************************************************************/
package org.jcamp.parser;

import java.util.Arrays;

import org.jcamp.math.IArray1D;
import org.jcamp.math.IArray2D;
import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.Fluorescence2DSpectrum;
import org.jcamp.spectrum.FluorescenceSpectrum;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between fluorescence spectrum class and JCAMPReader.
 * NON STANDARD
 * 
 * @author Thomas Weber
 */
public class FluorescenceJCAMPReader
  extends CommonSpectrumJCAMPReader implements ISpectrumJCAMPReader {

  /**
   */
  protected FluorescenceJCAMPReader() {
    super();
  }

  /**
   * read Fluorescence full spectrum.
   * 
   * @return FluorescenceSpectrum
   * @param block JCAMPBlock
   */
  private FluorescenceSpectrum createFS(JCAMPBlock block) throws JCAMPException {
    FluorescenceSpectrum spectrum;
    Unit xUnit = getXUnits(block);
    if (xUnit == null)
      xUnit = CommonUnit.nanometerWavelength;
    Unit yUnit = getYUnits(block);
    if (yUnit == null)
      yUnit = CommonUnit.absorbance;
    double xFactor = getXFactor(block);
    double yFactor = getYFactor(block);
    int nPoints = getNPoints(block);
    if (block.getDataRecord("XYDATA") != null) {
      double firstX = getFirstX(block);
      double lastX = getLastX(block);
      double[] intensities = getXYData(block, firstX, lastX, nPoints, xFactor, yFactor);
      if (intensities.length != nPoints)
	throw new JCAMPException("incorrect ##NPOINTS= or bad ##XYDATA=");
      IOrderedDataArray1D x = new EquidistantData(firstX, lastX, nPoints, xUnit);
      IDataArray1D y = new ArrayData(intensities, yUnit);
      spectrum = new FluorescenceSpectrum(x, y, true);
    } else if (block.getDataRecord("XYPOINTS") != null) {
      double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
      IOrderedDataArray1D x = new OrderedArrayData(xy[0], xUnit);
      IDataArray1D y = new ArrayData(xy[1], yUnit);
      spectrum = new FluorescenceSpectrum(x, y, false);
    } else
      throw new JCAMPException("missing data: ##XYDATA= or ##XYPOINTS= required.");
    setNotes(block, spectrum);
    return spectrum;
  }

  /**
   * read Fluorescence full spectrum.
   * 
   * @return Fluorescence2DSpectrum
   * @param block JCAMPBlock
   */
  private Fluorescence2DSpectrum createFS2D(JCAMPBlock block) throws JCAMPException {
    Fluorescence2DSpectrum spectrum;
    JCAMPNTuple ntuple = block.getNTuple();
    JCAMPVariable[] vars = block.getVariables();
    int nPages = ntuple.numPages();
    int emissionIndex = -1;
    int excitationIndex = -1;
    int intensityIndex = -1;
    for (int i = 0; i < vars.length; i++) {
      String name = vars[i].getName().toUpperCase();
      if ("INTENSITY".equalsIgnoreCase(name))
	intensityIndex = i;
      else if ("EMISSION".equalsIgnoreCase(name))
	emissionIndex = i;
      else if ("EXCITATION".equalsIgnoreCase(name))
	excitationIndex = i;
      else if ("PAGE".equals(name));
      else
	block.getErrorHandler().warn("unknown fluorescence variable: " + name);
    }
    for (int i = 0; i < vars.length; i++) {
      if (intensityIndex < 0 && vars[i].getType().equals(JCAMPVariable.Type.DEPENDENT)) {
    	  block.getErrorHandler().warn("intensity variable could not be recognized: using dependend variable at column " + (i + 1));
	intensityIndex = i;
      }
      if (emissionIndex < 0 && vars[i].getType().equals(JCAMPVariable.Type.INDEPENDENT)) {
    	  block.getErrorHandler().warn("emission variable could not be recognized: using independend variable at column " + (i + 1));
	emissionIndex = i;
      }
      if (excitationIndex < 0 && vars[i].getType().equals(JCAMPVariable.Type.INDEPENDENT)) {
    	  block.getErrorHandler().warn(
	    "excitation variable could not be recognized: using independend variable at column " + (i + 1));
	excitationIndex = i;
      }
    }
    if (emissionIndex < 0)
      throw new JCAMPException("missing emission variable");
    if (excitationIndex < 0)
      throw new JCAMPException("missing excitation variable");
    if (intensityIndex < 0)
      throw new JCAMPException("missing intensity variable");
    Double emissionFirst = vars[emissionIndex].getFirst();
    if (emissionFirst == null)
      throw new JCAMPException("missing ##FIRST= for emission");
    double mFirst = emissionFirst.doubleValue();
    Double emissionLast = vars[emissionIndex].getLast();
    if (emissionLast == null)
      throw new JCAMPException("missing ##LAST= for emission");
    double mLast = emissionLast.doubleValue();
    Integer emissionDim = vars[emissionIndex].getDimension();
    if (emissionDim == null)
      throw new JCAMPException("missing ##VARDIM= for emission");
    int mDim = emissionDim.intValue();
    Double excitationFirst = vars[excitationIndex].getFirst();
    if (excitationFirst == null)
      throw new JCAMPException("missing ##FIRST= for excitation");
    double xFirst = excitationFirst.doubleValue();
    Double excitationLast = vars[excitationIndex].getLast();
    if (excitationLast == null)
      throw new JCAMPException("missing ##LAST= for excitation");
    double xLast = excitationLast.doubleValue();
    Integer excitationDim = vars[excitationIndex].getDimension();
    if (excitationDim == null)
      throw new JCAMPException("missing ##VARDIM= for excitation");
    int xDim = excitationDim.intValue();
    JCAMPNTuplePage firstPage = block.getNTuple().getPage(0);
    boolean excitationIsX = true;
    if (firstPage.getPageVariableSymbols()[0].equals(vars[excitationIndex].getSymbol()))
      excitationIsX = false;

    Unit emissionUnit = vars[emissionIndex].getUnit();
    if (emissionUnit == null)
      emissionUnit = CommonUnit.nanometerWavelength;
    Unit excitationUnit = vars[excitationIndex].getUnit();
    if (excitationUnit == null)
      excitationUnit = CommonUnit.nanometerWavelength;
    Unit intensityUnit = vars[intensityIndex].getUnit();
    if (intensityUnit == null)
      intensityUnit = CommonUnit.absorbance;

    IOrderedDataArray1D x;
    IOrderedDataArray1D y;
    double[] intensities;
    if (excitationIsX) {
      x = new EquidistantData(xFirst, xLast, xDim, excitationUnit);
      x.setLabel("Excitation [" + excitationUnit + "]");
      y = new EquidistantData(mFirst, mLast, mDim, emissionUnit);
      y.setLabel("Emission [" + emissionUnit + "]");
      intensities = new double[x.getLength() * y.getLength()];
      if (nPages != emissionDim.intValue())
    	  block.getErrorHandler().warn("number of pages != emission dimension, possible missing values");
      for (int i = 0; i < nPages; i++) {
	JCAMPNTuplePage page = block.getNTuple().getPage(i);
	IArray2D data = page.getXYData();
	String emissionStr = page.getPageVariableValue(vars[emissionIndex].getSymbol());
	int index;
	if (emissionStr == null) {
	  index = i * x.getLength();
	  block.getErrorHandler().warn("missing ##PAGE= emission entry, assuming ordered pages");
	} else { // calculate index
	  double emission = Double.parseDouble(emissionStr);
	  index = y.indexAt(emission);
	  index *= x.getLength();
	}
	IArray1D iArr = data.getYArray();
	for (int j = 0; j < x.getLength(); j++)
	  intensities[index + j] = iArr.pointAt(j);
      }
    } else {
      x = new EquidistantData(mFirst, mLast, mDim, emissionUnit);
      x.setLabel("Emission [" + emissionUnit + "]");
      y = new EquidistantData(xFirst, xLast, xDim, excitationUnit);
      y.setLabel("Excitation [" + excitationUnit + "]");
      intensities = new double[x.getLength() * y.getLength()];
      if (nPages != excitationDim.intValue())
    	  block.getErrorHandler().warn("number of pages != excitation dimension, possible missing values");
      for (int i = 0; i < nPages; i++) {
	JCAMPNTuplePage page = block.getNTuple().getPage(i);
	IArray2D data = page.getXYData();
	int index;
	String excitationStr = page.getPageVariableValue(vars[excitationIndex].getSymbol());
	if (excitationStr == null) {
	  index = i * x.getLength();
	  block.getErrorHandler().warn("missing ##PAGE= excitation entry, assuming ordered pages");
	} else { // calculate index
	  double excitation = Double.parseDouble(excitationStr);
	  index = y.indexAt(excitation);
	  index *= x.getLength();
	}
	IArray1D iArr = data.getYArray();
	for (int j = 0; j < x.getLength(); j++)
	  intensities[index + j] = iArr.pointAt(j);
      }
    }
    spectrum = new Fluorescence2DSpectrum(x, y, new ArrayData(intensities, intensityUnit));

    setNotes(block, spectrum);
    return spectrum;
  }

  /**
   * create Fluorescence peak table (peak spectrum) from JCAMPBlock.
   * 
   * @return FluorescenceSpectrum
   * @param block JCAMPBlock
   * @exception JCAMPException exception thrown if parsing fails.
   */
  private FluorescenceSpectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
    FluorescenceSpectrum spectrum = null;
    Unit xUnit = getXUnits(block);
    if (xUnit == null)
      xUnit = CommonUnit.nanometerWavelength;
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
    Arrays.sort(peaks);
    double[] px = new double[nPoints];
    double[] py = new double[nPoints];
    for (int i = 0; i < nPoints; i++) {
      px[i] = peaks[i].getPosition()[0];
      py[i] = peaks[i].getHeight();
    }
    IOrderedDataArray1D x = new OrderedArrayData(px, xUnit);
    IDataArray1D y = new ArrayData(py, yUnit);
    spectrum = new FluorescenceSpectrum(x, y, false);
    spectrum.setPeakTable(peaks);
    if (tables.length > 1) {
      spectrum.setPatternTable((Pattern[]) tables[1]);
      if (tables.length > 2)
	spectrum.setAssignments((Assignment[]) tables[2]);
    }
    setNotes(block, spectrum);
    return spectrum;
  }

  /**
   * createSpectrum method comment.
   */
  @Override
  public Spectrum createSpectrum(JCAMPBlock block) throws JCAMPException {
    if (block.getSpectrumID() != ISpectrumIdentifier.FLUORESCENCE)
      block.getErrorHandler().fatal("JCAMP reader adapter missmatch");
    if (block.isNTupleBlock()) { // 2D
      Fluorescence2DSpectrum spectrum = createFS2D(block);
      //        setNotes(block, spectrum);
      return spectrum;
    }
    FluorescenceSpectrum spectrum = null;
    JCAMPBlock.Type type = block.getType();
    if (type.equals(JCAMPBlock.Type.FULLSPECTRUM))
      spectrum = createFS(block);
    else if (type.equals(JCAMPBlock.Type.PEAKTABLE))
      spectrum = createPeakTable(block);
    else if (type.equals(JCAMPBlock.Type.ASSIGNMENT))
      spectrum = createPeakTable(block);
    else // never reached
      block.getErrorHandler().fatal("illegal block type");
    return spectrum;
  }
}