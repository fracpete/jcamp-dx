package org.jcamp.parser;

import java.util.Iterator;

import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.FluorescenceSpectrum;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between Fluorescence spectrum class and JCAMPWriter.
 * 
 * @author Thomas Weber
 */
public class FluorescenceJCAMPWriter
  implements ISpectrumJCAMPWriter {
  
  private final static String CRLF = "\r\n";

  /**
   * UVJCAMPAdapter constructor comment.
   */
  protected FluorescenceJCAMPWriter() {
    super();
  }

  /**
   * build assignment table
   * 
   * @return java.lang.String
   * @param block int  block number (0: no xref, main block)
   * @param uv FluorescenceSpectrum
   */
  private String buildAssignmentTable(int block, FluorescenceSpectrum fl) {
    StringBuffer jcamp = new StringBuffer();
    Assignment[] assigns = fl.getAssignments();
    String title = fl.getTitle();
    int n = assigns.length;
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=FLUORESCENCE PEAK ASSIGNMENTS").append(CRLF);
    jcamp.append("##DATACLASS=ASSIGNMENTS").append(CRLF);
    jcamp.append(getJCAMPNotes(fl));
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getXUnitLDR(fl));
    jcamp.append(getYUnitLDR(fl));
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
   * @param uv FluorescenceSpectrum
   */
  private String buildFSData(int block, FluorescenceSpectrum fl) {
    StringBuffer jcamp = new StringBuffer();
    int n = fl.getXData().getLength();
    double x0 = fl.getXData().pointAt(0);
    double x1 = fl.getXData().pointAt(n - 1);
    double y0 = fl.getYData().pointAt(0);
    double y1 = fl.getYData().pointAt(n - 1);
    double xf = (n - 1) / (x0 - x1);
    int[] y = new int[n];
    double yf = fl.getYDataAs16BitIntArray(y);
    String title = fl.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=FLUORESCENCE").append(CRLF);
    jcamp.append("##DATACLASS=XYDATA").append(CRLF);
    jcamp.append(getJCAMPNotes(fl));
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getXUnitLDR(fl));
    jcamp.append(getYUnitLDR(fl));
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
   * @param uv FluorescenceSpectrum
   */
  private String buildPeakTable(int block, FluorescenceSpectrum fl) {
    StringBuffer jcamp = new StringBuffer();
    String title = fl.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=4.24").append(CRLF);
    jcamp.append("##DATATYPE=FLUORESCENCE PEAK TABLE").append(CRLF);
    jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
    jcamp.append(getJCAMPNotes(fl));
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getXUnitLDR(fl));
    jcamp.append(getYUnitLDR(fl));
    if (fl.hasPeakTable()) {
      Peak1D[] peaks = fl.getPeakTable();
      int n = peaks.length;
      jcamp.append("##NPOINTS=").append(n).append(CRLF);
      jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
      for (int i = 0; i < n; i++) {
	double x = peaks[i].getPosition()[0];
	double y = peaks[i].getPosition()[1];
	jcamp.append(x).append(',').append(y).append(CRLF);
      }
    } else {
      if (!fl.isFullSpectrum()) {
	int n = fl.getXData().getLength();
	jcamp.append("##NPOINTS=").append(n).append(CRLF);
	jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
	for (int i = 0; i < n; i++) {
	  double x = fl.getXData().pointAt(i);
	  double y = fl.getYData().pointAt(i);
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
  protected String getJCAMPNotes(Spectrum spectrum) {
    StringBuffer notesStr = new StringBuffer();
    Iterator notesIt = spectrum.getNotes().iterator();
    while (notesIt.hasNext()) {
      Note note = (Note) notesIt.next();
      NoteDescriptor descr = note.getDescriptor();
      if ("title".equals(descr.getKey()))
	continue;
      if (!descr.isAllowedForSpectrumID(ISpectrumIdentifier.FLUORESCENCE))
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
   * @param fs FluorescenceSpectrum
   */
  private static String getXUnitLDR(FluorescenceSpectrum fs) {
    String name;
    Unit unit = fs.getXData().getUnit();
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
   * @param fs FluorescenceSpectrum
   */
  private static String getYUnitLDR(FluorescenceSpectrum fs) {
    String name;
    Unit unit = fs.getYData().getUnit();
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
    if (!(spectrum instanceof FluorescenceSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    FluorescenceSpectrum uv = (FluorescenceSpectrum) spectrum;
    StringBuffer jcamp = new StringBuffer();
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
    if (!(spectrum instanceof FluorescenceSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    FluorescenceSpectrum uv = (FluorescenceSpectrum) spectrum;
    StringBuffer jcamp = new StringBuffer();
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