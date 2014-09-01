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
