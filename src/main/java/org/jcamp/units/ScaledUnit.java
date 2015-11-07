/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;

/**
 * unit created by scaling another unit.
 * 
 * @author Thomas Weber
 */
public final class ScaledUnit
  extends Unit {
  
  /** for serialization. */
  private static final long serialVersionUID = 5678271409956186226L;

  Unit unit;
  
  UnitScale scale;
  
  String name;
  
  String symbol;
  
  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    ScaledUnit unit = (ScaledUnit) super.clone();
    unit.unit = (Unit) this.unit.clone();
    unit.scale = (UnitScale) this.scale.clone();
    return unit;
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(BaseUnit unit, double factor, String name, String symbol) {
    this((Unit) unit, new UnitScale(factor), name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(BaseUnit unit, SIUnitScale factor) {
    this((Unit) unit, factor);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(BaseUnit unit, UnitScale factor, String name, String symbol) {
    this((Unit) unit, factor, name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(DerivedUnit unit, double factor, String name, String symbol) {
    this((Unit) unit, new UnitScale(factor), name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(DerivedUnit unit, SIUnitScale factor) {
    this((Unit) unit, factor);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(DerivedUnit unit, UnitScale factor, String name, String symbol) {
    this((Unit) unit, factor, name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public ScaledUnit(DimensionlessUnit unit, double factor, String name, String symbol) {
    this((Unit) unit, new UnitScale(factor), name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
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
   * @param identifier String
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
  @Override
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
  @Override
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
  @Override
  public String getName() {
    return name;
  }

  /**
   * gets unit quanity.
   * @return String
   */
  @Override
  public String getQuantity() {
    return unit.getQuantity();
  }

  /**
   * gets scale factor.
   * @return double
   */
  @Override
  public double getScaleFactor() {
    return scale.factor;
  }

  /**
   * gets unit symbol.
   * @return String 
   */
  @Override
  public String getSymbol() {
    return symbol;
  }

  /**
   * checks if unit is convertible to unit <code>thatUnit</code>.
   * @param thatUnit Unit
   * @return boolean
   */
  @Override
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
   * @return String
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
