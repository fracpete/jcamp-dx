/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.spectrum.assignments.AtomReference;

/**
 * peak assignment.
 * 
 * @author Thomas Weber
 */
public class Assignment
  implements ISpectrumLabel, Cloneable {
  
  private IAssignmentTarget[] targets = null;
  
  private Pattern pattern = null;

  /**
   * Assignment constructor comment.
   */
  public Assignment(Pattern pattern) {
    super();
    this.pattern = (Pattern) pattern.clone();
  }

  /**
   * Assignment constructor comment.
   */
  public Assignment(Pattern pattern, int[] atoms) {
    super();
    this.pattern = (Pattern) pattern.clone();
    targets = new AtomReference[atoms.length];
    for (int i = 0; i < atoms.length; i++) {
      this.targets[i] = new AtomReference(null, atoms[i]);
    }
  }

  /**
   * Assignment constructor comment.
   */
  public Assignment(Pattern pattern, IAssignmentTarget[] assigned) {
    super();
    this.pattern = (Pattern) pattern.clone();
    this.targets = assigned;
  }

  /**
   * cloning.
   * @return java.lang.Object
   */
  @Override
  public Object clone() {

    Assignment assign = null;
    try {
      assign = (Assignment) super.clone();
    } catch (CloneNotSupportedException e) {
    }
    assign.setPattern(this.pattern);
    assign.setTargets(this.targets);
    return assign;
  }

  /**
   * compareTo method comment.
   */
  public int compareTo(java.lang.Object obj) {
    double p0 = getPosition()[0];
    double p1 = ((ISpectrumLabel) obj).getPosition()[0];
    if (p0 < p1)
      return -1;
    if (p0 > p1)
      return 1;
    return 0;
  }

  /**
   * formats peak label.
   * @return java.lang.String
   */
  private String formatLabel() {
    if (targets == null || targets.length == 0)
      return "<n/a>";

    StringBuilder astr = new StringBuilder();
    astr.append("<");
    for (int i = 0; i < targets.length; i++) {
      astr.append(targets[i].getLabel());
      if (i < targets.length - 1)
	astr.append(",");
    }
    astr.append(">");
    return astr.toString();
  }

  /**
   * getLabel method comment.
   */
  public String getLabel() {
    return formatLabel();
  }

  /**
   * gets the pattern of the assignement
   * @return Pattern
   */
  public Pattern getPattern() {
    return pattern;
  }

  /**
   * gets the position (y is always 1.0)
   * @return double[]
   */
  public double[] getPosition() {
    return pattern.getPosition();
  }

  /**
   * gets corresponding spectrum.
   * @return Spectrum1D
   */
  public Spectrum1D getSpectrum() {
    return pattern.getSpectrum();
  }

  /**
   * gets assigned targets.
   * @return java.util.Vector
   */
  public IAssignmentTarget[] getTargets() {
    return targets;
  }

  /**
   * sets new pattern.
   * @param newPattern Pattern
   */
  public void setPattern(Pattern newPattern) {
    pattern = (Pattern) newPattern.clone();
  }

  /**
   * sets assignment to new position.
   * @param newPosition double[]
   */
  public void setPosition(double[] newPosition) {
    pattern.setPosition(newPosition);
  }

  /**
   * sets spectrum the assignment belongs to.
   * @param spectrum Spectrum1D
   */
  public void setSpectrum(Spectrum1D spectrum) {
    pattern.setSpectrum(spectrum);
  }

  /**
   * sets assigned targets.
   * @param newAssigned IAssignmentTarget[]
   */
  public void setTargets(IAssignmentTarget[] newAssigned) {
    targets = newAssigned;
  }

  /**
   * translate assignment position.
   */
  public void translate(double[] amount) {
    pattern.translate(amount);
  }
}
