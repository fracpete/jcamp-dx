/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import net.sf.kerner.utils.collections.trasformer.ToString;
import net.sf.kerner.utils.collections.trasformer.TransformerEnumerationToIterable;

import org.jcamp.spectrum.ISpectrumIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public class JCAMPBlock {

	private final static String CRLF = "\r\n";

	private final static Logger lg = LoggerFactory.getLogger(JCAMPBlock.class);

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

	private ASDFDecoder asdfDecoder = new ASDFDecoder();

	private BlockType blockType;
	/*
	 * A {@link java.util.Map map} storing references to all child blocks. Key
	 * is a block's ID (e.g. {@code ##BLOCKID= 1}).
	 */
	private Map<Integer, JCAMPBlock> childBlocks = new LinkedHashMap<Integer, JCAMPBlock>(
			10);
	private String data;

	// hashtable containing all data records (or list of data records for
	// multiple records with same key)
	private Hashtable<String, JCAMPDataRecord> dataRecords = new Hashtable<String, JCAMPDataRecord>(
			50);

	private final int end;
	private boolean isValidating = true;
	/*
	 * JCAMP file content.
	 */
	private final String jcamp;
	// array of data records with duplicate keys
	private JCAMPDataRecord[] ldrs;

	private JCAMPNTuple ntuple;
	private boolean ntupleBlock = false;
	private int numDataRecords;
	/*
	 * If this {@code JCAMPBlock} is part of a multi-block file, {@code parent}
	 * is the reference to the first block in this file. If this {@code
	 * JCAMPBlock} is the first block in a multi-block file, {@code parent} is
	 * {@code null}.
	 */
	private final JCAMPBlock parent;
	private JCAMPBlock[] references = null;
	private int spectrumType;
	private final int start;
	private JCAMPVariable[] vars = null;

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
	 * Create a {@code JCAMPBlock} from a JCAMP substring.
	 *
	 * @param jcamp
	 *            string representing a JCAMP file
	 * @param start
	 *            first index of substring, inclusive
	 * @param end
	 *            last index of substring, exclusive
	 * @param parent
	 *            parent {@code JCAMPBlock}
	 * @see java.lang.String#substring(int, int)
	 */
	public JCAMPBlock(JCAMPBlock parent, String jcamp, int start, int end,
			IErrorHandler errorHandler) throws JCAMPException {
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
	 * create JCAMPBlock from String
	 *
	 * @param jcamp
	 *            java.lang.String
	 */
	public JCAMPBlock(String jcamp) throws JCAMPException {
		this(jcamp, 0, jcamp.length());
	}

	/**
	 * Create JCAMPBlock from a JCAMP string.
	 *
	 * @param jcamp
	 *            JCAMP string
	 */
	public JCAMPBlock(String jcamp, IErrorHandler errorHandler)
			throws JCAMPException {
		this(jcamp, 0, jcamp.length(), errorHandler);
	}

	/**
	 * create JCAMPBlock from substring.
	 */
	public JCAMPBlock(String jcamp, int start, int end) throws JCAMPException {
		this(null, jcamp, start, end);
	}

	/**
	 * Create a {@code JCAMPBlock} from a JCAMP substring.
	 *
	 * @param jcamp
	 *            string representing a JCAMP file
	 * @param start
	 *            first index of substring, inclusive
	 * @param end
	 *            last index of substring, exclusive
	 * @see java.lang.String#substring(int, int)
	 */
	public JCAMPBlock(String jcamp, int start, int end,
			IErrorHandler errorHandler) throws JCAMPException {
		this(null, jcamp, start, end, errorHandler);
	}

	/**
	 * analyze data records for type of block.
	 */
	private void analyzeBlockType() throws JCAMPException {
		JCAMPDataRecord ldrJCAMPCS = getDataRecord("JCAMPCS");
		if (ldrJCAMPCS != null)
			this.blockType = BlockType.STRUCTURE;
		else {
			JCAMPDataRecord ldrDataType = getDataRecord("DATATYPE");
			if (ldrDataType == null)
				throw new JCAMPException("missing ##DATATYPE=");
			String dtype = ldrDataType.getContent().toUpperCase();
			if (dtype.indexOf("LINK") >= 0) {
				this.blockType = BlockType.LINK;
			} else if (dtype.indexOf("TABLE") >= 0) {
				this.blockType = BlockType.PEAKTABLE;
				analyzeSpectrumID(dtype);
			} else if (dtype.indexOf("ASSIGNMENT") >= 0) {
				this.blockType = BlockType.ASSIGNMENT;
				analyzeSpectrumID(dtype);
			} else {
				this.blockType = BlockType.FULLSPECTRUM;
				analyzeSpectrumID(dtype);
				if (spectrumType == ISpectrumIdentifier.MASS
						&& dtype.indexOf("CONTINUOUS") < 0)
					this.blockType = BlockType.PEAKTABLE;
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
				spectrumType = ISpectrumIdentifier.NMR;
			} else if (dataType.indexOf("MASS") > -1) {
				spectrumType = ISpectrumIdentifier.MASS;
			} else if (dataType.indexOf("INFRARED") > -1) {
				spectrumType = ISpectrumIdentifier.IR;
			} else if (dataType.indexOf("IR") > -1) {
				spectrumType = ISpectrumIdentifier.IR;
			} else if (dataType.indexOf("UV") > -1) {
				spectrumType = ISpectrumIdentifier.UV;
			} else if (dataType.indexOf("ULTRAVIOLET") > -1) {
				spectrumType = ISpectrumIdentifier.UV;
			} else if (dataType.indexOf("RAMAN") > -1) {
				spectrumType = ISpectrumIdentifier.RAMAN;
			} else if (dataType.indexOf("FLUORESCENCE") > -1) {
				spectrumType = ISpectrumIdentifier.FLUORESCENCE;
			} else if (dataType.indexOf("CHROMATOGRAM") > -1) {
				spectrumType = ISpectrumIdentifier.CHROMATOGRAM;
			}
		}
	}

	/**
	 * Gets block as a simple JCAMP string
	 *
	 *
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
			if (lg.isErrorEnabled()) {
				lg.error("LDR \"##" + key + "=\" not found in block");
			}
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

	public BlockType getBlockType() {
		return this.blockType;
	}

	/**
	 * Returns a direct reference to {@code this} child blocks.
	 *
	 * @return a direct reference to {@code this} child blocks
	 */
	public Collection<JCAMPBlock> getChildBlocks() {
		return this.childBlocks.values();
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
	public Enumeration<JCAMPDataRecord> getDataRecords() {
		return this.dataRecords.elements();
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
			Vector<JCAMPBlock> refs = new Vector<JCAMPBlock>();
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
				this.references[i] = refs.elementAt(i);
			}
		}
		return this.references;
	}

	public int getSpectrumType() {
		return spectrumType;
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
	 * Finds child blocks within <code>start</code> and <code>end</code> of
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
			if (lg.isDebugEnabled()) {
				lg.debug("New block " + jcampBlock);
			}
			childBlocks.put(new Integer(id), jcampBlock);
		}
		tmp.append(jcamp.substring(o0, this.end));
		this.data = tmp.toString();
	}

	/**
	 * Iterates over all data records and stores them in {@link #ldrs}.
	 * <code>dataRecords</code>
	 */
	private void initLDRs() {
		IStringIterator ldrIter = new LDRIterator(data);
		int blockIndex = 0;
		ArrayList<JCAMPDataRecord> tmp = new ArrayList<JCAMPDataRecord>();
		while (ldrIter.hasNext()) {
			int offset = ldrIter.getOffset();
			String ldr = ldrIter.next();
			JCAMPDataRecord dataRecord = new JCAMPDataRecord(this.data, offset,
					offset + ldr.length(), blockIndex);
			if (lg.isDebugEnabled()) {
				lg.debug("New data record: " + dataRecord);
			}
			JCAMPDataRecord ldrList = this.dataRecords.get(dataRecord.getKey());
			if (ldrList == null) {
				this.dataRecords.put(dataRecord.getKey(), dataRecord);
			} else {
				ldrList.listIterator().add(dataRecord);
			}
			tmp.add(dataRecord);
			blockIndex++;
		}
		if (blockIndex != tmp.size()) {
			throw new RuntimeException();
		}
		this.numDataRecords = blockIndex;
		if (lg.isDebugEnabled()) {
			lg.debug("numDataRecords=" + numDataRecords);
		}
		this.ldrs = new JCAMPDataRecord[tmp.size()];
		for (int i = 0; i < tmp.size(); i++)
			this.ldrs[i] = tmp.get(i);
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
				if (lg.isErrorEnabled()) {
					lg.error("missing ##NTUPLES=");
				}

			}
			if (getDataRecord("VARDIM") != null) {
				// assume NTUPLE block, but treat as error
				this.ntupleBlock = true;
				if (lg.isErrorEnabled()) {
					lg.error("missing ##NTUPLES=");
				}
			}
			if (!this.ntupleBlock)
				return;
		}
		JCAMPDataRecord endNTupleLDR = getDataRecord("ENDNTUPLES");
		if (endNTupleLDR == null) {
			if (lg.isErrorEnabled()) {
				lg.error("missing ##END NTUPLES=");
			}
		}
		this.ntuple = new JCAMPNTuple(this, startNTupleLDR, endNTupleLDR);
		this.ntupleBlock = true;
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
		Enumeration<JCAMPDataRecord> records = getDataRecords();
		while (records.hasMoreElements()) {
			JCAMPDataRecord ldr = records.nextElement();
			if (ldr.isData()) {
				if (dataLDR != null) {
					if (lg.isErrorEnabled()) {
						lg.error("more than one data LDR in block: use compound JCAMP");
					}
					break; // use first data block encountered
				} else
					dataLDR = ldr;
			}
		}
		if (dataLDR == null) {
			throw new JCAMPException("missing data LDR");
		}
		DataVariableInfo info = new DataVariableInfo(dataLDR);
		String[] symbols = info.getSymbols();
		JCAMPDataRecord ldr = getDataRecord("NPOINTS");
		if (ldr == null)
			throw new JCAMPException("missing required label ##NPOINTS=");
		int nPoints = Integer.parseInt(ldr.getContent());
		vars = new JCAMPVariable[symbols.length];
		for (int i = 0; i < symbols.length; i++) {
			String symbol = symbols[i].toUpperCase();
			JCAMPVariable v = new JCAMPVariable(symbol);
			if (i > 0)
				v.setType(JCAMPVariable.Type.DEPENDENT);
			else
				v.setType(JCAMPVariable.Type.INDEPENDENT);
			if (getBlockType().equals(BlockType.FULLSPECTRUM)) {
				if (info.isIncremental())
					v.setFormat(JCAMPVariable.Format.ASDF);
				else
					v.setFormat(JCAMPVariable.Format.AFFN);
			} else if (getBlockType().equals(BlockType.PEAKTABLE)
					|| getBlockType().equals(BlockType.ASSIGNMENT)) {
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
	 * Checks if block is a JCAMP link block.
	 *
	 */
	public boolean isLinkBlock() {
		return this.blockType.equals(BlockType.LINK);
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
		return this.blockType.equals(BlockType.STRUCTURE);
	}

	public boolean isValidating() {
		return isValidating;
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
	 * sets ASDFDecoder
	 *
	 * @param newAsdfDecoder
	 *            com.creon.chem.jcamp.ASDFDecoder
	 */
	public void setASDFDecoder(ASDFDecoder newAsdfDecoder) {
		asdfDecoder = newAsdfDecoder;
	}

	public void setSpectrumType(int newSpectrumID) {
		spectrumType = newSpectrumID;
	}

	public void setValidating(boolean useCheckValues) {
		this.isValidating = useCheckValues;
		this.asdfDecoder.enableValidation(useCheckValues);
	}

	@Override
	public String toString() {
		return "JCAMPBlock,SpecID="
				+ JCAMPReader.findAdapter(spectrumType)
				+ ", title="
				+ getDataRecord("TITLE").getValue(true)
				+ ", dataRecords="
				+ new ToString()
						.toString(new TransformerEnumerationToIterable<String>()
								.transform(dataRecords.keys()));
	}
}
