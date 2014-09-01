package org.jcamp.math;

/**
 * interface for data arrays that are bounded by an interval
 * @author Thomas Weber
 */
public interface IInterval2D extends IInterval {
    Range2D.Double getRange2D();
}
