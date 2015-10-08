/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.spectrum;

import org.jcamp.math.AxisMap;
import org.jcamp.math.DataException;
import org.jcamp.math.LinearAxisMap;
import org.jcamp.math.LinearGrid2D;
import org.jcamp.math.Range1D;
import org.jcamp.math.Range3D;
import org.jcamp.math.RectangularGrid2D;

/**
 * 
 * 
 * @author Thomas Weber
 * 
 * @author <a href="mailto:alexander.kerner@silico-sciences.com">Alexander
 *         Kerner</a>
 */
abstract public class Spectrum2D extends Spectrum {

	/** for serialization. */

	private static final long serialVersionUID = 8483858856131296895L;
	protected IOrderedDataArray1D yData;
	protected IOrderedDataArray1D xData;
	protected IDataArray1D zData;
	protected AxisMap xAxisMap;
	protected AxisMap yAxisMap;
	protected AxisMap zAxisMap;
	protected boolean fullSpectrum = true;
	private LinearGrid2D xyGrid = null;

	/**
	 * standard Spectrum2D ctor.
	 */
	protected Spectrum2D(IOrderedDataArray1D x, IOrderedDataArray1D y,
			IDataArray1D z) {
		super();
		this.xData = x;
		this.yData = y;
		this.zData = z;
		this.fullSpectrum = true;
		if (x instanceof IEquidistant && y instanceof IEquidistant)
			xyGrid = new LinearGrid2D(((IEquidistant) x).getDataGrid(),
					((IEquidistant) y).getDataGrid());
	}

	/**
	 * cloning.
	 * 
	 * @return java.lang.Object
	 */
	@Override
	public Object clone() {
		return super.clone();
	}

	/**
	 * gets data range.
	 */
	public Range3D.Double getDataRange() {
		return new Range3D.Double(xAxisMap.getDataRange(),
				yAxisMap.getDataRange(), zAxisMap.getDataRange());
	}

	@Override
	public boolean hasPeakTable() {
		return false;
	}

	/**
	 * gets label on x-axis.
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String getXAxisLabel() {
		if (xData != null)
			return xData.getLabel();
		else
			return "";
	}

	/**
	 * gets mapping to x axis.
	 * 
	 * @return AxisMap
	 */
	@Override
	public AxisMap getXAxisMap() {
		return xAxisMap;
	}

	/**
	 * gets x data.
	 * 
	 * @return com.creon.chem.spectrum.IOrderedDataArray1D
	 */
	public IOrderedDataArray1D getXData() {
		return xData;
	}

	/**
	 * gets full view range on x axis.
	 */
	public Range1D.Double getXFullViewRange() {
		return new Range1D.Double(xAxisMap.getFullViewRange());
	}

	/**
	 * gets label on y-axis.
	 * 
	 * @return java.lang.String
	 */
	@Override
	public String getYAxisLabel() {
		if (yData != null)
			return yData.getLabel();
		else
			return "";
	}

	/**
	 * gets mapping to y axis.
	 * 
	 * @return AxisMap
	 */
	@Override
	public AxisMap getYAxisMap() {
		return yAxisMap;
	}

	/**
	 * gets y data.
	 * 
	 * @return com.creon.chem.spectrum.IOrderedDataArray1D
	 */
	public IOrderedDataArray1D getYData() {
		return yData;
	}

	/**
	 * gets full view range on y axis.
	 */
	public Range1D.Double getYFullViewRange() {
		return new Range1D.Double(yAxisMap.getFullViewRange());
	}

	/**
	 * gets label on z-axis.
	 * 
	 * @return java.lang.String
	 */
	public String getZAxisLabel() {
		if (zData != null)
			return zData.getLabel();
		else
			return "";
	}

	/**
	 * gets mapping to z axis.
	 * 
	 * @return AxisMap
	 */
	public AxisMap getZAxisMap() {
		return zAxisMap;
	}

	/**
	 * gets z data.
	 * 
	 * @return com.creon.chem.spectrum.IDataArray1D
	 */
	public IDataArray1D getZData() {
		return zData;
	}

