package org.jcamp.parser;

/**
 * exceptions thrown at JCAMP-DX parsing.
 * 
 * @author Thomas Weber
 */
public class JCAMPException
  extends Exception {
  
  /** for serialization. */
  private static final long serialVersionUID = -4773819366174671655L;
  /**
   * JCAMPException constructor comment.
   */
  public JCAMPException() {
    super();
  }
  /**
   * JCAMPException constructor comment.
   * @param s java.lang.String
   */
  public JCAMPException(String s) {
    super(s);
  }
}
