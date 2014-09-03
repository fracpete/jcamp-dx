package org.jcamp.spectrum.notes;

/**
 * Insert the type's description here.
 * 
 * @author Thomas Weber
 */
public class BadContentException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = 1625864303430463264L;

  /**
   * BadContentException constructor comment.
   */
  public BadContentException() {
    super();
  }
  
  /**
   * BadContentException constructor comment.
   * @param s java.lang.String
   */
  public BadContentException(String s) {
    super(s);
  }
}
