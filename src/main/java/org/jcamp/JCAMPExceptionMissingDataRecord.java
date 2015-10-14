package org.jcamp;

import org.jcamp.parser.JCAMPException;

public class JCAMPExceptionMissingDataRecord extends JCAMPException {

	/**
	 *
	 */
	private static final long serialVersionUID = -6502359150424254874L;

	public JCAMPExceptionMissingDataRecord() {
		super();

	}

	public JCAMPExceptionMissingDataRecord(String message) {
		super(message);

	}

	public JCAMPExceptionMissingDataRecord(String message, Throwable cause) {
		super(message, cause);

	}

	public JCAMPExceptionMissingDataRecord(Throwable cause) {
		super(cause);

	}

}
