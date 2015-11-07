/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.util.Enumeration;
/**
 * tokenizer that correctly handles linefeeds.
 * @author Thomas Weber
 */
public class LineTokenizer implements Enumeration {
    private final static int CONV_NONE = 0; // no linefeed char
    private final static int CONV_LF = 1; // "\n" convention
    private final static int CONV_CR = 2; // "\r" convention
    private final static int CONV_CRLF = 3; // "\r\n" convention
    private final String text;
    private final int length;
    private final int convention;
    private int pos = 0;
    /**
     * LineTokenizer ctor.
     */
    public LineTokenizer(String text) {
        super();
        this.text = text;
        this.length = text.length();
        this.convention = guessConvention();
    }
    /**
     * return actual position or -1 if parsed till end.
     * 
     * @return int
     */
    public int getPosition() {
        return pos < length ? pos : -1;
    }
    /**
     * guess the texts linefeed convention
     * 
     * @return int
     */
    private int guessConvention() {
        int lf = text.indexOf("\r\n");
        if (lf < 0) {
            lf = text.indexOf("\n");
            if (lf < 0) {
                lf = text.indexOf("\r"); // non-standard Mac
                if (lf < 0)
                    return CONV_NONE;
                else
                    return CONV_CR;
            } else
                return CONV_LF;

        } else
            return CONV_CRLF;
    }
    /**
     * returns true if more lines are available.
     * 
     * @return boolean
     */
    public boolean hasMoreElements() {
        return hasMoreLines();
    }
    /**
     * returns true if more lines are available.
     * 
     * @return boolean
     */
    public boolean hasMoreLines() {
        if (pos >= length)
            return false;
        return true;
    }
    /**
     * returns true if more lines are available.
     * 
     * @return boolean
     */
    public boolean hasMoreTokens() {
        return hasMoreLines();
    }
    /**
     * returns the next line.
     * 
     * @return String
     */
    public Object nextElement() {
        return nextLine();
    }
    /**
     * returns the next line.
     * 
     * @return String
     */
    public String nextLine() {
        int p;
        String line = "";
        if (pos >= length)
            throw new java.util.NoSuchElementException();
        switch (convention) {
            case CONV_CR :
                p = text.indexOf("\r", pos);
                if (p < 0) {
                    line = text.substring(pos);
                    pos = length;
                } else {
                    line = text.substring(pos, p);
                    pos = p + 1;
                }
                break;
            case CONV_LF :
                p = text.indexOf("\n", pos);
                if (p < 0) {
                    line = text.substring(pos);
                    pos = length;
                } else {
                    line = text.substring(pos, p);
                    pos = p + 1;
                }
                break;
            case CONV_CRLF :
                p = text.indexOf("\r\n", pos);
                if (p < 0) {
                    line = text.substring(pos);
                    pos = length;
                } else {
                    line = text.substring(pos, p);
                    pos = p + 2;
                }
                break;
            case CONV_NONE :
                pos = length;
                line = text;
                break;
        }
        return line;
    }
    /**
     * returns the next line.
     * 
     * @return String
     */
    public String nextToken() {
        return nextLine();
    }
}
