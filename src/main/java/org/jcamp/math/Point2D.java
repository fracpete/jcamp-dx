/*******************************************************************************
* The JCAMP-DX project is the reference implemention of the 
* [IUPAC JCAMP-DX](http://www.jcamp-dx.org/protocols.html) spectroscopy data standard.
* 
*   Copyright (C) 2019 Lablicate GmbH
*
*    This library is free software; you can redistribute it and/or
*    modify it under the terms of the GNU Library General Public
*    License as published by the Free Software Foundation; either
*    version 2 of the License, or (at your option) any later version.
*
*    This library is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*    Library General Public License for more details.
*
* Contributors:
* Christoph Läubrich - initial API and implementation
*******************************************************************************/
package org.jcamp.math;

/**
 * Implements a point in the 2D space
 *
 */
public class Point2D {
	public double x;
	public double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
