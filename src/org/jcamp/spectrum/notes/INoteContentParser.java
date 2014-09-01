package org.jcamp.spectrum.notes;

/**
 * interface for parsing note contents from String representation
 * @author Thomas Weber
 */
public interface INoteContentParser {
    /**
     * parse the note content.
     * @return java.lang.Object
     * @param content java.lang.String
     * @param resultClass java.lang.Class
     */
    Object parseContent(String content, Class resultClass) throws BadContentException;
}
