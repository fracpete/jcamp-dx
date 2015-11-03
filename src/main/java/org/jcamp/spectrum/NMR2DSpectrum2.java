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
import org.jcamp.parser.JCAMPReader;
import org.jcamp.parser.JCAMPVariable;
import org.jcamp.units.CommonUnit;

/**
 *
 * @author fr
 */
public class NMR2DSpectrum2 extends Spectrum implements INMRSpectrum {

	public static final int DIMENSION = 2;
	protected final List<List<? extends Number>> zData = new ArrayList<List<? extends Number>>();
	protected String solvent = "TMS";
	protected Double temperature; // in K
	private String mode = JCAMPReader.STRICT;
	protected final IOrderedDataArray1D xData;
	protected final IOrderedDataArray1D yData;
	protected final String[] nucleus;
	protected final double[] frequency;
	protected double[] reference;

	public NMR2DSpectrum2(JCAMPVariable[] axes,
			String[] nucleus, double[] freq, String mode) {
		this(axes, nucleus, freq, new double[]{0.0, 0.0}, mode);
	}

	public NMR2DSpectrum2(JCAMPVariable[] axes,
			String[] nucleus, double[] freq, double[] ref, String mode) {
		// error checking
		if (axes.length != DIMENSION) {
			throw new IllegalArgumentException("axes length must be exactly " + DIMENSION);
		}
		if (nucleus.length != DIMENSION) {
			throw new IllegalArgumentException("nucleus length must be exactly " + DIMENSION);
		}
		if (freq.length != DIMENSION) {
			throw new IllegalArgumentException("frequency length must be exactly " + DIMENSION);
		}
		if (ref.length != DIMENSION) {
			throw new IllegalArgumentException("reference length must be exactly " + DIMENSION);
		}

		// set values
		this.xData = new EquidistantData(axes[0].getFirst(), axes[0].getLast(),
				axes[0].getDimension(), axes[0].getUnit());
		this.yData = new EquidistantData(axes[1].getFirst(), axes[1].getLast(),
				axes[1].getDimension(), axes[1].getUnit());
		this.nucleus = nucleus;
		this.frequency = freq;
		this.reference = ref;
		this.mode = mode;

		// make ppm if required
		convertToPPM(xData, frequency[0], reference[0]);
		convertToPPM(yData, frequency[1], reference[1]);
	}

	protected void convertToPPM(IOrderedDataArray1D data, double freq, double ref) {
		if (freq != freq
				|| ref != ref) {
			return;
		}
		if (data.getUnit().equals(CommonUnit.hertz)) {
			// ppm = (hz - ref) / freq;
			if (JCAMPReader.STRICT.equals(mode) && ref == ref) {
				data.translate(-ref);
			}
			data.scale(1. / freq);
			data.setUnit(CommonUnit.ppm);
		}
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
		return xData;
	}

	@Override
	public Object getYData() {
		return yData;
	}

	@Override
	public String getXAxisLabel() {
		return xData.getUnit().getSymbol();
	}

	public Range1D.Double getXFullViewRange() {
		return xData.getRange1D();
	}

	@Override
	public AxisMap getXAxisMap() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getYAxisLabel() {
		return yData.getUnit().getSymbol();
	}

	public Range1D.Double getYFullViewRange() {
		return xData.getRange1D();
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

	@Override
	public String getSolvent() {
		return solvent;
	}

	@Override
	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}

	@Override
	public Double getTemperature() {
		return temperature;
	}

	@Override
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public String[] getNucleus() {
		return nucleus;
	}

	public double[] getFrequency() {
		return frequency;
	}
}
