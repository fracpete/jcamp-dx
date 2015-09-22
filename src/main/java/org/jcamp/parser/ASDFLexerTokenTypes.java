/*******************************************************************************
 * Copyright (c) 2015.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 ******************************************************************************/
// $ANTLR 2.7.1: "ASDFLexer.g" -> "ASDFLexer.java"$

package org.jcamp.parser;

public interface ASDFLexerTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int DIGIT = 4;
	int SIGN = 5;
	int EOL = 6;
	int XCHECK = 7;
	int PAC = 8;
	int POSSQZ = 9;
	int NEGSQZ = 10;
	int POSDIF = 11;
	int NEGDIF = 12;
	int DUP = 13;
	int WS = 14;
	int ERROR = 15;
	int SL_COMMENT = 16;
}
