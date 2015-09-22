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
 * Iterator returning LDRs.
 * @author Thomas Weber
 */
public class LDRIterator implements IStringIterator {
    private static String labelRegExp = "##[^=\\n\\r]*=";
    private static RECompiler compiler = new RECompiler();
    private REProgram labelProgram = null;
    {
        try {
            labelProgram = compiler.compile(labelRegExp);
        } catch (RESyntaxException e) {
            e.printStackTrace();
        }
    }
    private RE labelRE = new RE(labelProgram, RE.MATCH_CASEINDEPENDENT);
    private int start = 0;
    private int end = -1;
    private String jcamp;
    /**
     * LDRIterator constructor comment.
     */
    public LDRIterator() {
        super();
    }
    public LDRIterator(String jcamp) {
        this.jcamp = jcamp;
        firstLabel();
    }
    private void firstLabel() {
        if (jcamp == null)
            return;
        if (start >= jcamp.length())
            return;
        if (labelRE.match(jcamp)) {
            start = labelRE.getParenStart(0);
            end = labelRE.getParenEnd(0);
            return;
        } else {
            start = jcamp.length();
            end = start;
            return;
        }
    }
    /**
     * @see com.creon.chem.jcamp.IStringIterator
     */
    public int getOffset() {
        if (jcamp != null && start < jcamp.length()) {
            return start;
        }
        return -1;
    }
    /**
     * @see com.creon.chem.jcamp.IStringIterator
     */
    public boolean hasNext() {
        if (jcamp == null)
            return false;
        if (start >= jcamp.length())
            return false;
        return true;
    }
    /**
     * @see com.creon.chem.jcamp.IStringIterator
     */
    public String next() {
        String ldr = null;
        //System.out.println(" start = " + start + " end = " + end);
        if (jcamp != null && start < jcamp.length()) {
            if (labelRE.match(jcamp, end + 1)) {
                ldr = jcamp.substring(start, labelRE.getParenStart(0) - 1);
                start = labelRE.getParenStart(0);
                end = labelRE.getParenEnd(0);
            } else {
                ldr = jcamp.substring(start);
                start = end = jcamp.length();
            }
        }
        return ldr;

    }
}
