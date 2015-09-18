package org.jcamp.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcamp.spectrum.ISpectrumIdentifier;

/**
 * class for handling JCAMP blocks.
 * 
 * @author Thomas Weber
 */
public class JCAMPBlock {
	private ASDFDecoder asdfDecoder = new ASDFDecoder();
	private static Log log = LogFactory.getLog(JCAMPBlock.class);
	private final static IErrorHandler DEFAULT_ERROR_HANDLER = new ErrorHandlerAdapter() {
		@Override
		public void fatal(String msg) throws JCAMPException {
			log.fatal(msg);
			throw new JCAMPException("FATAL ERROR! " + msg);
		}

		@Override
		public void error(String msg) throws JCAMPException {
			log.error(msg);
			throw new JCAMPException("ERROR! " + msg);
		}

		@Override
		public void warn(String msg) throws JCAMPException {
			log.warn(msg);
		}
	};
	private IErrorHandler errorHandler = DEFAULT_ERROR_HANDLER;

	public final static class Type implements Serializable {
		private static final long serialVersionUID = -8081269600789693382L;
		private final String key;
		private final int ordinal;

		private Type(int ordinal, String key) {
			this.ordinal = ordinal;
			this.key = key;
		}

		@Override
		public String toString() {
			return key;
		}

		public Collection types() {
			return TYPES_LIST;
		}

		private Object readResolve() throws java.io.ObjectStreamException {
			return TYPES[ordinal];
		}

		@Override
		public final int hashCode() {
			return ordinal;
		}

		@Override
		public final boolean equals(Object obj) {
			if (obj instanceof Type && ((Type) obj) == this)
				return true;
			return false;
		}

		public final static Type LINK = new Type(0, "link");
		public final static Type STRUCTURE = new Type(1, "structure");
		public final static Type FULLSPECTRUM = new Type(2, "full spectrum");
		public final static Type PEAKTABLE = new Type(3, "peak table");
		public final static Type ASSIGNMENT = new Type(4, "assignment");
		private final static Type[] TYPES = new Type[] { LINK, STRUCTURE,
				FULLSPECTRUM, PEAKTABLE, ASSIGNMENT };
		private final static List TYPES_LIST = Collections
				.unmodifiableList(Arrays.asList(TYPES));
	}

	private final static String CRLF = "\r\n";
	private final int start;
	private final int end;
	private final String jcamp;
	private final JCAMPBlock parent;
	private int numDataRecords;
	private Type type;
	private int spectrumID;
	private String data;
	private Hashtable<Integer, JCAMPBlock> childBlocks = new Hashtable<Integer, JCAMPBlock>(
			10);
	// hashtable containing all data records (or list of data records for
	// multiple records with same key)
	private Hashtable<String, JCAMPDataRecord> dataRecords = new Hashtable<String, JCAMPDataRecord>(
			50);
	// array of data records with duplicate keys
	private JCAMPDataRecord[] ldrs;
	private JCAMPBlock[] references = null;
	private boolean ntupleBlock = false;
	private JCAMPNTuple ntuple;
	private JCAMPVariable[] vars = null;
	private boolean isValidating = true;

	/**
	 * create JCAMPBlock from substring.
	 */
	public JCAMPBlock(JCAMPBlock parent, String jcamp, int start, int end)
			throws JCAMPException {
		super();
		this.jcamp = jcamp;
		this.start = start;
		this.end = end;
		this.parent = parent;
		initBlocks();
		initLDRs();
		analyzeBlockType();
		initNTuple();
		initVariables();
	}

	/**
	 * create JCAMPBlock from substring.
	 */
	public JCAMPBlock(JCAMPBlock parent, String jcamp, int start, int end,
			IErrorHandler errorHandler) throws JCAMPException {
		super();
		this.jcamp = jcamp;
		this.start = start;
		this.end = end;
		this.parent = parent;
		this.errorHandler = errorHandler;
		initBlocks();
		initLDRs();
		analyzeBlockType();
		initNTuple();
		initVariables();
	}

