/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Utility class for ASDF decoding of spectrum data
 * @author Thomas Weber
 */
public class ASDFDecoder {
    private static Log log = LogFactory.getLog(ASDFDecoder.class);
    private final static byte[] CRLF = {(byte) '\r', (byte) '\n' };
    private boolean isUnicodeAware = false;
    private boolean isValidating = true;

    /**
     * ASDFDecoder constructor.
     */
    public ASDFDecoder() {
    }

    /**
     * decode data LDR
     * 
     * @return int[]
     * @param ldr labeled data record
     * @param firstX double
     * @param lastX double
     * @param nPoints int
     */
    public double[] decode(JCAMPDataRecord ldr, double firstX, double lastX, double xFactor, int nPoints)
        throws JCAMPException {
        try {
            DataVariableInfo varInfo = new DataVariableInfo(ldr);
            if (varInfo.isIncremental()) {
                ASDFLexer lexer;
                if (isUnicodeAware) {
                    char[] xyData = getASDFBlockAsChars(ldr.getContent());
                    lexer = new ASDFLexer(new CharArrayReader(xyData));
                } else {
                    byte[] xyData = getASDFBlockAsBytes(ldr.getContent());
                    lexer = new ASDFLexer(new ByteArrayInputStream(xyData));
                }
                ASDFParser parser = new ASDFParser(lexer);
                if (isValidating)
                    parser.setCheckHandler(new ReplacingCheckHandler(firstX, lastX, xFactor, nPoints));
                parser.parse(nPoints);

                return parser.getYData();
            } else {
                String msg = "data not in ASDF encoding";
                log.error(msg);
                throw new JCAMPException(msg);
            }

        } catch (Throwable e) {
            throw new JCAMPException(e.getMessage());
        }
    }

    /**
     * decode a ASDF (ASCII Squeezed Difference Form) encoded data block
     * 
     * @return int[] y-data array
     * @param difdup String encoded data
     */
    public double[] decode(String difdup, double firstX, double lastX, double xFactor, int nPoints)
        throws JCAMPException {
        try {
            ASDFLexer lexer = new ASDFLexer(new StringReader(difdup));
            ASDFParser parser = new ASDFParser(lexer);
            if (isValidating)
                parser.setCheckHandler(new ReplacingCheckHandler(firstX, lastX, xFactor, nPoints));
            parser.parse(nPoints);
            return parser.getYData();
        } catch (Exception e) {
            String msg = "ASDF parsing failed";
            log.error(msg);
            throw new JCAMPException(msg);
        }
    }

    /**
     * enables/disables validation.
     * 
     * @param newIsValidating boolean
     */
    public void enableValidation(boolean newIsValidating) {
        isValidating = newIsValidating;
    }

    /**
     * convert ASDF data block of ASCII chars to byte array
     * 
     * @return int
     * @param buffer byte[]
     * @param start int
     */
    private static byte[] getASDFBlockAsBytes(String s) {
        // skip first line
        int length = s.length();
        int pos = 0;
        while (pos < length) {
            char c = s.charAt(pos);
            if (c == '\r') {
                pos++;
                c = s.charAt(pos);
                if (c == '\n') {
                    break;
                } else {
                    pos--;
                    break;
                }
            }
            if (c == '\n')
                break;
            pos++;
        }
        if (pos == length)
            return CRLF;
        pos++;
        // convert char to byte
        byte[] buffer = new byte[length - pos + 2];
        int i = 0;
        while (pos < length) {
            buffer[i] = (byte) s.charAt(pos);
            pos++;
            i++;
        }
        buffer[i] = CRLF[0];
        i++;
        buffer[i] = CRLF[1];
        return buffer;
    }

    /**
     * convert ASDF data block of ASCII chars to byte array
     * 
     * @return int
     * @param buffer byte[]
     * @param start int
     */
    private static char[] getASDFBlockAsChars(String s) {
        // skip first line
        int length = s.length();
        int pos = 0;
        while (pos < length) {
            char c = s.charAt(pos);
            if (c == '\r') {
                pos++;
                c = s.charAt(pos);
                if (c == '\n') {
                    break;
                } else {
                    pos--;
                    break;
                }
            }
            if (c == '\n')
                break;
            pos++;
        }
        if (pos == length)
            return new char[] { '\r', '\n' };
        pos++;
        char[] buffer = new char[length - pos + 2];
        int i = 0;
        while (pos < length) {
            buffer[i] = s.charAt(pos);
            pos++;
            i++;
        }
        buffer[i] = '\r';
        i++;
        buffer[i] = '\n';
        return buffer;
    }

    /**
     * sets decoder to allow handling of Unicode chars.
     * 
     * @return boolean
     */
    public boolean isUnicodeAware() {
        return isUnicodeAware;
    }

    /**
     * indicates if decoder validates.
     * 
     * @return boolean
     */
    public boolean isValidating() {
        return isValidating;
    }

    /**
     * sets Unicode awareness.
     * Note: this is NOT allowed in standard JCAMP
     * 
     * @param newIsUnicodeAware boolean
     */
    public void setUnicodeAwareness(boolean newIsUnicodeAware) {
        isUnicodeAware = newIsUnicodeAware;
    }
}
