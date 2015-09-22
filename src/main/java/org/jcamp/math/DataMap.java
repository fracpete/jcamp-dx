/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * abstract base class for data mapping.
 * @author Thomas Weber
 */
public abstract class DataMap {
    /**
     * DataMap constructor comment.
     */
    protected DataMap() {
        super();
    }
    /**
     * range of data mapping.
     * @return Range1D.Double
     */
    public abstract Range1D.Double getMapRange();
    /**
     * map real world data
     * @param world double
     * @return double
     */
    public abstract double[] map(double[] world);
    /**
     * map single world data point.
     * @param world double
     * @return double
     */
    public abstract double map(double world);
    /**
     * reverse map back to world data
     * @param world double
     * @return double[]
     */
    public abstract double[] reverseMap(double[] vpdata);
    /**
     * reverse map to world data.
     * @param world double
     * @return double
     */
    public abstract double reverseMap(double vpdata);
}
