package org.jcamp.parser;

/**
 * JCAMP mapping that writes the entry as a JCAMP comment.
 * @author Thomas Weber
 */
public class CommentNoteMarshaller implements IJCAMPNoteMarshaller {
    private String key;
    /**
     * CommentMapping constructor comment.
     */
    public CommentNoteMarshaller() {
        super();
    }
    /**
     * CommentMapping constructor comment.
     */
    public CommentNoteMarshaller(String key, String jcampLabel) {
        super();
        this.key = key;
    }
    /**
     * gets JCAMPLabel (normalized, without ## and =).
     * @return java.lang.String
     */
    public java.lang.String getJCAMPLabel() {
        return "";
    }
    /**
     * set JCAMP LDR label (without ## and =).
     * 
     * @param key java.lang.String
     */
    public void setJCAMPLabel(String key) {
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
     * marshalling as JCAMP comment.
     * 
     * @return java.lang.String
     * @param value java.lang.Object
     */
    public String toJCAMP(Object value) {
        StringBuffer label = new StringBuffer("##=");
        label.append(value.toString());
        int len = label.length();
        int line = len / 75;
        for (int i = 0; i < line; i++)
            label.insert(75 + (i * 78), "=\r\n");
        label.append("\r\n");
        return label.toString();
    }
}
