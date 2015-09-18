package org.jcamp.parser;

/**
 * {@code AFFNTokenizer} handles numeric data tables in AFFN format.
 * 
 * 
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public class AFFNTokenizer implements java.util.Enumeration<AFFNGroup> {

	private final DataType type;
	private String data;
	private final String[] varSymbols;
	private int pos;
	private int length;

	/**
	 * AFFNTokenizer constructor comment.
	 */
	public AFFNTokenizer(String[] symbols, String data) throws JCAMPException {
		super();
		this.type = checkSymbols(symbols);
		if (this.type == null)
			this.varSymbols = symbols;
		else
			this.varSymbols = this.type.getSymbols();
		this.data = normalizeData(varSymbols.length, data);
		this.length = this.data.length();
	}

	/**
	 * AFFNTokenizer constructor comment.
	 */
	public AFFNTokenizer(JCAMPDataRecord ldr) throws JCAMPException {
		super();
		DataVariableInfo varInfo = new DataVariableInfo(ldr);
		this.type = checkSymbols(varInfo.getSymbols());
		if (this.type == null)
			this.varSymbols = varInfo.getSymbols();
		else
			this.varSymbols = this.type.getSymbols();
		String ldrData = ldr.getContent();
		LineTokenizer lt = new LineTokenizer(ldrData);
		lt.nextLine(); // skip variable declaration
		this.data = normalizeData(varSymbols.length,
				ldrData.substring(lt.getPosition()));
		this.length = this.data.length();
	}

	/**
	 * check for errornous symbols or standard symbols.
	 * 
	 */
	private static DataType checkSymbols(String[] varSymbols)
			throws JCAMPException {
		if (varSymbols == null || varSymbols.length == 0)
			throw new JCAMPException("bad variable declaration");
		if (varSymbols.length == 2) {
			if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y"))
				return DataType.XY;
		} else if (varSymbols.length == 3) {
			if (varSymbols[0].equalsIgnoreCase("X")
					&& varSymbols[1].equalsIgnoreCase("Y")
					&& varSymbols[2].equalsIgnoreCase("W"))
				return DataType.XYW;
		}
		return null;
	}

	/**
	 * get variable symbols.
	 * 
	 * @return java.lang.String[]
	 */
	public final java.lang.String[] getSymbols() {
		return varSymbols;
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
	 * returns <code>true</code> if more data is available.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean hasMoreElements() {
		return hasMoreGroups();
	}

	/**
	 * returns <code>true</code> if more data is available.
	 * 
	 * @return boolean
	 */
	public boolean hasMoreGroups() {
		if (pos < length - 1)
			return true;
		return false;
	}

	/**
	 * gets next data group.
	 * 
	 * @return Object
	 */
	@Override
	public AFFNGroup nextElement() {
		try {
			return nextGroup();
		} catch (JCAMPException e) {
			throw new java.util.NoSuchElementException(e.getMessage());
		}
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.AFFNGroup
	 */
	public AFFNGroup nextGroup() throws JCAMPException {
		if (pos >= length)
			throw new JCAMPException("parsed beyond end of AFFN block");
		if (DataType.XY.equals(type))
			return nextXYGroup();
		if (DataType.XYW.equals(type))
			return nextXYWGroup();
		int n = varSymbols.length;
		double[] values = new double[n];
		for (int i = 0; i < n - 1; i++) {
			int p = data.indexOf(",", pos);
			if (p < 0)
				throw new JCAMPException("missing data in AFFN block");
			values[i] = Double.parseDouble(data.substring(pos, p));
			pos = p + 1;
		}

		int p = data.indexOf(";", pos);
		if (p < 0)
			throw new JCAMPException("extra data in AFFN block");
		values[n - 1] = Double.parseDouble(data.substring(pos, p));
		pos = p + 1;
		return new AFFNGroup(varSymbols, values);

	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.AFFNGroup
	 */
	private AFFNGroup nextXYGroup() throws JCAMPException {
		double x;
		double y;
		int p = data.indexOf(',', pos);
		if (p < 0) {
			throw new JCAMPException("missing x data");
		}
		x = Double.parseDouble(data.substring(pos, p));
		pos = p + 1;
		p = data.indexOf(';', pos);
		if (p < 0)
			throw new JCAMPException("missing y data");
		y = Double.parseDouble(data.substring(pos, p));
		pos = p + 1;
		return new AFFNGroup(x, y);
	}

	/**
	 * gets next data group.
	 * 
	 * @return com.creon.chem.jcamp.AFFNGroup
	 */
	private AFFNGroup nextXYWGroup() throws JCAMPException {
		double x;
		double y;
		double w;
		int p = data.indexOf(',', pos);
		if (p < 0)
			throw new JCAMPException("missing x data");
		x = Double.parseDouble(data.substring(pos, p));
		pos = p + 1;
		p = data.indexOf(',', pos);
		if (p < 0)
			throw new JCAMPException("missing y data");
		y = Double.parseDouble(data.substring(pos, p));
		pos = p + 1;
		p = data.indexOf(';', pos);
		if (p < 0)
			throw new JCAMPException("missing w data");
		w = Double.parseDouble(data.substring(pos, p));
		pos = p + 1;
		return new AFFNGroup(x, y, w);
	}

	/**
	 * normalize data block in standard form: values are separated by ',',
	 * groups are separated by ';' whitespace is skipped
	 * 
	 * @return java.lang.String
	 * @param orig
	 *            java.lang.String
	 */
	private static String normalizeData(int groupLength, String orig)
			throws JCAMPException {
		StringBuilder normal = new StringBuilder();
		boolean inNumber = false;
		int j = 0;
		for (int i = 0; i < orig.length(); i++) {
			char c = orig.charAt(i);
			if (Character.isWhitespace(c)) {
				if (inNumber) {
					inNumber = false;
					j++;
					if (j < groupLength)
						normal.append(',');
					else {
						normal.append(';');
						j = 0;
					}
				}
				continue;
			}
			if (c == ';') {
				if (inNumber) {
					inNumber = false;
					j++;
					if (j < groupLength)
						throw new JCAMPException("missing numbers in AFFN data");
					if (j > groupLength)
						throw new JCAMPException("extra numbers in AFFN data");
					normal.append(';');
					j = 0;
				}
				continue;
			}
			if (c == ',') {
				if (inNumber) {
					inNumber = false;
					j++;
					if (j >= groupLength)
						throw new JCAMPException("extra commas in AFFN data");
					normal.append(',');
				}
				continue;
			}
			if (Character.isDigit(c) || c == '.' || c == '+' || c == '-'
					|| c == 'e' || c == 'E') {
				normal.append(c);
				inNumber = true;
			} else {
				throw new JCAMPException("unexpected character \'" + c
						+ "\' in AFFN data");
			}
		}
		normal.append(';');
		return normal.toString();
	}
}
