/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
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
