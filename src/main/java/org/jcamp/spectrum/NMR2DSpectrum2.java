/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcamp.spectrum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jcamp.math.AxisMap;
import org.jcamp.math.IArray1D;
import org.jcamp.math.Range1D;
import org.jcamp.math.Range2D;
import org.jcamp.parser.JCAMPVariable;

/**
 *
 * @author fr
 */
public class NMR2DSpectrum2 extends Spectrum {

	protected final List<List<? extends Number>> zData = new ArrayList<List<? extends Number>>();
	protected String solvent = "TMS";
	protected final JCAMPVariable[] axes;
	protected final String[] nucleus;
	protected final double[] frequency;

	public NMR2DSpectrum2(JCAMPVariable[] axes, String[] nucleus, double[] frequency) {
		this.axes = axes;
		this.nucleus = nucleus;
		this.frequency = frequency;
	}

	@Override
	public boolean hasPeakTable() {
		return false;
	}

	@Override
	public Peak[] getPeakTable() {
		return null;
	}

	@Override
	public Object getXData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Object getYData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Range2D.Double getFullViewRange() {
		return new Range2D.Double(getXFullViewRange(), getYFullViewRange());
	}

	@Override
	public String getXAxisLabel() {
		return axes[0].getUnit().getSymbol();
	}

	public Range1D.Double getXFullViewRange() {
		return new Range1D.Double(axes[0].getMin(), axes[0].getMax());
	}

	@Override
	public AxisMap getXAxisMap() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getYAxisLabel() {
		return axes[1].getUnit().getSymbol();
	}

	public Range1D.Double getYFullViewRange() {
		return new Range1D.Double(axes[1].getMin(), axes[1].getMax());
	}

	@Override
	public AxisMap getYAxisMap() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isFullSpectrum() {
		return true;
	}

	public void addDataRow(IArray1D dataRow) {
		if (dataRow != null) {
			double[] zValues = dataRow.toArray();
			List<Double> zList = new ArrayList<Double>(zValues.length);
			for (int i = 0; i < zValues.length; i++) {
				zList.add(zValues[i]);
			}
			addDataRow(zList);
		}
	}

	public void addDataRow(Double[] dataRow) {
		addDataRow(Arrays.asList(dataRow));
	}

	public void addDataRow(Number[] dataRow) {
		addDataRow(Arrays.asList(dataRow));
	}

	public void addDataRow(List<? extends Number> dataRow) {
		zData.add(dataRow);
	}

	public List<? extends List<? extends Number>> getZData() {
		return zData;
	}

	public String getSolvent() {
		return solvent;
	}

	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}

	public String[] getNucleus() {
		return nucleus;
	}

	public double[] getFrequency() {
		return frequency;
	}

	public JCAMPVariable[] getAxes() {
		return axes;
	}
}
