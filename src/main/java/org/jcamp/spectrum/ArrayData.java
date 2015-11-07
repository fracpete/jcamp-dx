/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.Array1D;
import org.jcamp.math.IArray1D;
import org.jcamp.math.Range;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;
import org.jcamp.units.UnitException;

/**
 * Combination of a discrete interval with a physical unit.
 * 
 * @author Thomas Weber
 */
public class ArrayData implements IDataArray1D {

	/** for serialization. */
	private static final long serialVersionUID = -4103255291717042724L;

	private Array1D data;

	private Unit unit;

	private String label;

	public ArrayData(double[] data, Unit unit) {
		this.data = new Array1D(data, false);
		if (unit != null)
			this.unit = unit;
		else
			this.unit = CommonUnit.generic;
	}

	public ArrayData(IArray1D data) {
		this(data, CommonUnit.generic);
	}

	public ArrayData(IArray1D data, Unit unit) {
		this.data = new Array1D(data);
		if (unit != null)
			this.unit = unit;
		else
			this.unit = CommonUnit.generic;
	}

	@Override
	public ArrayData clone() {
		ArrayData o = null;
		try {
			o = (ArrayData) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		o.data = (Array1D) this.data.clone();
		o.unit = (Unit) this.unit.clone();
		return o;
	}

	/**
	 * convert data to new unit.
	 * 
	 * @param newUnit
	 *            Unit NOTE: original data is lost and new data is an expensive
	 *            array.
	 */
	@Override
	public void convertToUnit(Unit newUnit) throws UnitException {
		double[] array = new double[data.getLength()];
		for (int i = 0; i < data.getLength(); i++)
			array[i] = newUnit.convertFrom(data.pointAt(i), unit);
		setData(new Array1D(array, false));
		setUnit(newUnit);
	}

	/**
	 * gets data (measured data is discontinuous).
	 * 
	 * @return IArray1D
	 */
	public IArray1D getData() {
		return data;
	}

	@Override
	public String getLabel() {
		if (label == null)
			return unit.toString();
		return label;
	}

	/**
	 * gets number of data points.
	 */
	@Override
	public int getLength() {
		return data.getLength();
	}

	/**
	 * get data range.
	 * 
	 * @return Range.Double
	 */
	@Override
	public Range.Double getRange() {
		return data.getRange();
	}

	/**
	 * gets data range.
	 * 
	 * @return Range1D.Double
	 */
	@Override
	public Range1D.Double getRange1D() {
		return data.getRange1D();
	}

	/**
	 * gets the data unit.
	 * 
	 * @return Unit
	 */
	@Override
	public Unit getUnit() {
		return unit;
	}

	/**
	 * gets data point at index.
	 */
	@Override
	public double pointAt(int index) {
		return data.pointAt(index);
	}

	@Override
	public void scale(double amount) {
		this.data.scale(amount);
	}

	@Override
	public String toString() {
		return "ArrayData [data points=" + data.getLength() + ", unit=" + unit
				+ ", label=" + label + "]";
	}

	/**
	 * sets the data.
	 * 
	 * @param newData
	 *            Array1D
	 */
	void setData(Array1D newData) {
		data = newData;
	}

	@Override
	public void setLabel(String newLabel) {
		label = newLabel;
	}

	/**
	 * sets the unit for the data.
	 * 
	 * @param newUnit
	 *            Unit
	 */
	@Override
	public void setUnit(Unit newUnit) {
		unit = newUnit;
	}

	@Override
	public double[] toArray() {
		return data.toArray();
	}

	@Override
	public void translate(double amount) {
		data.translate(amount);
	}
}
