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
   * @return java.lang.String
   */
  public java.lang.String getLabel() {
    return Integer.toString(atomNo);
  }
  /**
   * gets name of target structure.
   * 
   * @return java.lang.String
   */
  public java.lang.String getStructure() {
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
   * @param newStructure java.lang.String
   */
  public void setStructure(java.lang.String newStructure) {
    if (newStructure == null)
      structure = "unknown";
    else
      structure = newStructure;
  }
  /**
   * string display
   * 
   * @return java.lang.String
   */
  @Override
  public String toString() {
    return new StringBuilder().append(structure).append(" <").append(atomNo).append(">").toString();
  }
}