	/**
	 * create JCAMPBlock from String
	 * 
	 * @param jcamp
	 *            java.lang.String
	 */
	public JCAMPBlock(String jcamp) throws JCAMPException {
		this(jcamp, 0, jcamp.length());
	}

	/**
	 * create JCAMPBlock from substring.
	 */
	public JCAMPBlock(String jcamp, int start, int end) throws JCAMPException {
		this(null, jcamp, start, end);
	}

	/**
	 * create JCAMPBlock from substring.
	 */
	public JCAMPBlock(String jcamp, int start, int end,
			IErrorHandler errorHandler) throws JCAMPException {
		this(null, jcamp, start, end, errorHandler);
	}

	/**
	 * create JCAMPBlock from String
	 * 
	 * @param jcamp
	 *            java.lang.String
	 */
	public JCAMPBlock(String jcamp, IErrorHandler errorHandler)
			throws JCAMPException {
		this(jcamp, 0, jcamp.length(), errorHandler);
	}

	/**
	 * analyze data records for type of block.
	 */
	private void analyzeBlockType() throws JCAMPException {
		JCAMPDataRecord ldrJCAMPCS = getDataRecord("JCAMPCS");
		if (ldrJCAMPCS != null)
			this.type = Type.STRUCTURE;
		else {
			JCAMPDataRecord ldrDataType = getDataRecord("DATATYPE");
			if (ldrDataType == null)
				throw new JCAMPException("missing ##DATATYPE=");
			String dtype = ldrDataType.getContent().toUpperCase();
			if (dtype.indexOf("LINK") >= 0) {
				this.type = Type.LINK;
			} else if (dtype.indexOf("TABLE") >= 0) {
				this.type = Type.PEAKTABLE;
				analyzeSpectrumID(dtype);
			} else if (dtype.indexOf("ASSIGNMENT") >= 0) {
				this.type = Type.ASSIGNMENT;
				analyzeSpectrumID(dtype);
			} else {
				this.type = Type.FULLSPECTRUM;
				analyzeSpectrumID(dtype);
				if (spectrumID == ISpectrumIdentifier.MASS
						&& dtype.indexOf("CONTINUOUS") < 0)
					this.type = Type.PEAKTABLE;
			}
		}
	}

	/**
	 * analyze ##DATATYPE= LDR for spectrum type.
	 * 
	 * @param datatype
	 *            java.lang.String
	 */
	private void analyzeSpectrumID(String dataType) {
		if (dataType != null) {
			dataType = dataType.toUpperCase();
			if (dataType.indexOf("NMR") > -1) {
				spectrumID = ISpectrumIdentifier.NMR;
			} else if (dataType.indexOf("MASS") > -1) {
				spectrumID = ISpectrumIdentifier.MASS;
			} else if (dataType.indexOf("INFRARED") > -1) {
				spectrumID = ISpectrumIdentifier.IR;
			} else if (dataType.indexOf("IR") > -1) {
				spectrumID = ISpectrumIdentifier.IR;
			} else if (dataType.indexOf("UV") > -1) {
				spectrumID = ISpectrumIdentifier.UV;
			} else if (dataType.indexOf("ULTRAVIOLET") > -1) {
				spectrumID = ISpectrumIdentifier.UV;
			} else if (dataType.indexOf("RAMAN") > -1) {
				spectrumID = ISpectrumIdentifier.RAMAN;
			} else if (dataType.indexOf("FLUORESCENCE") > -1) {
				spectrumID = ISpectrumIdentifier.FLUORESCENCE;
			} else if (dataType.indexOf("CHROMATOGRAM") > -1) {
				spectrumID = ISpectrumIdentifier.CHROMATOGRAM;
			}
		}
	}

	/**
	 * gets block as a simple JCAMP string
	 * 
	 * @return java.lang.String
	 */
	public String asSimpleJCAMP() {
		return this.data;
	}

