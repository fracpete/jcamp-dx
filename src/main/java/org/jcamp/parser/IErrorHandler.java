package org.jcamp.parser;

/**
 * error handling for JCAMP parsing.
 * @author Thomas Weber
 */
public interface IErrorHandler {
    /**
     * handle error condition.
     * @param msg java.lang.String
     */
    void error(String msg) throws JCAMPException;
    /**
     * handle fatal error
     * 
     * @param msg java.lang.String
     */
    void fatal(String msg) throws JCAMPException;
    /**
     * handle warning message.
     * 
     * @param msg java.lang.String
     */
    void warn(String msg) throws JCAMPException;
}
