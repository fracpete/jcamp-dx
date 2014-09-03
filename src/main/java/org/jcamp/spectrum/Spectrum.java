package org.jcamp.spectrum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jcamp.math.AxisMap;
import org.jcamp.spectrum.notes.Note;
import org.jcamp.spectrum.notes.NoteDescriptor;

/**
 * abstract base class for spectra data.
 * 
 * @author Thomas Weber
 */
public abstract class Spectrum
  implements Cloneable, Serializable {

  /** for serialization. */
  private static final long serialVersionUID = -8843455456952718734L;
  
  /** controlling spectrum in a spectra cascade,
    e.g. GC/MS: master GC, slave MS */
  protected Spectrum masterSpectrum = null;
  
  /** notes */
  HashMap noteMap = new HashMap(20);
  
  protected ArrayList notes = new ArrayList(20);

  /**
   * Spectrum constructor comment.
   */
  Spectrum() {
    super();
  }

  /**
   * cloning of spectra.
   * @return java.lang.Object
   */
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
    Iterator entries = spectrum.getNotes().iterator();
    while (entries.hasNext()) {
      Note note = (Note) entries.next();
      setNote(note.getDescriptor(), note.clone());
    }

  }

  /**
   * return integer identifier key.
   * @return java.lang.String
   */
  public abstract int getIdentifier();

  /**
   * get controlling master spectrum if spectrum is part of a composite.
   * @return Spectrum
   */
  public Spectrum getMasterSpectrum() {
    return masterSpectrum;
  }

  /**
   * get note by index.
   * 
   * @return org.jcamp.spectrum.notes.Note
   * @param index int
   */
  public Note getNote(int index) {
    if (index < notes.size())
      return (Note) notes.get(index);
    else
      return null;
  }

  /**
   * gets notes.
   * 
   * @return java.util.Collection
   */
  public Collection getNotes() {
    return Collections.unmodifiableList(notes);
  }

  /**
   * gets spectrum notes by NoteDescriptor
   * 
   * @return ArrayList Notes
   * @param NoteDescriptor descr
   */
  public List getNotes(NoteDescriptor descr) {
    ArrayList list = (ArrayList) noteMap.get(descr);
    if (list == null)
      return null;
    else
      return list;
  }

  /**
   * gets the creator of the spectrum.
   * 
   * @return java.lang.String
   */
  public java.lang.String getOrigin() {
    List origin = getNotes(NoteDescriptor.ORIGIN);
    if (origin != null)
      return (String) ((Note) origin.get(0)).getValue();
    else
      return "UNKNOWN ORIGIN";
  }

  /**
   * gets the rights owner.
   * 
   * @return java.lang.String
   */
  public java.lang.String getOwner() {
    List owner = getNotes(NoteDescriptor.OWNER);
    if (owner != null)
      return (String) ((Note) owner.get(0)).getValue();
    else
      return "UNKNOWN OWNER";
  }

  /**
   * return spectrum title.
   * @return String
   */
  public String getTitle() {
    List title = getNotes(NoteDescriptor.TITLE);
    if (title != null) {
      return (String) ((Note) title.get(0)).getValue();
    }
    else {
      return "SPECTRUM"; // never reached
    }
  }

  /**
   * get x-axis label
   * 
   * @return java.lang.String
   */
  public abstract String getXAxisLabel();

  /**
   * get x-axis mapping
   * 
   * @return AxisMap
   */
  public abstract AxisMap getXAxisMap();

  /**
   * get y-axis label
   * 
   * @return java.lang.String
   */
  public abstract String getYAxisLabel();

  /**
   * get y-axis mapping
   * 
   * @return AxisMap
   */
  public abstract AxisMap getYAxisMap();

  /**
   * flag indicating spectrum is controlled by another.
   * @return boolean
   */
  public boolean hasMasterSpectrum() {
    return masterSpectrum != null;
  }

  /**
   * flag indicating full spectra.
   * @return boolean
   */
  public abstract boolean isFullSpectrum();

  /**
   * check if same type of spectrum 
   * needed to compare multiple spectra.
   * @return boolean
   * @param otherSpectrum Spectrum
   */
  public abstract boolean isSameType(Spectrum otherSpectrum);

  /**
   * Insert the method's description here.
   * Creation date: (05.04.00 11:28:16)
   * @param newMasterSpectrum Spectrum
   */
  public void setMasterSpectrum(Spectrum newMasterSpectrum) {
    masterSpectrum = newMasterSpectrum;
  }

  /**
   * sets note for note descriptor. If unique exchanges value, otherwise adds new value.
   * 
   * @param key java.lang.String
   * @param value java.lang.Object
   */
  public synchronized void setNote(NoteDescriptor descr, Object value) {
    Note note = new Note();
    note.setValue(value);
    note.setDescriptor(descr);
    ArrayList noteArr = (ArrayList) noteMap.get(descr);
    if (noteArr != null && descr.isUnique()) {
      // ensure uniqueness, so exchange just value
      Note oldNote = (Note) noteArr.get(0);
      oldNote.setValue(value);
      note = oldNote;
      return;
    }
    notes.add(note);
    if (noteArr == null) {
      noteArr = new ArrayList(descr.isUnique() ? 1 : 10);
      noteMap.put(descr, noteArr);
    }
    noteArr.add(note);
  }

  /**
   * sets note.
   * 
   * @param key java.lang.String
   * @param value java.lang.Object
   */
  public void setNote(String key, Object value) {
    NoteDescriptor descr = NoteDescriptor.findByKey(key);
    if (descr == null) // non-standard, so use default ctor
      descr = new NoteDescriptor(key);
    setNote(descr, value);
  }

  /**
   * changes note values.
   * 
   * @param index int
   * @param value java.lang.Object
   */
  public synchronized void setNoteValue(int index, Object value) {
    if (index >= notes.size())
      return;
    ((Note) notes.get(index)).setValue(value);
  }

  /**
   * sets origin note.
   * 
   * @param newOrigin java.lang.String
   */
  public void setOrigin(java.lang.String newOrigin) {
    setNote(NoteDescriptor.ORIGIN, newOrigin);
  }

  /**
   * sets owner note
   * 
   * @param newOwner java.lang.String
   */
  public void setOwner(java.lang.String newOwner) {
    setNote(NoteDescriptor.OWNER, newOwner);
  }

  /**
   * set spectrum description here.
   * @param newTitle String
   */
  public void setTitle(String newTitle) {
    setNote(NoteDescriptor.TITLE, newTitle);
  }
}