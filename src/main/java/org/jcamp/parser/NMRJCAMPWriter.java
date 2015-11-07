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
import org.jcamp.spectrum.NMRFIDSpectrum;
import org.jcamp.spectrum.NMRSpectrum;
import org.jcamp.spectrum.Pattern;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;

/**
 * adapter between NMR spectrum class and JCAMPWriter.
 * 
 * @author Thomas Weber
 */
public class NMRJCAMPWriter
  implements ISpectrumJCAMPWriter {
  
  private final static String CRLF = "\r\n";
  
  /**
   * NMRJCAMPAdapter constructor comment.
   */
  protected NMRJCAMPWriter() {
    super();
  }
  /**
   * build assignment table
   * 
   * @return String
   * @param block int  block number (0: no xref, main block)
   * @param nmr NMRSpectrum
   */
  private String buildAssignmentTable(int block, NMRSpectrum nmr) {
    StringBuilder jcamp = new StringBuilder();
    Assignment[] assigns = nmr.getAssignments();
    String title = nmr.getTitle();
    String origin = nmr.getOrigin();
    String owner = nmr.getOwner();
    int n = assigns.length;
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=5.01").append(CRLF);
    jcamp.append("##DATATYPE=NMR PEAK ASSIGNMENTS").append(CRLF);
    jcamp.append("##DATACLASS=ASSIGNMENTS").append(CRLF);
    jcamp.append("##ORIGIN=").append(origin != null ? origin : "").append(CRLF);
    jcamp.append("##OWNER=").append(owner != null ? owner : "COPYRIGHT ??? BY UNKNOWN").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append("##.OBSERVENUCLEUS=").append(nmr.getNucleus()).append(CRLF);
    jcamp.append("##.OBSERVEFREQUENCY=").append(nmr.getFrequency()).append(CRLF);
    if (!nmr.getSolvent().equals("TMS")) {
      jcamp.append("##.SOLVENTREFERENCE=").append(nmr.getSolventReference()).append(CRLF);
    }

    jcamp.append("##XUNITS=PPM").append(CRLF);
    jcamp.append("##YUNITS=ARBITRARY UNITS").append(CRLF);
    jcamp.append("##NPOINTS=").append(n).append(CRLF);
    jcamp.append("##PEAKASSIGNMENTS=(XYMA)").append(CRLF);
    for (int i = 0; i < n; i++) {
      double x = assigns[i].getPosition()[0];
      jcamp
      .append('(')
	  .append(x)
	  .append(",1,")
	  .append(assigns[i].getPattern().getLabel())
	  .append(",<")
	  .append(assigns[i].getLabel())
	  .append(">)")
	  .append(CRLF);
    }
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * build FID data block.
   * 
   * @return String
   * @param nmr NMRFIDSpectrum
   */
  private String buildFIDData(int block, NMRFIDSpectrum nmr) {
    StringBuilder jcamp = new StringBuilder();
    int n = nmr.getXData().getLength();
    double x0 = nmr.getXData().pointAt(0);
    double x1 = nmr.getXData().pointAt(n - 1);
    double xf = (x1 - x0) / (n - 1);
    nmr.mapRealFID();
    double r0 = nmr.getYData().pointAt(0);
    double r1 = nmr.getYData().pointAt(n - 1);
    int[] r = new int[n];
    double rf = nmr.getYDataAs16BitIntArray(r);
    nmr.mapImaginaryFID();
    double im0 = nmr.getYData().pointAt(0);
    double im1 = nmr.getYData().pointAt(n - 1);
    int[] im = new int[n];
    double imf = nmr.getYDataAs16BitIntArray(im);
    String title = nmr.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=5.01").append(CRLF);
    jcamp.append("##DATATYPE=NMR FID").append(CRLF);
    jcamp.append("##DATACLASS=NTUPLES").append(CRLF);
    jcamp.append(getJCAMPNotes(nmr));
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append("##.OBSERVENUCLEUS=").append(nmr.getNucleus()).append(CRLF);
    jcamp.append("##.OBSERVEFREQUENCY=").append(nmr.getFrequency()).append(CRLF);
    jcamp.append("##.DELAY=").append(CRLF); // TODO
    jcamp.append("##.ACQUISITIONMODE=").append(CRLF);
    jcamp.append("##NTUPLES=NMR FID").append(CRLF);
    jcamp.append("##VAR_NAME=TIME,        FID/REAL,        FID/IMAG,       PAGE NUMBER").append(CRLF);
    jcamp.append("##SYMBOL=  X,           R,               I,              N").append(CRLF);
    jcamp.append("##VAR_TYPE=INDEPENDENT, DEPENDENT,       DEPENDENT,      PAGE").append(CRLF);
    jcamp.append("##VAR_FORM=AFFN,        ASDF,            ASDF,           AFFN").append(CRLF);
    jcamp.append("##VAR_DIM=").append(n).append(',').append(n).append(',').append(n).append(",2").append(CRLF);
    jcamp.append("##UNITS=   SECONDS,     ARBITRARY UNITS, ARBITRARY UNITS,").append(CRLF);
    jcamp.append("##FIRST=").append(x0).append(',').append(r0).append(',').append(im0).append(",1").append(CRLF);
    jcamp.append("##LAST=").append(x1).append(',').append(r1).append(',').append(im1).append(",2").append(CRLF);
    jcamp.append("##FACTOR=").append(xf).append(',').append(rf).append(',').append(imf).append(",1").append(CRLF);
    jcamp.append("##PAGE= N=1").append(CRLF);
    jcamp.append("##DATATABLE=(X++(R..R)), XYDATA").append(CRLF);
    jcamp.append(ASDFEncoder.encode(x0 * xf, x1 * xf, r));
    jcamp.append("##PAGE= N=2").append(CRLF);
    jcamp.append("##DATATABLE=(X++(I..I)), XYDATA").append(CRLF);
    jcamp.append(ASDFEncoder.encode(x0 * xf, x1 * xf, im));
    jcamp.append("##ENDNTUPLES=NMR FID").append(CRLF);
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * build full spectrum data block.
   * 
   * @return String
   * @param nmr NMRSpectrum
   */
  private String buildFSData(int block, NMRSpectrum nmr) {
    StringBuilder jcamp = new StringBuilder();
    int n = nmr.getXData().getLength();
    double x0 = nmr.getXData().pointAt(0);
    double x1 = nmr.getXData().pointAt(n - 1);
    double freq = nmr.getFrequency();
    double ref = nmr.getReference();
    if (nmr.getXData().getUnit().equals(CommonUnit.ppm)) {
      x0 = NMRSpectrum.convertToHertz(x0, freq, ref);
      x1 = NMRSpectrum.convertToHertz(x1, freq, ref);
    }
    double y0 = nmr.getYData().pointAt(0);
    double y1 = nmr.getYData().pointAt(n - 1);
    double xf = (n - 1) / (x0 - x1);

    int[] y = new int[n];
    double yf = nmr.getYDataAs16BitIntArray(y);
    String title = nmr.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=5.01").append(CRLF);
    jcamp.append("##DATATYPE=NMR SPECTRUM").append(CRLF);
    jcamp.append("##DATACLASS=XYDATA").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(nmr));
    jcamp.append("##.OBSERVENUCLEUS=").append(nmr.getNucleus()).append(CRLF);
    jcamp.append("##.OBSERVEFREQUENCY=").append(nmr.getFrequency()).append(CRLF);
    String solvent = nmr.getSolvent();
    if (ref == ref) {
      // ensure we have a reference frequency (possibly a peak table)
      int shiftPoint = (int) ((ref - x0) / xf);
      jcamp
      .append("##.SHIFTREFERENCE=,")
      .append(nmr.getSolvent())
      .append(",")
      .append(shiftPoint)
      .append(",")
      .append(nmr.getSolventReference())
      .append(CRLF);
    }
    if (!solvent.equals("TMS")) {
      jcamp.append("##.SOLVENTREFERENCE=").append(nmr.getSolventReference()).append(CRLF);
    }
    jcamp.append("##XUNITS=HZ").append(CRLF);
    jcamp.append("##YUNITS=ARBITRARY UNITS").append(CRLF);
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
   * build pattern table
   * 
   * @return String
   * @param block int  block number (0: no xref, main block)
   * @param nmr NMRSpectrum
   */
  private String buildPatternTable(int block, NMRSpectrum nmr) {
    StringBuilder jcamp = new StringBuilder();
    Pattern[] pattern = nmr.getPatternTable();
    String title = nmr.getTitle();
    int n = pattern.length;
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=5.01").append(CRLF);
    jcamp.append("##DATATYPE=NMR PEAK TABLE").append(CRLF);
    jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(nmr));
    jcamp.append("##.OBSERVENUCLEUS=").append(nmr.getNucleus()).append(CRLF);
    jcamp.append("##.OBSERVEFREQUENCY=").append(nmr.getFrequency()).append(CRLF);
    if (!nmr.getSolvent().equals("TMS")) {
      jcamp.append("##.SOLVENTREFERENCE=").append(nmr.getSolventReference()).append(CRLF);
    }
    jcamp.append("##XUNITS=PPM").append(CRLF);
    jcamp.append("##YUNITS=ARBITRARY UNITS").append(CRLF);
    jcamp.append("##NPOINTS=").append(n).append(CRLF);
    jcamp.append("##PEAKTABLE=(XYM..XYM)").append(CRLF);
    for (int i = 0; i < n; i++) {
      double x = pattern[i].getPosition()[0];
      double y = pattern[i].getPosition()[1];
      jcamp.append(x).append(',').append(y).append(',').append(pattern[i].getLabel()).append(CRLF);
    }
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * build a peak table.
   * 
   * @return String
   * @param nmr NMRSpectrum
   */
  private String buildPeakTable(int block, NMRSpectrum nmr) {
    StringBuilder jcamp = new StringBuilder();
    Peak1D[] peaks = nmr.getPeakTable();
    int n = peaks.length;
    String title = nmr.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=5.01").append(CRLF);
    jcamp.append("##DATATYPE=NMR PEAK TABLE").append(CRLF);
    jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
    jcamp.append(getJCAMPNotes(nmr));
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append("##.OBSERVENUCLEUS=").append(nmr.getNucleus()).append(CRLF);
    jcamp.append("##.OBSERVEFREQUENCY=").append(nmr.getFrequency()).append(CRLF);
    if (!nmr.getSolvent().equals("TMS")) {
      jcamp.append("##.SOLVENTREFERENCE=").append(nmr.getSolventReference()).append(CRLF);
    }
    jcamp.append("##XUNITS=PPM").append(CRLF);
    jcamp.append("##YUNITS=ARBITRARY UNITS").append(CRLF);
    jcamp.append("##NPOINTS=").append(n).append(CRLF);
    jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
    for (int i = 0; i < n; i++) {
      double x = peaks[i].getPosition()[0];
      double y = peaks[i].getPosition()[1];
      jcamp.append(x).append(',').append(y).append(CRLF);
    }
    jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * gets spectrum notes in JCAMP form.
   * title note is ignored, it is handled directly
   * shk3: BLOCKID is also ignored, this caused multiply block elements in a block
   * when writing a link spectrum.
   * @return String
   */
  protected String getJCAMPNotes(Spectrum spectrum) {
    StringBuilder notesStr = new StringBuilder();
    Iterator notesIt = spectrum.getNotes().iterator();
    while (notesIt.hasNext()) {
      Note note = (Note) notesIt.next();
      NoteDescriptor descr = note.getDescriptor();
      if ("title".equals(descr.getKey()))
	continue;
      if (!descr.isAllowedForSpectrum(spectrum))
	continue;
      if ("BLOCKID".equals(descr.getKey()))
	continue;
      Object value = note.getValue();
      IJCAMPNoteMarshaller marshaller = NoteMarshallerFactory.getInstance().findByDescriptor(descr);
      notesStr.append(marshaller.toJCAMP(value));
    }
    return notesStr.toString();
  }
  /**
   * writes spectrum as compound JCAMP.
   */
  public String toJCAMP(Spectrum spectrum) throws JCAMPException {
    if (!(spectrum instanceof NMRSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    NMRSpectrum nmr = (NMRSpectrum) spectrum;
    StringBuilder jcamp = new StringBuilder();
    String title = nmr.getTitle();
    // main data
    int nDataBlocks = 1;
    int block = 1;
    boolean linked = false;
    if (nmr.hasAssignments())
      nDataBlocks++;
    else { // pattern are only included pattern if no assignment table is present
      if (nmr.hasPatternTable())
	nDataBlocks++;
    }
    if (nmr.hasPeakTable() && nmr.isFullSpectrum())
      nDataBlocks++;
    if (nDataBlocks > 1) {
      jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
      jcamp.append("##JCAMP-DX=5.01").append(CRLF);
      jcamp.append("##DATATYPE=LINK").append(CRLF);
      jcamp.append("##NBLOCKS=").append(nDataBlocks + 1).append(CRLF);
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
      block++;
      linked = true;
    }
    if (nmr.isFullSpectrum()) {
      if (nmr.isFID()) {
	jcamp.append(buildFIDData(linked ? block : 0, (NMRFIDSpectrum) nmr));
	block++;
      } else {
	jcamp.append(buildFSData(linked ? block : 0, nmr));
	block++;
      }
      if (nmr.hasAssignments()) {
	jcamp.append(buildAssignmentTable(linked ? block : 0, nmr));
	block++;
      } else {
	if (nmr.hasPatternTable()) {
	  jcamp.append(buildPatternTable(linked ? block : 0, nmr));
	  block++;
	}
      }
      if (nmr.hasPeakTable()) {
	jcamp.append(buildPeakTable(linked ? block : 0, nmr));
	block++;
      }
    } else { // peak spectrum
      jcamp.append(buildPeakTable(linked ? block : 0, nmr));
    block++;
    if (nmr.hasAssignments()) {
      jcamp.append(buildAssignmentTable(linked ? block : 0, nmr));
      block++;
    } else {
      if (nmr.hasPatternTable()) {
	jcamp.append(buildPatternTable(linked ? block : 0, nmr));
	block++;
      }
    }
    }
    if (linked)
      jcamp.append("##END=").append(CRLF);
    return jcamp.toString();
  }
  /**
   * writes spectrum as simple JCAMP with possible information loss.
   */
  public String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException {
    if (!(spectrum instanceof NMRSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    NMRSpectrum nmr = (NMRSpectrum) spectrum;
    StringBuilder jcamp = new StringBuilder();
    // main data
    if (nmr.isFullSpectrum()) {
      if (nmr.isFID()) {
	jcamp.append(buildFIDData(0, (NMRFIDSpectrum) nmr));
      } else {
	jcamp.append(buildFSData(0, nmr));
      }
    } else {
      if (nmr.hasAssignments()) {
	jcamp.append(buildAssignmentTable(0, nmr));
      } else if (nmr.hasPatternTable()) {
	jcamp.append(buildPatternTable(0, nmr));
      } else if (nmr.hasPeakTable()) {
	jcamp.append(buildPeakTable(0, nmr));
      }
    }
    return jcamp.toString();
  }
}
