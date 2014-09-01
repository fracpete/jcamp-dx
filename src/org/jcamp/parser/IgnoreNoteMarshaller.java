package org.jcamp.parser;

/**
 * JCAMP mapping that ignores the entry.
 * @author Thomas Weber
 */
public class IgnoreNoteMarshaller implements IJCAMPNoteMarshaller {
    private String key;
    private String JCAMPlabel;
    /**
     * IgnoreMapping constructor comment.
     */
    public IgnoreNoteMarshaller() {
        super();
    }
    /**
     * IgnoreMapping constructor comment.
     */
    public IgnoreNoteMarshaller(String key) {
        this(key, key.toUpperCase());
    }
    /**
     * IgnoreMapping constructor comment.
     */
    public IgnoreNoteMarshaller(String key, String JCAMPLabel) {
        super();
        this.key = key;
        this.JCAMPlabel = JCAMPLabel;
    }
    /**
     * gets JCAMPLabel (normalized, without ## and =).
     * @return java.lang.String
     */
    public java.lang.String getJCAMPLabel() {
        return Utils.normalizeLabel(JCAMPlabel);
    }
    /**
     * set JCAMP LDR label (without ## and =).
     * 
     * @param key java.lang.String
     */
    public void setJCAMPLabel(String label) {
        this.JCAMPlabel = label;
    }
    /**
     * set hashkey.
     * 
     * @param key java.lang.String
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * gets JCAMP representation.
     * 
     * @return java.lang.String
     * @param value java.lang.Object
     */
    public String toJCAMP(Object value) {
        return "";
    }
}
