package org.jcamp.math;
import java.io.Serializable;
/**
 * extended array with sorted values.
 * @author Thomas Weber
 */
public abstract class OrderedArray extends Array implements Cloneable, Serializable {

    /**
     * OrderedArray constructor comment.
     */
    public OrderedArray() {
        super();
    }

    /**
     * OrderedArray constructor comment.
     * @param length int
     */
    public OrderedArray(int length) {
        super(length);
    }

    /**
     * @return java.lang.Object
     */
    public Object clone() {
        OrderedArray array = null;
        //	try {
        array = (OrderedArray) super.clone();
        //	} catch (CloneNotSupportedException e) {}
        return array;
    }
}