package org.jcamp.units;
/**
 * unit created by scaling another unit.
 * @author Thomas Weber
 */
public final class ScaledUnit extends Unit {
    Unit unit;
    UnitScale scale;
    String name;
    String symbol;
    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        ScaledUnit unit = (ScaledUnit) super.clone();
        unit.unit = (Unit) this.unit.clone();
        unit.scale = (UnitScale) this.scale.clone();
        return unit;
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(BaseUnit unit, double factor, String name, String symbol) {
        this((Unit) unit, new UnitScale(factor), name, symbol);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(BaseUnit unit, SIUnitScale factor) {
        this((Unit) unit, factor);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(BaseUnit unit, UnitScale factor, String name, String symbol) {
        this((Unit) unit, factor, name, symbol);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(DerivedUnit unit, double factor, String name, String symbol) {
        this((Unit) unit, new UnitScale(factor), name, symbol);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(DerivedUnit unit, SIUnitScale factor) {
        this((Unit) unit, factor);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(DerivedUnit unit, UnitScale factor, String name, String symbol) {
        this((Unit) unit, factor, name, symbol);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    public ScaledUnit(DimensionlessUnit unit, double factor, String name, String symbol) {
        this((Unit) unit, new UnitScale(factor), name, symbol);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    ScaledUnit(Unit unit, SIUnitScale factor) {
        super(makeName(unit, factor), makeSymbol(unit, factor));
        this.unit = unit;
        this.scale = factor;
        this.name = makeName(unit, factor);
        this.symbol = makeSymbol(unit, factor);
    }

    /**
     * ScaledUnit constructor comment.
     * @param identifier java.lang.String
     */
    ScaledUnit(Unit unit, UnitScale factor, String name, String symbol) {
        super(symbol, name);
        this.unit = unit;
        this.scale = factor;
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * convert value from unit <code>thatUnit</code> to this unit.
     * @param value double
     * @param thatUnit Unit
     * @return double
     */
    public double convertFrom(double value, Unit thatUnit) throws UnitException {
        if (isConvertibleTo(thatUnit)) {
            if ((thatUnit instanceof ScaledUnit)
                || (thatUnit instanceof BaseUnit)
                || (thatUnit instanceof DerivedUnit))
                return value * thatUnit.getScaleFactor() / getScaleFactor();
            else
                return thatUnit.convertTo(value, this);
        } else
            throw new UnitException("units not convertible");
    }

    /**
     * convert value to unit <code>thatUnit</code> from this unit.
     * @param value double
     * @param thatUnit Unit
     * @return double
     */
    public double convertTo(double value, Unit thatUnit) throws UnitException {
        if (isConvertibleTo(thatUnit)) {
            if ((thatUnit instanceof ScaledUnit)
                || (thatUnit instanceof BaseUnit)
                || (thatUnit instanceof DerivedUnit))
                return value * getScaleFactor() / thatUnit.getScaleFactor();
            else
                return thatUnit.convertFrom(value, this);
        } else
            throw new UnitException("units not convertible");
    }

    /**
     * gets unit name.
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * gets unit quanity.
     * @return String
     */
    public java.lang.String getQuantity() {
        return unit.getQuantity();
    }

    /**
     * gets scale factor.
     * @return double
     */
    public double getScaleFactor() {
        return scale.factor;
    }

    /**
     * gets unit symbol.
     * @return String 
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * checks if unit is convertible to unit <code>thatUnit</code>.
     * @param thatUnit Unit
     * @return boolean
     */
    public boolean isConvertibleTo(Unit thatUnit) {
        if (thatUnit instanceof ScaledUnit)
            return unit.isConvertibleTo(((ScaledUnit) thatUnit).unit);
        else if (thatUnit instanceof BaseUnit)
            return unit.isConvertibleTo((BaseUnit) thatUnit);
        else if (thatUnit instanceof DerivedUnit)
            return unit.isConvertibleTo((DerivedUnit) thatUnit);
        return false;
    }

    /**
     * getDefinition method comment.
     */
    private static String makeName(Unit unit, UnitScale scale) {
        if (scale.name != null && scale.name.length() > 0) {
            if (unit.equals(BaseUnit.kilogram))
                return scale.name + "gram";
            return scale.name + unit.getName();
        } else
            return unit.getName();
    }

    /**
     * Insert the method's description here.
     * Creation date: (01/14/00 14:54:38)
     * @return java.lang.String
     * @param unit com.labcontrol.graphics.data.Unit
     * @param scale com.labcontrol.graphics.data.UnitScale
     */
    private final static String makeSymbol(Unit unit, UnitScale scale) {
        String id;
        if (unit.equals(BaseUnit.kilogram))
            id = "g";
        else
            id = unit.getSymbol();

        if (scale.prefix != null && scale.prefix.length() > 0) {
            return scale.prefix + id;
        } else
            return id;
    }
}