	/**
	 * gets full view range on z axis.
	 */
	public Range1D.Double getZFullViewRange() {
		return new Range1D.Double(zAxisMap.getFullViewRange());
	}

	/**
	 * interpolate spectrum z-value for given spectrum xy coordinate.
	 * 
	 * @param x
	 *            double
	 * @param y
	 *            double
	 * @return double interpolated height
	 */
	public double interpolateZ(double xvalue, double yvalue) {
		if (xyGrid != null)
			return interpolateZGrid(xvalue, yvalue);
		else
			return interpolateZArray(xvalue, yvalue);
	}

	/**
	 * interpolate spectrum z-value for given spectrum xy coordinate.
	 * 
	 * @param x
	 *            double
	 * @param y
	 *            double
	 * @return double interpolated height
	 */
	private double interpolateZArray(double xvalue, double yvalue) {
		// slow algo for xy arrays
		final double EPS = 1E-10;
		int lengthX = xData.getLength();

		int[] bx;
		int[] by;
		try {
			bx = xData.boundIndices(xvalue);
			by = yData.boundIndices(yvalue);
		} catch (DataException e) {
			return 0.0;
		}
		;
		double x0, x1, y0, y1, dx, dy;
		if (bx[0] == bx[1]) {
			if (by[0] == by[1]) // on grid!
				return zData.pointAt(by[0] * lengthX + bx[0]);
			// calculate interpolation weights
			x0 = xData.pointAt(bx[0]);
			x1 = x0;
			dx = 0.;
			y1 = yData.pointAt(by[1]);
			y0 = yData.pointAt(by[0]);
			if (yvalue - y0 > EPS)
				if (y1 - yvalue > EPS)
					dy = Math.max(0., Math.min(1., (yvalue - y0) / (y1 - y0)));
				else
					dy = 1.0;
			else
				dy = 0.;
		} else {
			x0 = xData.pointAt(bx[0]);
			x1 = xData.pointAt(bx[1]);

			if (xvalue - x0 > EPS)
				if (x1 - xvalue > EPS)
					dx = Math.max(0., Math.min(1., (xvalue - x0) / (x1 - x0)));
				else
					dx = 1.0;
			// min max to avoid rounding errors
			else
				dx = 0.;
			if (by[0] == by[1]) {
				y0 = yData.pointAt(by[0]);
				y1 = y0;
				dy = 0;
			} else {
				y0 = yData.pointAt(by[0]);
				y1 = yData.pointAt(by[1]);
				if (yvalue - y0 > EPS)
					if (y1 - yvalue > EPS)
						dy = Math.max(0.,
								Math.min(1., (yvalue - y0) / (y1 - y0)));
					else
						dy = 1.0;
				else
					dy = 0.;
			}
		}
		if (dx == 0) {
			if (dy == 0)
				return zData.pointAt(by[0] * lengthX + bx[0]);
			else if (dy == 1)
				return zData.pointAt(by[1] * lengthX + bx[0]);
		} else if (dx == 1) {
			if (dy == 0)
				return zData.pointAt(by[0] * lengthX + bx[1]);
			else if (dy == 1)
				return zData.pointAt(by[1] * lengthX + bx[1]);
		}
		// bounding rectangle:
		int lb = by[0] * lengthX + bx[0];
		int rb = by[0] * lengthX + bx[1];
		int lt = by[1] * lengthX + bx[0];
		int rt = by[1] * lengthX + bx[1];
		double zlb = zData.pointAt(lb);
		double zrb = zData.pointAt(rb);
		double zlt = zData.pointAt(lt);
		double zrt = zData.pointAt(rt);
		double zvalue;
		if (dx + dy - 1 < 0) {
			zvalue = zlb + dx * (zrb - zlb) + dy * (zlt - zlb);
		} else {
			dx = 1.0 - dx;
			dy = 1.0 - dy;
			zvalue = zrt + dx * (zlt - zrt) + dy * (zrb - zrt);
		}
		return zvalue;
	}

