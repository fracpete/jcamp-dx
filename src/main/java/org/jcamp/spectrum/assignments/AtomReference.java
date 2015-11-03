/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum.assignments;

import java.io.Serializable;

import org.jcamp.spectrum.IAssignmentTarget;

/**
 * atom assignment target.
 * 
 * @author Thomas Weber
 */
public class AtomReference
  implements IAssignmentTarget, Cloneable, Serializable {
  
  /** for serialization. */
  private static final long serialVersionUID = 5359291202230446911L;

  private String structure = "unknown";
  
  private int atomNo;
  
  /**
   * AtomReference constructor comment.
   */
  public AtomReference(String structure, int atomno) {
    super();
    if (structure != null)
      this.structure = structure;
    else
      this.structure = "unknown";
    this.atomNo = atomno;
  }
  /**
   * cloning.
   */
  @Override
  public Object clone() {
    AtomReference ref;
    try {
      ref = (AtomReference) super.clone();
    } catch (CloneNotSupportedException e) {
      // never reached
      throw new Error("something went wrong in clone");
    }
    ref.setAtomNumber(this.atomNo);
    ref.setStructure(this.structure);
    return ref;
  }
  /**
   * comparision.
   * 
   * @return boolean
   * @param object java.lang.Object
   */
  @Override
  public boolean equals(Object object) {
    if (object instanceof AtomReference) {
      AtomReference ref = (AtomReference) object;
      if (ref == this)
	return true;
      return ref.atomNo == atomNo && ref.structure.equals(structure);
    }
    return false;
  }
  /**
   * gets atom number
   * 
   * @return int
   */
  public int getAtomNumber() {
    return atomNo;
  }
  /**
   * gets short label for display above peak.
   * 
   * @return String
   */
  public String getLabel() {
    return Integer.toString(atomNo);
  }
  /**
   * gets name of target structure.
   * 
   * @return String
   */
  public String getStructure() {
    return structure;
  }
  /**
   * hash value.
   * 
   * @return int
   */
  @Override
  public int hashCode() {
    return atomNo ^ structure.hashCode();
  }
  /**
   * sets the atom number.
   * 
   * @param newAtomNo int
   */
  public void setAtomNumber(int newAtomNo) {
    atomNo = newAtomNo;
  }
  /**
   * Insert the method's description here.
   * 
   * @param newStructure String
   */
  public void setStructure(String newStructure) {
    if (newStructure == null)
      structure = "unknown";
    else
      structure = newStructure;
  }
  /**
   * string display
   * 
   * @return String
   */
  @Override
  public String toString() {
    return new StringBuilder().append(structure).append(" <").append(atomNo).append(">").toString();
  }
}
