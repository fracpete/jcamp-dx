package org.jcamp.units;

/**
 * class for SI base units and a generic dimensionless unit.
 * no public constructors. base units are accessed by public
 * final static members.
 * @author Thomas Weber
 */
public final class BaseUnit extends Unit {
    /*
     * The base units of the SI system of units:
     */
    private String name;
    private String symbol;
    private String quantity;
    /**
    	* generic dimensionless unit
    	*/
    public final static BaseUnit generic = new BaseUnit("Arbitrary Unit", "", "");
    /**
     * Base unit of electric current.
     * The ampere is that constant current which, if maintained
     * in two straight parallel conductors of infinite length,
     * of negligible circular cross section, and placed 1 meter
     * apart in vacuum, would produce between these conductors a
     * force equal to 2 x 10^-7 newton per meter of length.
     */
    public final static BaseUnit ampere = new BaseUnit("Electric Current", "ampere", "A");

    /**
     * Base unit of luminous intensity.
     * The candela is the luminous intensity, in a given
     * direction, of a source that emits monochromatic
     * radiation of frequency 540 x 10^12 hertz and that has a
     * radiant intensity in that direction of (1/683) watt per
     * steradian.
     */
    public final static BaseUnit candela = new BaseUnit("Luminous Intensity", "candela", "cd");

    /**
     * Base unit of thermodynamic temperature.
     * The kelvin, unit of thermodynamic temperature, is the
     * fraction 1/273.16 of the thermodynamic temperature of the
     * triple point of water.
     */
    public final static BaseUnit kelvin = new BaseUnit("Temperature", "kelvin", "K");

    /**
     * Base unit of mass.
     * The kilogram is the unit of mass; it is equal to the mass
     * of the international prototype of the kilogram.
     */
    public final static BaseUnit kilogram = new BaseUnit("Mass", "kilogram", "kg");

    /**
     * Base unit of length.
     * The meter is the length of the path travelled by light
     * in vacuum during a time interval of 1/299 792 458 of a
     * second.
     */
    public final static BaseUnit meter = new BaseUnit("Length", "meter", "m");

    /**
     * Base unit of time.
     * The second is the duration of 9 192 631 770 periods of
     * the radiation corresponding to the trasition between
     * the two hyperfine levels of the ground state of the
     * cesium-133 atom.
     */
    public final static BaseUnit second = new BaseUnit("Time", "second", "s");

    /**
     * Base unit of amount of substance.
     * The mole is the amount of substance of a system which
     * contains as many elementary entities as there are atoms
     * in 0.012 kilogram of carbon 12.
     */
    public final static BaseUnit mole = new BaseUnit("Amount of Substance", "mole", "mol");

    /**
     * Base unit of angular measure.
     * The radian is the plane angle between two radii of a
     * circle that cut off on the circumference an arc equal in
     * length to the radius.
     */
    public final static BaseUnit radian = new BaseUnit("Angle", "radian", "rad");

    /**
     * Base unit of solid angle.
     * The steradian is the solid angle that, having its vertex
     * in the center of a sphere, cuts off an area of the surface
     * equal to that of a square with sides of length equal to the
     * radius of the sphere.
     */
    public final static BaseUnit steradian = new BaseUnit("Solid Angle", "steradian", "sr");
    private final static BaseUnit[] baseUnits =
        new BaseUnit[] { generic, ampere, candela, kelvin, kilogram, meter, second, mole, radian, steradian };

    /**
     * BaseUnit constructor comment.
     * @param identifier java.lang.String
     */
    protected BaseUnit(String quantity, String name, String symbol) {
        super(symbol, name);
        this.quantity = quantity;
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
        if (this.equals(generic))
            return value;
        if (isConvertibleTo(thatUnit)) {
            if (thatUnit instanceof BaseUnit)
                return value;
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
        if (this.equals(generic))
            return value;
        if (isConvertibleTo(thatUnit)) {
            if (thatUnit instanceof BaseUnit)
                return value;
            else
                return thatUnit.convertFrom(value, this);
        } else
            throw new UnitException("units not convertible");
    }
    /**
     * checks for equality.
     * @param o java.lang.Object
     * @return boolean
     */
    public boolean equals(Object o) {
        if (o instanceof BaseUnit) {
            BaseUnit base = (BaseUnit) o;
            if (getIdentifier() == base.getIdentifier())
                return true;
        }
        return false;
    }
    /**
     * gets unit name.
     * @return String
     */
    public String getName() {
        return name;
    }
    /**
     * gets quantity.
     * @return String
     */
    public java.lang.String getQuantity() {
        return quantity;
    }
    /**
     * gets scale factor.
     * @return double
     */
    public final double getScaleFactor() {
        return 1.0;
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
        if (this.equals(generic))
            return true;
        if (this.equals(thatUnit))
            return true;
        return thatUnit.isConvertibleTo(this);
    }
}
