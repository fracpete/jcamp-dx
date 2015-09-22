/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

/**
 * mapping for JCAMP marshalling.
 * @author Thomas Weber
 */
public interface IJCAMPNoteMarshaller {
    /**
     * gets JCAMPLabel (normalized, without ## and =).
     * @return java.lang.String
     */
    String getJCAMPLabel();
    /**
     * set JCAMP LDR label (without ## and =).
     * 
     * @param key java.lang.String
     */
    void setJCAMPLabel(String key);
    /**
     * set hashkey.
     * 
     * @param key java.lang.String
     */
    void setKey(String key);
    /**
     * formats note to JCAMP.
     * 
     * @return java.lang.String
     * @param value java.lang.Object
     */
    String toJCAMP(Object value);
}
