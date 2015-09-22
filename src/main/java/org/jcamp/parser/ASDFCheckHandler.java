/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

interface ASDFCheckHandler {
	public final static int ABORT = 0;
	public final static int REPLACE = 1;
	public final static int OK = 2;
	public final static int IGNORE = 2;
	/**
	* checks current parsing state for x-sequence and y-value
	* if x-sequence is wrong, set current index within <code>state</code>
	* to correct value via <code>state.setCurrentIndex(int newIndex)</code>
	* if y-value is wrong, set current y-value within <code>state</code>
	* to correct value via <code>state.setCurrentY(int newY)</code>
	* @param ASDFParsingState state the current parse state
	* @return int returns the error handling policy:
	*    OK|IGNORE: state is correct
	*    REPLACE: state is modified, do correction
	*    ABORT: state is unrecoverable, throws JCAMPException
	*/
	int check(ASDFParseState state);
}
