package org.jcamp.spectrum.notes;

/**
 * default note content parser that supports basic Java types and Date.
 * @author Thomas Weber
 */
public class DefaultNoteContentParser implements INoteContentParser {
    private final static DefaultNoteContentParser theInstance = new DefaultNoteContentParser();
    /**
     * DefaultNoteContentParser constructor comment.
     */
    private DefaultNoteContentParser() {
        super();
    }
    /**
     * Singleton instance accessor method.
     * @return com.creon.chem.spectrum.DefaultNoteContentParser
     */
    public final static DefaultNoteContentParser getInstance() {
        return theInstance;
    }
    /**
     * parse the note content.
     * @return java.lang.Object
     * @param content java.lang.String
     * @param resultClass java.lang.Class
     */
    public Object parseContent(String content, Class resultClass) throws BadContentException {
        if (String.class.equals(resultClass)) {
            return content;
        } else if (Integer.class.equals(resultClass)) {
            try {
                return new Integer(content);
            } catch (NumberFormatException ex) {
                throw new BadContentException("expecting integer but got: " + content);
            }
        } else if (Double.class.equals(resultClass)) {
            try {
                return new Double(content);
            } catch (NumberFormatException ex) {
                throw new BadContentException("expecting double but got: " + content);
            }
        } else if (Boolean.class.equals(resultClass)) {
            try {
                return new Boolean(content);
            } catch (Exception ex) {
                throw new BadContentException("expecting boolean (true/false) but got: " + content);
            }
        } else if (Float.class.equals(resultClass)) {
            try {
                return new Float(content);
            } catch (NumberFormatException ex) {
                throw new BadContentException("expecting float but got: " + content);
            }
        } else if (Byte.class.equals(resultClass)) {
            try {
                return new Byte(content);
            } catch (NumberFormatException ex) {
                throw new BadContentException("expecting byte but got: " + content);
            }
        }
        return content;
    }
}
