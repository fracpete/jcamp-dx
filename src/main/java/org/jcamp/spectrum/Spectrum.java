/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;

/**
 * Prototype implementation for spectra data.
 * 
 * @see Spectrum1D
 * @see Spectrum2D
 * @see IRSpectrum
 * 
 * @author Thomas Weber
 * 
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public abstract class Spectrum implements ISpectrum {


	/** for serialization. */
	private static final long serialVersionUID = -8843455456952718734L;

	/**
	 * controlling spectrum in a spectra cascade, e.g. GC/MS: master GC, slave
	 * MS
	 */
	protected Spectrum masterSpectrum = null;

	/** notes */
	HashMap<NoteDescriptor, ArrayList<Note>> noteMap = new HashMap<NoteDescriptor, ArrayList<Note>>(
			20);

	protected ArrayList<Note> notes = new ArrayList<Note>(20);

	Spectrum() {
		super();
	}

	@Override
	public Object clone() {
		Spectrum spectrum = null;
		try {
			spectrum = (Spectrum) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		spectrum.copyNotes(this);
		return spectrum;
	}

	/**
	 * notes deep copying for clone()
	 * 
	 */
	protected void copyNotes(Spectrum spectrum) {
		Iterator<Note> entries = spectrum.getNotes().iterator();
		while (entries.hasNext()) {
			Note note = entries.next();
			setNote(note.getDescriptor(), note.clone());
		}

	}

	/**
	 * return integer identifier key.
	 * 
	 * @return String
	 */
	@Override
	public int getIdentifier() {
		return -1;
	}

	/**
	 * get controlling master spectrum if spectrum is part of a composite.
	 * 
	 * @return Spectrum
	 */
	@Override
	public Spectrum getMasterSpectrum() {
		return masterSpectrum;
	}

	/**
	 * get note by index.
	 * 
	 * @return org.jcamp.spectrum.notes.Note
	 * @param index
	 *            int
	 */
	@Override
	public Note getNote(int index) {
		if (index < notes.size())
			return notes.get(index);
		else
			return null;
	}

	/**
	 * gets notes.
	 * 
	 * @return java.util.Collection
	 */
	@Override
	public Collection<Note> getNotes() {
		return Collections.unmodifiableList(notes);
	}

	/**
	 * gets spectrum notes by NoteDescriptor
	 * 
	 * @return ArrayList Notes
	 * @param NoteDescriptor
	 *            descr
	 */
	@Override
	public List<Note> getNotes(NoteDescriptor descr) {
		ArrayList<Note> list = noteMap.get(descr);
		if (list == null)
			return null;
		else
			return list;
	}

	/**
	 * gets the creator of the spectrum.
	 * 
	 * @return String
	 */
	@Override
	public String getOrigin() {
		List<Note> origin = getNotes(NoteDescriptor.ORIGIN);
		if (origin != null)
			return (String) origin.get(0).getValue();
		else
			return "UNKNOWN ORIGIN";
	}

	/**
	 * gets the rights owner.
	 * 
	 * @return String
	 */
	@Override
	public String getOwner() {
		List<Note> owner = getNotes(NoteDescriptor.OWNER);
		if (owner != null)
			return (String) owner.get(0).getValue();
		else
			return "UNKNOWN OWNER";
	}

	/**
	 * return spectrum title.
	 * 
	 * @return String
	 */
	@Override
	public String getTitle() {
		List<Note> title = getNotes(NoteDescriptor.TITLE);
		if (title != null) {
			return (String) title.get(0).getValue();
		} else {
			return "SPECTRUM"; // never reached
		}
	}


	/**
	 * flag indicating spectrum is controlled by another.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean hasMasterSpectrum() {
		return masterSpectrum != null;
	}


	/**
	 * check if same type of spectrum needed to compare multiple spectra.
	 * 
	 * @return boolean
	 * @param otherSpectrum
	 *            Spectrum
	 */
	@Override
	public boolean isSameType(Spectrum otherSpectrum) {
		if (otherSpectrum == null) {
			return false;
		}
		return getClass().equals(otherSpectrum.getClass());
	}

	/**
	 * Insert the method's description here. Creation date: (05.04.00 11:28:16)
	 * 
	 * @param newMasterSpectrum
	 *            Spectrum
	 */
	@Override
	public void setMasterSpectrum(Spectrum newMasterSpectrum) {
		masterSpectrum = newMasterSpectrum;
	}

	/**
	 * sets note for note descriptor. If unique exchanges value, otherwise adds
	 * new value.
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            java.lang.Object
	 */
	@Override
	public synchronized void setNote(NoteDescriptor descr, Object value) {
		Note note = new Note();
		note.setValue(value);
		note.setDescriptor(descr);
		ArrayList<Note> noteArr = noteMap.get(descr);
		if (noteArr != null && descr.isUnique()) {
			// ensure uniqueness, so exchange just value
			Note oldNote = noteArr.get(0);
			oldNote.setValue(value);
			note = oldNote;
			return;
		}
		notes.add(note);
		if (noteArr == null) {
			noteArr = new ArrayList<Note>(descr.isUnique() ? 1 : 10);
			noteMap.put(descr, noteArr);
		}
		noteArr.add(note);
	}

	/**
	 * sets note.
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            java.lang.Object
	 */
	@Override
	public void setNote(String key, Object value) {
		NoteDescriptor descr = NoteDescriptor.findByKey(key);
		if (descr == null) // non-standard, so use default ctor
			descr = new NoteDescriptor(key);
		setNote(descr, value);
	}

	/**
	 * changes note values.
	 * 
	 * @param index
	 *            int
	 * @param value
	 *            java.lang.Object
	 */
	@Override
	public synchronized void setNoteValue(int index, Object value) {
		if (index >= notes.size())
			return;
		notes.get(index).setValue(value);
	}

	/**
	 * sets origin note.
	 * 
	 * @param newOrigin
	 *            String
	 */
	@Override
	public void setOrigin(String newOrigin) {
		setNote(NoteDescriptor.ORIGIN, newOrigin);
	}

	/**
	 * sets owner note
	 * 
	 * @param newOwner
	 *            String
	 */
	@Override
	public void setOwner(String newOwner) {
		setNote(NoteDescriptor.OWNER, newOwner);
	}

	/**
	 * set spectrum description here.
	 * 
	 * @param newTitle
	 *            String
	 */
	@Override
	public void setTitle(String newTitle) {
		setNote(NoteDescriptor.TITLE, newTitle);
	}
}
