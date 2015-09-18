package org.jcamp.parser;

import java.util.Iterator;

import org.jcamp.spectrum.Assignment;
import org.jcamp.spectrum.IRSpectrum;
import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.Peak1D;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * adapter between IR spectrum class and JCAMPWriter.
 * 
 * @author Thomas Weber
 */
public class IRJCAMPWriter extends CommonSpectrumJCAMPWriter implements
		ISpectrumJCAMPWriter {

	private final static String CRLF = "\r\n";

	/**
	 * IRJCAMPAdapter constructor comment.
	 */
	protected IRJCAMPWriter() {
		super();
	}

	/**
	 * build assignment table
	 * 
	 * @return java.lang.String
	 * @param block
	 *            int block number (0: no xref, main block)
	 * @param ir
	 *            IRSpectrum
	 */
	private String buildAssignmentTable(int block, IRSpectrum ir) {
		StringBuilder jcamp = new StringBuilder();
		Assignment[] assigns = ir.getAssignments();
		String title = ir.getTitle();
		int n = assigns.length;
		jcamp.append("##TITLE=").append(title != null ? title : "")
				.append(CRLF);
		jcamp.append("##JCAMP-DX=4.24").append(CRLF);
		jcamp.append("##DATATYPE=INFRARED PEAK ASSIGNMENTS").append(CRLF);
		jcamp.append("##DATACLASS=ASSIGNMENTS").append(CRLF);
		if (block != 0) {
			jcamp.append("##BLOCKID=").append(block).append(CRLF);
		}
		jcamp.append(getJCAMPNotes(ir));
		jcamp.append(getXUnitLDR(ir));
		jcamp.append(getYUnitLDR(ir));
		jcamp.append("##NPOINTS=").append(n).append(CRLF);
		jcamp.append("##PEAKASSIGNMENTS=(XYA)").append(CRLF);
		for (int i = 0; i < n; i++) {
			double x = assigns[i].getPosition()[0];
			jcamp.append('(').append(x).append(",1,").append(",<")
					.append(assigns[i].getLabel()).append(">)").append(CRLF);
		}
		jcamp.append("##END=").append(CRLF);
		return jcamp.toString();
	}

	/**
	 * build full spectrum data block.
	 * 
	 * @return java.lang.String
	 * @param ir
	 *            IRSpectrum
	 */
	private String buildFSData(int block, IRSpectrum ir) {
		StringBuilder jcamp = new StringBuilder();
		int n = ir.getXData().getLength();
		double x0 = ir.getXData().pointAt(0);
		double x1 = ir.getXData().pointAt(n - 1);
		double y0 = ir.getYData().pointAt(0);
		double y1 = ir.getYData().pointAt(n - 1);
		double xf = (n - 1) / (x0 - x1);
		int[] y = new int[n];
		double yf = ir.getYDataAs16BitIntArray(y);
		String title = ir.getTitle();
		jcamp.append("##TITLE=").append(title != null ? title : "")
				.append(CRLF);
		jcamp.append("##JCAMP-DX=4.24").append(CRLF);
		jcamp.append("##DATATYPE=INFRARED SPECTRUM").append(CRLF);
		jcamp.append("##DATACLASS=XYDATA").append(CRLF);
		if (block != 0) {
			jcamp.append("##BLOCKID=").append(block).append(CRLF);
		}
		jcamp.append(getJCAMPNotes(ir));
		jcamp.append(getXUnitLDR(ir));
		jcamp.append(getYUnitLDR(ir));
		jcamp.append("##NPOINTS=").append(n).append(CRLF);
		jcamp.append("##FIRSTX=").append(x0).append(CRLF);
		jcamp.append("##LASTX=").append(x1).append(CRLF);
		jcamp.append("##DELTAX=").append((x1 - x0) / (n - 1)).append(CRLF);
		jcamp.append("##XFACTOR=").append(xf).append(CRLF);
		jcamp.append("##FIRSTY=").append(y0).append(CRLF);
		jcamp.append("##LASTY=").append(y1).append(CRLF);
		jcamp.append("##YFACTOR=").append(yf).append(CRLF);
		jcamp.append("##XYDATA=(X++(Y..Y))").append(CRLF);
		jcamp.append(ASDFEncoder.encode(Math.round(x0 * xf),
				Math.round(x1 * xf), y));
		jcamp.append("##END=").append(CRLF);
		return jcamp.toString();
	}

	/**
	 * build a peak table.
	 * 
	 * @return java.lang.String
	 * @param ir
	 *            IRSpectrum
	 */
	private String buildPeakTable(int block, IRSpectrum ir) {
		StringBuilder jcamp = new StringBuilder();
		String title = ir.getTitle();
		jcamp.append("##TITLE=").append(title != null ? title : "")
				.append(CRLF);
		jcamp.append("##JCAMP-DX=4.24").append(CRLF);
		jcamp.append("##DATATYPE=INFRARED PEAK TABLE").append(CRLF);
		jcamp.append("##DATACLASS=PEAK TABLE").append(CRLF);
		if (block != 0) {
			jcamp.append("##BLOCKID=").append(block).append(CRLF);
		}
		jcamp.append(getJCAMPNotes(ir));
		jcamp.append(getXUnitLDR(ir));
		jcamp.append(getYUnitLDR(ir));
		if (ir.hasPeakTable()) {
			Peak1D[] peaks = ir.getPeakTable();
			int n = peaks.length;
			jcamp.append("##NPOINTS=").append(n).append(CRLF);
			jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
			for (int i = 0; i < n; i++) {
				double x = peaks[i].getPosition()[0];
				double y = peaks[i].getPosition()[1];
				jcamp.append(x).append(',').append(y).append(CRLF);
			}
		} else {
			if (!ir.isFullSpectrum()) {
				int n = ir.getXData().getLength();
				jcamp.append("##NPOINTS=").append(n).append(CRLF);
				jcamp.append("##PEAKTABLE=(XY..XY)").append(CRLF);
				for (int i = 0; i < n; i++) {
					double x = ir.getXData().pointAt(i);
					double y = ir.getYData().pointAt(i);
					jcamp.append(x).append(',').append(y).append(CRLF);
				}
			}
		}
		jcamp.append("##END=").append(CRLF);
		return jcamp.toString();
	}

	/**
	 * gets spectrum notes in JCAMP form. title note is ignored, it is handled
	 * directly
	 * 
	 * @return java.lang.String
	 */
	@Override
	protected String getJCAMPNotes(Spectrum spectrum) {
		StringBuilder notesStr = new StringBuilder();
		Iterator<Note> notesIt = spectrum.getNotes().iterator();
		while (notesIt.hasNext()) {
			Note note = notesIt.next();
			NoteDescriptor descr = note.getDescriptor();
			if ("title".equals(descr.getKey()))
				continue;
			if (!descr.isAllowedForSpectrumID(ISpectrumIdentifier.IR))
				continue;
			Object value = note.getValue();
			IJCAMPNoteMarshaller marshaller = NoteMarshallerFactory
					.getInstance().findByDescriptor(descr);
			notesStr.append(marshaller.toJCAMP(value));
		}
		return notesStr.toString();
	}

	/**
	 * returns standard JCAMP unit name.
	 * 
	 * @return java.lang.String
	 * @param ir
	 *            IRSpectrum
	 */
	private static String getXUnitLDR(IRSpectrum ir) {
		String name;
		Unit unit = ir.getXData().getUnit();
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
	 * @param ir
	 *            IRSpectrum
	 */
	private static String getYUnitLDR(IRSpectrum ir) {
		String name;
		Unit unit = ir.getYData().getUnit();
		if (unit.equals(CommonUnit.transmittance)
				|| unit.equals(CommonUnit.percentTransmittance))
			name = "##YUNITS=TRANSMITTANCE" + CRLF;
		else if (unit.equals(CommonUnit.reflectance)
				|| unit.equals(CommonUnit.percentReflectance))
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
	@Override
	public String toJCAMP(Spectrum spectrum) throws JCAMPException {
		if (!(spectrum instanceof IRSpectrum))
			throw new JCAMPException("JCAMP adapter missmatch");
		IRSpectrum ir = (IRSpectrum) spectrum;
		StringBuilder jcamp = new StringBuilder();
		String title = ir.getTitle();
		// main data
		int nDataBlocks = 1;
		int block = 1;
		boolean linked = false;
		if (ir.hasAssignments())
			nDataBlocks++;
		if (ir.hasPeakTable() && ir.isFullSpectrum())
			nDataBlocks++;
		if (nDataBlocks > 1) {
			jcamp.append("##TITLE=").append(title != null ? title : "")
					.append(CRLF);
			jcamp.append("##JCAMP-DX=4.24").append(CRLF);
			jcamp.append("##DATATYPE=LINK").append(CRLF);
			jcamp.append("##NBLOCKS=").append(nDataBlocks + 1).append(CRLF);
			jcamp.append("##BLOCKID=").append(block).append(CRLF);
			jcamp.append(super.getJCAMPNotes(spectrum));
			block++;
			linked = true;
		}
		if (ir.isFullSpectrum()) {
			jcamp.append(buildFSData(linked ? block : 0, ir));
			block++;
			if (ir.hasAssignments()) {
				jcamp.append(buildAssignmentTable(linked ? block : 0, ir));
				block++;
			}
			if (ir.hasPeakTable()) {
				jcamp.append(buildPeakTable(linked ? block : 0, ir));
				block++;
			}
		} else { // peak spectrum
			jcamp.append(buildPeakTable(linked ? block : 0, ir));
			block++;
			if (ir.hasAssignments()) {
				jcamp.append(buildAssignmentTable(linked ? block : 0, ir));
				block++;
			}
		}
		if (linked)
			jcamp.append("##END=").append(CRLF);
		return jcamp.toString();
	}

	/**
	 * writes spectrum as simple JCAMP
	 */
	@Override
	public String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException {
		if (!(spectrum instanceof IRSpectrum))
			throw new JCAMPException("JCAMP adapter missmatch");
		IRSpectrum ir = (IRSpectrum) spectrum;
		StringBuilder jcamp = new StringBuilder();
		// main data
		if (ir.isFullSpectrum()) {
			jcamp.append(buildFSData(0, ir));
		} else { // peak spectrum
			if (ir.hasAssignments()) {
				jcamp.append(buildAssignmentTable(0, ir));
			} else
				jcamp.append(buildPeakTable(0, ir));
		}
		return jcamp.toString();
	}
}
