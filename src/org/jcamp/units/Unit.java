package org.jcamp.units;
import java.io.Serializable;
/**
 * abstract base class for units.
 * @author Thomas Weber
 */
public abstract class Unit implements Cloneable, Serializable {
    private static java.util.Hashtable idMap = new java.util.Hashtable();
    private static java.util.Hashtable aliasMap = new java.util.Hashtable();
    private final String identifier;
    static {
        initHashtables();
    }

    /**
     * create unit with identifier <code>identifier</code>
     * @param identifier String
     */
    protected Unit(String identifier) {
        super();
        if (identifier != null && identifier.length() > 0)
            identifier.replace(' ', '_');
        this.identifier = identifier.toUpperCase();
        Unit.idMap.put(this.identifier, this);
    }

    /**
     * create unit with identifier <code>identifier</code>
     * @param identifier String
     */
    protected Unit(String identifier, String alias) {
        super();
        if (identifier != null && identifier.length() > 0)
            identifier.replace(' ', '_');
        this.identifier = identifier.toUpperCase();
        Unit.idMap.put(this.identifier, this);
        addAlias(this, alias);
    }

    /**
     * add alias for unit dictionary.
     * @param unit Unit
     * @param alias java.lang.String
     */
    public static void addAlias(Unit unit, String alias) {
        if (alias != null && alias.length() > 0) {
            String key = alias.toUpperCase();
            key.replace(' ', '_');
            if (Unit.aliasMap.get(key) == null) {
                Unit.aliasMap.put(key, unit);
            }
        }
    }

    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        Unit unit = null;
        try {
            unit = (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return unit;
    }

    /**
     * convert double <code>value</code> from <code>thatUnit</code>
     * into this unit. returns converted value.
     * @return double
     * @param value double
     * @param thatUnit Unit
     */
    public abstract double convertFrom(double value, Unit thatUnit) throws UnitException;

    /**
     * convert double <code>value</code> given in this unit 
     * into <code>thatUnit</code>
     * returns converted value.
     * @return double
     * @param value double
     * @param thatUnit Unit
     */
    public abstract double convertTo(double value, Unit thatUnit) throws UnitException;

    /**
     * compare units, if they have the same key and describe the same quantity they are considered equal.
     * @return boolean
     * @param obj java.lang.Object
     */
    public boolean equals(Object obj) {
        if (obj instanceof Unit) {
            Unit unit = (Unit) obj;
            if (unit.getIdentifier().equals(this.identifier))
                return true;
        }
        return false;
    }

    /**
     * strip any SI-prefix and try to find BaseUnit
     * @return Unit
     * @param id java.lang.String
     */
    static Unit getBaseUnitFromKey(String id) {
        int n = SIUnitScale.prefixes.length;
        Unit unit;
        String ID = id.toUpperCase();
        for (int i = 0; i < n; i++) {
            String prefix = SIUnitScale.prefixes[i].getName().toUpperCase();
            if (id.startsWith(prefix)) {
                unit = Unit.getUnitFromKey(id.substring(prefix.length()));
                if (unit != null)
                    return new ScaledUnit(unit, SIUnitScale.prefixes[i]);
            }
            prefix = SIUnitScale.prefixes[i].getPrefix().toUpperCase();
            if (id.startsWith(prefix)) {
                unit = Unit.getUnitFromKey(id.substring(prefix.length()));
                if (unit != null)
                    return new ScaledUnit(unit, SIUnitScale.prefixes[i]);
            }
        }
        return null;
    }

    /**
     * gets unit identifier (e.g. unit symbol).
     * @return java.lang.String
     */
    final String getIdentifier() {
        return identifier;
    }

    /**
     * gets unit definition (long name).
     * @return java.lang.String
     */
    public abstract String getName();

    /**
     * gets quantity measured by this unit.
     * @return java.lang.String
     */
    public abstract String getQuantity();

    /**
     * gets scale factor.
     * @return double
     */
    abstract double getScaleFactor();

    /**
     * gets unit symbol.
     * @return java.lang.String
     */
    public abstract String getSymbol();

    /**
     * get unit from unit key.
     * @return Unit
     * @param key java.lang.String
     */
    static Unit getUnitFromKey(String key) {
        // direct test
        Unit unit = (Unit) (Unit.idMap.get(key));
        // alias test
        if (unit == null) {
            unit = (Unit) (Unit.aliasMap.get(key));
        }
        return unit;
    }

    /**
     * get unit from unit string.
     * @return Unit
     * @param id java.lang.String
     */
    public static Unit getUnitFromString(String id) {
        String key = id.toUpperCase();
        Unit unit = getUnitFromKey(key);
        // scaled unit
        if (unit == null) {
            unit = getBaseUnitFromKey(key);
        }
        if (unit == null) { // plural s?
            if (key.endsWith("ES") && key.length() > 5) {
                String skey = key.substring(0, key.length() - 2);
                unit = getUnitFromKey(skey);
                if (unit == null) { // plural s?
                    unit = getBaseUnitFromKey(skey);
                }
            }
            if (unit == null && (key.endsWith("S") && key.length() > 4)) {
                String skey = key.substring(0, key.length() - 1);
                unit = getUnitFromKey(skey);
                if (unit == null) { // plural s?
                    unit = getBaseUnitFromKey(skey);
                }
            }
        }
        if (unit == null) {
            return new AliasUnit(CommonUnit.generic, id);
        } else
            return unit;
    }

    /**
     * get hashcode.
     * 
     * @return int
     */
    public int hashCode() {
        return identifier.hashCode();
    }

    /**
     * initialize Hashtables.
     * 
     */
    private static void initHashtables() {
        try {
            Class.forName("org.jcamp.units.BaseUnit");
            Class.forName("org.jcamp.units.CommonUnit");
        } catch (ClassNotFoundException e) {
            System.err.println("predefined units not available");
        }

    }

    /**
     * check if <code>thatUnit</code> is convertible into
     * this unit.
     * @return boolean
     * @param unit Unit
     */
    public abstract boolean isConvertibleTo(Unit thatUnit);

    /**
     * gets String representation.
     * @return java.lang.String
     */
    public final String toString() {
        String s = getSymbol();
        if (s == null || s.length() == 0)
            s = getName();
        if (s == null || s.length() == 0)
            s = getIdentifier();
        return s;
    }
}