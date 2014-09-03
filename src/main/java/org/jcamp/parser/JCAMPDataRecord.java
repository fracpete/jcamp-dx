package org.jcamp.parser;

import java.util.ListIterator;

import org.apache.regexp.RE;
import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

/**
 * class for reading JCAMP Labeled Data Records (LDR)s.
 * JCAMPDataRecord objects are constructed by the containing JCAMPBlock class
 * 
 * @author Thomas Weber
 * @see JCAMPBlock
 */
public class JCAMPDataRecord {
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
      throw new Error("bad RE in DataVariableInfo");
    }
  }
  private RE xyDataRE = new RE(XYDATA_REPRG);
  private RE xyPointsRE = new RE(XYPOINTS_REPRG);
  // flag if data LDR
  private boolean isData = false;
  // normalized key	
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
  class Iterator implements ListIterator {
    JCAMPDataRecord current;
    Iterator(JCAMPDataRecord current) {
      this.current = current;
    }
    public void add(Object o) {
      if (!(o instanceof JCAMPDataRecord))
	return;
      JCAMPDataRecord newLDR = (JCAMPDataRecord) o;
      JCAMPDataRecord cLDR = current;
      // find last in list
      while (cLDR.next != null)
	cLDR = cLDR.next;
      // append to last list item	
      cLDR.next = newLDR;
      newLDR.prev = cLDR;
      newLDR.index = cLDR.index + 1;
    }
    public boolean hasNext() {
      return current.next != null;
    }
    public Object next() {
      return current.next;
    }
    public int nextIndex() {
      if (current.next != null)
	return current.index + 1;
      else
	return -1;
    }
    public boolean hasPrevious() {
      return current.prev != null;
    }
    public Object previous() {
      return current.prev;
    }
    public int previousIndex() {
      if (current.prev != null)
	return current.index - 1;
      else
	return -1;
    }
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
    public void set(Object o) {
      if (current == null)
	return;
      if (!(o instanceof JCAMPDataRecord))
	return;
      JCAMPDataRecord s = (JCAMPDataRecord) o;
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
  /**
   * JCAMPLabel constructor comment.
   */
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
    this.key = Utils.normalizeLabel(label.substring(2, equalSignPos));
    //	this.value = label.substring(equalSignPos+1);
    if (xyDataRE.match(this.key)
	|| xyPointsRE.match(this.key)
	|| this.key.equals("DATATABLE")
	|| this.key.equals("PEAKTABLE")
	|| this.key.equals("PEAKASSIGNMENTS"))
      this.isData = true;
  }
  /**
   * JCAMPLabel constructor comment.
   */
  JCAMPDataRecord(ListIterator list, String jcamp, int start, int end, int blockIndex) {
    this(jcamp, start, end, blockIndex);
    list.add(this);
  }
  /**
   * gets index of LDR within block.
   * @return int
   */
  public int getBlockIndex() {
    return blockIndex;
  }
  /**
   * gets comments in data (all concatenated).
   * 
   * @return java.lang.String
   */
  public String getComments() {
    return Utils.extractComments(getValue());
  }
  /**
   * gets data content (value without comments).
   * 
   * @return java.lang.String
   */
  public String getContent() {
    if (content == null)
      content = Utils.removeComments(getValue()).trim();
    return content;
  }
  /**
   * gets end position within JCAMP string.
   * @return int
   */
  public int getEnd() {
    return end;
  }
  /**
   * gets index of first LDR with this key within block.
   * @return int
   */
  public int getFirstBlockIndex() {
    JCAMPDataRecord d = prev;
    while (d != null)
      d = d.prev;

    return d.blockIndex;
  }
  /**
   * gets normalized key.
   * @return java.lang.String
   */
  public java.lang.String getKey() {
    return key;
  }
  /**
   * gets index within LDR list.
   * @return index int
   */
  public int getListIndex() {
    return index;
  }
  /**
   * gets original, not normalized key.
   * @return java.lang.String
   */
  public String getOriginalKey() {
    return jcamp.substring(start + 2, start + equalSignPos);
  }
  /**
   * gets start position within JCAMP string.
   * @return int
   */
  public int getStart() {
    return start;
  }
  /**
   * gets content of LDR.
   * @return java.lang.String
   */
  public java.lang.String getValue() {
    if (value == null) {
      value = jcamp.substring(start + equalSignPos + 1, end);
    }
    return value;
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
   * @return Iterator
   */
  public Iterator iterator() {
    return new JCAMPDataRecord.Iterator(this);
  }
  /**
   * gets list iterator over LDRs with same key.
   * @return ListIterator
   */
  public ListIterator listIterator() {
    return new JCAMPDataRecord.Iterator(this);
  }
}
