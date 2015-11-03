/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;
/**
 * unit created by adding an offset to another unit.
 * 
 * @author Thomas Weber
 */
public final class OffsetUnit
  extends Unit {
  
  /** for serialization. */
  private static final long serialVersionUID = 7394622355294421470L;

  Unit unit;
  
  double offset;
  
  String symbol;
  
  String name;
  
  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    OffsetUnit unit = (OffsetUnit) super.clone();
    unit.unit = (Unit) this.unit.clone();
    return unit;
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public OffsetUnit(BaseUnit unit, double offset, String name, String symbol) {
    this((Unit) unit, offset, name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public OffsetUnit(DerivedUnit unit, double offset, String name, String symbol) {
    this((Unit) unit, offset, name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  public OffsetUnit(ScaledUnit unit, double offset, String name, String symbol) {
    this((Unit) unit, offset, name, symbol);
  }

  /**
   * ScaledUnit constructor comment.
   * @param identifier String
   */
  private OffsetUnit(Unit unit, double offset, String name, String symbol) {
    super(name, symbol);
    this.unit = unit;
    this.offset = offset;
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
	return value * thatUnit.getScaleFactor() / getScaleFactor() - offset;
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
	return value * thatUnit.getScaleFactor() / getScaleFactor() + offset;
      else
	return thatUnit.convertFrom(value, this);
    } else
      throw new UnitException("units not convertible");
  }

  /**
   * getDefinition method comment.
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * gets quantity.
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
    return unit.getScaleFactor();
  }

  /**
   * getDefinition method comment.
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
    if (thatUnit instanceof OffsetUnit)
      return unit.isConvertibleTo(((OffsetUnit) thatUnit).unit);
    else if (thatUnit instanceof ScaledUnit)
      return unit.isConvertibleTo(((ScaledUnit) thatUnit).unit);
    else if (thatUnit instanceof BaseUnit)
      return unit.isConvertibleTo((BaseUnit) thatUnit);
    else if (thatUnit instanceof DerivedUnit)
      return unit.isConvertibleTo((DerivedUnit) thatUnit);
    return false;
  }
}
