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
 * colormap with descrete colors.
 * @author Thomas Weber
 */
public class ColorTable extends ColorMap {
    private final static Color[] WFCOLORS = new Color[16];
    static {
        WFCOLORS[0] = new Color(0, 0, 80);
        WFCOLORS[1] = new Color(0, 0, 140);
        WFCOLORS[2] = new Color(0, 120, 160);
        WFCOLORS[3] = new Color(0, 180, 180);
        WFCOLORS[4] = new Color(0, 255, 180);
        WFCOLORS[5] = new Color(0, 220, 100);
        WFCOLORS[6] = new Color(0, 255, 0);
        WFCOLORS[7] = new Color(0, 210, 0);
        WFCOLORS[8] = new Color(140, 230, 0);
        WFCOLORS[9] = new Color(230, 230, 0);
        WFCOLORS[10] = new Color(255, 255, 0);
        WFCOLORS[11] = new Color(255, 180, 0);
        WFCOLORS[12] = new Color(220, 100, 0);
        WFCOLORS[13] = new Color(220, 0, 0);
        WFCOLORS[14] = new Color(255, 0, 0);
        WFCOLORS[15] = new Color(255, 120, 120);
    }

    private final static Color[] TWCOLORS = new Color[256];
    static {
        for (int i = 0; i < 256; i++)
            TWCOLORS[i] = new Color((i / 16) * 16, i, 255 - i);
    }
    private Color[] colorTable = WFCOLORS;
    /**
     * WFColorMap constructor comment.
     */
    ColorTable() {
        super();
    }
    /**
     * ColorTable constructor given a mapping grid.
     * @param data Grid1D
     */
    public ColorTable(Grid1D data) {
        super(data);
    }
    /**
     * ColorTable constructor comment given an interval.
     * @param data Interval
     */
    public ColorTable(IInterval1D data) {
        this(makeGrid(data.getRange1D()));
    }
    /**
     * ColorTable constructor given a range.
     * @param Interval
     */
    public ColorTable(Range1D data) {
        this(makeGrid(new Range1D.Double(data)));
    }
    /**
     * gets color table.
     * @return java.awt.Color[]
     */
    public java.awt.Color[] getColorTable() {
        return colorTable;
    }
    /**
     * gets color table legend.
     * @return Range1D.Double[] array of legend ranges
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
     * make data mapping grid.
     * @param range Range1D.Double
     * @return LinearGrid1D
     */
    private static LinearGrid1D makeGrid(Range1D.Double range) {
        double step = alignGridStep(range.getXWidth() / WFCOLORS.length);
        double left = Math.floor(range.getXMin() / step) * step;
        double right = Math.ceil(range.getXMax() / step) * step;
        return new LinearGrid1D(left, right, step);
    }
    /**
     * maps value to color
     */
    public java.awt.Color map(double value) {
        double grid = getColorGrid().coordinateAt(value);
        int colorIndex = Math.max(0, Math.min(colorTable.length - 1, (int) grid));
        return colorTable[colorIndex];
    }
    /**
     * sets color table.
     * @param newColorTable java.awt.Color[]
     */
    public void setColorTable(java.awt.Color[] newColorTable) {
        if (colorTable.length != newColorTable.length) {
            Range.Double gridRange = getColorGrid().getRange();
            setColorGrid(new LinearGrid1D(gridRange.getMin(0), gridRange.getMax(0), newColorTable.length));
        }
        colorTable = newColorTable;
    }
}
