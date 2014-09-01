package org.jcamp.parser;

/**
 * group of data listed in AFFN (ASCII Free Format Numeric).
 * @author Thomas Weber
 */
public class AFFNGroup {
	private final DataType type;
	private final String[] varSymbols;
	private double[] values;
/**
 * AFFNGroup constructor comment.
 */
public AFFNGroup(String[] symbols) {
	super();
	this.type = null;
	this.varSymbols = symbols;
	this.values = new double[symbols.length];
}
/**
 * AFFNGroup constructor comment.
 */
public AFFNGroup(String[] symbols, double[] values) {
    super();
    this.type = null;
    this.varSymbols = symbols;
    this.values = new double[symbols.length];
    for (int i = 0; i < symbols.length; i++)
        this.values[i] = values[i];
}
/**
 * AFFNGroup constructor for standard XYW data.
 */
public AFFNGroup(double x, double y) {
	super();
	this.type = DataType.XY;
	this.varSymbols = DataType.XY.getSymbols();
	this.values = new double[] {x,y};
}
/**
 * AFFNGroup constructor for standard XYW data.
 */
public AFFNGroup(double x, double y, double w) {
    super();
    this.type = DataType.XYW;
    this.varSymbols = DataType.XYW.getSymbols();
    this.values = new double[] { x, y, w };
}
/**
 * return variable symbols.
 * 
 * @return java.lang.String[]
 */
public final String[] getSymbols() {
	return varSymbols;
}
/**
 * get standard type or null.
 * 
 * @return com.creon.chem.jcamp.DataType
 */
public final DataType getType() {
	return type;
}
/**
 * gets value at index
 * 
 * @return double
 * @param int index
 */
public double getValue(int index) {
	return values[index];
}
/**
 * gets value for symbol c
 * 
 * @return double
 * @param c java.lang.String
 */
public double getValue(String c) throws JCAMPException {
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
	throw new JCAMPException("unknown ASDF variable: " + symbol);
}
/**
 * sets value at index.
 * 
 * @param index int
 * @param value double
 */
public void setValue(int index, double value) {
    this.values[index] = value;
}
/**
 * sets value for symbol.
 * 
 * @param symbol String
 * @param value double
 */
public void setValue(String symbol, double value) throws JCAMPException {
	this.values[indexOfSymbol(symbol)] = value;
}
/**
 * size of group.
 * 
 * @return int
 */
public int size() {
	return values.length;
}
}
