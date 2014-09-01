header {
package org.jcamp.parser;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
}

class ASDFParser extends Parser;
options {
	importVocab=ASDFLexer;
	defaultErrorHandler=false;
}

{
	/**
	 * Log4J
	 */
    private static Log log = LogFactory.getLog(ASDFLexer.class);
	protected final static ASDFCheckHandler IGNORE_HANDLER = new ASDFCheckHandler() {
		public int check(ASDFParseState state) {
			return ASDFCheckHandler.IGNORE;
		}
 	};
	private static class LineContent {
		public double xCheck;
		public Vector yVector = null;
	};
	/**
	 * current difdup mode
	 */
	protected boolean currentInDIF = false;
	protected boolean lastInDIF = false;
	/**
	 * accumulated y values
	 */
	protected Vector yValues = new Vector();
	/** 
	 * parsing state for checks
	 */
	protected ASDFParseState state = null;
	/**
	 * handler performing x-sequence and y-value checks
	 */
	protected ASDFCheckHandler checkHandler = IGNORE_HANDLER;
	/**
	 * sets active x-sequence/y-value check handler to <code>handler</code>
	 * if null, an ignoring handler is set
	 */
	public void setCheckHandler(ASDFCheckHandler handler) {
		if (handler == null)
			checkHandler = IGNORE_HANDLER;
		else
			checkHandler = handler;
	}
	/**
	 * gets array of parsed y-data
	 * @return int[]
	 */
	public int[] getYData() {
		int n = yValues.size();
		int[] y = new int[n];
		for (int i=0; i<n; i++ )
		{
			y[i] = ((Integer) yValues.elementAt(i)).intValue();
		}
		return y;
	}
	
	/**
	 * parses with an expected size of 32000 data points
	 * @param int expectedSize 
	 */
	public void parse() throws JCAMPException {
		parse(32000);
	}
	/**
	 * parses an ASDF block
	 * @param int expectedSize 
	 */
	public void parse(int expectedSize) throws JCAMPException {
		try
		{
			block(expectedSize);
		} catch (RecognitionException e)
		{
			log.error("parsing failed");
			throw new JCAMPException("parsing failed\n"+e.getMessage());
		} 
		catch (TokenStreamException e)
		{
			log.error("parsing failed");
			throw new JCAMPException("parsing failed\n"+e.getMessage());
		}
	}
	/**
	 * parsing from standard in
	 */
	public static void main(String[] args) {
        ASDFLexer lexer = new ASDFLexer(System.in);
        ASDFParser parser = new ASDFParser(lexer);
        try {
                parser.parse();
        }
        catch (Exception e) {
                System.err.println(e);
        }
		int[] y = parser.getYData();
		for (int i=0; i<y.length ;i++ )
		{
			System.out.println(i+"\t"+y[i]);
		}
	}
	
	protected int parsePAC(Token p) {
		int v = 0;
		int i = 0;
		boolean negative = false;
		char[] c = p.getText().toCharArray();
		if (c[0] == '-') {
			i++;
			negative = true;
		} else if (c[0] == '+') {
			i++;
		}
		v = (int) (c[i] - '0');	
		i++;
		while (i<c.length)
		{
			v *= 10;
			v += (int) (c[i]-'0');
			i++;
		}
		if (negative) 
			v = -v;
		if (log.isDebugEnabled())
			log.debug("PAC-Number: "+Integer.toString(v));
		return(v);
	}
	
	protected int parsePOSSQZ(Token p) {
		int v = 0;
		char[] c = p.getText().toCharArray();
		if (c[0] == '@') 
			v = 0;
		else  
			v = (int) (c[0]-'A')+1;
		for (int i=1; i<c.length;i++ )
		{
			v *= 10;
			v += (int) (c[i]-'0');
		}
		if (log.isDebugEnabled())
			log.debug("SQZ-Number: "+Integer.toString(v));
		return(v);
	}

	protected int parseNEGSQZ(Token n) {
		int v = 0;
		char[] c = n.getText().toCharArray();
		v = (int) (c[0]-'a')+1;
		for (int i=1; i<c.length;i++ )
		{
			v *= 10;
			v += (int) (c[i]-'0');
		}
		v = -v;
		if (log.isDebugEnabled())
			log.debug("SQZ-Number: "+Integer.toString(v));
		return(v);
	}
	
	protected int parsePOSDIF(Token p) {
		int v = 0;
		char[] c = p.getText().toCharArray();
		if (c[0] == '%') 
			v = 0;
		else  
			v = (int) (c[0]-'J')+1;
		for (int i=1; i<c.length;i++ )
		{
			v *= 10;
			v += (int) (c[i]-'0');
		}
		if (log.isDebugEnabled())
			log.debug("DIF-Number: "+Integer.toString(v));
		return(v);
		
	}
	protected int parseNEGDIF(Token n) {
		int v = 0;
		char[] c = n.getText().toCharArray();
		v = (int) (c[0]-'j')+1;
		for (int i=1; i<c.length;i++ )
		{
			v *= 10;
			v += (int) (c[i]-'0');
		}
		v = -v;
		if (log.isDebugEnabled())
			log.debug("DIF-Number: "+Integer.toString(v));
		return(v);
	}

	protected int parseDUP(Token d) {
		int v = 0;
		char[] c = d.getText().toCharArray();
		if (c[0] == 's') 
			v = 9;
		else  
			v = (int) (c[0]-'S')+1;
		for (int i=1; i<c.length;i++ )
		{
			v *= 10;
			v += (int) (c[i]-'0');
		}
		if (log.isDebugEnabled())
			log.debug("DUP-Number: "+Integer.toString(v));
		return(v);
	}
	protected void error(String msg) throws JCAMPException {
		log.error(msg);
		throw new JCAMPException(msg);
	}
}

