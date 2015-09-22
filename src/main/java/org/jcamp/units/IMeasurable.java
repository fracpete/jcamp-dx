/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;
/**
 * interface indicating that data is measurable,
 * e.g. has a phyiscal unit.
 * @author Thomas Weber
 */
public interface IMeasurable {
    /**
     * sets a new unit for measured data and converts values from old unit.
     * 
     * @param newUnit org.jcamp.units.Unit
     */
    void convertToUnit(Unit newUnit) throws UnitException;

    /**
     * gets the measure unit
     * @return Unit
     *
     * @author Thomas Weber
     */
    Unit getUnit();

    /**
     * sets the measure unit without data conversion
     * @param unit Unit
     *
     * @author Thomas Weber
     */
    void setUnit(Unit unit);
}
