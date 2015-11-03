/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;

/**
 * unit that is an alias for another.
 * 
 * @author Thomas Weber
 */
public class AliasUnit
  extends Unit {
  
  /** for serialization. */
  private static final long serialVersionUID = -8078385129952183426L;
  
  Unit unit;
  
  String aliasSymbol;
  
  String aliasName;
  
  /**
   * AliasUnit constructor comment.
   * @param aliasidentifier String
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
   * @param aliasidentifier String
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
  @Override
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
  @Override
  public double convertFrom(double value, Unit thatUnit) throws UnitException {
    return unit.convertFrom(value, thatUnit);
  }
  /**
   * convert value to unit <code>thatUnit</code> from this unit.
   * @param value double
   * @param thatUnit Unit
   * @return double
   */
  @Override
  public double convertTo(double value, Unit thatUnit) throws UnitException {
    return unit.convertTo(value, thatUnit);
  }
  /**
   * gets unit name.
   * @return String
   */
  @Override
  public String getName() {
    return this.aliasName;
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
  double getScaleFactor() {
    return unit.getScaleFactor();
  }
  /**
   * gets unit symbol.
   * @return String
   */
  @Override
  public String getSymbol() {
    return this.aliasSymbol;
  }
  /**
   * checks if unit is convertible to unit <code>thatUnit</code>.
   * @param thatUnit Unit
   * @return boolean
   */
  @Override
  public boolean isConvertibleTo(Unit thatUnit) {
    return unit.isConvertibleTo(thatUnit);
  }
}