	/**
	 * change a data record to a new value and return modified JCAMPBlock.
	 * 
	 * @return JCAMPBlock modified block
	 * @param key
	 *            java.lang.String normalized key
	 * @param newValue
	 *            JCAMPDataRecord new data record value
	 */
	public JCAMPBlock changeDataRecord(String key, String newValue)
			throws JCAMPException {
		JCAMPDataRecord oldLDR = getDataRecord(key);
		if (oldLDR == null) {
			errorHandler.error("LDR \"##" + key + "=\" not found in block");
			return new JCAMPBlock(this.getJCAMP());
		}
		StringBuilder newJCAMP = new StringBuilder();
		if (oldLDR.getStart() > 0) {
			newJCAMP.append(this.jcamp.substring(0, oldLDR.getStart() - 1));
			if (newJCAMP.charAt(newJCAMP.length() - 1) != '\n')
				newJCAMP.append(CRLF);
		}
		newJCAMP.append("##").append(key).append("=").append(newValue);
		if (newJCAMP.charAt(newJCAMP.length() - 1) != '\n')
			newJCAMP.append(CRLF);
		if (oldLDR.getEnd() < this.jcamp.length() - 1)
			newJCAMP.append(this.jcamp.substring(oldLDR.getEnd() + 1));
		return new JCAMPBlock(newJCAMP.toString());
	}

	/**
	 * get all data records, including records with duplicate key.
	 * 
	 * @return com.labcontrol.jcamp.reader.JCAMPDataRecord[]
	 */
	public JCAMPDataRecord[] getAllDataRecords() {
		return ldrs;
	}

	/**
	 * gets current ASDF decoder.
	 * 
	 * @return com.creon.chem.jcamp.ASDFDecoder
	 */
	public ASDFDecoder getASDFDecoder() {
		return asdfDecoder;
	}

	/**
	 * gets block by block ID.
	 * 
	 * @return JCAMPBlock
	 * @param id
	 *            int
	 */
	public JCAMPBlock getBlock(int id) {
		return this.childBlocks.get(new Integer(id));
	}

	/**
	 * gets blocks within block.
	 * 
	 * @return java.util.Enumeration
	 */
	public Enumeration<JCAMPBlock> getBlocks() {
		return this.childBlocks.elements();
	}

	/**
	 * get data record by index within block (counting all duplicates).
	 * 
	 * @return JCAMPDataRecord
	 * @param index
	 *            int
	 */
	public JCAMPDataRecord getDataRecord(int index) {
		return ldrs[index];
	}

	/**
	 * get data records by normalized key
	 * 
	 * @return JCAMPDataRecord
	 * @param key
	 *            java.lang.String
	 */
	public JCAMPDataRecord getDataRecord(String key) {
		return dataRecords.get(key);
	}

	/**
	 * gets LDRs for block.
	 * 
	 * @return java.util.Enumeration
	 */
	public Enumeration getDataRecords() {
		return this.dataRecords.elements();
	}

	/**
	 * Insert the method's description here.
	 * 
	 * @return com.creon.chem.jcamp.IErrorHandler
	 */
	public IErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * gets block ID.
	 * 
	 * @return int
	 */
	public int getID() {
		JCAMPDataRecord ldr = this.dataRecords.get("BLOCKID");
		if (ldr == null)
			return -1;
		String blockID = ldr.getValue();
		String id = Utils.removeComments(blockID).trim();
		return Integer.parseInt(id);
	}

	/**
	 * gets JCAMP string containing block.
	 * 
	 * @return java.lang.String
	 */
	public String getJCAMP() {
		return this.jcamp;
	}

	/**
	 * get ntuple of block.
	 * 
	 * @return com.labcontrol.jcamp.reader.JCAMPNTuple
	 */
	public JCAMPNTuple getNTuple() {
		return this.ntuple;
	}

