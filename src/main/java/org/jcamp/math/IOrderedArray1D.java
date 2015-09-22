/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * real interval with monotone ordered discrete points.
 * allows assignment of a single discrete point/index to a position within the interval. 
 * 
 * @author Thomas Weber
 */
public interface IOrderedArray1D
  extends IArray1D {
  
  public int indexAt(double p);
}
