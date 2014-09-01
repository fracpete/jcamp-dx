package org.jcamp.parser;

// $ANTLR 2.7.1: "ASDFParser.g" -> "ASDFParser.java"$

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;

public class ASDFParser extends antlr.LLkParser implements ASDFParserTokenTypes {
    private static Log log = LogFactory.getLog(ASDFParser.class);
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
    public static final String[] _tokenNames =
        {
            "<0>",
            "EOF",
            "<2>",
            "NULL_TREE_LOOKAHEAD",
            "DIGIT",
            "SIGN",
            "EOL",
            "XCHECK",
            "PAC",
            "POSSQZ",
            "NEGSQZ",
            "POSDIF",
            "NEGDIF",
            "DUP",
            "WS",
            "ERROR",
            "SL_COMMENT" };

    private static final long _tokenSet_0_data_[] = { 50944L, 0L };
    public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);

    public ASDFParser(ParserSharedInputState state) {
        super(state, 1);
        tokenNames = _tokenNames;
    }
    public ASDFParser(TokenBuffer tokenBuf) {
        this(tokenBuf, 1);
    }
    protected ASDFParser(TokenBuffer tokenBuf, int k) {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }
    public ASDFParser(TokenStream lexer) {
        this(lexer, 1);
    }
    protected ASDFParser(TokenStream lexer, int k) {
        super(lexer, k);
        tokenNames = _tokenNames;
    }
    protected final void block(int expectedSize) throws RecognitionException, TokenStreamException, JCAMPException {
    	
        LineContent lineContent = null;
        state = new ASDFParseState();
        yValues.setSize(0);
        yValues.ensureCapacity(expectedSize);
        currentInDIF = lastInDIF = false;

        {
            int _cnt38 = 0;
            _loop38 : do {
                if ((LA(1) == XCHECK || LA(1) == WS)) {
                    lineContent = line();

                    int indexBeforeCheck;
                    state.setCheckX(lineContent.xCheck);
                    if (lastInDIF) {
                        // last y-value was in DIF mode 
                        // first y-element is y-value check
                        indexBeforeCheck = state.getCurrentIndex();
                        state.setCheckY(((Double) lineContent.yVector.elementAt(0)).doubleValue());
                        state.setCurrentY(((Double)yValues.lastElement()).intValue());
                    } else {
                        // second element is a real data point
                        // insert it into data point array and increase index
                        Double y0 = (Double) lineContent.yVector.elementAt(0);
                        yValues.addElement(y0);
                        state.incrCurrentIndex();
                        indexBeforeCheck = state.getCurrentIndex();
                        // insert dummy check values
                        state.setCheckY(y0.intValue());
                        state.setCurrentY(y0.intValue());
                    }

                    switch (checkHandler.check(state)) {
                        case ASDFCheckHandler.IGNORE :
                            break;
                        case ASDFCheckHandler.ABORT :
                            {
                                error(
                                    "ASDF parsing aborted, failed check in line "
                                        + Integer.toString(state.getCurrentLineNumber()));
                            }
                        case ASDFCheckHandler.REPLACE :
                            {
                                // compare indices
                                int indexAfterCheck = state.getCurrentIndex();
                                if (indexBeforeCheck < indexAfterCheck) {
                                    // if new index is greater, insert error values (last value so far)
                                    Double errorValue = (Double) yValues.lastElement();
                                    for (int i = indexBeforeCheck; i < indexAfterCheck; i++) {
                                        yValues.addElement(errorValue);
                                    }
                                } else if (indexBeforeCheck > indexAfterCheck) {
                                    // if new index is lesser, remove values
                                    yValues.setSize(indexAfterCheck + 1);
                                    yValues.setElementAt(new Double(state.getCurrentY()), indexAfterCheck);
                                } else {
                                    // simply replace current value
                                    yValues.setElementAt(new Double(state.getCurrentY()), indexAfterCheck);
                                }
                                break;
                            }
                    }
                    // insert rest of yValues vector starting at second element 
                    for (int i = 1; i < lineContent.yVector.size(); i++) {
                        yValues.addElement((Double)lineContent.yVector.elementAt(i));
                        state.incrCurrentIndex();
                    }
                    state.incrCurrentLineNumber();
                    lastInDIF = currentInDIF;

                } else {
                    if (_cnt38 >= 1) {
                        break _loop38;
                    } else {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }

                _cnt38++;
            } while (true);
        }
    }
    protected final int difNumber() throws RecognitionException, TokenStreamException {
        int v;

        Token p = null;
        Token n = null;

        v = 0;

        switch (LA(1)) {
            case POSDIF :
                {
                    {
                        p = LT(1);
                        match(POSDIF);
                    }

                    v = parsePOSDIF(p);

                    break;
                }
            case NEGDIF :
                {
                    {
                        n = LT(1);
                        match(NEGDIF);
                    }

                    v = parseNEGDIF(n);

                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return v;
    }
    protected final Vector difNumbers() throws RecognitionException, TokenStreamException {
        Vector v;

        int s = 0;
        int d = 0;
        int u = 0;
        v = new Vector(80); // initial capacity of 80 data points per line

        {
            switch (LA(1)) {
                case POSSQZ :
                case NEGSQZ :
                    {
                        {
                            s = sqzNumber();

                            v.addElement(new Double(s));
                            currentInDIF = false;

                        }
                        break;
                    }
                case ERROR :
                    {
                        {
                            match(ERROR);
                        }

                        if (yValues.size() > 0)
                            v.addElement(yValues.lastElement());
                        else
                            v.addElement(new Double(0));
                        currentInDIF = false;

                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            _loop23 : do {
                switch (LA(1)) {
                    case POSSQZ :
                    case NEGSQZ :
                        {
                            {
                                s = sqzNumber();

                                currentInDIF = false;
                                v.addElement(new Double(s));

                            }
                            break;
                        }
                    case POSDIF :
                    case NEGDIF :
                        {
                            {
                                d = difNumber();

                                currentInDIF = true;
                                double l = ((Double) v.lastElement()).doubleValue();
                                v.addElement(new Double(l + d));

                            }
                            break;
                        }
                    case DUP :
                        {
                            {
                                u = dupNumber();

                                if (currentInDIF) {
                                    int l = v.size();
                                    // calculate last difference
                                    double l0 = ((Double) v.elementAt(l - 1)).doubleValue();
                                    double l1 = ((Double) v.elementAt(l - 2)).doubleValue();
                                    double dif = l0 - l1;
                                    // repeat addition of difference u-1 times
                                    for (int i = 1; i < u; i++) {
                                        l0 += dif;
                                        v.addElement(new Double(l0));
                                    }
                                } else {
                                    // get last value
                                	double l = ((Double) v.lastElement()).doubleValue();
                                    // repeat last value u-1 times
                                    for (int i = 1; i < u; i++) {
                                        v.addElement(new Double(l));
                                    }
                                }

                            }
                            break;
                        }
                    case ERROR :
                        {
                            {
                                match(ERROR);
                            }

                            v.addElement(v.lastElement());

                            break;
                        }
                    default :
                        {
                            break _loop23;
                        }
                }
            } while (true);
        }
        return v;
    }
    protected final int dupNumber() throws RecognitionException, TokenStreamException {
        int v;

        Token d = null;

        v = 0;

        {
            d = LT(1);
            match(DUP);
        }

        v = parseDUP(d);

        return v;
    }
    protected void error(String msg) throws JCAMPException {
        log.error(msg);
        throw new JCAMPException(msg);
    }
    /**
     * gets array of parsed y-data
     * @return int[]
     */
    public double[] getYData() {
        int n = yValues.size();
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            y[i] = ((Double) yValues.elementAt(i)).doubleValue();
        }
        return y;
    }
    protected final LineContent line() throws RecognitionException, TokenStreamException, JCAMPException {
        LineContent lineContent;

        lineContent = new LineContent();
        double x;
        Vector y;

        {
            switch (LA(1)) {
                case WS :
                    {
                        match(WS);
                        break;
                    }
                case XCHECK :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            x = xcheck();

            lineContent.xCheck = x;

        }
        {
            if ((LA(1) == WS)) {
                match(WS);
            } else if ((_tokenSet_0.member(LA(1)))) {
            } else {
                throw new NoViableAltException(LT(1), getFilename());
            }

        }
        {
            switch (LA(1)) {
                case POSSQZ :
                case NEGSQZ :
                case ERROR :
                    {
                        {
                            y = difNumbers();
                            match(EOL);

                            if (y == null) {
                                error("ASDF parsing failed in line " + Integer.toString(state.getCurrentLineNumber()));
                            }
                            lineContent.yVector = y;

                        }
                        break;
                    }
                case PAC :
                case WS :
                    {
                        {
                            y = pacNumbers();
                            match(EOL);

                            if (y == null) {
                                error("ASDF parsing failed in line " + Integer.toString(state.getCurrentLineNumber()));
                            }
                            lineContent.yVector = y;

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        return lineContent;
    }
    /**
     * parsing from standard in
     */
    public static void main(String[] args) {
        ASDFLexer lexer = new ASDFLexer(System.in);
        ASDFParser parser = new ASDFParser(lexer);
        try {
            parser.parse();
        } catch (Exception e) {
            System.err.println(e);
        }
        double[] y = parser.getYData();
        for (int i = 0; i < y.length; i++) {
            System.out.println(i + "\t" + y[i]);
        }
    }
    protected final double pacNumber() throws RecognitionException, TokenStreamException {
        double v;

        Token p = null;

        v = 0;

        {
            switch (LA(1)) {
                case WS :
                    {
                        match(WS);
                        break;
                    }
                case PAC :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        {
            p = LT(1);
            match(PAC);
            v = parsePAC(p);
        }
        return v;
    }
    protected final Vector pacNumbers() throws RecognitionException, TokenStreamException {
        Vector v;

        double p = 0;
        v = new Vector(80); // initial capacity of 80 data points per line

        {
            int _cnt28 = 0;
            _loop28 : do {
                if ((LA(1) == PAC || LA(1) == WS)) {
                    {
                        p = pacNumber();

                        v.addElement(new Double(p));

                    }
                    {
                        if ((LA(1) == WS)) {
                            match(WS);
                        } else if ((LA(1) == EOL || LA(1) == PAC || LA(1) == WS)) {
                        } else {
                            throw new NoViableAltException(LT(1), getFilename());
                        }

                    }
                } else {
                    if (_cnt28 >= 1) {
                        break _loop28;
                    } else {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
                }

                _cnt28++;
            } while (true);
        }
        return v;
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
        try {
            block(expectedSize);
        } catch (RecognitionException e) {
            log.error("parsing failed");
            throw new JCAMPException("parsing failed\n" + e.getMessage());
        } catch (TokenStreamException e) {
            log.error("parsing failed");
            throw new JCAMPException("parsing failed\n" + e.getMessage());
        }
    }
    protected int parseDUP(Token d) {
        int v = 0;
        char[] c = d.getText().toCharArray();
        if (c[0] == 's')
            v = 9;
        else
            v = (int) (c[0] - 'S') + 1;
        for (int i = 1; i < c.length; i++) {
            v *= 10;
            v += (int) (c[i] - '0');
        }
        if (log.isDebugEnabled())
            log.debug("DUP-Number: " + Integer.toString(v));
        return (v);
    }
    protected int parseNEGDIF(Token n) {
        int v = 0;
        char[] c = n.getText().toCharArray();
        v = (int) (c[0] - 'j') + 1;
        for (int i = 1; i < c.length; i++) {
            v *= 10;
            v += (int) (c[i] - '0');
        }
        v = -v;
        if (log.isDebugEnabled())
            log.debug("DIF-Number: " + Integer.toString(v));
        return (v);
    }
    protected int parseNEGSQZ(Token n) {
        int v = 0;
        char[] c = n.getText().toCharArray();
        v = (int) (c[0] - 'a') + 1;
        for (int i = 1; i < c.length; i++) {
            v *= 10;
            v += (int) (c[i] - '0');
        }
        v = -v;
        if (log.isDebugEnabled())
            log.debug("SQZ-Number: " + Integer.toString(v));
        return (v);
    }
    protected double parsePAC(Token p) {
//        int v = 0;s
//        int i = 0;
//        boolean negative = false;
//        char[] c = p.getText().toCharArray();
        
//        if (c[0] == '-') {
//            i++;
//            negative = true;
//        } else if (c[0] == '+') {
//            i++;
//        }
//        v = (int) (c[i] - '0');
//        i++;
//        while (i < c.length) {
//            v *= 10;
//            v += (int) (c[i] - '0');
//            i++;
//        }
//        if (negative)
//            v = -v;
//        if (log.isDebugEnabled())
//            log.debug("PAC-Number: " + Integer.toString(v));
//        return (v);
    	
      return Double.parseDouble(p.getText());
    	
    }
    protected int parsePOSDIF(Token p) {
        int v = 0;
        char[] c = p.getText().toCharArray();
        if (c[0] == '%')
            v = 0;
        else
            v = (int) (c[0] - 'J') + 1;
        for (int i = 1; i < c.length; i++) {
            v *= 10;
            v += (int) (c[i] - '0');
        }
        if (log.isDebugEnabled())
            log.debug("DIF-Number: " + Integer.toString(v));
        return (v);

    }
    protected int parsePOSSQZ(Token p) {
        int v = 0;
        char[] c = p.getText().toCharArray();
        if (c[0] == '@')
            v = 0;
        else
            v = (int) (c[0] - 'A') + 1;
        for (int i = 1; i < c.length; i++) {
            v *= 10;
            v += (int) (c[i] - '0');
        }
        if (log.isDebugEnabled())
            log.debug("SQZ-Number: " + Integer.toString(v));
        return (v);
    }
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
    protected final int sqzNumber() throws RecognitionException, TokenStreamException {
        int v;

        Token p = null;
        Token n = null;

        v = 0;

        switch (LA(1)) {
            case POSSQZ :
                {
                    {
                        p = LT(1);
                        match(POSSQZ);
                    }

                    v = parsePOSSQZ(p);

                    break;
                }
            case NEGSQZ :
                {
                    {
                        n = LT(1);
                        match(NEGSQZ);
                    }

                    v = parseNEGSQZ(n);

                    break;
                }
            default :
                {
                    throw new NoViableAltException(LT(1), getFilename());
                }
        }
        return v;
    }
    protected final double xcheck() throws RecognitionException, TokenStreamException {
        double d;

        Token x = null;

        d = 0;

        {
            x = LT(1);
            match(XCHECK);

            d = Double.valueOf(x.getText()).doubleValue();
            if (log.isDebugEnabled())
                log.debug("X-Check: " + Double.toString(d));

        }
        return d;
    }
}
