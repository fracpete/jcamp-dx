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