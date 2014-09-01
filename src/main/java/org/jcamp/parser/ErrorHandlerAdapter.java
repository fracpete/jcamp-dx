package org.jcamp.parser;

/**
 * default implementation of IErrorHandler
 * @author Thomas Weber
 */
public class ErrorHandlerAdapter implements IErrorHandler {
    /**
     * ErrorHandlerAdapter constructor comment.
     */
    public ErrorHandlerAdapter() {
        super();
    }
    /**
     * @see com.creon.chem.jcamp.IErrorHandler
     */
    public void error(String msg) throws JCAMPException {
        throw new JCAMPException("ERROR! " + msg);
    }
    /**
     * @see com.creon.chem.jcamp.IErrorHandler
     */
    public void fatal(String msg) throws JCAMPException {
        throw new JCAMPException("FATAL ERROR! " + msg);
    }
    /**
     * @see com.creon.chem.jcamp.IErrorHandler
     */
    public void warn(String msg) throws JCAMPException {
    }
}
