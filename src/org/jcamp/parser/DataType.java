package org.jcamp.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * enumeration of all standard AFFN data table types.
 * @author Thomas Weber
 */
public final class DataType implements java.io.Serializable {
    private final String[] symbols;
    private final String key;
    private final int ordinal;
    public final static DataType XY = new DataType(0, new String[] { "X", "Y" });
    public final static DataType XYW = new DataType(1, new String[] { "X", "Y", "W" });
    public final static DataType XYA = new DataType(2, new String[] { "X", "Y", "A" });
    public final static DataType XYWA = new DataType(3, new String[] { "X", "Y", "W", "A" });
    public final static DataType XYM = new DataType(4, new String[] { "X", "Y", "M" });
    public final static DataType XYMA = new DataType(5, new String[] { "X", "Y", "M", "A" });
    public final static DataType XYMWA = new DataType(6, new String[] { "X", "Y", "M", "W", "A" });
    private final static DataType[] TYPES = new DataType[] { XY, XYW, XYA, XYWA, XYM, XYMA, XYMWA };
    private final static List TYPES_LIST = Collections.unmodifiableList(Arrays.asList(TYPES));
    private DataType(int ordinal, String[] symbols) {
        this.ordinal = ordinal;
        this.symbols = symbols;
        StringBuffer tmp = new StringBuffer();
        for (int i = 0; i < symbols.length; i++)
            tmp.append(symbols[i]);
        key = tmp.toString();
    }
    public final boolean equals(Object obj) {
        if (obj instanceof DataType && ((DataType) obj) == this)
            return true;
        return false;
    }
    /**
     * gets variable symbols.
     * 
     * @return java.lang.String[]
     */
    public String[] getSymbols() {
        return symbols;
    }
    public final int hashCode() {
        return ordinal;
    }
    private Object readResolve() throws java.io.ObjectStreamException {
        return TYPES[ordinal];
    }
    public String toString() {
        return key;
    }
    public Collection types() {
        return TYPES_LIST;
    }
}
