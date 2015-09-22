/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.units;

/**
 * many predefined units
 * @author Thomas Weber
 */
public class CommonUnit {
    public static final Unit generic = BaseUnit.generic;
    public static final Unit ampere = BaseUnit.ampere;
    public static final Unit candela = BaseUnit.candela;
    public static final Unit kelvin = BaseUnit.kelvin;
    public static final Unit kilogram = BaseUnit.kilogram;
    public static final Unit meter = BaseUnit.meter;
    public static final Unit second = BaseUnit.second;
    public static final Unit mole = BaseUnit.mole;
    public static final Unit radian = BaseUnit.radian;
    public static final Unit steradian = BaseUnit.steradian;
    // derived SI units
    public static final Unit minute = new ScaledUnit(BaseUnit.second, 60, "minute", "min");
    public static final Unit hour = new ScaledUnit(BaseUnit.second, 60 * 60, "hour", "h");
    public static final Unit gram = new ScaledUnit(BaseUnit.kilogram, 1E-3, "gram", "g");
    public static final Unit meterSquared =
        new DerivedUnit(new BaseUnit[] { BaseUnit.meter }, new int[] { 2 }, "Area", "square meter", "m\u00B2");
    public static final Unit meterCubed =
        new DerivedUnit(new BaseUnit[] { BaseUnit.meter }, new int[] { 3 }, "Volume", "cubic meter", "m\u00B3");
    public static final Unit liter = new ScaledUnit((DerivedUnit) meterCubed, 1E-6, "liter", "L");
    public static final Unit kgPerMeterCubed =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter },
            new int[] { 1, -3 },
            "Density",
            "kilogram per cubic meter",
            "kg/m\u00B3");
    public static final Unit meterPerSecond =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.meter, BaseUnit.second },
            new int[] { 1, -1 },
            "Velocity",
            "meter per second",
            "m/s");
    public static final Unit meterPerSecondSquared =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.meter, BaseUnit.second },
            new int[] { 1, -2 },
            "Acceleration",
            "meter per square second",
            "m/s\u00B2");
    public static final Unit newton =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second },
            new int[] { 1, 1, -2 },
            "Force",
            "newton",
            "N");
    public static final Unit pascal =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second },
            new int[] { 1, -1, -2 },
            "Pressure",
            "pascal",
            "Pa");
    public static final Unit joule =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second },
            new int[] { 1, 2, -2 },
            "Energy",
            "joule",
            "J");
    public static final Unit watt =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second },
            new int[] { 1, 2, -3 },
            "Power",
            "watt",
            "W");
    public static final Unit volt =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second, BaseUnit.ampere },
            new int[] { 1, 2, -3, -1 },
            "Electric Potential",
            "volt",
            "V");
    public static final Unit ohm =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second, BaseUnit.ampere },
            new int[] { 1, 2, -3, -2 },
            "Electric Resistance",
            "ohm",
            "\u038f");
    public static final Unit siemens =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second, BaseUnit.ampere },
            new int[] { -1, -2, 3, 2 },
            "Electric Conductance",
            "siemens",
            "S");
    public static final Unit farad =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second, BaseUnit.ampere },
            new int[] { -1, -2, 4, 2 },
            "Electric Capacitance",
            "farad",
            "F");
    public static final Unit coulomb =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.ampere, BaseUnit.second },
            new int[] { 1, 1 },
            "Electric Charge",
            "coulomb",
            "C");
    public static final Unit weber =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.meter, BaseUnit.second, BaseUnit.ampere },
            new int[] { 1, 2, -2, -1 },
            "Magnetic Flux",
            "weber",
            "Wb");
    public static final Unit tesla =
        new DerivedUnit(
            new BaseUnit[] { BaseUnit.kilogram, BaseUnit.second, BaseUnit.ampere },
            new int[] { 1, -2, -1 },
            "Magnetic Flux Density",
            "tesla",
            "T");
    public static final Unit celsius = new OffsetUnit(BaseUnit.kelvin, 273.15, "Celsius", "\u00B0C");
    public static final Unit hertz =
        new DerivedUnit(new BaseUnit[] { BaseUnit.second }, new int[] { -1 }, "Frequency", "hertz", "Hz");
    public static final Unit perMeter =
        new DerivedUnit(new BaseUnit[] { BaseUnit.meter }, new int[] { -1 }, "Wavenumber", "per meter", "1/m");
    // imperial and old units
    public static final Unit inch = new ScaledUnit((BaseUnit) meter, 2.54E-2, "inch", "inch");
    public static final Unit cal = new ScaledUnit((DerivedUnit) joule, 4.184, "calorie", "cal");
    public static final Unit atm = new ScaledUnit((DerivedUnit) pascal, 1.01325E5, "atmosphere", "atm");
    public static final Unit torr = new ScaledUnit((DerivedUnit) pascal, 1.01325E5 / 760, "Torr", "Torr");
    public static final Unit degree = new ScaledUnit((BaseUnit) radian, Math.PI / 180.0, "degree", "\u00b0");
    // QM
    public static final Unit elementaryCharge =
        new ScaledUnit((DerivedUnit) coulomb, 1.602E-19, "elementary charge", "e");
    public static final Unit hartree = new ScaledUnit((DerivedUnit) joule, 4.36E-18, "Hartree", "Hartree");
    public static final Unit bohr = new ScaledUnit(BaseUnit.meter, 5.292E-11, "Bohr", "Bohr");
    public static final Unit eV = new ScaledUnit((DerivedUnit) joule, 96.48456, "electron volt", "eV");
    public static final Unit angstoem = new ScaledUnit(BaseUnit.meter, 1E-10, "\u00C5ngstroem", "\u00C5");
    public static final Unit relativeAtomMass = new DerivedUnit("Relative Atom Mass", "atomic mass unit", "amu");
    public static final Unit relativeElementaryCharge = new DerivedUnit("Relative Elementary Charge", "", "");
    /* 
    * for spectra e.g. JCAMP-DX
    */
    public static final Unit ppm =
        new ScaledUnit(new DimensionlessUnit("Arbitrary Unit", "", ""), 1E-6, "parts per million", "ppm");
    public static final Unit percent =
        new ScaledUnit(new DimensionlessUnit("Arbitrary Unit", "", ""), 1E-2, "percent", "%");
    public static final Unit permille =
        new ScaledUnit(new DimensionlessUnit("Arbitrary Unit", "", ""), 1E-6, "permille", "\u2030");
    public static final Unit mz = new DerivedUnit("", "m/z", "m/z");
    public static final Unit points = new DerivedUnit("Points", "", "");
    public static final Unit counts = new DimensionlessUnit("Counts", "", "");
    public static final Unit abundance = new DimensionlessUnit("Abundance", "", "");
    public static final Unit relativeAbundance = new DimensionlessUnit("Relative Abundance", "", "");
    public static final Unit intensity = new DimensionlessUnit("Intensity", "", "");
    public static final Unit absorbance = new DimensionlessUnit("Absorbance", "", "");
    public static final Unit reflectance = new DimensionlessUnit("Reflectance", "", "");
    public static final Unit transmittance = new DimensionlessUnit("Transmittance", "", "");
    public static final Unit kubelka = new AliasUnit(BaseUnit.generic, "Kubelka-Munk");
    public static final Unit percentTransmittance = new DimensionlessUnit("% Transmittance", "percent", "%");
    public static final Unit percentReflectance = new DimensionlessUnit("% Reflectance", "percent", "%");
    public static final Unit percentAbsorbance = new DimensionlessUnit("% Absorbance", "percent", "%");
    public static final Unit percentIntensity = new DimensionlessUnit("% Intensity", "percent", "%");
    public static final Unit perCM = new ScaledUnit((DerivedUnit) perMeter, 1E2, "per centimeter", "1/cm");
    public static final Unit meterWavelength =
        new DerivedUnit(new BaseUnit[] { BaseUnit.meter }, new int[] { 1 }, "Wavelength", "meter", "m");
    public static final Unit nanometerWavelength = new ScaledUnit(meterWavelength, SIUnitScale.nano);
    public static final Unit micrometerWavelength = new ScaledUnit(meterWavelength, SIUnitScale.micro);

    public static final Unit bit = new DimensionlessUnit("Bit", "bit", "bit");

}
