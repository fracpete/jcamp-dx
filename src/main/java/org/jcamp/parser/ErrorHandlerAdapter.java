/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
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
