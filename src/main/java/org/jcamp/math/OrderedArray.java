/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
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