protected
pacNumber returns [int v] 
{
	v = 0;
}
	: (WS)? (p:PAC { v = parsePAC(p); } )
	;


protected
sqzNumber returns [int v] 
{
	v = 0;
}
	: (p:POSSQZ) 
		{
			v = parsePOSSQZ(p); 
		}
	| (n:NEGSQZ)
		{
			v = parseNEGSQZ(n);
		}
	;


protected
difNumber returns [int v] 
{
	v = 0;
}
	: (p:POSDIF) 
		{
			v = parsePOSDIF(p);
		}
	| (n:NEGDIF) 
		{
			v = parseNEGDIF(n);
		}
	;

protected
dupNumber returns [int v]
{
	v = 0;
}
	: (d:DUP) 
		{
			v = parseDUP(d);
		}
	;



protected
xcheck returns [double d] 
{
	d = 0;
}
	: 
	  (x:XCHECK
	  	{ 
	  		d = Double.valueOf(x.getText()).doubleValue();
			if (log.isDebugEnabled())
				log.debug("X-Check: "+Double.toString(d));
		}
	  ) 
 	;


protected
difNumbers returns [Vector v]
{
	int s = 0;
	int d = 0;
	int u = 0;
	v = new Vector(80); // initial capacity of 80 data points per line
}
	:  // first is always in SQZ format
	  ( 
		(s=sqzNumber
			{ 
				v.addElement(new Integer(s));
				currentInDIF = false;
	 		}
		)
		// if an error is the first value, repeat last value of previous line
		| (ERROR)
			{
				if (yValues.size()>0)
					v.addElement(yValues.lastElement());
				else
					v.addElement(new Integer(0));
				currentInDIF = false;
			}
	  ) 
	  // following can be SQZ, DIF or DUP
	  (
		(s=sqzNumber
		  	{ 
				currentInDIF = false;
				v.addElement(new Integer(s)); 
			}
		)
	   |(d=difNumber 
	   		{ 
				currentInDIF = true;
				int l = ((Integer) v.lastElement()).intValue();
				v.addElement(new Integer(l+d)); 
			}
		)
	   |(u=dupNumber 
	   		{
				if (currentInDIF) {
					int l = v.size();
					// calculate last difference
					int l0 = ((Integer) v.elementAt(l-1)).intValue();
					int l1 = ((Integer) v.elementAt(l-2)).intValue();
					int dif = l0-l1;
					// repeat addition of difference u-1 times
					for (int i=1; i<u; i++) {
						l0 += dif;
						v.addElement(new Integer(l0));
					}
				} else { 
					// get last value
					int l = ((Integer) v.lastElement()).intValue();
					// repeat last value u-1 times
					for (int i=1; i<u; i++) {
						v.addElement(new Integer(l));
					}
				}
			}
		)
		// in case of an error, simply repeat last value
		| (ERROR)
			{
				v.addElement(v.lastElement());
			}
	   )*
	;

