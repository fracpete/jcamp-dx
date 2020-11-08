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
* Christoph Läubrich - replace logging by error handler
*******************************************************************************/
package org.jcamp.parser;

import java.util.StringTokenizer;

import org.jcamp.math.IArray1D;
import org.jcamp.spectrum.ArrayData;
import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.EquidistantData;
import org.jcamp.spectrum.IDataArray1D;
import org.jcamp.spectrum.IOrderedDataArray1D;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.NMRFIDSpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.OrderedArrayData;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between NMR spectrum class and JCAMPReader.
 * 
 * @author Thomas Weber
 */
public class NMRJCAMPReader
  extends CommonSpectrumJCAMPReader
  implements ISpectrumJCAMPReader {

  private String mode;
  
  /**
   * NMRJCAMPAdapter constructor comment.
   */
  protected NMRJCAMPReader(String mode) {
    super();
    this.mode = mode;
  }
  /**
   * create NMR FID spectrum from JCAMPBlock.
   * 
   * @return NMRSpectrum
   * @param block JCAMPBlock
   * @exception JCAMPException exception thrown if parsing fails.
   */
  private NMRSpectrum createFID(JCAMPBlock block) throws JCAMPException {
    NMRSpectrum spectrum = null;
    String nucleus = getNucleus(block);
    double reference = getShiftReference(block);
    double freq = getFrequency(block);
    if (!block.isNTupleBlock()) {
      block.getErrorHandler().warn("illegal FID, assuming y values are real FID part");
      // this should never be happen, let's assume we have only real part of FID
      Unit xUnit = getXUnits(block);
      if (xUnit == null)
	xUnit = CommonUnit.hertz;
      Unit yUnit = getYUnits(block);
      if (yUnit == null)
	yUnit = CommonUnit.intensity;
      double xFactor = getXFactor(block);
      double yFactor = getYFactor(block);
      int nPoints = getNPoints(block);
      IOrderedDataArray1D x;
      IDataArray1D y;
      if (block.getDataRecord("XYDATA") != null) {
	double firstX = getFirstX(block);
	double lastX = getLastX(block);
	double[] intensities = getXYData(block, firstX, lastX, nPoints, xFactor, yFactor);
	if (intensities.length != nPoints)
	  throw new JCAMPException("incorrect ##NPOINTS= or bad ##XYDATA=");
	x = new EquidistantData(firstX, lastX, nPoints, xUnit);
	y = new ArrayData(intensities, yUnit);
      } else if (block.getDataRecord("XYPOINTS") != null) {
	double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
	x = new OrderedArrayData(xy[0], xUnit);
	y = new ArrayData(xy[1], yUnit);
      } else
	throw new JCAMPException("missing data: ##XYDATA= or ##XYPOINTS= required.");
      spectrum = new NMRFIDSpectrum(x, y, nucleus, freq, reference);
    } else {
      JCAMPVariable x = block.getVariable("X");
      JCAMPVariable r = block.getVariable("R");
      JCAMPVariable i = block.getVariable("I");
      if (x == null)
	block.getErrorHandler().fatal("missing X variable");
      if (r == null)
	block.getErrorHandler().fatal("missing real fid");
      if (i == null)
	block.getErrorHandler().fatal("missing imaginary fid");
      Double firstX = x.getFirst();
      if (firstX == null)
	block.getErrorHandler().fatal("missing required ##FIRST= for X");
      Double lastX = x.getLast();
      if (lastX == null)
	block.getErrorHandler().fatal("missing required ##LAST= for X");
      Double xFactor = x.getFactor();
      if (xFactor == null) {
	block.getErrorHandler().error("missing required ##FACTOR= for X");
	xFactor = new Double(1.0);
      }

      Double rFactor = r.getFactor();
      if (rFactor == null) {
	block.getErrorHandler().error("missing required ##FACTOR= for R");
	rFactor = new Double(1.0);
      }
      Double iFactor = i.getFactor();
      if (iFactor == null) {
	block.getErrorHandler().error("missing required ##FACTOR= for I");
	iFactor = new Double(1.0);
      }

      Integer dim = x.getDimension();
      if (dim == null)
	block.getErrorHandler().fatal("missing required ##VARDIM= for X");
      int nPoints = dim.intValue();
      Unit xUnit = x.getUnit();
      Unit rUnit = r.getUnit();
      JCAMPNTuplePage page0 = block.getNTuple().getPage(0);
      JCAMPNTuplePage page1 = block.getNTuple().getPage(1);
      if (page0.getDatatableVariableSymbols()[1].equalsIgnoreCase("I")) { // swap pages, imaginary is first
	JCAMPNTuplePage tmp = page0;
	page0 = page1;
	page1 = tmp;
      }
      IArray1D reals = page0.getXYData().getYArray();
      IArray1D imags = page1.getXYData().getYArray();
      IOrderedDataArray1D xData = new EquidistantData(firstX.doubleValue(), lastX.doubleValue(), nPoints, xUnit);
      IDataArray1D rData = new ArrayData(reals, rUnit);
      IDataArray1D iData = new ArrayData(imags, rUnit);
      spectrum = new NMRFIDSpectrum(xData, rData, iData, nucleus, freq, reference);
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
  private NMRSpectrum createFS(JCAMPBlock block) throws JCAMPException {
    NMRSpectrum spectrum = null;
    String nucleus = getNucleus(block);
    if (!block.isNTupleBlock()) { // standard JCAMP
      Unit xUnit = getXUnits(block);
      if (xUnit == null)
	xUnit = CommonUnit.hertz;
      Unit yUnit = getYUnits(block);
      if (yUnit == null)
	yUnit = CommonUnit.intensity;
      double xFactor = getXFactor(block);
      double yFactor = getYFactor(block);
      int nPoints = getNPoints(block);
      IOrderedDataArray1D x = null;
      IDataArray1D y = null;
      if (block.getDataRecord("XYDATA") != null) {
	double firstX = getFirstX(block);
	double lastX = getLastX(block);
	double[] intensities = getXYData(block, firstX, lastX, nPoints, xFactor, yFactor);
	if (intensities.length != nPoints){
      block.getErrorHandler().warn(
	      "incorrect ##NPOINTS= or bad ##XYDATA=\n"
		  + "found "
		  + intensities.length
		  + ", but ##NPOINTS= "
		  + nPoints+", setting NPOINTS to "+intensities.length);
	  nPoints=intensities.length;
	}
	x = new EquidistantData(firstX, lastX, nPoints, xUnit);
	y = new ArrayData(intensities, yUnit);
      } else if (block.getDataRecord("XYPOINTS") != null) {
	double xy[][] = getXYPoints(block, nPoints, xFactor, yFactor);
	x = new OrderedArrayData(xy[0], xUnit);
	y = new ArrayData(xy[1], yUnit);
      } else
	block.getErrorHandler().fatal("missing data: ##XYDATA= or ##XYPOINTS= required.");

      double reference = Double.NaN;
      try {
	int refPoint = getShiftReferencePoint(block);
	reference = x.pointAt(refPoint);
      } catch (JCAMPException e) {
	reference = getShiftReference(block);
      }
      double freq = getFrequency(block);
      spectrum = new NMRSpectrum(x, y, nucleus, freq, reference, true, mode);
      return spectrum;
    } else {
      // workaround for Bruker WinNMR real+imag 1D NMR spectra
      JCAMPVariable x = block.getVariable("X");
      JCAMPVariable r = block.getVariable("R");
      if (x == null)
	block.getErrorHandler().fatal("missing X variable");
      if (r == null)
	block.getErrorHandler().fatal("missing real part");
      Double firstX = x.getFirst();
      if (firstX == null)
	block.getErrorHandler().fatal("missing required ##FIRST= for X");
      Double lastX = x.getLast();
      if (lastX == null)
	block.getErrorHandler().fatal("missing required ##LAST= for X");
      Double xFactor = x.getFactor();
      if (xFactor == null) {
	block.getErrorHandler().error("missing required ##FACTOR= for X");
	xFactor = new Double(1.0);
      }

      Double rFactor = r.getFactor();
      if (rFactor == null) {
	block.getErrorHandler().error("missing required ##FACTOR= for R");
	rFactor = new Double(1.0);
      }
      Integer dim = x.getDimension();
      if (dim == null)
	block.getErrorHandler().fatal("missing required ##VARDIM= for X");
      int nPoints = dim.intValue();
      Unit xUnit = x.getUnit();
      Unit rUnit = r.getUnit();
      // first page are reals (TODO: check)
      JCAMPNTuplePage page = block.getNTuple().getPage(0);
      if (page.getDatatableVariableSymbols()[1].equalsIgnoreCase("I"))
	page = block.getNTuple().getPage(1);
      IArray1D reals = page.getXYData().getYArray();
      IOrderedDataArray1D xData = new EquidistantData(firstX.doubleValue(), lastX.doubleValue(), nPoints, xUnit);
      IDataArray1D rData = new ArrayData(reals, rUnit);
      double reference = Double.NaN;
      try {
	int refPoint = getShiftReferencePoint(block);
	reference = xData.pointAt(refPoint);
      } catch (JCAMPException e) {
	reference = getShiftReference(block);
      }
      double freq = getFrequency(block);
      spectrum = new NMRSpectrum(xData, rData, nucleus, freq, reference, true, mode);
      return spectrum;
    }
  }
  /**
   * create NMR peak table (peak spectrum) from JCAMPBlock.
   * 
   * @return NMRSpectrum
   * @param block JCAMPBlock
   * @exception JCAMPException exception thrown if parsing fails.
   */
  private NMRSpectrum createPeakTable(JCAMPBlock block) throws JCAMPException {
    NMRSpectrum spectrum = null;
    String nucleus = getNucleus(block);
    Unit xUnit = getXUnits(block);
    if (xUnit == null)
      xUnit = CommonUnit.hertz;
    Unit yUnit = getYUnits(block);
    if (yUnit == null)
      yUnit = CommonUnit.intensity;
    double xFactor = getXFactor(block);
    double yFactor = getYFactor(block);
    int refPoint;
    double reference = Double.NaN;
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
      // often solventreference is missing in peak tables
      reference = getShiftReference(block);
    }
    try {
      freq = getFrequency(block);
    } catch (JCAMPException e) {
      // often observe frequency is missing in peak tables
      block.getErrorHandler().warn(e.getMessage());
    }
    spectrum = new NMRSpectrum(x, y, nucleus, freq, reference, false, mode);
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
    if (block.getSpectrumID() != ISpectrumIdentifier.NMR)
      block.getErrorHandler().fatal("adapter missmatch");
    boolean isFID = false;
    NMRSpectrum spectrum = null;
    JCAMPDataRecord ldrDataType = block.getDataRecord("DATATYPE");
    String dataType = ldrDataType.getContent().toUpperCase();
    if (dataType.indexOf("FID") >= 0)
      isFID = true;
    // currently many NMR with NTUPLES are FIDs, should generate an error when 2D NMR is available  
    // 
    // Bruker writes X->R/I NMR spectra with WinNMR
    //

    if (!isFID && block.isNTupleBlock()) {
      JCAMPVariable x = block.getNTuple().getVariable("X");
      if (x == null) {
        block.getErrorHandler().fatal("missing X variable");
      }
      if (x.getUnit().equals(CommonUnit.second)) {
	block.getErrorHandler().warn("NMR FID without NMR FID data type");
	isFID = true;
      }
      block.getErrorHandler().warn("nD NMR or NMR spectra with real and imaginary part");
    }
    if (isFID) {
      spectrum = createFID(block);
    } else {
      JCAMPBlock.Type type = block.getType();
      if (type.equals(JCAMPBlock.Type.FULLSPECTRUM))
	spectrum = createFS(block);
      else if (type.equals(JCAMPBlock.Type.PEAKTABLE))
	spectrum = createPeakTable(block);
      else if (type.equals(JCAMPBlock.Type.ASSIGNMENT))
	spectrum = createPeakTable(block);
      else // never reached
      block.getErrorHandler().fatal("illegal block type");
    }
    String solvent = getSolvent(block);
    spectrum.setSolvent(solvent);
    spectrum.setSolventReference(getSolventReference(block, solvent));
    setNotes(block, spectrum);
    getLinkedAssignments(block, spectrum);
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
    if (ldrOffset == null)
      throw new JCAMPException();
    String os = ldrOffset.getContent();
    return Double.valueOf(os).doubleValue();
  }
  /**
   * gets ##$SF= content (Bruker specific)
   * 
   * @return double
   * @param block JCAMPBlock
   * @exception JCAMPException The exception description.
   */
  private double getBrukerSF(JCAMPBlock block) throws JCAMPException {
    JCAMPDataRecord ldrSF = block.getDataRecord("$SF");
    if (ldrSF == null)
      throw new JCAMPException();
    String sf = ldrSF.getContent();
    return Double.valueOf(sf).doubleValue();
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
    if (ldrSW == null)
      throw new JCAMPException();
    String sw = ldrSW.getContent();
    return Double.valueOf(sw).doubleValue();
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
	block.getErrorHandler().fatal("missing required label ##.OBSERVEFREQUENCY=");
	return 0;
      }
      block.getErrorHandler().warn("missing required label ##.OBSERVEFREQUENCY=, using Bruker custom labels");
      return freq;
    } else {
      freq = Double.valueOf(ldrFrequency.getContent()).doubleValue();
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
  private void getLinkedAssignments(JCAMPBlock block, NMRSpectrum spectrum) throws JCAMPException {
    JCAMPBlock[] linked = block.getReferences();
    for (int i = 0; i < linked.length; i++) {
      if (linked[i].isStructureBlock() || linked[i].isLinkBlock())
	continue;
      JCAMPBlock.Type type = linked[i].getType();
      if (linked[i].getSpectrumID() != ISpectrumIdentifier.NMR)
	continue;
      if (type.equals(JCAMPBlock.Type.PEAKTABLE) || type.equals(JCAMPBlock.Type.ASSIGNMENT)) {
	NMRSpectrum linkSpectrum = createPeakTable(linked[i]);
	// check if nucleus, frequency, and solvent reference are equal
	if (!linkSpectrum.getNucleus().equals(spectrum.getNucleus()))
	  continue;
	if (linkSpectrum.getFrequency() != linkSpectrum.getFrequency()) // Double.NaN
	  linkSpectrum.setFrequency(spectrum.getFrequency());
	if (linkSpectrum.getReference() != linkSpectrum.getReference()) // Double.NaN
	  linkSpectrum.setReference(spectrum.getReference());
	if (linkSpectrum.hasPeakTable())
	  spectrum.setPeakTable(linkSpectrum.getPeakTable());
	if (linkSpectrum.hasPatternTable())
	  spectrum.setPatternTable(linkSpectrum.getPatternTable());
	if (linkSpectrum.hasAssignments())
	  spectrum.setAssignments(linkSpectrum.getAssignments());
      }
    }
  }
  /**
   * gets ##.OBSERVENUCLEUS= content
   * 
   * @return java.lang.String
   * @param block JCAMPBlock
   * @exception JCAMPException The exception description.
   */
  private String getNucleus(JCAMPBlock block) throws JCAMPException {
    JCAMPDataRecord ldrNucleus = block.getDataRecord(".OBSERVENUCLEUS");
    if (ldrNucleus == null)
      throw new JCAMPException("missing required label: ##.OBSERVENUCLEUS=");
    return ldrNucleus.getContent();
  }
  /**
   * gets ##.SHIFTREFERENCE= content
   * NOTE: this is a open issue in the JCAMP spec. With standard JCAMP 5.00 it is not possible
   * to specify a reference point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE0
   * is marked as optional instead of required.
   * @return double
   * @param block JCAMPBlock
   * @exception JCAMPException The exception description.
   */
  private double getShiftReference(JCAMPBlock block) throws JCAMPException {
    double reference = Double.NaN;
    // first try Bruker custom labels
    try {
      double freq = getBrukerSF(block);
      reference = freq * (getBrukerSW(block) - getBrukerOffset(block));
      block.getErrorHandler().warn("missing ##.SHIFTREFERENCE=, using Bruker custom labels");
      return reference;
    } catch (JCAMPException e) {
      JCAMPDataRecord ldrReferencePoint = block.getDataRecord("$REFERENCEPOINT");
      if (ldrReferencePoint == null) {
	block.getErrorHandler().warn("missing shift reference: assuming 0.0 Hz");
	return 0.0;
      } else {
	block.getErrorHandler().warn("missing ##.SHIFTREFERENCE=, using SpecInfo ##$REFERENCEPOINT=");
	return Double.valueOf(ldrReferencePoint.getContent()).doubleValue();
      }
    }
  }
  /**
   * gets ##.SHIFTREFERENCE= content
   * NOTE: this is a open issue in the JCAMP spec. With standard JCAMP 5.00 it is not possible
   * to specify a reference point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE0
   * is marked as optional instead of required.
   * @return double
   * @param block JCAMPBlock
   * @exception JCAMPException The exception description.
   */
  private int getShiftReferencePoint(JCAMPBlock block) throws JCAMPException {
    JCAMPDataRecord ldrShiftReference = block.getDataRecord(".SHIFTREFERENCE");
    if (ldrShiftReference == null) {
      // block.getErrorHandler().warn("missing ##.SHIFTREFERENCE=");
      throw new JCAMPException();
    } else {
      String shiftRef = ldrShiftReference.getContent();
      StringTokenizer commaTokenizer = new java.util.StringTokenizer(shiftRef, ",");
      commaTokenizer.nextToken(); // skip INTERNAL/EXTERNAL
      commaTokenizer.nextToken(); // skip solvent
      try{
	int shiftPoint = Integer.parseInt(commaTokenizer.nextToken().trim());
	return shiftPoint;
      }catch(NumberFormatException ex){
	//shk3: looking at the code where the method is used, it looks like
	//there is a fallback, but the fallback only works if
	//a JCAMPException is thrown
	throw new JCAMPException(ex.getMessage());
      }
    }
  }
  /**
   * gets ##.SHIFTREFERENCE= content
   * NOTE: this is a open issue in the JCAMP spec. With standard JCAMP 5.00 it is not possible
   * to specify a reference point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE0
   * is marked as optional instead of required.
   * @return double
   * @param block JCAMPBlock
   * @exception JCAMPException The exception description.
   */
  private String getSolvent(JCAMPBlock block) throws JCAMPException {
    JCAMPDataRecord ldr = block.getDataRecord(".SHIFTREFERENCE");
    if (ldr == null) {
      ldr = block.getDataRecord("$SOLVENT"); // Bruker??? or SpecInfo???
      if (ldr == null)
	return "TMS";
      else
	return ldr.getContent();
    } else {
      String shiftRef = ldr.getContent();
      StringTokenizer commaTokenizer = new java.util.StringTokenizer(shiftRef, ",");
      commaTokenizer.nextToken(); // skip INTERNAL/EXTERNAL
      return commaTokenizer.nextToken();
    }
  }
  /**
   * gets solvent reference in ppm from either ##.SHIFTREFERENCE= or ##.SOLVENTREFERENCE= content
   * NOTE: this is a open issue in the JCAMP spec. With standard JCAMP 5.00 it is not possible
   * to specify a reference point, with 5.01 it is possible, but the resp. label ##.SHIFTREFERENCE=
   * is marked as optional instead of required.
   * @return double
   * @param block JCAMPBlock
   * @param solvent the solvent
   * @exception JCAMPException The exception description.
   */
  private double getSolventReference(JCAMPBlock block, String solvent) throws JCAMPException {
    JCAMPDataRecord ldr = block.getDataRecord(".SHIFTREFERENCE");
    if (ldr == null) {
      ldr = block.getDataRecord(".SOLVENTREFERENCE");
      if (ldr == null) {
	if (solvent.equals("TMS"))
	  return 0.0;
	else {
	  block.getErrorHandler().warn(
	      "missing required label for non-TMS solvent: ##.SOLVENTREFERENCE=, setting reference to 0.0");
	  return 0.0;
	}
      } else
	return Double.parseDouble(ldr.getContent());

    } else {
      String shiftRef = ldr.getContent();
      String[] tokens = shiftRef.split(",", -1);
      //shk3 changed this to using split since the StringTokenizer ignores empty fields
      //and the .SHIFTREFERENCE may look like ,TMS,0,0.0
      //            StringTokenizer commaTokenizer = new java.util.StringTokenizer(shiftRef, ",");
      //            commaTokenizer.nextToken(); // skip INTERNAL/EXTERNAL
      //            commaTokenizer.nextToken(); // skip solvent
      //            commaTokenizer.nextToken(); // skip shiftPoint
      //            double solvRef = Double.parseDouble(commaTokenizer.nextToken().trim());
      double solvRef = Double.parseDouble(tokens[3]);
      return solvRef;
    }
  }
}
