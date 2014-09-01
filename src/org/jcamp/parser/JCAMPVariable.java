package org.jcamp.parser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jcamp.units.Unit;
/**
 * variable in JCAMP blocks.
 * standard: X, Y and NTUPLE eg in NMR FID X,R,I
 * @author Thomas Weber
 */
public class JCAMPVariable implements Serializable {
    final static class Format implements Serializable {
        private final String key;
        private final int ordinal;
        private Format(int ordinal, String key) {
            this.ordinal = ordinal;
            this.key = key;
        }
        public String toString() {
            return key;
        }
        public Collection formats() {
            return FORMATS_LIST;
        }
        private Object readResolve() throws java.io.ObjectStreamException {
            return FORMATS[ordinal];
        }
        public final int hashCode() {
            return ordinal;
        }
        public final boolean equals(Object obj) {
            if (obj instanceof Format && ((Format) obj) == this)
                return true;
            return false;
        }
        public final static Format TEXT = new Format(0, "TEXT");
        public final static Format STRING = new Format(1, "STRING");
        public final static Format AFFN = new Format(2, "AFFN");
        public final static Format ASDF = new Format(3, "ASDF");
        private final static Format[] FORMATS = new Format[] { TEXT, STRING, AFFN, ASDF };
        private final static List FORMATS_LIST = Collections.unmodifiableList(Arrays.asList(FORMATS));
    }
    final static class Type implements Serializable {
        private final String key;
        private final int ordinal;
        private Type(int ordinal, String key) {
            this.ordinal = ordinal;
            this.key = key;
        }
        public String toString() {
            return key;
        }
        public Collection types() {
            return TYPES_LIST;
        }
        private Object readResolve() throws java.io.ObjectStreamException {
            return TYPES[ordinal];
        }
        public final int hashCode() {
            return ordinal;
        }
        public final boolean equals(Object obj) {
            if (obj instanceof Type && ((Type) obj) == this)
                return true;
            return false;
        }
        public final static Type INDEPENDENT = new Type(0, "independent");
        public final static Type DEPENDENT = new Type(1, "dependent");
        public final static Type PAGE = new Type(1, "page");
        private final static Type[] TYPES = new Type[] { INDEPENDENT, DEPENDENT, PAGE };
        private final static List TYPES_LIST = Collections.unmodifiableList(Arrays.asList(TYPES));
    }
    private String symbol;
    private Format format;
    private String label;
    private String name;
    private Unit unit;
    private Type type = Type.INDEPENDENT;
    private Double min;
    private Double max;
    private Double factor;
    private Double first;
    private Double last;
    private Integer dim;
    /**
     * 
     * 
     * @param symbol java.lang.String
     */
    JCAMPVariable(String symbol) {
        setSymbol(symbol);
    }
    /**
     * gets dimension of variable.
     * 
     * @return java.lang.Integer
     */
    public java.lang.Integer getDimension() {
        return dim;
    }
    /**
     * gets the factor or null
     * 
     * @return java.lang.Double
     */
    public java.lang.Double getFactor() {
        return factor;
    }
    /**
     * gets the first value or null.
     * 
     * @return java.lang.Double
     */
    public java.lang.Double getFirst() {
        return first;
    }
    /**
     * get data format (AFFN, ASDF).
     * 
     * @return JCAMPVariable.Format
     */
    public Format getFormat() {
        return format;
    }
    /**
     * gets variable label.
     * 
     * @return java.lang.String
     */
    public java.lang.String getLabel() {
        return label;
    }
    /**
     * gets the last value or null.
     * 
     * @return java.lang.Double
     */
    public java.lang.Double getLast() {
        return last;
    }
    /**
     * gets the maximum or null.
     * 
     * @return java.lang.Double
     */
    public java.lang.Double getMax() {
        return max;
    }
    /**
     * gets the minimum or null.
     * 
     * @return java.lang.Double
     */
    public java.lang.Double getMin() {
        return min;
    }
    /**
     * gets normalized variable name.
     * 
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return name;
    }
    /**
     * gets the variable symbol.
     * 
     * @return java.lang.String
     */
    public java.lang.String getSymbol() {
        return symbol;
    }
    /**
     * gets dependency type
     * 
     * @return Type
     */
    public Type getType() {
        return type;
    }
    /**
     * gets the unit;
     * 
     * @return Unit
     */
    public Unit getUnit() {
        return unit;
    }
    /**
     * set dimension of variable.
     * 
     * @param newDim int
     */
    void setDimension(int newDim) {
        dim = new Integer(newDim);
    }
    /**
     * set dimension of variable.
     * 
     * @param newDim java.lang.Integer
     */
    void setDimension(java.lang.Integer newDim) {
        dim = newDim;
    }
    /**
     * sets the factor.
     * 
     * @param newFactor double
     */
    void setFactor(double newFactor) {
        factor = new Double(newFactor);
    }
    /**
     * sets the factor.
     * 
     * @param newFactor java.lang.Double
     */
    void setFactor(java.lang.Double newFactor) {
        factor = newFactor;
    }
    /**
     * sets the first value.
     * 
     * @param newFirst double
     */
    void setFirst(double newFirst) {
        first = new Double(newFirst);
    }
    /**
     * sets the first value.
     * 
     * @param newFirst java.lang.Double
     */
    void setFirst(java.lang.Double newFirst) {
        first = newFirst;
    }
    /**
     * sets data format.
     * 
     * @param newFormat JCAMPVariable.Format
     */
    void setFormat(Format newFormat) {
        format = newFormat;
    }
    /**
     * sets axis label.
     * 
     * @param newLabel java.lang.String
     */
    void setLabel(java.lang.String newLabel) {
        label = newLabel;
    }
    /**
     * sets the last.
     * 
     * @param newLast java.lang.Double
     */
    void setLast(double newLast) {
        last = new Double(newLast);
    }
    /**
     * sets the last.
     * 
     * @param newLast java.lang.Double
     */
    void setLast(java.lang.Double newLast) {
        last = newLast;
    }
    /**
     * sets the maximum.
     * 
     * @param newMax double
     */
    void setMax(double newMax) {
        max = new Double(newMax);
    }
    /**
     * sets the maximum.
     * 
     * @param newMax java.lang.Double
     */
    void setMax(java.lang.Double newMax) {
        max = newMax;
    }
    /**
     * sets the minimum.
     * 
     * @param newMin double
     */
    void setMin(double newMin) {
        min = new Double(newMin);
    }
    /**
     * sets the minimum.
     * 
     * @param newMin java.lang.Double
     */
    void setMin(java.lang.Double newMin) {
        min = newMin;
    }
    /**
     * Insert the method's description here.
     * 
     * @param newName java.lang.String
     */
    void setName(java.lang.String newName) {
        name = newName;
    }
    /**
     * sets the variable symbol.
     * 
     * @param newSymbol java.lang.String
     */
    void setSymbol(java.lang.String newSymbol) {
        symbol = newSymbol.toUpperCase();
    }
    /**
     * sets type to DEPENDENT or INDEPENDENT.
     * 
     * @param newType Type
     */
    void setType(Type newType) {
        type = newType;
    }
    /**
     * sets the variable's unit.
     * 
     * @param newUnit java.lang.String
     */
    void setUnit(Unit newUnit) {
        unit = newUnit;
    }
    /**
     * sets the variable's unit.
     * 
     * @param newUnit java.lang.String
     */
    void setUnit(java.lang.String newUnit) {
        unit = Unit.getUnitFromString(newUnit);
    }
    /**
     * pretty print variable.
     * 
     * @return java.lang.String
     */
    public String toString() {
        StringBuffer tmp = new StringBuffer(getSymbol());
        tmp.append(": ");
        tmp.append(type);
        if (type.equals(Type.INDEPENDENT) && first != null && last != null) {
            tmp.append(" from=").append(first);
            tmp.append(" to=").append(last);
        }
        if (factor != null)
            tmp.append(" factor=").append(factor);
        if (unit != null)
            tmp.append(" unit=").append(unit);

        return tmp.toString();
    }
}
