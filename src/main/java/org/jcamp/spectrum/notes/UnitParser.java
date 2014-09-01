package org.jcamp.spectrum.notes;

import org.jcamp.units.Unit;
/**
 * parser for units.
 * @author Thomas Weber
 */
public class UnitParser implements INoteContentParser {
    /**
     * UnitParser constructor comment.
     */
    public UnitParser() {
        super();
    }
    /**
     * parse the note content.
     * @return java.lang.Object
     * @param content java.lang.String
     * @param resultClass java.lang.Class
     */
    public Object parseContent(String content, Class resultClass) {
        if (Unit.class.isAssignableFrom(resultClass))
            return Unit.getUnitFromString(content);
        return null;
    }
}
