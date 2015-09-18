package org.jcamp.parser;

import java.util.Vector;

/**
 * class for reading a NTUPLE table.
 * 
 * @author Thomas Weber
 */
public class JCAMPNTuple {

	private JCAMPDataRecord startLDR; // ##NTUPLES=
	private JCAMPDataRecord endLDR; // ##ENDNTUPLES=
	private JCAMPBlock containingBlock;
	private JCAMPDataRecord[] headers;
	private JCAMPNTuplePage[] pages;
	private JCAMPVariable[] vars;
	private int numHeaders;
	private int firstIndex;
	private int lastIndex;

	/**
	 * JCAMPNTuple constructor comment.
	 */
	JCAMPNTuple(JCAMPBlock block, JCAMPDataRecord startLDR,
			JCAMPDataRecord endLDR) throws JCAMPException {
		super();
		this.containingBlock = block;
		this.startLDR = startLDR;
		this.endLDR = endLDR;
		initialize();
	}

	/**
	 * gets containing block.
	 * 
	 * @return JCAMPBlock
	 */
	public JCAMPBlock getBlock() {
		return this.containingBlock;
	}

	/**
	 * gets a header LDR.
	 * 
	 * @return JCAMPDataRecord
	 * @param key
	 *            java.lang.String
	 */
	public JCAMPDataRecord getHeader(String key) {
		if (headers != null)
			for (int i = 0; i < headers.length; i++) {
				if (key.equals(headers[i].getKey()))
					return headers[i];
			}
		return null;
	}

	/**
	 * gets array of NTUPLE headers.
	 * 
	 * @return JCAMPDataRecord[]
	 */
	public JCAMPDataRecord[] getHeaders() {
		return this.headers;
	}

	/**
	 * gets containing JCAMP
	 * 
	 * @return java.lang.String
	 */
	public String getJCAMP() {
		return containingBlock.getJCAMP();
	}

	/**
	 * gets NTUPLE page number <code>index</code>.
	 * 
	 * @return com.labcontrol.jcamp.reader.JCAMPNTuplePage
	 * @param index
	 *            int
	 */
	public JCAMPNTuplePage getPage(int index) {
		if (this.pages != null)
			return this.pages[index];
		return null;
	}

	/**
	 * gets array of NTUPLE pages.
	 * 
	 * @return JCAMPNTuplePage[]
	 */
	public JCAMPNTuplePage[] getPages() {
		return this.pages;
	}

	/**
	 * gets the variable by symbol <code>symbol</code>.
	 * 
	 * @param String
	 *            symbol
	 * @return com.creon.chem.jcamp.JCAMPVariable
	 */
	public JCAMPVariable getVariable(String symbol) {
		symbol = symbol.toUpperCase();
		for (int i = 0; i < vars.length; i++) {
			if (symbol.equals(vars[i].getSymbol()))
				return vars[i];
		}
		return null;
	}

	/**
	 * gets variable by name.
	 * 
	 * @return com.creon.chem.jcamp.JCAMPVariable
	 * @param name
	 *            java.lang.String
	 */
	public JCAMPVariable getVariableByName(String name) {
		name = name.toUpperCase();
		for (int i = 0; i < vars.length; i++) {
			if (name.equals(vars[i].getName()))
				return vars[i];
		}
		return null;
	}

	/**
	 * gets array of variables defined in NTUPLE.
	 * 
	 * @return com.creon.chem.jcamp.JCAMPVariable[]
	 */
	public JCAMPVariable[] getVariables() {
		return vars;
	}

	/**
	 * get variable value array in page dimension.
	 * 
	 * @return java.lang.String[]
	 * @param ldrKey
	 *            java.lang.String
	 * @param v
	 *            JCAMPVariable
	 */
	public String[] getVariableValues(String ldrKey, JCAMPVariable v) {
		String[] values = new String[numPages()];
		for (int i = 0; i < numPages(); i++) {
			JCAMPDataRecord ldr = pages[i].getDataRecord(ldrKey);
			if (ldr == null) {
				if (v != null) {
					values[i] = pages[i].getPageVariableValue(v.getSymbol());
				} else
					values[i] = null;
			} else {
				values[i] = ldr.getContent();
			}
		}
		return values;
	}

