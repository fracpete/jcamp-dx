/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

/**
 * mapping of spectrum note.
 * 
 * @author Thomas Weber
 */
public class DefaultNoteMarshaller
  implements IJCAMPNoteMarshaller {
  
  protected String JCAMPLabel;
  protected String key;
  /**
   * default ctor used by reflection.
   */
  public DefaultNoteMarshaller() {
  }
  /**
   * NoteMapping constructor comment.
   */
  public DefaultNoteMarshaller(String key) {
    this(key, key.toUpperCase());
  }
  /**
   * NoteMapping constructor comment.
   */
  public DefaultNoteMarshaller(String key, String JCAMPLabel) {
    super();
    this.key = key;
    this.JCAMPLabel = JCAMPLabel;
  }
  /**
   * gets JCAMP LDR label.
   * 
   * @return String
   */
  public String getJCAMPLabel() {
    return Utils.normalizeLabel(JCAMPLabel);
  }
  /**
   * gets note key.
   * 
   * @return String
   */
  protected String getKey() {
    return key;
  }
  /**
   * sets the JCAMP label.
   * 
   * @param newJCAMPLabel String
   */
  public void setJCAMPLabel(String newJCAMPLabel) {
    JCAMPLabel = newJCAMPLabel;
  }
  /**
   * sets the note key.
   * 
   * @param newKey String
   */
  public void setKey(String newKey) {
    key = newKey;
  }
  /**
   * transforms note into JCAMP form
   * makes linefeed after 75 characters if necessary and uses '=' as
   * a continuation character as required.
   * @see com.creon.chem.jcamp.IJCAMPMapping
   */
  public String toJCAMP(Object value) {
    StringBuilder label = new StringBuilder("##").append(JCAMPLabel).append('=');
    label.append(value.toString());
    int len = label.length();
    int line = len / 75;
    for (int i = 0; i < line; i++)
      label.insert(75 + (i * 78), "=\r\n");
    label.append("\r\n");
    return label.toString();
  }
}
