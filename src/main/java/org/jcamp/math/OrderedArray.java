package org.jcamp.math;

import java.io.Serializable;

/**
 * extended array with sorted values.
 * 
 * @author Thomas Weber
 */
public abstract class OrderedArray
  extends Array
  implements Cloneable, Serializable {

  /** for serialization. */
  private static final long serialVersionUID = 5527645205009351239L;

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
  @Override
  public Object clone() {
    OrderedArray array = null;
    //	try {
    array = (OrderedArray) super.clone();
    //	} catch (CloneNotSupportedException e) {}
    return array;
  }
}