	/**
	 * initialize headers.
	 * 
	 */
	private void initHeaders() {
		JCAMPDataRecord[] blockLDR = containingBlock.getAllDataRecords();
		numHeaders = 0;
		for (int i = firstIndex; i <= lastIndex; i++) {
			if (blockLDR[i].getKey().equals("PAGE")) {
				break;
			}
			numHeaders++;
		}
		headers = new JCAMPDataRecord[numHeaders];
		System.arraycopy(blockLDR, firstIndex, headers, 0, numHeaders);
	}

	/**
	 * collect header LDRs
	 * 
	 */
	private void initialize() throws JCAMPException {
		firstIndex = startLDR.getBlockIndex() + 1;
		lastIndex = endLDR.getBlockIndex() - 1;
		if (firstIndex > lastIndex)
			return;
		initHeaders();
		initVariables();
		initPages();
	}

	/**
	 * initialize pages.
	 * 
	 */
	private void initPages() throws JCAMPException {
		JCAMPDataRecord[] blockLDR = containingBlock.getAllDataRecords();
		JCAMPDataRecord page1 = blockLDR[firstIndex + numHeaders];
		JCAMPDataRecord page2 = null;
		Vector tmp = new Vector();
		for (int i = firstIndex + numHeaders + 1; i < lastIndex; i++) {
			if (blockLDR[i].getKey().equals("PAGE")) {
				page2 = blockLDR[i - 1];
				JCAMPNTuplePage ntuplePage = new JCAMPNTuplePage(this, page1,
						page2);
				tmp.addElement(ntuplePage);
				page1 = blockLDR[i];
			}
		}
		if (page1 != null) {
			JCAMPNTuplePage ntuplePage = new JCAMPNTuplePage(this, page1,
					blockLDR[lastIndex]);
			tmp.addElement(ntuplePage);
		}
		pages = new JCAMPNTuplePage[tmp.size()];
		for (int i = 0; i < pages.length; i++)
			pages[i] = (JCAMPNTuplePage) tmp.elementAt(i);
	}

