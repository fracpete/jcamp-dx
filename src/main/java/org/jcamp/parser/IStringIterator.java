/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

/**
 * iterator over a string returning substring positions
 * @author Thomas Weber
 * @see JCAMPParser
 */
interface IStringIterator {
    /**
     * get current offset within string.
     */
    int getOffset();
    /**
     * check if another substring is available.
     * @return boolean
     */
    boolean hasNext();
    /**
     * gets next substring.
     * @return java.lang.String
     */
    String next();
}