protected
pacNumbers returns [Vector v]
{
	int p = 0;
	v = new Vector(80); // initial capacity of 80 data points per line
}
	:  
		(
			( 
				p=pacNumber 
				{
					v.addElement(new Integer(p));
				}
			) (WS)?
		)+ 
	;


protected
line returns [LineContent lineContent]	throws JCAMPException
{
	lineContent = new LineContent();
	double x;
	Vector y;
}
	:  	
	  	(WS)?	
		(x=xcheck {	
			lineContent.xCheck = x; 
		})
		(WS)? 
	  	(
			(
	  		y=difNumbers EOL 
		  		{ 
				if (y == null) {
					error( "ASDF parsing failed in line "  
						+ Integer.toString(state.getCurrentLineNumber()));
				}
				lineContent.yVector = y;
				}
		    )
	    | 	(
		  	y=pacNumbers EOL 
			  	{ 
				if (y == null) {
					error("ASDF parsing failed in line "  
						+ Integer.toString(state.getCurrentLineNumber()));
				}
				lineContent.yVector = y;
				}
			)
		)
	;


protected
block[int expectedSize] throws JCAMPException
{
	LineContent lineContent = null;
	state = new ASDFParseState();
	yValues.setSize(0);
	yValues.ensureCapacity(expectedSize);
	currentInDIF = lastInDIF = false;
}
	: (lineContent=line
		{
			int indexBeforeCheck;
			state.setCheckX(lineContent.xCheck);
			if (lastInDIF) { 
				// last y-value was in DIF mode 
				// first y-element is y-value check
				indexBeforeCheck = state.getCurrentIndex();
				state.setCheckY(((Integer) lineContent.yVector.elementAt(0)).intValue());
				state.setCurrentY(((Integer) yValues.lastElement()).intValue());
			} else 	{
				// second element is a real data point
				// insert it into data point array and increase index
				Integer y0 = (Integer) lineContent.yVector.elementAt(0);
 				yValues.addElement(y0);
				state.incrCurrentIndex();
				indexBeforeCheck = state.getCurrentIndex();
				// insert dummy check values
				state.setCheckY(y0.intValue());
				state.setCurrentY(y0.intValue());
			}	
			
			switch (checkHandler.check(state)) {
				case ASDFCheckHandler.IGNORE:
					break;
				case ASDFCheckHandler.ABORT: {
					error("ASDF parsing aborted, failed check in line "
						+Integer.toString(state.getCurrentLineNumber()));
				}
				case ASDFCheckHandler.REPLACE: {
					// compare indices
					int indexAfterCheck = state.getCurrentIndex();
					if (indexBeforeCheck < indexAfterCheck) {
						// if new index is greater, insert error values (last value so far)
						Integer errorValue = (Integer) yValues.lastElement();
						for (int i=indexBeforeCheck; i<indexAfterCheck; i++ )
						{
							yValues.addElement(errorValue);
						}
					} else if (indexBeforeCheck>indexAfterCheck) {
						// if new index is lesser, remove values
						yValues.setSize(indexAfterCheck+1);
						yValues.setElementAt(new Integer(state.getCurrentY()), indexAfterCheck);
					}
					else {
						// simply replace current value
						yValues.setElementAt(new Integer(state.getCurrentY()), indexAfterCheck);
					}
					break;
				}
			}		
			// insert rest of yValues vector starting at second element 
			for (int i=1; i<lineContent.yVector.size(); i++)
			{
 				yValues.addElement(lineContent.yVector.elementAt(i));
				state.incrCurrentIndex();
			}
			state.incrCurrentLineNumber();
			lastInDIF = currentInDIF;
		}
		)+ 
	;
	