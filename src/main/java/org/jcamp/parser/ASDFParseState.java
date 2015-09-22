/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
package org.jcamp.parser;

final class ASDFParseState {
	private int currentLineNumber = 1;
	private int currentIndex = -1;
 	private int currentY = 0;
	private double checkX = 0.0;
	private double checkY = 0;
	public ASDFParseState() {
	}
	public double getCheckX() { 
		return this.checkX; 
	}
	public double getCheckY() { 
		return this.checkY; 
	}
	public int getCurrentIndex() { 
		return this.currentIndex; 
	}
	public int getCurrentLineNumber() { 
		return this.currentLineNumber; 
	}
	public int getCurrentY() { 
		return this.currentY; 
	}
	void incrCurrentIndex() {
		currentIndex ++;
	}
	void incrCurrentIndex(int incr) {
		currentIndex += incr;	
	}
	void incrCurrentLineNumber() {
		currentLineNumber ++;
	}
	void setCheckX(double x) 
	{
		this.checkX = x;
	}
	void setCheckY(double y) 
	{
		this.checkY = y;
	}
	public void setCurrentIndex(int index) { 
		this.currentIndex = index; 	
	}
	public void setCurrentY(int y) 
	{ 
		this.currentY = y; 	
	}
}
