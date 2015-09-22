/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum.notes;

import java.io.Serializable;

/**
 * note (property) for spectrum.
 * 
 * @author Thomas Weber
 */
public class Note
  implements Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = 8692106189365309099L;

  private java.lang.Object fieldValue = null;
  
  protected transient java.beans.PropertyChangeSupport propertyChange;
  
  private NoteDescriptor fieldDescriptor = NoteDescriptor.IGNORE;

  /**
   * Note constructor comment.
   */
  public Note() {
    super();
  }

  /**
   * Note constructor comment.
   */
  public Note(NoteDescriptor desc, Object value) {
    super();
    setDescriptor(desc);
    setValue(value);
  }

  /**
   * The addPropertyChangeListener method was generated to support the propertyChange field.
   */
  public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
    getPropertyChange().addPropertyChangeListener(listener);
  }

  /**
   * The addPropertyChangeListener method was generated to support the propertyChange field.
   */
  public synchronized void addPropertyChangeListener(
      java.lang.String propertyName,
      java.beans.PropertyChangeListener listener) {
    getPropertyChange().addPropertyChangeListener(propertyName, listener);
  }

  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    Note note = null;
    try {
      note = (Note) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    note.fieldDescriptor = (NoteDescriptor) this.fieldDescriptor.clone();
    return note;
  }

  /**
   * The firePropertyChange method was generated to support the propertyChange field.
   */
  public void firePropertyChange(java.beans.PropertyChangeEvent evt) {
    getPropertyChange().firePropertyChange(evt);
  }

  /**
   * The firePropertyChange method was generated to support the propertyChange field.
   */
  public void firePropertyChange(java.lang.String propertyName, int oldValue, int newValue) {
    getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * The firePropertyChange method was generated to support the propertyChange field.
   */
  public void firePropertyChange(
      java.lang.String propertyName,
      java.lang.Object oldValue,
      java.lang.Object newValue) {
    getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * The firePropertyChange method was generated to support the propertyChange field.
   */
  public void firePropertyChange(java.lang.String propertyName, boolean oldValue, boolean newValue) {
    getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Gets the descriptor property (com.creon.chem.spectrum.NoteDescriptor) value.
   * @return The descriptor property value.
   * @see #setDescriptor
   */
  public NoteDescriptor getDescriptor() {
    return fieldDescriptor;
  }

  /**
   * Accessor for the propertyChange field.
   */
  protected java.beans.PropertyChangeSupport getPropertyChange() {
    if (propertyChange == null) {
      propertyChange = new java.beans.PropertyChangeSupport(this);
    };
    return propertyChange;
  }

  /**
   * Gets the value property (java.lang.Object) value.
   * @return The value property value.
   * @see #setValue
   */
  public java.lang.Object getValue() {
    return fieldValue;
  }

  /**
   * The hasListeners method was generated to support the propertyChange field.
   */
  public synchronized boolean hasListeners(java.lang.String propertyName) {
    return getPropertyChange().hasListeners(propertyName);
  }

  /**
   * The removePropertyChangeListener method was generated to support the propertyChange field.
   */
  public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
    getPropertyChange().removePropertyChangeListener(listener);
  }

  /**
   * The removePropertyChangeListener method was generated to support the propertyChange field.
   */
  public synchronized void removePropertyChangeListener(
      java.lang.String propertyName,
      java.beans.PropertyChangeListener listener) {
    getPropertyChange().removePropertyChangeListener(propertyName, listener);
  }

  /**
   * Sets the descriptor property (com.creon.chem.spectrum.NoteDescriptor) value.
   * @param descriptor The new value for the property.
   * @see #getDescriptor
   */
  public void setDescriptor(NoteDescriptor descriptor) {
    fieldDescriptor = descriptor;
  }

  /**
   * Sets the value property (java.lang.Object) value.
   * @param value The new value for the property.
   * @see #getValue
   */
  public void setValue(java.lang.Object value) {
    Object oldValue = fieldValue;
    fieldValue = value;
    if (oldValue == value)
      return;
    if (oldValue != null && oldValue.equals(value))
      return;
    firePropertyChange("value", oldValue, value);
  }

  /**
   * string display.
   * 
   * @return java.lang.String
   */
  @Override
  public String toString() {
    StringBuilder tmp = new StringBuilder("[").append(getDescriptor().toString()).append("] ");
    if (fieldValue != null)
      tmp.append("\"").append(fieldValue.toString()).append("\"");
    else
      tmp.append("null");
    return tmp.toString();
  }
}
