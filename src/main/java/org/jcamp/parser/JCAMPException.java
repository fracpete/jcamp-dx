/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * ****************************************************************************
 */
package org.jcamp.parser;

/**
 * Exceptions thrown at JCAMP-DX parsing.
 *
 * @author Thomas Weber
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
public class JCAMPException
		extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7234910260603380490L;

	public JCAMPException() {
	}

	public JCAMPException(String message) {
		super(message);
	}

	public JCAMPException(String message, Throwable cause) {
		super(message, cause);
	}

	public JCAMPException(Throwable cause) {
		super(cause);
	}
}
