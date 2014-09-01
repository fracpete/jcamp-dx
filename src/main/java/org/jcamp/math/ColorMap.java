package org.jcamp.math;

import java.awt.Color;
/**
 * mapping of data to color.
 * @author Thomas Weber
 */
public abstract class ColorMap {
    private Grid1D colorGrid;
    /**
     * ColorMap constructor comment.
     */
    ColorMap() {
        super();
    }
    /**
     * ColorMap constructor comment.
     */
    public ColorMap(Grid1D colorGrid) {
        super();
        setColorGrid(colorGrid);
    }
    /**
     * align grid step to pretty values.
     * @return double
     * @param s double
     */
    public static double alignGridStep(double s) {
        double fac = 1.0;
        if (s < 1E-10)
            return 1E-10; // prevents endless loop if s == 0.0

        while (s > 100.0) {
            s *= 0.1;
            fac *= 10.0;
        }
        while (s < 10.0) {
            s *= 10.0;
            fac *= 0.1;
        }
        if (s > 50.0) {
            if (Math.abs(s - 100.0) > Math.abs(s - 50.0))
                s = 50.0;
            else
                s = 100.0;
        }
        /*	else if (s>25.0)
        	{
        		if (Math.abs(s-50.0)>Math.abs(s-25.0))
        			s = 25.0;
        		else
        			s = 50.0;
        	} */
        else if (s > 20.0) {
            if (Math.abs(s - 25.0) > Math.abs(s - 20.0))
                s = 25.0;
            else
                s = 20.0;
        } else if (s > 10.0) {
            if (Math.abs(s - 20.0) > Math.abs(s - 10.0))
                s = 20.0;
            else
                s = 10.0;
        }

        return s * fac;
    }
    /**
     * gets color grid
     * @return Grid1D
     */
    public Grid1D getColorGrid() {
        return colorGrid;
    }
    /**
     * gets legend ranges.
     * @return Range1D[]
     */
    abstract public Range1D.Double[] getLegend();
    /**
     * gets mapping range.
     * @return Range1D.Double
     */
    public Range1D.Double getMapRange() {
        return colorGrid.getRange1D();
    }
    public Color[] map(double[] values) {
        Color[] colorTable = new Color[values.length];
        for (int i = 0; i < values.length; i++)
            colorTable[i] = map(values[i]);
        return colorTable;
    }
    public abstract Color map(double value);
    /**
     * sets color mapping grid.
     * @param newColorGrid Grid1D
     */
    public void setColorGrid(Grid1D newColorGrid) {
        colorGrid = newColorGrid;
    }
}
