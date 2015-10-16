/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *****************************************************************************
 */
package org.jcamp.spectrum;

import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * UV/VIS spectra.
 *
 * @author Thomas Weber
 */
public class UVSpectrum
		extends OpticalSpectrum1D {

	/**
	 * for serialization.
	 */
	private static final long serialVersionUID = 6675975986023513801L;

	public final static Unit DEFAULT_XUNIT = CommonUnit.nanometerWavelength;

	public final static Unit DEFAULT_YUNIT = CommonUnit.intensity;

	/**
	 */
	protected UVSpectrum() {
		super();
	}

	/**
	 * standard ctor.
	 *
	 * @param x isFrozen.spectrum.IOrderedDataArray1D
	 * @param y isFrozen.spectrum.IDataArray1D
	 */
	public UVSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
		super(x, y);
	}

	/**
	 * standard ctor.
	 *
	 * @param x isFrozen.spectrum.IOrderedDataArray1D
	 * @param y isFrozen.spectrum.IDataArray1D
	 * @param fullSpectrum boolean
	 */
	public UVSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
		super(x, y, fullSpectrum);
	}

	/**
	 * gets spectrum ID.
	 *
	 * @return int
	 */
	@Override
	public int getIdentifier() {
		return ISpectrumIdentifier.UV;
	}
}
