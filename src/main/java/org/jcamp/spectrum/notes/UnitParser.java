/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
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
     * @param content String
     * @param resultClass java.lang.Class
     */
    public Object parseContent(String content, Class resultClass) {
        if (Unit.class.isAssignableFrom(resultClass))
            return Unit.getUnitFromString(content);
        return null;
    }
}
