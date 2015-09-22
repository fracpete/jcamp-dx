/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;
/**
 * internal helper class for derived units.
 * @author Thomas Weber
 */
class UnitFactor implements Cloneable {
    public BaseUnit baseUnit;
    public int power;

    /**
     * UnitFactor constructor comment.
     */
    public UnitFactor(BaseUnit baseUnit, int power) {
        this.baseUnit = baseUnit;
        this.power = power;
    }

    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        UnitFactor factor = null;
        try {
            factor = (UnitFactor) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        factor.baseUnit = (BaseUnit) this.baseUnit.clone();
        return factor;
    }

    /**
     * equality check.
     * @return boolean
     * @param o java.lang.Object
     */
    public boolean equals(Object o) {
        if (o instanceof UnitFactor) {
            UnitFactor f = (UnitFactor) o;
            if (baseUnit.equals(f.baseUnit) && power == f.power)
                return true;
        }
        return false;
    }
}
