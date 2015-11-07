/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;

/**
 * unit that is derived from base units.
 * 
 * @author Thomas Weber
 */
public final class DerivedUnit
  extends Unit {
  
  /** for serialization. */
  private static final long serialVersionUID = -6912254468800545391L;

  String quantity = null;
  
  String name = null;
  
  String symbol = null;
  
  UnitFactor[] factors;

  public DerivedUnit() {
    super("");
    this.factors = new UnitFactor[0];
    this.name = "";
    this.quantity = "";
    this.symbol = "";
  }
  public DerivedUnit(BaseUnit[] baseUnits, int[] powers, String quantity, String name, String symbol) {
    this(makeFactors(baseUnits, powers), makeID(quantity, name, symbol), name);
    this.name = name;
    this.symbol = symbol;
    this.quantity = quantity;
  }
  private DerivedUnit(UnitFactor[] factors, String identifier) {
    super(identifier);
    this.factors = factors;
  }
  private DerivedUnit(UnitFactor[] factors, String identifier, String alias) {
    super(identifier, alias);
    this.factors = factors;
  }
  /**
   * constructor for dimensionless units
   */
  public DerivedUnit(String quantity, String name, String symbol) {
    super(makeID(quantity, name, symbol), name);
    this.factors = new UnitFactor[0];
    this.quantity = quantity;
    this.name = name;
    this.symbol = symbol;
  }
  /**
   * cloning.
   * 
   * @return java.lang.Object
   */
  @Override
  public Object clone() {
    DerivedUnit unit = (DerivedUnit) super.clone();
    for (int i = 0; i < factors.length; i++)
      unit.factors[i] = (UnitFactor) this.factors[i].clone();
    return unit;
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
      if (thatUnit instanceof DerivedUnit)
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
  @Override
  public double convertTo(double value, Unit thatUnit) throws UnitException {
    if (isConvertibleTo(thatUnit)) {
      if (thatUnit instanceof DerivedUnit)
	return value;
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
   * gets quantity.
   * @return String
   */
  @Override
  public String getQuantity() {
    return quantity;
  }
  /**
   * gets scale factor.
   * @return double
   */
  @Override
  public final double getScaleFactor() {
    return 1.0;
  }
  /**
   * gets symbol.
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
    if (thatUnit instanceof DerivedUnit) {
      DerivedUnit thatDerived = (DerivedUnit) thatUnit;
      if (factors.length == thatDerived.factors.length) {
	for (int i = 0; i < factors.length; i++) {
	  if (!factors[i].equals(thatDerived.factors[i]))
	    return false;
	}
	return true;
      }
      return false;
    } else if (thatUnit instanceof BaseUnit)
      return false;
    else
      return thatUnit.isConvertibleTo(this);
  }
  /**
   * make unit factors.
   * @param baseUnits BaseUnit[]
   * @param powers int[]
   * @return UnitFactor[]
   */
  private static UnitFactor[] makeFactors(BaseUnit[] baseUnits, int[] powers) {
    int n = baseUnits.length;
    UnitFactor[] factors = new UnitFactor[n];
    for (int i = 0; i < n; i++) {
      factors[i] = new UnitFactor(baseUnits[i], powers[i]);
    }
    return factors;
  }
  /**
   * make key from name of symbol.
   * @param quantity String
   * @param name String
   * @param symbol String
   * @return String
   */
  private static String makeID(String quantity, String name, String symbol) {
    if (symbol != null && symbol.length() > 0)
      return symbol;
    if (name != null && name.length() > 0)
      return name;
    return quantity;
  }
}
