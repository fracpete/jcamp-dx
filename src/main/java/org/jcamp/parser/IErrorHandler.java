/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

/**
 * Error handling for JCAMP parsing.
 * 
 * @deprecated
 * 
 * @author Thomas Weber
 */
public interface IErrorHandler {
    /**
     * handle error condition.
     * @param msg String
     */
    void error(String msg) throws JCAMPException;
    /**
     * handle fatal error
     * 
     * @param msg String
     */
    void fatal(String msg) throws JCAMPException;
    /**
     * handle warning message.
     * 
     * @param msg String
     */
    void warn(String msg) throws JCAMPException;
}
