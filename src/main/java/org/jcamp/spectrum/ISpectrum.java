/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcamp.spectrum;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.jcamp.math.AxisMap;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;

/**
 *
 * @author fr
 */
public interface ISpectrum extends Cloneable, Serializable {

	Object clone();

	/**
	 * return integer identifier key.
	 *
	 * @return String
	 */
	int getIdentifier();

	/**
	 * get controlling master spectrum if spectrum is part of a composite.
	 *
	 * @return Spectrum
	 */
	Spectrum getMasterSpectrum();

	/**
	 * get note by index.
	 *
	 * @return org.jcamp.spectrum.notes.Note
	 * @param index
	 *            int
	 */
	Note getNote(int index);

	/**
	 * gets notes.
	 *
	 * @return java.util.Collection
	 */
	Collection<Note> getNotes();

	/**
	 * gets spectrum notes by NoteDescriptor
	 *
	 * @return ArrayList Notes
	 * @param NoteDescriptor
	 *            descr
	 */
	List<Note> getNotes(NoteDescriptor descr);

	/**
	 * gets the creator of the spectrum.
	 *
	 * @return String
	 */
	String getOrigin();

	/**
	 * gets the rights owner.
	 *
	 * @return String
	 */
	String getOwner();

	Peak[] getPeakTable();

	/**
	 * return spectrum title.
	 *
	 * @return String
	 */
	String getTitle();

	/**
	 * get x-axis label
	 *
	 * @return String
	 */
	String getXAxisLabel();

	/**
	 * get x-axis mapping
	 *
	 * @return AxisMap
	 */
	AxisMap getXAxisMap();

	Object getXData();

	/**
	 * get y-axis label
	 *
	 * @return String
	 */
	String getYAxisLabel();

	/**
	 * get y-axis mapping
	 *
	 * @return AxisMap
	 */
	AxisMap getYAxisMap();

	Object getYData();

	/**
	 * flag indicating spectrum is controlled by another.
	 *
	 * @return boolean
	 */
	boolean hasMasterSpectrum();

	boolean hasPeakTable();

	/**
	 * flag indicating full spectra.
	 *
	 * @return boolean
	 */
	boolean isFullSpectrum();

	/**
	 * check if same type of spectrum needed to compare multiple spectra.
	 *
	 * @return boolean
	 * @param otherSpectrum
	 *            Spectrum
	 */
	boolean isSameType(Spectrum otherSpectrum);

	/**
	 * Insert the method's description here. Creation date: (05.04.00 11:28:16)
	 *
	 * @param newMasterSpectrum
	 *            Spectrum
	 */
	void setMasterSpectrum(Spectrum newMasterSpectrum);

	/**
	 * sets note for note descriptor. If unique exchanges value, otherwise adds
	 * new value.
	 *
	 * @param key
	 *            String
	 * @param value
	 *            java.lang.Object
	 */
	void setNote(NoteDescriptor descr, Object value);

	/**
	 * sets note.
	 *
	 * @param key
	 *            String
	 * @param value
	 *            java.lang.Object
	 */
	void setNote(String key, Object value);

	/**
	 * changes note values.
	 *
	 * @param index
	 *            int
	 * @param value
	 *            java.lang.Object
	 */
	void setNoteValue(int index, Object value);

	/**
	 * sets origin note.
	 *
	 * @param newOrigin
	 *            String
	 */
	void setOrigin(String newOrigin);

	/**
	 * sets owner note
	 *
	 * @param newOwner
	 *            String
	 */
	void setOwner(String newOwner);

	/**
	 * set spectrum description here.
	 *
	 * @param newTitle
	 *            String
	 */
	void setTitle(String newTitle);
	
}