	/**
	 * init variables for NTUPLE block.
	 * 
	 * @exception com.creon.chem.jcamp.JCAMPException
	 *                incorrect variable declarations.
	 */
	private void initVariables() throws JCAMPException {
		JCAMPDataRecord ldr = getHeader("SYMBOL");
		if (ldr == null)
			throw new JCAMPException("missing variable declaration in NTUPLES");
		String[] symbols = Utils.splitStringCSV(ldr.getContent());
		vars = new JCAMPVariable[symbols.length];
		for (int i = 0; i < vars.length; i++) {
			vars[i] = new JCAMPVariable(symbols[i].toUpperCase());
		}

		ldr = getHeader("VARNAME");
		if (ldr != null) {
			String[] labels = Utils.splitStringCSV(ldr.getContent());
			// if (labels.length != vars.length)
			// throw new JCAMPException(
			// "bad ##VARNAME=: number of name entries != number of variables");
			for (int i = 0; i < labels.length; i++) {
				vars[i].setLabel(labels[i]);
				vars[i].setName(Utils.normalizeLabel(labels[i]));
			}
		}
		ldr = getHeader("VARTYPE");
		if (ldr != null) {
			String[] types = Utils.splitStringCSV(ldr.getContent());
			if (types.length != vars.length)
				throw new JCAMPException(
						"bad ##VARTYPE=: number of type entries != number of variables");
			for (int i = 0; i < vars.length; i++) {
				if (types[i].equalsIgnoreCase("INDEPENDENT"))
					vars[i].setType(JCAMPVariable.Type.INDEPENDENT);
				else if (types[i].equalsIgnoreCase("DEPENDENT"))
					vars[i].setType(JCAMPVariable.Type.DEPENDENT);
				else if (types[i].equalsIgnoreCase("PAGE"))
					vars[i].setType(JCAMPVariable.Type.PAGE);
			}
		}
		ldr = getHeader("VARFORM");
		if (ldr != null) {
			String[] forms = Utils.splitStringCSV(ldr.getContent());
			if (forms.length != vars.length)
				throw new JCAMPException(
						"bad ##VARFORM=: number of varform entries != number of variables");
			for (int i = 0; i < vars.length; i++) {
				if ("AFFN".equalsIgnoreCase(forms[i]))
					vars[i].setFormat(JCAMPVariable.Format.AFFN);
				else if ("ASDF".equalsIgnoreCase(forms[i]))
					vars[i].setFormat(JCAMPVariable.Format.ASDF);
				else if ("STRING".equalsIgnoreCase(forms[i]))
					vars[i].setFormat(JCAMPVariable.Format.STRING);
				else
					vars[i].setFormat(JCAMPVariable.Format.TEXT);
			}
		}
		ldr = getHeader("VARDIM");
		if (ldr != null) {
			String[] dims = Utils.splitStringCSV(ldr.getContent());
			if (dims.length != vars.length)
				throw new JCAMPException(
						"bad ##VARDIM=: number of dimension entries != number of variables");
			for (int i = 0; i < vars.length; i++) {
				vars[i].setDimension(Integer.parseInt(dims[i]));
			}
		}
		ldr = getHeader("FIRST");
		if (ldr != null) {
			String[] firsts = Utils.splitStringCSV(ldr.getContent());
			if (firsts.length != vars.length)
				throw new JCAMPException(
						"bad ##FIRST=: number of first values != number of variables");
			for (int i = 0; i < vars.length; i++) {
				vars[i].setFirst(new Double(firsts[i]));
			}
		}
		ldr = getHeader("LAST");
		if (ldr != null) {
			String[] lasts = Utils.splitStringCSV(ldr.getContent());
			if (lasts.length != vars.length)
				throw new JCAMPException(
						"bad ##LAST=: number of last values != number of variables");
			for (int i = 0; i < vars.length; i++) {
				vars[i].setLast(new Double(lasts[i]));
			}
		}

		ldr = getHeader("MIN");
		if (ldr != null) {
			String[] mins = Utils.splitStringCSV(ldr.getContent());
			if (mins.length != vars.length)
				throw new JCAMPException(
						"bad ##MIN=: number of min values != number of variables");
			for (int i = 0; i < vars.length; i++) {
				vars[i].setMin(new Double(mins[i]));
			}
		}

		ldr = getHeader("MAX");
		if (ldr != null) {
			String[] maxs = Utils.splitStringCSV(ldr.getContent());
			if (maxs.length != vars.length)
				throw new JCAMPException(
						"bad ##MAX=: number of max values != number of variables");
			for (int i = 0; i < vars.length; i++) {
				vars[i].setMax(new Double(maxs[i]));
			}
		}
		ldr = getHeader("FACTOR");
		if (ldr != null) {
			String[] factors = Utils.splitStringCSV(ldr.getContent());
			if (factors.length != vars.length)
				throw new JCAMPException(
						"bad ##FACTOR=: number of factor values != number of variables");
			for (int i = 0; i < vars.length; i++) {
				vars[i].setFactor(new Double(factors[i]));
			}
		}
		ldr = getHeader("UNITS");
		if (ldr != null) {
			String[] units = Utils.splitStringCSV(ldr.getContent());
			// if (units.length != vars.length)
			// throw new JCAMPException(
			// "bad ##UNITS=: number of unit entries != number of variables");
			for (int i = 0; i < units.length; i++) {
				vars[i].setUnit(units[i]);
			}
		}

	}

	/**
	 * gets number of header LDRs.
	 * 
	 * @return int
	 */
	public int numHeaders() {
		return numHeaders;
	}

	/**
	 * gets number of pages.
	 * 
	 * @return int
	 */
	public int numPages() {
		if (pages == null)
			return 0;
		return pages.length;
	}
}
