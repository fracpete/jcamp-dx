/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

import org.jcamp.spectrum.Spectrum;
/**
 * writer for spectrums as JCAMP
 * @author Thomas Weber
 */
public interface ISpectrumJCAMPWriter {
    String toJCAMP(Spectrum spectrum) throws JCAMPException;
    String toSimpleJCAMP(Spectrum spectrum) throws JCAMPException;
}