	/**
	 * interpolate spectrum z-value for given spectrum xy coordinate.
	 * 
	 * @param x
	 *            double
	 * @param y
	 *            double
	 * @return double interpolated height
	 */
	private double interpolateZGrid(double xvalue, double yvalue) {
		// faster algo for xy grids
		int lengthX = xyGrid.getXGrid().getLength();
		int lengthY = xyGrid.getYGrid().getLength();
		double gx = xyGrid.getXGrid().coordinateAt(xvalue);
		double gy = xyGrid.getYGrid().coordinateAt(yvalue);
		if (gx < -0.5 || gx > lengthX - 0.5 || gy < -0.5 || gy > lengthY - 0.5) { // out
																					// of
																					// bounds
			return Double.NaN;
		}
		// find nearest integer lesser than g
		int gx0 = (int) Math.floor(gx + 0.5);
		if (gx0 < 0)
			gx0 = 0;
		if (gx0 > lengthX - 2)
			gx0 = lengthX - 2;
		int gy0 = (int) Math.floor(gy + 0.5);
		if (gy0 < 0)
			gy0 = 0;
		if (gy0 > lengthY - 2)
			gy0 = lengthY - 2;
		// bounding rectangle:
		int lb = gy0 * lengthX + gx0;
		int rb = gy0 * lengthX + gx0 + 1;
		int lt = (gy0 + 1) * lengthX + gx0;
		int rt = (gy0 + 1) * lengthX + gx0 + 1;
		double dx = Math.max(0.0, Math.min(1.0, gx - gx0));
		double dy = Math.max(0.0, Math.min(1.0, gy - gy0));
		double zlb = zData.pointAt(lb);
		double zrb = zData.pointAt(rb);
		double zlt = zData.pointAt(lt);
		double zrt = zData.pointAt(rt);
		if (dx + dy - 1 < 0) {
			return zlb + dx * (zrb - zlb) + dy * (zlt - zlb);
		} else {
			dx = Math.max(0.0, Math.min(1.0, gx0 + 1 - gx));
			dy = Math.max(0.0, Math.min(1.0, gy0 + 1 - gy));
			dx = gx0 + 1 - gx;
			dy = gy0 + 1 - gy;
			return zrt + dx * (zlt - zrt) + dy * (zrb - zrt);
		}
	}

	/**
	 * isFullSpectrum method comment.
	 */
	@Override
	public boolean isFullSpectrum() {
		return fullSpectrum;
	}

	/**
	 * isSameType method comment.
	 */
	@Override
	public boolean isSameType(Spectrum otherSpectrum) {
		return false;
	}

	/**
	 * faster complete interpolation
	 * 
	 * @return double[]
	 * @param newGrid
	 *            com.creon.math.RectangularGrid2D
	 */
	public double[] resample(RectangularGrid2D newGrid) {
		if (xyGrid != null)
			return xyGrid.interpolate(newGrid, zData.toArray());
		else {
			int n = newGrid.getLength();
			double[] sampled = new double[n];
			for (int i = 0; i < n; i++) {
				double[] xy = newGrid.gridPointAt(i);
				sampled[i] = interpolateZArray(xy[0], xy[1]);
			}
			return sampled;
		}
	}

	/**
	 * sets full spectrum status.
	 * 
	 * @param newFullSpectrum
	 *            boolean
	 */
	public void setFullSpectrum(boolean newFullSpectrum) {
		fullSpectrum = newFullSpectrum;
	}

	/**
	 * set ful view data range in mapping should be overloaded
	 * 
	 * @param range
	 *            Range3D
	 */
	public void setFullViewRange(Range3D.Double range) {
		setXFullViewRange(range.getRange(0));
		setYFullViewRange(range.getRange(1));
		setZFullViewRange(range.getRange(2));
	}

	/**
	 * sets full view range on x axis
	 */
	public void setXFullViewRange(Range1D.Double dataRange) {
		xAxisMap = new LinearAxisMap(xData, dataRange);
	}

	/**
	 * sets full view range on y axis.
	 */
	public void setYFullViewRange(Range1D.Double dataRange) {
		yAxisMap = new LinearAxisMap(yData, dataRange);
	}

	/**
	 * sets full view range on z axis
	 */
	public void setZFullViewRange(Range1D.Double dataRange) {
		zAxisMap = new LinearAxisMap(zData, dataRange);
	}
}
