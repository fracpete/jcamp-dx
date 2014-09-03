package org.jcamp.math;

/**
 * special exception in data handling.
 * 
 * @author Thomas Weber
 */
public class DataException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = -1506747424253191086L;
  
  /**
   * DataException constructor comment.
   */
  public DataException() {
    super();
  }
  
  /**
   * DataException constructor comment.
   * @param s java.lang.String
   */
  public DataException(String s) {
    super(s);
  }
}
