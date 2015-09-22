/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

import java.awt.Color;
/**
 * color mapping to grey scale.
 * @author Thomas Weber
 */
public class GreyColorMap extends ColorMap {
    /**
     * ctor.
     * @param grid Grid1D mapping grid
     */
    public GreyColorMap(Grid1D grid) {
        super(grid);
    }
    /**
     * ctor.
     * @param data IInterval1D mapping interval
     */
    public GreyColorMap(IInterval1D data) {
        this(makeGrid(data.getRange1D()));
    }
    /**
     * ctor.
     * @param range Range1D mapping range
     */
    public GreyColorMap(Range1D range) {
        this(makeGrid(new Range1D.Double(range)));
    }
    /**
     * gets legend ranges.
     */
    public Range1D.Double[] getLegend() {
        int n = getColorGrid().getLength();
        Range1D.Double[] legend = new Range1D.Double[n];
        for (int i = 0; i < n; i++) {
            legend[i] = new Range1D.Double(getColorGrid().pointAt(i), getColorGrid().pointAt(i + 1));
        }
        return legend;
    }
    /**
     * make mapping grid.
     * @return LinearGrid1D
     * @param range Range1D.Double
     */
    private static LinearGrid1D makeGrid(Range1D.Double range) {
        double step = alignGridStep(range.getXWidth() / 64);
        double left = Math.floor(range.getXMin() / step) * step;
        double right = Math.ceil(range.getXMax() / step) * step;
        return new LinearGrid1D(left, right, step);
    }
    /**
     * maps value to color.
     */
    public java.awt.Color map(double value) {
        double grid = getColorGrid().coordinateAt(value);
        value = 255 - 255 * (grid / getColorGrid().getLength());
        int rgb = (int) Math.max(0, Math.min(255, value));
        return new Color(rgb, rgb, rgb);
    }
}
