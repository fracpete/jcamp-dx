header{
package org.jcamp.parser;
import antlr.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
}

class ASDFLexer extends Lexer;

options {
 exportVocab=ASDFLexer;
 k=2;
 defaultErrorHandler=false;
}

{
    private static Log log = LogFactory.getLog(ASDFLexer.class);
	private boolean atBOL = true;
}
protected
DIGIT :  '0' .. '9' 
	;
	
protected
SIGN :  '+' | '-'  
	 ;

EOL : ('\n'|'\r'('\n')?)	
	{
		if (log.isDebugEnabled())
			log.debug("EOL-Token");
		newline();
		atBOL = true;
	}
   ; 
	 
XCHECK
	:
  	  (SIGN)? (DIGIT)* ('.')? (DIGIT)+ {
	  	if (!atBOL) {
			_ttype = PAC;
			if (log.isDebugEnabled())
				log.debug("PAC-Token");
		} else {
			if (log.isDebugEnabled())
				log.debug("XCHECK-Token");
		}
		atBOL = false;	
	}
 	;

PAC 
	: (SIGN)? (DIGIT)+ {
 		if (log.isDebugEnabled())
			log.debug("PAC-Token");
	}
	;
	
POSSQZ
	: ( '@'|'A' .. 'I' ) (DIGIT)* {
		if (log.isDebugEnabled())
			log.debug("POSSQZ-Token");
	}
	;

NEGSQZ
	: ( 'a' .. 'i' ) (DIGIT)* {
		if (log.isDebugEnabled())
			log.debug("NEGSQZ-Token");
	}
	;

POSDIF
	: ( '%' | 'J' .. 'R' ) (DIGIT)* {
		if (log.isDebugEnabled())
			log.debug("POSDIF-Token");
	}
	;

NEGDIF	
	: ( 'j' .. 'r' ) (DIGIT)* {
		if (log.isDebugEnabled())
			log.debug("NEGDIF-Token");
	}
	;

DUP 
	: ( 'S' .. 'Z' | 's' ) (DIGIT)* {
		if (log.isDebugEnabled())
			log.debug("DUP-Token");
	}
	;

WS 
	:  (' ' | '\t')+   
	;

ERROR 
	: '?'
	;



 // Single-line comments
protected SL_COMMENT	
	:	"$$" (~('\n'|'\r'))* (EOL)	{
				_ttype = EOL; 
			}	
	;