	/**
	 * gets crossreferences, works only with SpecInfo convention!
	 * 
	 * @return JCAMPBlock[]
	 */
	public JCAMPBlock[] getReferences() {
		if (this.references == null) {
			Vector refs = new Vector();
			if (this.parent == null) {
				this.references = new JCAMPBlock[0];
				return this.references;
			}
			JCAMPDataRecord xrefLDR = getDataRecord("CROSSREFERENCE");
			if (xrefLDR == null) {
				this.references = new JCAMPBlock[0];
				return this.references;
			}
			String xref = xrefLDR.getValue();
			xref = Utils.removeComments(xref).trim();
			StringTokenizer commaTokenizer = new StringTokenizer(xref, ",");
			while (commaTokenizer.hasMoreTokens()) {
				String token = commaTokenizer.nextToken().trim();
				try {
					int ref = Integer.parseInt(token);
					JCAMPBlock linkBlock = this.parent.getBlock(ref);
					refs.addElement(linkBlock);
				} catch (NumberFormatException e) {
				}
			}
			this.references = new JCAMPBlock[refs.size()];
			for (int i = 0; i < refs.size(); i++) {
				this.references[i] = (JCAMPBlock) refs.elementAt(i);
			}
		}
		return this.references;
	}

	/**
	 * gets spectrum identifier.
	 * 
	 * @return int
	 */
	public int getSpectrumID() {
		return spectrumID;
	}

	/**
	 * gets block type.
	 * 
	 * @return JCAMPBlock.Type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * gets the variable for symbol symbol.
	 * 
	 * @param String
	 *            symbol
	 * @return com.creon.chem.jcamp.JCAMPVariable
	 */
	public JCAMPVariable getVariable(String symbol) {
		if (isNTupleBlock())
			return ntuple.getVariable(symbol);
		else {
			symbol = symbol.toUpperCase();
			for (int i = 0; i < vars.length; i++) {
				if (symbol.equals(vars[i].getSymbol()))
					return vars[i];
			}
			return null;
		}
	}

	/**
	 * gets the variable array
	 * 
	 * @return com.creon.chem.jcamp.JCAMPVariable[]
	 */
	public JCAMPVariable[] getVariables() {
		if (isNTupleBlock())
			return ntuple.getVariables();
		else
			return vars;
	}

	/**
	 * find child blocks within <code>start</code> and <code>end</code> of
	 * <code>jcamp</code> string. collects remaining labels in string
	 * <code>data</code>
	 */
	private void initBlocks() throws JCAMPException {
		int id = 0;
		StringBuilder tmp = new StringBuilder();
		BlockIterator blockIter = new BlockIterator(jcamp.substring(this.start,
				this.end));
		int o0 = start;
		int o1;
		while (blockIter.hasNext()) {
			int offset = blockIter.getOffset();
			o1 = this.start + offset - 1;
			tmp.append(jcamp.substring(o0, o1));
			String block = blockIter.next();
			o0 = this.start + offset + block.length();
			o1++;
			JCAMPBlock jcampBlock = new JCAMPBlock(this, jcamp, o1, o0);
			int blockID = jcampBlock.getID();
			if (blockID < 0) {
				id--;
			} else
				id = blockID;
			childBlocks.put(new Integer(id), jcampBlock);
		}
		tmp.append(jcamp.substring(o0, this.end));
		this.data = tmp.toString();
	}

	/**
	 * iterator over all data records and store them into the hashtable
	 * <code>dataRecords</code>
	 */
	private void initLDRs() {
		IStringIterator ldrIter = new LDRIterator(data);
		int blockIndex = 0;
		ArrayList tmp = new ArrayList();
		while (ldrIter.hasNext()) {
			int offset = ldrIter.getOffset();
			String ldr = ldrIter.next();
			JCAMPDataRecord dataRecord = new JCAMPDataRecord(this.data, offset,
					offset + ldr.length(), blockIndex);
			JCAMPDataRecord ldrList = this.dataRecords.get(dataRecord.getKey());
			if (ldrList == null) {
				this.dataRecords.put(dataRecord.getKey(), dataRecord);
			} else {
				ldrList.listIterator().add(dataRecord);
			}
			tmp.add(dataRecord);
			blockIndex++;
		}
		this.numDataRecords = blockIndex;
		this.ldrs = new JCAMPDataRecord[tmp.size()];
		for (int i = 0; i < tmp.size(); i++)
			this.ldrs[i] = (JCAMPDataRecord) tmp.get(i);
	}

