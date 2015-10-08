/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

/**
 * {@link java.util.Iterator Iterator} over JCAMP labels.
 * 
 * @author Thomas Weber
 * 
 */
public class LabelIterator implements IStringIterator {
	private static String labelRegExp = "^##[^=\\n\\r]*=";
	private static RECompiler compiler = new RECompiler();
	private REProgram labelProgram = null;
	{
		try {
			labelProgram = compiler.compile(labelRegExp);
		} catch (RESyntaxException e) {
			e.printStackTrace();
		}
	}
	// shk3: we need a multiline match, else only the first label is recognized
	private RE labelRE = new RE(labelProgram, RE.MATCH_CASEINDEPENDENT
			| RE.MATCH_MULTILINE);
	private int offset = 0;
	private String jcamp;

	public LabelIterator(String jcamp) {
		super();
		this.jcamp = jcamp;
	}

	@Override
	public int getOffset() {
		if (jcamp == null)
			return -1;
		if (labelRE.match(jcamp, offset))
			return labelRE.getParenStart(0);
		else
			return -1;

	}

	@Override
	public boolean hasNext() {
		if (jcamp == null)
			return false;
		return labelRE.match(jcamp, offset);
	}

	@Override
	public String next() {
		if (jcamp == null)
			return null;
		int startMatch = offset;
		int endMatch = 0;
		String match;
		if (labelRE.match(jcamp, startMatch)) {
			match = labelRE.getParen(0);
			startMatch = labelRE.getParenStart(0);
			endMatch = labelRE.getParenEnd(0);
			offset = endMatch + 1;
			return match;
		} else
			return null;
	}
}
