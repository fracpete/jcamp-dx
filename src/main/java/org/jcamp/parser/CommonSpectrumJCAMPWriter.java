package org.jcamp.parser;

import java.util.Iterator;

import org.jcamp.spectrum.Spectrum;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;
/**
 * abstract base class for all JCAMP writers defined here.
 * @author Thomas Weber
 */
public abstract class CommonSpectrumJCAMPWriter implements ISpectrumJCAMPWriter {
/**
 * CommonSpectrumJCAMPWriter constructor comment.
 */
public CommonSpectrumJCAMPWriter() {
	super();
}
/**
 * gets spectrum notes in JCAMP form.
 * title note is ignored, it is handled directly
 * @return java.lang.String
 */
protected String getJCAMPNotes(Spectrum spectrum) {
	StringBuilder notesStr = new StringBuilder();
	Iterator notesIt = spectrum.getNotes().iterator();
	while (notesIt.hasNext()) {
		Note note = (Note) notesIt.next();
		NoteDescriptor descr = note.getDescriptor();
		if ("title".equals(descr.getKey()))
			continue;
		if (!descr.isGlobal())
			continue;
		IJCAMPNoteMarshaller marshaller =
			NoteMarshallerFactory.getInstance().findByDescriptor(descr);
		Object value = note.getValue();
		notesStr.append(marshaller.toJCAMP(value));
	}
	return notesStr.toString();
}
}
