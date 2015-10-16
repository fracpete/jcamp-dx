/**
 * *****************************************************************************
 * Copyright (c) 2015. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *****************************************************************************
 */
package org.jcamp.spectrum;

import org.jcamp.math.IInterval1D;
import org.jcamp.math.IrregularAxisMap;
import org.jcamp.math.IrregularGrid1D;
import org.jcamp.math.Range1D;

/**
 * Raman spectra.
 *
 * @author Thomas Weber
 */
public class RamanSpectrum
		extends OpticalSpectrum1D {

	/**
	 * for serialization.
	 */
	private static final long serialVersionUID = -6727363432637680666L;

	private final static double[] DEFAULT_WAVENUMBERS
			= new double[]{4000.0, 3500.0, 3000.0, 2500.0, 2000.0, 1800, 1600, 1400, 1200, 1000, 800, 600, 400};
	private final static IrregularGrid1D DEFAULT_XGRID = new IrregularGrid1D(DEFAULT_WAVENUMBERS);

	/**
	 */
	protected RamanSpectrum() {
		super();
	}

	/**
	 * standard ctor.
	 *
	 * @param x org.jcamp.spectrum.IOrderedDataArray1D
	 * @param y org.jcamp.spectrum.IDataArray1D
	 */
	public RamanSpectrum(IOrderedDataArray1D x, IDataArray1D y) {
		super(x, y);
	}

	/**
	 * standard ctor.
	 *
	 * @param x org.jcamp.spectrum.IOrderedDataArray1D
	 * @param y org.jcamp.spectrum.IDataArray1D
	 * @param fullSpectrum boolean
	 */
	public RamanSpectrum(IOrderedDataArray1D x, IDataArray1D y, boolean fullSpectrum) {
		super(x, y, fullSpectrum);
	}

	/**
	 * gets spectrum ID.
	 *
	 * @return int
	 */
	@Override
	public int getIdentifier() {
		return ISpectrumIdentifier.RAMAN;
	}

	/**
	 * sets full view range on x axis.
	 */
	@Override
	public void setXFullViewRange(Range1D.Double dataRange) {
		IrregularGrid1D newGrid = new IrregularGrid1D(DEFAULT_XGRID, dataRange);
		this.xAxisMap = new IrregularAxisMap((IInterval1D) getXData(), newGrid);
	}
}
