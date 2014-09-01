package org.jcamp.spectrum;
import java.io.Serializable;

import org.jcamp.math.IArray1D;
import org.jcamp.units.IMeasurable;
/**
 * 1D data arrays extended by a unit and label
 * @author Thomas Weber
 */
public interface IDataArray1D extends IArray1D, IMeasurable, Cloneable, Serializable {
    /**
     * sets data label
     * 
     * @param label java.lang.String
     */
    void setLabel(String label);

    public Object clone();

    /**
     * get data label.
     * 
     * @return java.lang.String
     */
    String getLabel();
}