/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.util.Iterator;

import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.UVSpectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between UV spectrum class and JCAMPWriter.
 * 
 * @author Thomas Weber
 */
public class UVJCAMPWriter
  extends CommonSpectrumJCAMPWriter
  implements ISpectrumJCAMPWriter {
  
  private final static String CRLF = "\r\n";
  
  /**
   * UVJCAMPAdapter constructor comment.
   */
  protected UVJCAMPWriter() {
    super();
  }
  /**
   * build assignment table
   * 
   * @return String
   * @param block int  block number (0: no xref, main block)
   * @param uv UVSpectrum
   */
  private String buildAssignmentTable(int block, UVSpectrum uv) {
    StringBuilder jcamp = new StringBuilder();
    Assignment[] assigns = uv.getAssignments();
    String title = uv.getTitle();
    int n = assigns.length;
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=UV/VIS PEAK ASSIGNMENTS").append(CRLF);
    jcamp.append("##DATACLASS=ASSIGNMENTS").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(uv));
    jcamp.append(getXUnitLDR(uv));
    jcamp.append(getYUnitLDR(uv));
    jcamp.append("##NPOINTS=").append(n).append(CRLF);
    jcamp.append("##PEAKASSIGNMENTS=(XYA)").append(CRLF);
    for (int i = 0; i < n; i++) {
      double x = assigns[i].getPosition()[0];
      jcamp.append('(').append(x).append(",1,").append(",<").append(assigns[i].getLabel()).append(">)").append(
	  CRLF);
    }
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * build full spectrum data block.
   * 
   * @return String
   * @param uv UVSpectrum
   */
  private String buildFSData(int block, UVSpectrum uv) {
    StringBuilder jcamp = new StringBuilder();
    int n = uv.getXData().getLength();
    double x0 = uv.getXData().pointAt(0);
    double x1 = uv.getXData().pointAt(n - 1);
    double y0 = uv.getYData().pointAt(0);
    double y1 = uv.getYData().pointAt(n - 1);
    double xf = (n - 1) / (x0 - x1);
    int[] y = new int[n];
    double yf = uv.getYDataAs16BitIntArray(y);
    String title = uv.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=UV/VIS SPECTRUM").append(CRLF);
    jcamp.append("##DATACLASS=XYDATA").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(uv));
    jcamp.append(getXUnitLDR(uv));
    jcamp.append(getYUnitLDR(uv));
    jcamp.append("##NPOINTS=").append(n).append(CRLF);
    jcamp.append("##FIRSTX=").append(x0).append(CRLF);
    jcamp.append("##LASTX=").append(x1).append(CRLF);
    jcamp.append("##DELTAX=").append((x1 - x0) / (n - 1)).append(CRLF);
    jcamp.append("##XFACTOR=").append(xf).append(CRLF);
    jcamp.append("##FIRSTY=").append(y0).append(CRLF);
    jcamp.append("##LASTY=").append(y1).append(CRLF);
    jcamp.append("##YFACTOR=").append(yf).append(CRLF);
    jcamp.append("##XYDATA=(X++(Y..Y))").append(CRLF);
    jcamp.append(ASDFEncoder.encode(Math.round(x0 * xf), Math.round(x1 * xf), y));
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * build a peak table.
   * 
   * @return String
   * @param uv UVSpectrum
   */
  private String buildPeakTable(int block, UVSpectrum uv) {
    StringBuilder jcamp = new StringBuilder();
    String title = uv.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=UV/VIS PEAK TABLE").append(CRLF);
    jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(uv));
    jcamp.append(getXUnitLDR(uv));
    jcamp.append(getYUnitLDR(uv));
    if (uv.hasPeakTable()) {
      Peak1D[] peaks = uv.getPeakTable();
      int n = peaks.length;
      jcamp.append("##NPOINTS=").append(n).append(CRLF);
      jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
      for (int i = 0; i < n; i++) {
	double x = peaks[i].getPosition()[0];
	double y = peaks[i].getPosition()[1];
	jcamp.append(x).append(',').append(y).append(CRLF);
      }
    } else {
      if (!uv.isFullSpectrum()) {
	int n = uv.getXData().getLength();
	jcamp.append("##NPOINTS=").append(n).append(CRLF);
	jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
	for (int i = 0; i < n; i++) {
	  double x = uv.getXData().pointAt(i);
	  double y = uv.getYData().pointAt(i);
	  jcamp.append(x).append(',').append(y).append(CRLF);
	}
      }
    }
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * gets spectrum notes in JCAMP form.
   * title note is ignored, it is handled directly
   * @return String
   */
  @Override
  protected String getJCAMPNotes(Spectrum spectrum) {
    StringBuilder notesStr = new StringBuilder();
    Iterator notesIt = spectrum.getNotes().iterator();
    while (notesIt.hasNext()) {
      Note note = (Note) notesIt.next();
      NoteDescriptor descr = note.getDescriptor();
      if ("title".equals(descr.getKey()))
	continue;
      if (!descr.isAllowedForSpectrumID(ISpectrumIdentifier.UV))
	continue;
      Object value = note.getValue();
      IJCAMPNoteMarshaller marshaller = NoteMarshallerFactory.getInstance().findByDescriptor(descr);
      notesStr.append(marshaller.toJCAMP(value));
    }
    return notesStr.toString();
  }
  /**
   * returns  standard JCAMP unit name.
   * 
   * @return String
   * @param uv UVSpectrum
   */
  private static String getXUnitLDR(UVSpectrum uv) {
    String name;
    Unit unit = uv.getXData().getUnit();
    if (unit.equals(CommonUnit.perCM))
      name = "##XUNITS=1/CM" + CRLF;
    else if (unit.equals(CommonUnit.micrometerWavelength))
      name = "##XUNITS=MICROMETERS" + CRLF;
    else if (unit.equals(CommonUnit.nanometerWavelength))
      name = "##XUNITS=NANOMETERS" + CRLF;
    else if (unit.equals(CommonUnit.second))
      name = "##XUNITS=SECONDS" + CRLF;
    else
      name = "##XUNITS=ARBITRARY UNITS" + CRLF;
    return name;
  }
  /**
   * return standard JCAMP unit name.
   * 
   * @return String
   * @param uv UVSpectrum
   */
  private static String getYUnitLDR(UVSpectrum uv) {
    String name;
    Unit unit = uv.getYData().getUnit();
    if (unit.equals(CommonUnit.transmittance) || unit.equals(CommonUnit.percentTransmittance))
      name = "##YUNITS=TRANSMITTANCE" + CRLF;
    else if (unit.equals(CommonUnit.reflectance) || unit.equals(CommonUnit.percentReflectance))
      name = "##YUNITS=REFLECTANCE" + CRLF;
    else if (unit.equals(CommonUnit.absorbance))
      name = "##YUNITS=ABSORBANCE" + CRLF;
    else if (unit.equals(CommonUnit.kubelka))
      name = "##YUNITS=KUBELKA-MUNK" + CRLF;
    else
      name = "##YUNITS=ARBITRARY UNITS" + CRLF;
    return name;
  }
  /**
   * writes spectrum as compound JCAMP.
   */
  public String toJCAMP(Spectrum spectrum) throws JCAMPException {
    if (!(spectrum instanceof UVSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    UVSpectrum uv = (UVSpectrum) spectrum;
    StringBuilder jcamp = new StringBuilder();
    String title = uv.getTitle();
    // main data
    int nDataBlocks = 1;
    int block = 1;
    boolean linked = false;
    if (uv.hasAssignments())
      nDataBlocks++;
    if (uv.hasPeakTable() && uv.isFullSpectrum())
      nDataBlocks++;
    if (nDataBlocks > 1) {
      jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
      jcamp.append("##JCAMP-DX=4.24").append(CRLF);
      jcamp.append("##DATATYPE=LINK").append(CRLF);
      jcamp.append("##NBLOCKS=").append(nDataBlocks + 1).append(CRLF);
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
      jcamp.append(super.getJCAMPNotes(spectrum));
      block++;
      linked = true;
    }
    if (uv.isFullSpectrum()) {
      jcamp.append(buildFSData(linked ? block : 0, uv));
      block++;
      if (uv.hasAssignments()) {
	jcamp.append(buildAssignmentTable(linked ? block : 0, uv));
	block++;
      }
      if (uv.hasPeakTable()) {
	jcamp.append(buildPeakTable(linked ? block : 0, uv));
	block++;
      }
    } else { // peak spectrum
      jcamp.append(buildPeakTable(linked ? block : 0, uv));
    block++;
    if (uv.hasAssignments()) {
      jcamp.append(buildAssignmentTable(linked ? block : 0, uv));
      block++;
    }
    }
    if (linked)
      jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * writes spectrum as simple JCAMP.
   */
  public String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException {
    if (!(spectrum instanceof UVSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    UVSpectrum uv = (UVSpectrum) spectrum;
    StringBuilder jcamp = new StringBuilder();
    if (uv.isFullSpectrum()) {
      jcamp.append(buildFSData(0, uv));
    } else { // peak spectrum
      if (uv.hasAssignments()) {
	jcamp.append(buildAssignmentTable(0, uv));
      } else {
	jcamp.append(buildPeakTable(0, uv));
      }
    }
    return jcamp.toString();
  }
}
