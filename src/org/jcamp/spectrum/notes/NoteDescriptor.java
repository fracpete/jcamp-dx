package org.jcamp.spectrum.notes;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jcamp.spectrum.ISpectrumIdentifier;
import org.jcamp.spectrum.Spectrum;
import org.jcamp.units.Unit;
/**
 * descriptor for spectrum notes.
 * @author Thomas Weber
 */
public class NoteDescriptor implements Cloneable, Serializable {
    /**
     * default note content parser that supports basic Java types and Date.
     * @author Thomas Weber
     */
    private final static INoteContentParser DEFAULT_NOTECONTENT_PARSER = DefaultNoteContentParser.getInstance();
    private java.lang.Class fieldNoteContentClass = String.class;
    private INoteContentParser fieldNoteContentParser = DEFAULT_NOTECONTENT_PARSER;
    private Unit fieldUnit = null;
    private boolean fieldUnique = true;
    private boolean fieldDefault = false;
    private java.lang.String fieldName = new String();
    private java.lang.String fieldKey = new String();
    private static Hashtable notesDescriptors = new Hashtable();
    private java.lang.String fieldParentKey = null;
    private int fieldDeniedMask = 0;
    private int fieldAllowedMask = -1;
    public static NoteDescriptor TITLE;
    public static NoteDescriptor OWNER;
    public static NoteDescriptor ORIGIN;
    public static NoteDescriptor JCAMPDX;
    public static NoteDescriptor IGNORE;
    public static  java.io.InputStream is; 
    static {
        initDefaults();
        initNoteDescriptors();
    }
    /**
     * default ctor;
     * 
     * @param name java.lang.String
     */
    public NoteDescriptor(String key) {
        this(key, key);
    }
    /**
     * default ctor;
     * 
     * @param name java.lang.String
     */
    public NoteDescriptor(String key, String name) {
        this(key, name, false);
    }
    /**
     * ctor for predefined Notes
     * 
     * @param name java.lang.String
     */
    private NoteDescriptor(String key, String name, Class noteClass, boolean isUnique, boolean isDefault) {
        setKey(key);
        setName(name);
        setNoteContentClass(noteClass);
        setUnique(isUnique);
        setDefault(isDefault);
        if (isDefault) {
            notesDescriptors.put(key, this);
        }
    }
    /**
     * 
     * @param key String
     * @param name String
     * @param isDefault boolean
     */
    private NoteDescriptor(String key, String name, boolean isDefault) {
        setKey(key);
        setName(name);
        setDefault(isDefault);
        setUnique(true);
        if (isDefault) {
            notesDescriptors.put(key, this);
        }
    }
    /**
     * cloning.
     * 
     * @return java.lang.Object
     */
    public Object clone() {
        NoteDescriptor desc = null;
        try {
            desc = (NoteDescriptor) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        desc.fieldUnit = (Unit) this.fieldUnit.clone();
        return desc;
    }
    /**
     * create mask of spectrum types for this note.
     * @return int
     * @param maskStr java.lang.String
     */
    private static int createSpectrumMask(String maskStr) {
        int mask = 0;
        int ALL = -1;
        StringTokenizer tokenizer = new StringTokenizer(maskStr, "|");
        while (tokenizer.hasMoreTokens()) {
            boolean notFlag = false;
            String token = tokenizer.nextToken();
            if (token.charAt(0) == '^') {
                notFlag = true;
                token = token.substring(1);
            }
            if (token.equalsIgnoreCase("ir"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.IR) : ISpectrumIdentifier.IR);
            else if (token.equalsIgnoreCase("uv"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.UV) : ISpectrumIdentifier.UV);
            else if (token.equalsIgnoreCase("ms"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.MS) : ISpectrumIdentifier.MS);
            else if (token.equalsIgnoreCase("nmr"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.NMR) : ISpectrumIdentifier.NMR);
            else if (token.equalsIgnoreCase("raman"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.RAMAN) : ISpectrumIdentifier.RAMAN);
            else if (token.equalsIgnoreCase("flourescence"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.FLUORESCENCE) : ISpectrumIdentifier.FLUORESCENCE);
            else if (token.equalsIgnoreCase("chromatogram"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.CHROMATOGRAM) : ISpectrumIdentifier.CHROMATOGRAM);
            else if (token.equalsIgnoreCase("2d"))
                mask |= (notFlag ? (ALL ^ ISpectrumIdentifier.SPEC2D) : ISpectrumIdentifier.SPEC2D);
            else if (token.equalsIgnoreCase("all")) {
                mask = (notFlag ? 0 : ALL);
                break;
            }
        }
        return mask;
    }
    /**
     * comparison.
     * 
     * @return boolean
     * @param obj java.lang.Object
     */
    public boolean equals(Object obj) {
        if (obj instanceof NoteDescriptor && ((NoteDescriptor) obj).getKey().equals(fieldKey))
            return true;
        return false;
    }
    /**
     * accessor method to hashtable.
     * 
     * @return com.creon.chem.spectrum.NoteDescriptor
     * @param key java.lang.String
     */
    public static NoteDescriptor findByKey(String key) {
        return (NoteDescriptor) notesDescriptors.get(key);
    }
    /**
     * gets the allowedSpectrumMask property (int) value. It is a mask of allowed ISpectrumIdentifier.
     * @return The allowedSpectrumMask property value.
     * @see #setAllowedSpectrumMask
     * @see ISpectrumIdentifier
     */
    public int getAllowedSpectrumMask() {
        return fieldAllowedMask;
    }
    /**
     * gets the deniedSpectrumMask property (int) value. It is a mask of denied ISpectrumIdentifier.
     * @return The deniedSpectrumMask property value.
     * @see #setDeniedSpectrumMask
     * @see ISpectrumIdentifier
     */
    public int getDeniedSpectrumMask() {
        return fieldDeniedMask;
    }
    /**
     * Gets the key property (java.lang.String) value.
     * @return The key property value.
     * @see #setKey
     */
    public java.lang.String getKey() {
        return fieldKey;
    }
    /**
     * Gets the name property (java.lang.String) value.
     * @return The name property value.
     * @see #setName
     */
    public java.lang.String getName() {
        return fieldName;
    }
    /**
     * Gets the noteContentClass property (java.lang.Class) value.
     * @return The noteContentClass property value.
     * @see #setNoteContentClass
     */
    public java.lang.Class getNoteContentClass() {
        return fieldNoteContentClass;
    }
    /**
     * Gets the noteContentParser property (java.lang.Class) value.
     * @return The noteContentParser property value.
     * @see #setNoteContentParser
     */
    public INoteContentParser getNoteContentParser() {
        return fieldNoteContentParser;
    }
    /**
     * Gets the parentKey property (java.lang.String) value.
     * @return The parentKey property value.
     * @see #setParentKey
     */
    public java.lang.String getParentKey() {
        return fieldParentKey;
    }
    /**
     * Gets the unit property (Unit) value.
     * @return The unit property value.
     * @see #setUnit
     */
    public Unit getUnit() {
        return fieldUnit;
    }
    /**
     * gets hash value.
     * 
     * @return int
     */
    public int hashCode() {
        return fieldKey.hashCode();
    }
    /**
     * Insert the method's description here.
     * 
     */
    private static void initDefaults() {
        TITLE = new NoteDescriptor("title", "Title", String.class, true, true);
        OWNER = new NoteDescriptor("owner", "Owner", String.class, true, true);
        ORIGIN = new NoteDescriptor("origin", "Origin", String.class, true, true);
        JCAMPDX = new NoteDescriptor("jcampdx", "JCAMP-DX Version", String.class, true, true);
        IGNORE = new NoteDescriptor("#IGNORE", "", String.class, true, true);
    }
    /**
     * initialize table of note descriptors from default property file.
     * 
     */
    private static void initNoteDescriptors() {
        Properties notesProps = new Properties();
        is = null;
        try {
            UnitParser unitParser = new UnitParser();
	    //is not working!! seems to null everytime
            is = NoteDescriptor.class.getResourceAsStream("notes.properties");
	    if (is == null)
                return;
            notesProps.load(is);
            Enumeration notesNames = notesProps.propertyNames();
            while (notesNames.hasMoreElements()) {
                String key = (String) notesNames.nextElement();
                if (key.indexOf('.') < 0) {
                    String displayName = (String) notesProps.get(key);
                    String notesClassName = (String) notesProps.get(key + ".class");
                    Class notesClass;
                    if (notesClassName == null)
                        notesClass = String.class;
                    else {
                        try {
                            notesClass = Class.forName(notesClassName);
                        } catch (ClassNotFoundException e) {
                            notesClass = String.class;
                        }
                    }

                    String parserClassName = (String) notesProps.get(key + ".parser");
                    INoteContentParser parser;
                    if (parserClassName == null)
                        parser = DEFAULT_NOTECONTENT_PARSER;
                    else {
                        try {
                            parser = (INoteContentParser) (Class.forName(parserClassName).newInstance());
                        } catch (Exception e) {
                            parser = DEFAULT_NOTECONTENT_PARSER;
                        }
                    }
                    String uniqueStr = (String) notesProps.get(key + ".unique");
                    boolean unique = true;
                    if (uniqueStr != null)
                        unique = Boolean.valueOf(uniqueStr).booleanValue();

                    String parentKey = (String) notesProps.get(key + ".parent");

                    String allowedMaskStr = (String) notesProps.get(key + ".allowed");
                    int allowedMask = -1;
                    if (allowedMaskStr != null)
                        allowedMask = createSpectrumMask(allowedMaskStr);

                    String deniedMaskStr = (String) notesProps.get(key + ".denied");
                    int deniedMask = 0;
                    if (deniedMaskStr != null)
                        deniedMask = createSpectrumMask(deniedMaskStr);

                    Unit unit = null;
                    String unitStr = (String) notesProps.get(key + ".unit");
                    if (unitStr != null)
                        unit = (Unit) unitParser.parseContent(unitStr, Unit.class);

                    NoteDescriptor descr = new NoteDescriptor(key, displayName, notesClass, unique, true);
                    descr.setNoteContentParser(parser);
                    descr.setUnit(unit);
                    descr.setParentKey(parentKey);
                    descr.setAllowedSpectrumMask(allowedMask);
                    descr.setDeniedSpectrumMask(deniedMask);

                }
            }
        } catch (java.io.IOException e) {
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
            }
        }
    }
    /**
     * check if note is allowed for spectrum.
     * @return boolean
     * @param spectrum com.creon.chem.spectrum.Spectrum
     */
    public boolean isAllowedForSpectrum(Spectrum spectrum) {
        return isAllowedForSpectrumID(spectrum.getIdentifier());
    }
    /**
     * check if note is allowed for spectrum identifier.
     * @return boolean
     * @param id int
     * @see ISpectrumIdentifier
     */
    public boolean isAllowedForSpectrumID(int id) {
        if ((id & fieldAllowedMask) == 0)
            return false;
        if ((id & fieldDeniedMask) == id)
            return false;
        return true;
    }
    /**
     * Gets the default property (boolean) value.
     * @return The default property value.
     * @see #setDefault
     */
    public boolean isDefault() {
        return fieldDefault;
    }
    /**
     * indicates that note is global (valid for all spectrum types).
     * @return boolean
     */
    public boolean isGlobal() {
        return fieldAllowedMask == -1 && fieldDeniedMask == 0;
    }
    /**
     * Gets the unique property (boolean) value.
     * @return The unique property value.
     * @see #setUnique
     */
    public boolean isUnique() {
        return fieldUnique;
    }
    /**
     * sets the allowedSpectrumMask property (int) value.
     * @param allowedSpectrumMask The new value for the property.
     * @see #getAllowedSpectrumMask
     */
    public void setAllowedSpectrumMask(int spectrumMask) {
        fieldAllowedMask = spectrumMask;
    }
    /**
     * Sets the default property (boolean) value.
     * @param isDefault The new value for the property.
     * @see #getDefault
     */
    private void setDefault(boolean isDefault) {
        fieldDefault = isDefault;
    }
    /**
     * sets the deniedSpectrumMask property (int) value.
     * @param deniedSpectrumMask The new value for the property.
     * @see #getDeniedSpectrumMask
     */
    public void setDeniedSpectrumMask(int spectrumMask) {
        fieldDeniedMask = spectrumMask;
    }
    /**
     * Sets the key property (java.lang.String) value.
     * @param key The new value for the property.
     * @see #getKey
     */
    public void setKey(java.lang.String key) {
        fieldKey = key;
    }
    /**
     * Sets the name property (java.lang.String) value.
     * @param name The new value for the property.
     * @see #getName
     */
    public void setName(java.lang.String name) {
        fieldName = name;
    }
    /**
     * Sets the noteContentClass property (java.lang.Class) value.
     * @param noteContentClass The new value for the property.
     * @see #getNoteContentClass
     */
    public void setNoteContentClass(java.lang.Class noteClass) {
        fieldNoteContentClass = noteClass;
    }
    /**
     * Sets the noteContentParser property (java.lang.Class) value.
     * @param noteContentParser The new value for the property.
     * @see #getNoteContentParser
     */
    public void setNoteContentParser(INoteContentParser parser) {
        fieldNoteContentParser = parser;
    }
    /**
     * Sets the parentKey property (java.lang.String) value.
     * @param parentKey The new value for the property.
     * @see #getParentKey
     */
    public void setParentKey(java.lang.String parentKey) {
        fieldParentKey = parentKey;
    }
    /**
     * Sets the unique property (boolean) value.
     * @param unique The new value for the property.
     * @see #getUnique
     */
    public void setUnique(boolean unique) {
        fieldUnique = unique;
    }
    /**
     * Sets the Unit property (Unit) value.
     * @param Unit The new value for the property.
     * @see #getUnit
     */
    public void setUnit(Unit unit) {
        fieldUnit = unit;
    }
    /**
     * string display.
     * 
     * @return java.lang.String
     */
    public String toString() {
        return getKey();
    }
}
