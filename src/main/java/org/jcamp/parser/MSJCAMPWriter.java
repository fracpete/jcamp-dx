package org.jcamp.parser;

import java.util.Iterator;

import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.MassSpectrum;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between MS spectrum class and JCAMPWriter.
 * 
 * @author Thomas Weber
 */
public class MSJCAMPWriter
  extends CommonSpectrumJCAMPWriter
  implements ISpectrumJCAMPWriter {
  
  private final static String CRLF = "\r\n";
  
  /**
   * MSJCAMPAdapter constructor comment.
   */
  protected MSJCAMPWriter() {
    super();
  }
  /**
   * build a peak table.
   * 
   * @return java.lang.String
   * @param ms MassSpectrum
   */
  private String buildPeakTable(int block, MassSpectrum ms) {
    StringBuffer jcamp = new StringBuffer();
    String title = ms.getTitle();
    jcamp.append("##TITLE=").append(title != null ? title : "").append(CRLF);
    jcamp.append("##JCAMP-DX=5.00").append(CRLF);
    jcamp.append("##DATATYPE=MASS SPECTRUM").append(CRLF);
    jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
    if (block != 0) {
      jcamp.append("##BLOCKID=").append(block).append(CRLF);
    }
    jcamp.append(getJCAMPNotes(ms));
    jcamp.append(getXUnitLDR(ms));
    jcamp.append(getYUnitLDR(ms));
    if (ms.hasPeakTable()) {
      Peak1D[] peaks = ms.getPeakTable();
      int n = peaks.length;
      jcamp.append("##NPOINTS=").append(n).append(CRLF);
      jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
      for (int i = 0; i < n; i++) {
	double x = peaks[i].getPosition()[0];
	double y = peaks[i].getPosition()[1];
	jcamp.append(x).append(',').append(y).append(CRLF);
      }
    } else {
      if (!ms.isFullSpectrum()) {
	int n = ms.getXData().getLength();
	jcamp.append("##NPOINTS=").append(n).append(CRLF);
	jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
	for (int i = 0; i < n; i++) {
	  double x = ms.getXData().pointAt(i);
	  double y = ms.getYData().pointAt(i);
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
      if (!descr.isAllowedForSpectrumID(ISpectrumIdentifier.MS))
	continue;
      Object value = note.getValue();
      IJCAMPNoteMarshaller marshaller = NoteMarshallerFactory.getInstance().findByDescriptor(descr);
      if (marshaller != null) {
	notesStr.append(marshaller.toJCAMP(value));
      }
    }
    return notesStr.toString();
  }
  /**
   * returns  standard JCAMP unit name.
   * 
   * @return java.lang.String
   * @param ms MassSpectrum
   */
  private static String getXUnitLDR(MassSpectrum ms) {
    String name;
    Unit unit = ms.getXData().getUnit();
    if (unit.equals(CommonUnit.mz))
      name = "##XUNITS=M/Z" + CRLF;
    else
      name = "##XUNITS=ARBITRARY UNITS" + CRLF;
    return name;
  }
  /**
   * return standard JCAMP unit name.
   * 
   * @return java.lang.String
   * @param ms MassSpectrum
   */
  private static String getYUnitLDR(MassSpectrum ms) {
    String name;
    Unit unit = ms.getYData().getUnit();
    if (unit.equals(CommonUnit.relativeAbundance) || unit.equals(CommonUnit.abundance))
      name = "##YUNITS=RELATIVE ABUNDANCE" + CRLF;
    else
      name = "##YUNITS=ARBITRARY UNITS" + CRLF;
    return name;
  }
  /**
   * writes spectrum as compound JCAMP.
   */
  public String toJCAMP(Spectrum spectrum) throws JCAMPException {
    if (!(spectrum instanceof MassSpectrum))
      throw new JCAMPException("JCAMP adapter missmatch");
    MassSpectrum ms = (MassSpectrum) spectrum;
    StringBuffer jcamp = new StringBuffer();
    // mass spectra are always peak spectra
    jcamp.append(buildPeakTable(0, ms));
    return jcamp.toString();
  }
  /**
   * writes spectrum as simple JCAMP.
   */
  public String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException {
    // mass spectra are always simple JCAMPs
    return toJCAMP(spectrum);
  }
}
