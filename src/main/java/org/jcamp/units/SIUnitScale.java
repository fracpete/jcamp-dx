package org.jcamp.units;

/**
 * SI unit scales.
 * @author Thomas Weber
 */
public final class SIUnitScale
  extends UnitScale {
  
  /** for serialization. */
  private static final long serialVersionUID = 8324691727166488954L;
  
  public final static SIUnitScale atto = new SIUnitScale(1E-18, "atto", "a");
  public final static SIUnitScale femto = new SIUnitScale(1E-15, "femto", "f");
  public final static SIUnitScale pico = new SIUnitScale(1E-12, "pico", "p");
  public final static SIUnitScale nano = new SIUnitScale(1E-9, "nano", "n");
  public final static SIUnitScale micro = new SIUnitScale(1E-6, "micro", "\u00B5");
  public final static SIUnitScale milli = new SIUnitScale(1E-3, "milli", "m");
  public final static SIUnitScale centi = new SIUnitScale(1E-2, "centi", "c");
  public final static SIUnitScale deci = new SIUnitScale(1E-1, "deci", "d");
  public final static SIUnitScale deka = new SIUnitScale(1E1, "deka", "da");
  public final static SIUnitScale hecto = new SIUnitScale(1E2, "hecto", "h");
  public final static SIUnitScale kilo = new SIUnitScale(1E3, "kilo", "k");
  public final static SIUnitScale mega = new SIUnitScale(1E6, "mega", "M");
  public final static SIUnitScale giga = new SIUnitScale(1E9, "giga", "G");
  public final static SIUnitScale tera = new SIUnitScale(1E12, "tera", "T");
  public final static SIUnitScale peta = new SIUnitScale(1E15, "peta", "P");
  public final static SIUnitScale exa = new SIUnitScale(1E18, "exa", "E");
  public final static SIUnitScale prefixes[] =
      new SIUnitScale[] {
    atto,
    femto,
    pico,
    nano,
    micro,
    milli,
    centi,
    deci,
    deka,
    hecto,
    kilo,
    mega,
    giga,
    tera,
    peta,
    exa };

  /**
   * SIUnitScale constructor comment.
   * @param factor double
   * @param name java.lang.String
   * @param prefix java.lang.String
   */
  private SIUnitScale(double factor, String name, String prefix) {
    super(factor, name, prefix);
  }
}
