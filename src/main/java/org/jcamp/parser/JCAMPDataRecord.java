/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import java.util.ListIterator;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

/**
 * Class for reading JCAMP Labeled Data Records (LDR)s. JCAMPDataRecord objects
 * are constructed by the containing JCAMPBlock class.
 * 
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 * 
 * @see JCAMPBlock
 */
public class JCAMPDataRecord {

	public final static boolean DEFAULT_NORMALIZE_VALUE_STRING = false;

	private final static String XYDATA_RE = "^([:alpha:][:digit:]*)([:alpha:][:digit:]*)DATA$";
	private final static String XYPOINTS_RE = "^([:alpha:][:digit:]*)([:alpha:][:digit:]*)POINTS$";
	private final static REProgram XYDATA_REPRG;
	private final static REProgram XYPOINTS_REPRG;
	static {
		try {
			RECompiler compiler = new RECompiler();
			XYDATA_REPRG = compiler.compile(XYDATA_RE);
			XYPOINTS_REPRG = compiler.compile(XYPOINTS_RE);
		} catch (RESyntaxException e) {
			throw new RuntimeException("bad RE in DataVariableInfo");
		}
	}
	private RE xyDataRE = new RE(XYDATA_REPRG);
	private RE xyPointsRE = new RE(XYPOINTS_REPRG);
	// flag if data LDR
	private boolean isData = false;
	/*
	 * normalized key, e.g. "TITLE", parsed from
	 * "##TITLE=	S2015_1275_1.120.1.1r".
	 */
	private String key;
	// value of LDR
	private String value;
	private String content;
	// containing JCAMP string
	private String jcamp;
	// start offset
	private int start;
	// offset of '=' (end of key)
	private int equalSignPos;
	// end offset
	private int end;
	// index within list
	private int index;
	// absolute index within parent block
	private int blockIndex;
	// next JCAMPDataRecord with same key
	private JCAMPDataRecord next = null;
	// previous JCAMPDataRecord with same key
	private JCAMPDataRecord prev = null;

	private boolean normalizeValueString = true;

	public synchronized boolean isNormalizeValueString() {
		return normalizeValueString;
	}

	public synchronized void setNormalizeValueString(
			boolean normalizeValueString) {
		this.normalizeValueString = normalizeValueString;
	}

	@Override
	public String toString() {
		return "JCAMPDataRecord [isData=" + isData() + ", key=" + getKey()
				+ ", value=" + getValue(true) + ", blockIndex="
				+ getBlockIndex() + "]";
	}

	class Iterator implements ListIterator<JCAMPDataRecord> {
		JCAMPDataRecord current;

		Iterator(JCAMPDataRecord current) {
			this.current = current;
		}

		@Override
		public void add(JCAMPDataRecord o) {
			if (!(o instanceof JCAMPDataRecord))
				return;
			JCAMPDataRecord newLDR = o;
			JCAMPDataRecord cLDR = current;
			// find last in list
			while (cLDR.next != null)
				cLDR = cLDR.next;
			// append to last list item
			cLDR.next = newLDR;
			newLDR.prev = cLDR;
			newLDR.index = cLDR.index + 1;
		}

		@Override
		public boolean hasNext() {
			return current.next != null;
		}

		@Override
		public JCAMPDataRecord next() {
			return current.next;
		}

		@Override
		public int nextIndex() {
			if (current.next != null)
				return current.index + 1;
			else
				return -1;
		}

		@Override
		public boolean hasPrevious() {
			return current.prev != null;
		}

		@Override
		public JCAMPDataRecord previous() {
			return current.prev;
		}

		@Override
		public int previousIndex() {
			if (current.prev != null)
				return current.index - 1;
			else
				return -1;
		}

		@Override
		public void remove() {
			JCAMPDataRecord p = current.prev;
			JCAMPDataRecord n = current.next;
			if (p != null) {
				p.next = n;
			}
			if (n != null) {
				n.prev = p;
				n.index--;
				while (n.next != null) {
					n = n.next;
					n.index--;
				}
			}
		}

