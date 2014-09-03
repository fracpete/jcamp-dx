package org.jcamp.parser;

import java.util.Iterator;

import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.RamanSpectrum;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between Raman spectrum class and JCAMPWriter.
 * 
 * @author Thomas Weber
 */
public class RamanJCAMPWriter
  extends CommonSpectrumJCAMPWriter
  implements ISpectrumJCAMPWriter {
  
  private final static String CRLF = "\r\n";
  
  /**
   * RamanJCAMPAdapter constructor comment.
   */
  protected RamanJCAMPWriter() {
    super();
  }
  /**
   * build assignment table
   * 
   * @return java.lang.String
   * @param block int  block number (0: no xref, main block)
   * @param raman RamanSpectrum
   */
  private String buildAssignmentTable(int block, RamanSpectrum raman) {
    StringBuffer jcamp = new StringBuffer();
    Assignment[] assigns = raman.getAssignments();
    String title = raman.getTitle();
    int n = assigns.length;
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=RAMAN PEAK ASSIGNMENTS").append(CRLF);
    jcamp.append("##DATACLASS=ASSIGNMENTS").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(raman));
    jcamp.append(getXUnitLDR(raman));
    jcamp.append(getYUnitLDR(raman));
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
   * @return java.lang.String
   * @param raman RamanSpectrum
   */
  private String buildFSData(int block, RamanSpectrum raman) {
    StringBuffer jcamp = new StringBuffer();
    int n = raman.getXData().getLength();
    double x0 = raman.getXData().pointAt(0);
    double x1 = raman.getXData().pointAt(n - 1);
    double y0 = raman.getYData().pointAt(0);
    double y1 = raman.getYData().pointAt(n - 1);
    double xf = (n - 1) / (x0 - x1);
    int[] y = new int[n];
    double yf = raman.getYDataAs16BitIntArray(y);
    String title = raman.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=RAMAN SPECTRUM").append(CRLF);
    jcamp.append("##DATACLASS=XYDATA").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(raman));
    jcamp.append(getXUnitLDR(raman));
    jcamp.append(getYUnitLDR(raman));
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
   * @return java.lang.String
   * @param raman RamanSpectrum
   */
  private String buildPeakTable(int block, RamanSpectrum raman) {
    StringBuffer jcamp = new StringBuffer();
    String title = raman.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=RAMAN PEAK TABLE").append(CRLF);
    jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(raman));
    jcamp.append(getXUnitLDR(raman));
    jcamp.append(getYUnitLDR(raman));
    if (raman.hasPeakTable()) {
      Peak1D[] peaks = raman.getPeakTable();
      int n = peaks.length;
      jcamp.append("##NPOINTS=").append(n).append(CRLF);
      jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
      for (int i = 0; i < n; i++) {
	double x = peaks[i].getPosition()[0];
	double y = peaks[i].getPosition()[1];
	jcamp.append(x).append(',').append(y).append(CRLF);
      }
    } else {
      if (!raman.isFullSpectrum()) {
	int n = raman.getXData().getLength();
	jcamp.append("##NPOINTS=").append(n).append(CRLF);
	jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
	for (int i = 0; i < n; i++) {
	  double x = raman.getXData().pointAt(i);
	  double y = raman.getYData().pointAt(i);
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
   * @return java.lang.String
   */
  @Override
  protected String getJCAMPNotes(Spectrum spectrum) {
    StringBuffer notesStr = new StringBuffer();
    Iterator notesIt = spectrum.getNotes().iterator();
    while (notesIt.hasNext()) {
      Note note = (Note) notesIt.next();
      NoteDescriptor descr = note.getDescriptor();
      if ("title".equals(descr.getKey()))
	continue;
      if (!descr.isAllowedForSpectrumID(ISpectrumIdentifier.RAMAN))
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
   * @return java.lang.String
   * @param raman RamanSpectrum
   */
  private static String getXUnitLDR(RamanSpectrum raman) {
    String name;
    Unit unit = raman.getXData().getUnit();
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
   * @return java.lang.String
   * @param raman RamanSpectrum
   */
  private static String getYUnitLDR(RamanSpectrum raman) {
    String name;
    Unit unit = raman.getYData().getUnit();
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
    if (!(spectrum instanceof RamanSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    RamanSpectrum raman = (RamanSpectrum) spectrum;
    StringBuffer jcamp = new StringBuffer();
    String title = raman.getTitle();
    // main data
    int nDataBlocks = 1;
    int block = 1;
    boolean linked = false;
    if (raman.hasAssignments())
      nDataBlocks++;
    if (raman.hasPeakTable() && raman.isFullSpectrum())
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
    if (raman.isFullSpectrum()) {
      jcamp.append(buildFSData(linked ? block : 0, raman));
      block++;
      if (raman.hasAssignments()) {
	jcamp.append(buildAssignmentTable(linked ? block : 0, raman));
	block++;
      }
      if (raman.hasPeakTable()) {
	jcamp.append(buildPeakTable(linked ? block : 0, raman));
	block++;
      }
    } else { // peak spectrum
      jcamp.append(buildPeakTable(linked ? block : 0, raman));
    block++;
    if (raman.hasAssignments()) {
      jcamp.append(buildAssignmentTable(linked ? block : 0, raman));
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
    if (!(spectrum instanceof RamanSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    RamanSpectrum raman = (RamanSpectrum) spectrum;
    StringBuffer jcamp = new StringBuffer();
    // main data
    if (raman.isFullSpectrum()) {
      jcamp.append(buildFSData(0, raman));
    } else { // peak spectrum
      if (raman.hasAssignments()) {
	jcamp.append(buildAssignmentTable(0, raman));
      } else {
	jcamp.append(buildPeakTable(0, raman));
      }
    }
    return jcamp.toString();
  }
}
