package org.jcamp.units;

/**
 * unit that is an alias for another
 * @author Thomas Weber
 */
public class AliasUnit extends Unit {
    Unit unit;
    String aliasSymbol;
    String aliasName;
    /**
     * AliasUnit constructor comment.
     * @param aliasidentifier java.lang.String
     */
    public AliasUnit(Unit unit, String aliasName) {
        super(unit.getIdentifier());
        Unit.addAlias(unit, aliasName);
        this.unit = unit;
        this.aliasName = aliasName;
        this.aliasSymbol = unit.getSymbol();
    }
    /**
     * AliasUnit constructor comment.
     * @param aliasidentifier java.lang.String
     */
    public AliasUnit(Unit unit, String aliasName, String aliasSymbol) {
        super(unit.getIdentifier());
        Unit.addAlias(unit, aliasName);
        Unit.addAlias(unit, aliasSymbol);
        this.unit = unit;
        this.aliasName = aliasName;
        this.aliasSymbol = aliasSymbol;
    }
    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        AliasUnit unit = (AliasUnit) super.clone();
        unit.unit = (Unit) this.unit.clone();
        return unit;
    }
    /**
     * convert value to unit <code>thatUnit</code> from this unit.
     * @param value double
     * @param thatUnit Unit
     * @return double
     */
    public double convertFrom(double value, Unit thatUnit) throws UnitException {
        return unit.convertFrom(value, thatUnit);
    }
    /**
     * convert value to unit <code>thatUnit</code> from this unit.
     * @param value double
     * @param thatUnit Unit
     * @return double
     */
    public double convertTo(double value, Unit thatUnit) throws UnitException {
        return unit.convertTo(value, thatUnit);
    }
    /**
     * gets unit name.
     * @return String
     */
    public String getName() {
        return this.aliasName;
    }
    /**
     * gets quantity.
     * @return String
     */
    public String getQuantity() {
        return unit.getQuantity();
    }
    /**
     * gets scale factor.
     * @return double
     */
    double getScaleFactor() {
        return unit.getScaleFactor();
    }
    /**
     * gets unit symbol.
     * @return String
     */
    public String getSymbol() {
        return this.aliasSymbol;
    }
    /**
     * checks if unit is convertible to unit <code>thatUnit</code>.
     * @param thatUnit Unit
     * @return boolean
     */
    public boolean isConvertibleTo(Unit thatUnit) {
        return unit.isConvertibleTo(thatUnit);
    }
}
