/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * direct linear mapping from data to axis.
 * 
 * @author Thomas Weber
 */
public class LinearAxisMap extends AxisMap {

	public LinearAxisMap(IInterval1D data) {
		super(data);
	}

	public LinearAxisMap(IInterval1D data, Range1D fullViewRange) {
		super(data);
		setFullViewRange(fullViewRange);
		// calcGrid();
	}

	/**
	 * calc grid from zoom range.
	 */
	@Override
	protected void calcGrid() {
		double gridStep = alignTickStep(realZoomRange.getXWidth() / 5);
		double gridStart = Math.floor((realZoomRange.getXMin()) / gridStep)
				* gridStep;
		double gridEnd = Math.ceil((realZoomRange.getXMax()) / gridStep)
				* gridStep;
		grid = new LinearGrid1D(gridStart, gridEnd, gridStep);
		gridZoomRange.set(grid.coordinateAt(realZoomRange.getXMin()),
				grid.coordinateAt(realZoomRange.getXMax()));
	}

	/**
	 * maps data grid to axis.
	 * 
	 * @return Grid1D
	 * @param grid
	 *            Grid1D
	 */
	@Override
	public Grid1D map(Grid1D grid) {
		if (grid instanceof LinearGrid1D)
			return new LinearGrid1D(map(((LinearGrid1D) grid).getStart()),
					map(((LinearGrid1D) grid).getEnd()), grid.getLength());
		else
			return super.map(grid);
	}

	/**
	 * reverse maps data grid to axis.
	 * 
	 * @return Grid1D
	 * @param grid
	 *            Grid1D
	 */
	@Override
	public Grid1D reverseMap(Grid1D grid) {
		if (grid instanceof LinearGrid1D)
			return new LinearGrid1D(
					reverseMap(((LinearGrid1D) grid).getStart()),
					reverseMap(((LinearGrid1D) grid).getEnd()),
					grid.getLength());
		else
			return super.reverseMap(grid);
	}
}
