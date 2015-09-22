/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * interval of width 0.
 * @author Thomas Weber
 */
public class EmptyArray1D implements IArray1D {
    /**
     * EmptyInterval1D constructor comment.
     */
    public EmptyArray1D() {
        super();
    }
    /**
     * getLength method comment.
     */
    public int getLength() {
        return 0;
    }
    /**
     * getRange method comment.
     */
    public Range.Double getRange() {
        return new Range1D.Double();
    }
    /**
     * gets interval range.
     */
    public Range1D.Double getRange1D() {
        return new Range1D.Double();
    }
    /**
     * pointAt method comment.
     */
    public double pointAt(int index) {
        return Double.NaN;
    }
    /**
     * translate method comment.
     */
    public void scale(double amount) {
    }
    /**
     * toArray method comment.
     */
    public double[] toArray() {
        return new double[0];
    }
    /**
     * translate method comment.
     */
    public void translate(double amount) {
    }
}
