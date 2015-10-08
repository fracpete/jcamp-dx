/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

/**
 * {@link java.util.Iterator Iterator} over a string returning substring
 * positions.
 * 
 * @author Thomas Weber
 * 
 * @see JCAMPParser
 */
interface IStringIterator {
	/**
	 * Gets the current offset within string.
	 */
	int getOffset();

	/**
	 * Checks if another substring is available.
	 */
	boolean hasNext();

	/**
	 * Gets next substring.
	 * 
	 */
	String next();
}
