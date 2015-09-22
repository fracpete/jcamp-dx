/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import org.jcamp.spectrum.Multiplicity;
/**
 * group of data listed in peaktables or peak assignments.
 * @author Thomas Weber
 */
public class DataGroup {
    private final DataType type;
    private final String[] varSymbols;
    private Object[] values;
    /**
     * DataGroup constructor comment.
     */
    public DataGroup(String[] symbols) {
        super();
        this.type = null;
        this.varSymbols = symbols;
        this.values = new Object[symbols.length];
    }
    /**
     * AFFNGroup constructor comment.
     */
    public DataGroup(String[] symbols, Object[] values) {
        super();
        this.type = null;
        this.varSymbols = symbols;
        this.values = new Object[symbols.length];
        for (int i = 0; i < symbols.length; i++)
            this.values[i] = values[i];
    }
    /**
     * DataGroup ctor for XY.
     */
    public DataGroup(double x, double y) {
        super();
        this.type = DataType.XY;
        this.varSymbols = DataType.XY.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y)};
    }
    /**
     * DataGroup constructor for standard XYM data.
     */
    public DataGroup(double x, double y, char m) {
        super();
        this.type = DataType.XYM;
        this.varSymbols = DataType.XYM.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y), Multiplicity.multiplicityOf(m)};
    }
    /**
     * DataGroup constructor for standard XYMWA data.
     */
    public DataGroup(double x, double y, char m, double w, String a) {
        super();
        this.type = DataType.XYMWA;
        this.varSymbols = DataType.XYMWA.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y), Multiplicity.multiplicityOf(m), new Double(w), a };
    }
    /**
     * DataGroup constructor for standard XYMA data.
     */
    public DataGroup(double x, double y, char m, String a) {
        super();
        this.type = DataType.XYMA;
        this.varSymbols = DataType.XYMA.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y), Multiplicity.multiplicityOf(m), a };
    }
    /**
     * DataGroup constructor for standard XYW data.
     */
    public DataGroup(double x, double y, double w) {
        super();
        this.type = DataType.XYW;
        this.varSymbols = DataType.XYW.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y), new Double(w)};
    }
    /**
     * DataGroup constructor for standard XYWA data.
     */
    public DataGroup(double x, double y, double w, String a) {
        super();
        this.type = DataType.XYWA;
        this.varSymbols = DataType.XYWA.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y), new Double(w), a };
    }
    /**
     * DataGroup constructor for standard XYA data.
     */
    public DataGroup(double x, double y, String a) {
        super();
        this.type = DataType.XYA;
        this.varSymbols = DataType.XYA.getSymbols();
        this.values = new Object[] { new Double(x), new Double(y), a };
    }
    /**
     * AFFNGroup constructor comment.
     */
    public DataGroup(DataType type, Object[] values) {
        super();
        this.type = null;
        this.varSymbols = type.getSymbols();
        this.values = new Object[values.length];
        for (int i = 0; i < values.length; i++)
            this.values[i] = values[i];
    }
    /**
     * DataGroup ctor for XY.
     */
    public DataGroup(Double x, Double y) {
        super();
        this.type = DataType.XY;
        this.varSymbols = DataType.XY.getSymbols();
        this.values = new Object[] { x, y };
    }
    /**
     * DataGroup constructor for standard XYM data.
     */
    public DataGroup(Double x, Double y, Multiplicity m) {
        super();
        this.type = DataType.XYM;
        this.varSymbols = DataType.XYM.getSymbols();
        this.values = new Object[] { x, y, m };
    }
    /**
     * DataGroup constructor for standard XYMWA data.
     */
    public DataGroup(Double x, Double y, Multiplicity m, Double w, String a) {
        super();
        this.type = DataType.XYMWA;
        this.varSymbols = DataType.XYMWA.getSymbols();
        this.values = new Object[] { x, y, m, w, a };
    }
    /**
     * DataGroup constructor for standard XYMA data.
     */
    public DataGroup(Double x, Double y, Multiplicity m, String a) {
        super();
        this.type = DataType.XYMA;
        this.varSymbols = DataType.XYMA.getSymbols();
        this.values = new Object[] { x, y, m, a };
    }
    /**
     * DataGroup constructor for standard XYW data.
     */
    public DataGroup(Double x, Double y, Double w) {
        super();
        this.type = DataType.XYW;
        this.varSymbols = DataType.XYW.getSymbols();
        this.values = new Object[] { x, y, w };
    }
    /**
     * DataGroup constructor for standard XYWA data.
     */
    public DataGroup(Double x, Double y, Double w, String a) {
        super();
        this.type = DataType.XYWA;
        this.varSymbols = DataType.XYWA.getSymbols();
        this.values = new Object[] { x, y, w, a };
    }
    /**
     * DataGroup constructor for standard XYA data.
     */
    public DataGroup(Double x, Double y, String a) {
        super();
        this.type = DataType.XYA;
        this.varSymbols = DataType.XYA.getSymbols();
        this.values = new Object[] { x, y, a };
    }
    /**
     * Insert the method's description here.
     * 
     * @return com.creon.chem.jcamp.DataType
     */
    public final DataType getType() {
        return type;
    }
    /**
     * gets value at index
     * 
     * @return Object
     * @param int index
     */
    public Object getValue(int index) {
        return values[index];
    }
    /**
     * gets value for symbol c
     * 
     * @return Object
     * @param c java.lang.String
     */
    public Object getValue(String c) throws JCAMPException {
        return values[indexOfSymbol(c)];
    }
    /**
     * gets index of variable symbol.
     * 
     * @return int
     * @param symbol java.lang.String
     */
    public int indexOfSymbol(String symbol) throws JCAMPException {
        for (int i = 0; i < varSymbols.length; i++) {
            if (symbol.equalsIgnoreCase(varSymbols[i]))
                return i;
        }
        throw new JCAMPException("unknown data variable: " + symbol);
    }
    /**
     * sets value at index.
     * 
     * @param index int
     * @param value Object
     */
    public void setValue(int index, Object value) {
        this.values[index] = value;
    }
    /**
     * sets value for symbol.
     * 
     * @param symbol String
     * @param value Object
     */
    public void setValue(String symbol, Object value) throws JCAMPException {
        this.values[indexOfSymbol(symbol)] = value;
    }
}
