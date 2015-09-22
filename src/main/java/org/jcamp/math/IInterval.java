/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.math;

/**
 * interface for data that is bounded by an interval
 * 
 * @author Thomas Weber
 */
public interface IInterval {
  
  /**
   * return interval range
   * @return Range.Double
   */
  public Range.Double getRange();
}
