/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.jcamp.spectrum.notes.NoteDescriptor;

/**
 * factory for NoteDescriptors from JCAMP LDRs
 * 
 * @author Thomas Weber
 */
public class NoteDescriptorFactory {
  
  private static NoteDescriptorFactory theInstance = null;
  
  private static Hashtable jcampKeys = new Hashtable();

  /**
   * NoteDescriptorFactory constructor comment.
   */
  private NoteDescriptorFactory() {
    super();
    initFactory();
  }

  /**
   * accessor method to hashtable.
   * 
   * @return com.creon.chem.spectrum.NoteDescriptor
   * @param jcampKey String
   */
  public NoteDescriptor findByJCAMPKey(String jcampKey) {
    NoteDescriptor descr = (NoteDescriptor) jcampKeys.get(jcampKey);
    if (descr == null) { // no predefined descriptor found
      String key = jcampKey.toLowerCase();
      if (key.charAt(0) == '$')
	key = key.substring(1);
      descr = new NoteDescriptor(key, jcampKey);
      descr.setUnique(false);
    }
    return descr;
  }

  /**
   * factory accessor method.
   * 
   * @return com.creon.chem.jcamp.NoteDescriptorFactory
   */
  public static NoteDescriptorFactory getInstance() {
    if (theInstance == null)
      theInstance = new NoteDescriptorFactory();
    return theInstance;
  }

  /**
   * initialization.
   * 
   */

  private void initFactory() {
    // predefine JCAMP labels that are not notes or are treated separately
    jcampKeys.put("XYDATA", NoteDescriptor.IGNORE);
    jcampKeys.put("PEAKTABLE", NoteDescriptor.IGNORE);
    jcampKeys.put("PEAKASSIGNMENT", NoteDescriptor.IGNORE);
    jcampKeys.put("DATATABLE", NoteDescriptor.IGNORE);
    jcampKeys.put("XYPOINTS", NoteDescriptor.IGNORE);
    jcampKeys.put("PAGE", NoteDescriptor.IGNORE);
    jcampKeys.put("NTUPLES", NoteDescriptor.IGNORE);
    jcampKeys.put("DATACLASS", NoteDescriptor.IGNORE);
    jcampKeys.put("DATATYPE", NoteDescriptor.IGNORE);
    jcampKeys.put("END", NoteDescriptor.IGNORE);
    jcampKeys.put("ENDNTUPLES", NoteDescriptor.IGNORE);
    jcampKeys.put("VARNAME", NoteDescriptor.IGNORE);
    jcampKeys.put("VARTYPE", NoteDescriptor.IGNORE);
    jcampKeys.put("VARDIM", NoteDescriptor.IGNORE);
    jcampKeys.put("SYMBOL", NoteDescriptor.IGNORE);
    jcampKeys.put("LAST", NoteDescriptor.IGNORE);
    jcampKeys.put("FIRST", NoteDescriptor.IGNORE);
    jcampKeys.put("LASTX", NoteDescriptor.IGNORE);
    jcampKeys.put("FIRSTX", NoteDescriptor.IGNORE);
    jcampKeys.put("UNITS", NoteDescriptor.IGNORE);
    jcampKeys.put("XUNITS", NoteDescriptor.IGNORE);
    jcampKeys.put("YUNITS", NoteDescriptor.IGNORE);
    jcampKeys.put("LASTY", NoteDescriptor.IGNORE);
    jcampKeys.put("FIRSTY", NoteDescriptor.IGNORE);
    jcampKeys.put("XFACTOR", NoteDescriptor.IGNORE);
    jcampKeys.put("YFACTOR", NoteDescriptor.IGNORE);
    jcampKeys.put("NPOINTS", NoteDescriptor.IGNORE);
    jcampKeys.put("JCAMPDX", NoteDescriptor.IGNORE);
    jcampKeys.put("JCAMPCS", NoteDescriptor.IGNORE);
    jcampKeys.put("TITLE", NoteDescriptor.IGNORE); // special handling

    // read other labels from property file
    Properties notesProps = new Properties();
    java.io.InputStream is = null;
    try {
      is = NoteDescriptor.class.getResourceAsStream("notes.properties");
      if (is == null)
	return;
      notesProps.load(is);

      Enumeration notesNames = notesProps.propertyNames();
      while (notesNames.hasMoreElements()) {
	String key = (String) notesNames.nextElement();
	if (key.indexOf('.') < 0) {
	  NoteDescriptor descr = NoteDescriptor.findByKey(key);
	  if (descr == null)
	    continue;
	  String jcamp = (String) notesProps.get(key + ".jcamp");
	  if (jcamp != null) {
	    jcampKeys.put(Utils.normalizeLabel(jcamp), descr);
	  }

	}
      }
    } catch (java.io.IOException e) {
    } finally {
      try {
	if (is != null)
	  is.close();
      } catch (Exception e) {
      }
    }

  }
}