	/**
	 * initialize JCAMPNTuplePages. TODO: handle multiple NTUPLES blocks?
	 */
	private void initNTuple() throws JCAMPException {
		if (isStructureBlock())
			return;
		JCAMPDataRecord startNTupleLDR = getDataRecord("NTUPLES");
		if (startNTupleLDR == null) {
			this.ntupleBlock = false;
			// make some safety checks
			if (getDataRecord("SYMBOL") != null) {
				// assume NTUPLE block, but treat as error
				this.ntupleBlock = true;
				errorHandler.error("missing ##NTUPLES=");
			}
			if (getDataRecord("VARDIM") != null) {
				// assume NTUPLE block, but treat as error
				this.ntupleBlock = true;
				errorHandler.error("missing ##NTUPLES=");
			}
			if (!this.ntupleBlock)
				return;
		}
		JCAMPDataRecord endNTupleLDR = getDataRecord("ENDNTUPLES");
		if (endNTupleLDR == null) {
			errorHandler.error("missing ##END NTUPLES=");
		}
		this.ntuple = new JCAMPNTuple(this, startNTupleLDR, endNTupleLDR);
		this.ntupleBlock = true;
	}

	/**
	 * Parses the string as double.
	 * 
	 * @param s
	 *            the string to parse
	 * @return the parsed double
	 */
	private Double parseDouble(String s) {
		try {
			return new Double(s);
		} catch (NumberFormatException e) {
			return new Double(s.replace(",", "."));
		}
	}

	/**
	 * find definitions for all variables.
	 * 
	 * @exception com.creon.chem.jcamp.JCAMPException
	 *                parsing errors.
	 */
	private void initVariables() throws JCAMPException {
		if (isStructureBlock()) // JCAMP CS has no variables
			return;
		if (isNTupleBlock()) // NTUPLEs are already initialized
			return;
		if (isLinkBlock())
			return;
		JCAMPDataRecord dataLDR = null;
		Enumeration records = getDataRecords();
		while (records.hasMoreElements()) {
			JCAMPDataRecord ldr = (JCAMPDataRecord) records.nextElement();
			if (ldr.isData()) {
				if (dataLDR != null) {
					errorHandler
							.error("more than one data LDR in block: use compound JCAMP");
					break; // use first data block encountered
				} else
					dataLDR = ldr;
			}
		}
		if (dataLDR == null) {
			errorHandler.fatal("missing data LDR");
		}
		DataVariableInfo info = new DataVariableInfo(dataLDR);
		String[] symbols = info.getSymbols();
		JCAMPDataRecord ldr = getDataRecord("NPOINTS");
		if (ldr == null)
			errorHandler.fatal("missing required label ##NPOINTS=");
		int nPoints = Integer.parseInt(ldr.getContent());
		vars = new JCAMPVariable[symbols.length];
		for (int i = 0; i < symbols.length; i++) {
			String symbol = symbols[i].toUpperCase();
			JCAMPVariable v = new JCAMPVariable(symbol);
			if (i > 0)
				v.setType(JCAMPVariable.Type.DEPENDENT);
			else
				v.setType(JCAMPVariable.Type.INDEPENDENT);
			if (getType().equals(Type.FULLSPECTRUM)) {
				if (info.isIncremental())
					v.setFormat(JCAMPVariable.Format.ASDF);
				else
					v.setFormat(JCAMPVariable.Format.AFFN);
			} else if (getType().equals(Type.PEAKTABLE)
					|| getType().equals(Type.ASSIGNMENT)) {
				if (symbol.equals("X") || symbol.equals("Y")
						|| symbol.equals("W"))
					v.setFormat(JCAMPVariable.Format.AFFN);
				else
					v.setFormat(JCAMPVariable.Format.STRING);
			}
			v.setDimension(nPoints);
			ldr = getDataRecord(symbol + "LABEL");
			if (ldr != null)
				v.setLabel(ldr.getContent());
			ldr = getDataRecord(symbol + "UNITS");
			if (ldr != null)
				v.setUnit(ldr.getContent());
			ldr = getDataRecord("FIRST" + symbol);
			if (ldr != null)
				v.setFirst(parseDouble(ldr.getContent()));
			ldr = getDataRecord("LAST" + symbol);
			if (ldr != null)
				v.setLast(parseDouble(ldr.getContent()));
			ldr = getDataRecord(symbol + "FACTOR");
			if (ldr != null)
				v.setFactor(parseDouble(ldr.getContent()));
			ldr = getDataRecord("MIN" + symbol);
			if (ldr != null)
				v.setMin(parseDouble(ldr.getContent()));
			ldr = getDataRecord("MAX" + symbol);
			if (ldr != null)
				v.setMax(parseDouble(ldr.getContent()));
			vars[i] = v;
		}
	}

