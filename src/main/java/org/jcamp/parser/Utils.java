package org.jcamp.parser;

import java.util.ArrayList;

/**
 * static helper functions.
 * 
 * @author Thomas Weber
 */
public class Utils {
  
  /**
   * remove JCAMP line comments from String <code>text</code>
   * @return java.lang.String
   * @param text java.lang.String
   */
  public static String extractComments(String text) {
    StringBuilder tmp = new StringBuilder();
    int i = 0;
    int n = text.length();
    char c0, c1;
    outer : for (i = 0; i < n; i++) {
      c0 = text.charAt(i);
      if (c0 == '$') {
	i++;
	if (i < n) {
	  c1 = text.charAt(i);
	  if (c1 != '$') {
	    continue;
	  }
	} else {
	  continue;
	}
	i++;
	// eat chars till linefeed
	while (i < n) {
	  c0 = text.charAt(i);
	  tmp.append(c0);
	  if (c0 != '\n' && c0 != '\r') {
	    i++;
	  } else {
	    continue outer;
	  }
	}
      }
    }
    return tmp.toString();
  }
  /**
   * normalize label as in JCAMP standard:
   * remove ' ', '_', '-','/' and make upper case
   * @param rawLabelName
   * @return String normalized string
   */
  public static String normalizeLabel(String rawLabelName) {
    StringBuilder tmp = new StringBuilder();
    for (int i = 0; i < rawLabelName.length(); i++) {
      switch (rawLabelName.charAt(i)) {
	case ' ' :
	case '_' :
	case '-' :
	case '/' :
	  break;
	default :
	  tmp.append(Character.toUpperCase(rawLabelName.charAt(i)));
      }
    }
    return tmp.toString();
  }
  /**
   * change all whitespace chars to ' ', collapsing more than one ws into one.
   * 
   * @return java.lang.String
   * @param orig java.lang.String
   */
  public static String normalizeWhitespace(String orig) {
    StringBuilder normal = new StringBuilder();
    boolean inWS = false;
    for (int i = 0; i < orig.length(); i++) {
      char c = orig.charAt(i);
      if (Character.isWhitespace(c) && !inWS) {
	normal.append(' ');
	inWS = true;
      } else {
	normal.append(c);
	inWS = false;
      }
    }
    return normal.toString();
  }
  /**
   * remove JCAMP line comments from String <code>text</code>
   * @return java.lang.String
   * @param text java.lang.String
   */
  public static String removeComments(String text) {
    StringBuilder tmp = new StringBuilder();
    int i = 0;
    int n = text.length();
    char c0, c1;
    outer : for (i = 0; i < n; i++) {
      c0 = text.charAt(i);
      if (c0 == '$') {
	i++;
	if (i < n) {
	  c1 = text.charAt(i);
	  if (c1 != '$') {
	    tmp.append(c0).append(c1);
	    continue;
	  }
	} else {
	  tmp.append(c0);
	  continue;
	}
	i++;
	// eat chars till linefeed
	while (i < n) {
	  c0 = text.charAt(i);
	  if (c0 != '\n' && c0 != '\r') {
	    i++;
	  } else {
	    tmp.append(c0);
	    continue outer;
	  }
	}
      } else
	tmp.append(c0);
    }
    return tmp.toString();
  }
  /**
   * split String with comma separated values into a trimmed String array.
   * @param line
   * @return String[]
   */
  public static String[] splitStringCSV(String line) {
    line = removeComments(line);
    ArrayList tmp = new ArrayList();
    int pos = 0;
    int len = line.length();
    StringBuilder entry = new StringBuilder();
    while (pos < len) {
      char c = line.charAt(pos);
      if (c == ',') {
	tmp.add(entry.toString());
	entry.setLength(0);
      } else
	entry.append(c);
      pos++;
    }
    tmp.add(entry.toString());
    String[] arr = new String[tmp.size()];
    for (int i = 0; i < arr.length; i++)
      arr[i] = ((String) tmp.get(i)).trim();
    return arr;
  }
}