		@Override
		public void set(JCAMPDataRecord o) {
			if (current == null)
				return;
			if (!(o instanceof JCAMPDataRecord))
				return;
			JCAMPDataRecord s = o;
			JCAMPDataRecord p = current.prev;
			JCAMPDataRecord n = current.next;
			s.index = current.index;
			current.prev = current.next = null;
			current.index = 0;
			s.prev = s.next = null;
			current = s;
			if (p != null) {
				p.next = s;
				s.prev = p;
			}
			if (n != null) {
				n.prev = s;
				s.next = n;
			}
		}
	};

	JCAMPDataRecord(String jcamp, int start, int end, int blockIndex) {
		super();
		this.jcamp = jcamp;
		this.start = start;
		this.end = end;
		this.next = null;
		this.prev = null;
		this.index = 0;
		this.blockIndex = blockIndex;
		String label = jcamp.substring(start, end);
		this.equalSignPos = label.indexOf('=');

		/*
		 * Start at pos. 2, since label starts always with "##"
		 */
		this.key = Utils.normalizeLabel(label.substring(2, equalSignPos));

		// assign value not before first access, see #getValue.
		// this.value = label.substring(equalSignPos+1);
		if (xyDataRE.match(this.key) || xyPointsRE.match(this.key)
				|| this.key.equals("DATATABLE") || this.key.equals("PEAKTABLE")
				|| this.key.equals("PEAKASSIGNMENTS"))
			this.isData = true;
	}

	/**
	 * JCAMPLabel constructor comment.
	 */
	JCAMPDataRecord(ListIterator<JCAMPDataRecord> list, String jcamp,
			int start, int end, int blockIndex) {
		this(jcamp, start, end, blockIndex);
		list.add(this);
	}

	/**
	 * gets index of LDR within block.
	 * 
	 * @return int
	 */
	public int getBlockIndex() {
		return blockIndex;
	}

	/**
	 * Gets comments in data (all concatenated).
	 * 
	 * 
	 */
	public String getComments() {
		return Utils.extractComments(getValue());
	}

	/**
	 * Gets data content (value without comments).
	 * 
	 * 
	 */
	public synchronized String getContent() {
		if (content == null)
			content = Utils.removeComments(getValue()).trim();
		return content;
	}

	/**
	 * gets end position within JCAMP string.
	 * 
	 * @return int
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * gets index of first LDR with this key within block.
	 * 
	 * @return int
	 */
	public int getFirstBlockIndex() {
		JCAMPDataRecord d = prev;
		while (d != null)
			d = d.prev;
		// TODO: BUG
		return d.blockIndex;
	}

	/**
	 * gets normalized key.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getKey() {
		return key;
	}

	/**
	 * gets index within LDR list.
	 * 
	 * @return index int
	 */
	public int getListIndex() {
		return index;
	}

	/**
	 * gets original, not normalized key.
	 * 
	 * @return java.lang.String
	 */
	public String getOriginalKey() {
		return jcamp.substring(start + 2, start + equalSignPos);
	}

	/**
	 * gets start position within JCAMP string.
	 * 
	 * @return int
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Gets content of LDR.
	 * 
	 */
	public synchronized String getValue() {
		return getValue(isNormalizeValueString());
	}

	/**
	 * Gets content of LDR.
	 * 
	 */
	public synchronized String getValue(boolean normalized) {
		if (value == null) {
			value = jcamp.substring(start + equalSignPos + 1, end);
		}
		if (normalized) {
			value = getNormalizedValueString(value);
		}
		return value;
	}

	String getNormalizedValueString(String value) {
		return value.replace("\n", "").replace("\r", "");
	}

	/**
	 * indicate if LDR contains data.
	 * 
	 * @return boolean
	 */
	public boolean isData() {
		return isData;
	}

	/**
	 * gets iterator over multiple LDRs with same key.
	 * 
	 * @return Iterator
	 */
	public Iterator iterator() {
		return new JCAMPDataRecord.Iterator(this);
	}

	/**
	 * gets list iterator over LDRs with same key.
	 * 
	 * @return ListIterator
	 */
	public ListIterator<JCAMPDataRecord> listIterator() {
		return new JCAMPDataRecord.Iterator(this);
	}
}
