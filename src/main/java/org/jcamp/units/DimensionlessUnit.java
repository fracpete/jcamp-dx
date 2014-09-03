package org.jcamp.units;

/**
 * unit without dimensions.
 * 
 * @author Thomas Weber 
 */
public final class DimensionlessUnit
  extends Unit {
  
  /** for serialization. */
  private static final long serialVersionUID = -744159462713730383L;

  String quantity;
  
  String name;
  
  String symbol;
  
  /**
   * ScaledUnit constructor comment.
   * @param identifier java.lang.String
   */
  public DimensionlessUnit(String quantity, String name, String symbol) {
    super(quantity, name);
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
  @Override
  public double convertFrom(double value, Unit thatUnit) throws UnitException {
    if (isConvertibleTo(thatUnit)) {
      if (thatUnit instanceof DimensionlessUnit || thatUnit.equals(BaseUnit.generic))
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
      if (thatUnit instanceof DimensionlessUnit || thatUnit.equals(BaseUnit.generic))
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
  public java.lang.String getQuantity() {
    return quantity;
  }
  /**
   * getScaleFactor method comment.
   */
  @Override
  final double getScaleFactor() {
    return 1.0;
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
    return BaseUnit.generic.isConvertibleTo(thatUnit);
  }
}