	/**
	 * checks if block is a JCAMP link block.
	 * 
	 * @return boolean
	 */
	public boolean isLinkBlock() {
		return this.type.equals(Type.LINK);
	}

	/**
	 * indicates a block containing ntuples.
	 * 
	 * @return boolean
	 */
	public boolean isNTupleBlock() {
		return this.ntupleBlock;
	}

	/**
	 * checks if block is a JCAMP structure block.
	 * 
	 * @return boolean
	 */
	public boolean isStructureBlock() {
		return this.type.equals(Type.STRUCTURE);
	}

	/**
	 * testing.
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(String[] args) {
		String jcamp = null;
		try {
			File dxFile = new java.io.File(args[0]);
			FileReader dxIn = new FileReader(dxFile);
			if (dxIn != null) {
				int size;
				int read;
				int r;
				char[] data;
				size = (int) dxFile.length();
				read = 0;
				data = new char[size];
				do {
					r = dxIn.read(data, read, size - read);
					read += r;
				} while (r > 0);
				dxIn.close();
				jcamp = String.valueOf(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			JCAMPBlock block = new JCAMPBlock(jcamp);
			System.out.println("Block Data:\n" + block.data);
			System.out.println("Child Blocks: " + block.childBlocks.size());
		} catch (JCAMPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * gets number of child blocks.
	 * 
	 * @return int
	 */
	public int numBlocks() {
		return childBlocks.size();
	}

	/**
	 * gets number of LDRs.
	 * 
	 * @return int
	 */
	public int numDataRecords() {
		return numDataRecords;
	}

	/**
	 * gets number of LDRs.
	 * 
	 * @return int
	 */
	public int numVariables() {
		return vars.length;
	}

	/**
	 * sets ASDFDecoder
	 * 
	 * @param newAsdfDecoder
	 *            com.creon.chem.jcamp.ASDFDecoder
	 */
	public void setASDFDecoder(ASDFDecoder newAsdfDecoder) {
		asdfDecoder = newAsdfDecoder;
	}

	/**
	 * sets error handler.
	 * 
	 * @param newErrorHandler
	 *            com.creon.chem.jcamp.IErrorHandler
	 */
	public void setErrorHandler(IErrorHandler newErrorHandler) {
		errorHandler = newErrorHandler;
	}

	/**
	 * sets spectrum identifier.
	 * 
	 * @param newSpectrumID
	 *            int
	 */
	public void setSpectrumID(int newSpectrumID) {
		spectrumID = newSpectrumID;
	}

	public boolean isValidating() {
		return isValidating;
	}

	public void setValidating(boolean useCheckValues) {
		this.isValidating = useCheckValues;
		this.asdfDecoder.enableValidation(useCheckValues);
	}
}