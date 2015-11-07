/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.DataException;
import org.jcamp.math.LinearGrid1D;
import org.jcamp.math.Range1D;
import org.jcamp.units.CommonUnit;
import org.jcamp.units.Unit;

/**
 * Implementation of IOrderedDataArray1D for equidistant incremental data
 * 
 * @author Thomas Weber
 */
public class EquidistantData implements IOrderedDataArray1D, IEquidistant {

	/** for serialization. */
	private static final long serialVersionUID = 293967635222847967L;

	private LinearGrid1D data;

	private Unit unit;

	private String label;

	public EquidistantData(double first, double last, int n, Unit unit) {
		super();
		this.data = new LinearGrid1D(first, last, n);
		if (unit != null)
			this.unit = unit;
		else
			this.unit = CommonUnit.generic;
	}

	public EquidistantData(LinearGrid1D data) {
		this(data, CommonUnit.generic);
	}

	public EquidistantData(LinearGrid1D data, Unit unit) {
		super();
		this.data = data;
		if (unit != null)
			this.unit = unit;
		else
			this.unit = CommonUnit.generic;
	}

	@Override
	public int[] boundIndices(double position)
			throws org.jcamp.math.DataException {
		Range1D.Double r = data.getRange1D();
		int max = data.getLength() - 1;
		if (!r.contains(position))
			throw new DataException("data point out of data range");
		double c = data.coordinateAt(position);
		if (isAscending()) {
			int c0 = Math.max(0, (int) Math.floor(c));
			int c1 = Math.min(max, (int) Math.ceil(c));
			return new int[] { c0, c1 };
		} else {
			int c0 = Math.min(max, (int) Math.ceil(c));
			int c1 = Math.max(0, (int) Math.floor(c));
			return new int[] { c0, c1 };
		}
	}

	@Override
	public org.jcamp.math.Range1D.Double bounds(double position)
			throws org.jcamp.math.DataException {
		int[] bi = boundIndices(position);
		return new Range1D.Double(data.pointAt(bi[0]), data.pointAt(bi[1]));
	}

	@Override
	public double ceil(double position) throws org.jcamp.math.DataException {
		return data.pointAt(ceilIndex(position));
	}

	@Override
	public int ceilIndex(double position) throws org.jcamp.math.DataException {
		double c = data.coordinateAt(position);
		Range1D.Double r = data.getRange1D();
		if (position > r.getXMax())
			throw new DataException("no ceiling data point for position");
		int max = data.getLength() - 1;
		if (isAscending()) {
			if (position < r.getXMin())
				return 0;
			else
				return Math.min(max, (int) Math.ceil(c));
		} else {
			if (position < r.getXMin())
				return max;
			else
				return Math.max(0, (int) Math.floor(c));
		}
	}

	@Override
	public EquidistantData clone() {
		EquidistantData o = null;
		try {
			o = (EquidistantData) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		o.data = (LinearGrid1D) this.data.clone();
		o.unit = (Unit) this.unit.clone();
		return o;
	}

	@Override
	public void convertToUnit(org.jcamp.units.Unit newUnit)
			throws org.jcamp.units.UnitException {
		double newStart = newUnit.convertFrom(this.data.getStart(), this.unit);
		double newEnd = newUnit.convertFrom(this.data.getEnd(), this.unit);
		this.data = new LinearGrid1D(newStart, newEnd, data.getLength());
	}

	@Override
	public double floor(double position) throws org.jcamp.math.DataException {
		return data.pointAt(floorIndex(position));
	}

	@Override
	public int floorIndex(double position) throws org.jcamp.math.DataException {
		double c = data.coordinateAt(position);
		Range1D.Double r = data.getRange1D();
		int max = data.getLength() - 1;
		if (position < r.getXMin())
			throw new DataException("no floor data point for position");
		if (isAscending()) {
			if (position > r.getXMax())
				return max;
			else
				return Math.max(0, (int) Math.floor(c));
		} else {
			if (position > r.getXMax())
				return 0;
			else
				return Math.min(max, (int) Math.ceil(c));
		}
	}

	@Override
	public LinearGrid1D getDataGrid() {
		return data;
	}

	@Override
	public String getLabel() {
		if (label == null)
			return unit.toString();
		return label;
	}

	@Override
	public int getLength() {
		return data.getLength();
	}

	@Override
	public org.jcamp.math.Range.Double getRange() {
		return data.getRange();
	}

	@Override
	public org.jcamp.math.Range1D.Double getRange1D() {
		return data.getRange1D();
	}

	@Override
	public org.jcamp.units.Unit getUnit() {
		return this.unit;
	}

	@Override
	public int indexAt(double p) {
		return data.indexAt(p);
	}

	@Override
	public boolean isAscending() {
		return data.isAscending();
	}

	@Override
	public double pointAt(int index) {
		return data.pointAt(index);
	}

	@Override
	public void scale(double amount) {
		data.scale(amount);
	}

	@Override
	public void setLabel(String newLabel) {
		label = newLabel;
	}

	@Override
	public void setUnit(org.jcamp.units.Unit newUnit) {
		unit = newUnit;
	}

	@Override
	public double[] toArray() {
		return data.toArray();
	}

	@Override
	public String toString() {
		return "EquidistantData [data points=" + data.getLength() + ", unit="
				+ unit + ", label=" + label + "]";
	}

	@Override
	public void translate(double amount) {
		data.translate(amount);
	}
}
