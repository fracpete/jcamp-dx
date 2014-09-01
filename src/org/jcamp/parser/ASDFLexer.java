// $ANTLR 2.7.1: "ASDFLexer.g" -> "ASDFLexer.java"$

package org.jcamp.parser;
import java.io.InputStream;
import java.io.Reader;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.InputBuffer;
import antlr.LexerSharedInputState;
import antlr.NoViableAltForCharException;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.collections.impl.BitSet;

public class ASDFLexer extends antlr.CharScanner implements ASDFLexerTokenTypes, TokenStream {

    private static Log log = LogFactory.getLog(ASDFLexer.class);
    private boolean atBOL = true;
    public ASDFLexer(InputStream in) {
        this(new ByteBuffer(in));
    }
    public ASDFLexer(Reader in) {
        this(new CharBuffer(in));
    }
    public ASDFLexer(InputBuffer ib) {
        this(new LexerSharedInputState(ib));
    }
    public ASDFLexer(LexerSharedInputState state) {
        super(state);
        literals = new Hashtable();
        caseSensitiveLiterals = true;
        setCaseSensitive(true);
    }

    public Token nextToken() throws TokenStreamException {
        Token theRetToken = null;
        tryAgain : for (;;) {
            Token _token = null;
            int _ttype = Token.INVALID_TYPE;
            resetText();
            try { // for char stream error handling
                try { // for lexical error handling
                    switch (LA(1)) {
                        case '\n' :
                        case '\r' :
                            {
                                mEOL(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '@' :
                        case 'A' :
                        case 'B' :
                        case 'C' :
                        case 'D' :
                        case 'E' :
                        case 'F' :
                        case 'G' :
                        case 'H' :
                        case 'I' :
                            {
                                mPOSSQZ(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case 'a' :
                        case 'b' :
                        case 'c' :
                        case 'd' :
                        case 'e' :
                        case 'f' :
                        case 'g' :
                        case 'h' :
                        case 'i' :
                            {
                                mNEGSQZ(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '%' :
                        case 'J' :
                        case 'K' :
                        case 'L' :
                        case 'M' :
                        case 'N' :
                        case 'O' :
                        case 'P' :
                        case 'Q' :
                        case 'R' :
                            {
                                mPOSDIF(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case 'j' :
                        case 'k' :
                        case 'l' :
                        case 'm' :
                        case 'n' :
                        case 'o' :
                        case 'p' :
                        case 'q' :
                        case 'r' :
                            {
                                mNEGDIF(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case 'S' :
                        case 'T' :
                        case 'U' :
                        case 'V' :
                        case 'W' :
                        case 'X' :
                        case 'Y' :
                        case 'Z' :
                        case 's' :
                            {
                                mDUP(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '\t' :
                        case ' ' :
                            {
                                mWS(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        case '?' :
                            {
                                mERROR(true);
                                theRetToken = _returnToken;
                                break;
                            }
                        default :
                            if ((_tokenSet_0.member(LA(1))) && (true)) {
                                mXCHECK(true);
                                theRetToken = _returnToken;
                            } else if ((_tokenSet_1.member(LA(1))) && (true)) {
                                mPAC(true);
                                theRetToken = _returnToken;
                            } else {
                                if (LA(1) == EOF_CHAR) {
                                    uponEOF();
                                    _returnToken = makeToken(Token.EOF_TYPE);
                                } else {
                                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                                }
                            }
                    }
                    if (_returnToken == null)
                        continue tryAgain; // found SKIP token
                    _ttype = _returnToken.getType();
                    _ttype = testLiteralsTable(_ttype);
                    _returnToken.setType(_ttype);
                    return _returnToken;
                } catch (RecognitionException e) {
                    throw new TokenStreamRecognitionException(e);
                }
            } catch (CharStreamException cse) {
                if (cse instanceof CharStreamIOException) {
                    throw new TokenStreamIOException(((CharStreamIOException) cse).io);
                } else {
                    throw new TokenStreamException(cse.getMessage());
                }
            }
        }
    }

    protected final void mDIGIT(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = DIGIT;
        int _saveIndex;

        matchRange('0', '9');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    protected final void mSIGN(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = SIGN;
        int _saveIndex;

        switch (LA(1)) {
            case '+' :
                {
                    match('+');
                    break;
                }
            case '-' :
                {
                    match('-');
                    break;
                }
            default :
                {
                    throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                }
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mEOL(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = EOL;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '\n' :
                    {
                        match('\n');
                        break;
                    }
                case '\r' :
                    {
                        match('\r');
                        {
                            if ((LA(1) == '\n')) {
                                match('\n');
                            } else {
                            }

                        }
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }

        if (log.isDebugEnabled())
            log.debug("EOL-Token");
        newline();
        atBOL = true;

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mXCHECK(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = XCHECK;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '+' :
                case '-' :
                    {
                        mSIGN(false);
                        break;
                    }
                case '.' :
                case '0' :
                case '1' :
                case '2' :
                case '3' :
                case '4' :
                case '5' :
                case '6' :
                case '7' :
                case '8' :
                case '9' :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }
        {
            _loop9 : do {
                if (((LA(1) >= '0' && LA(1) <= '9')) && (_tokenSet_2.member(LA(2)))) {
                    mDIGIT(false);
                } else {
                    break _loop9;
                }

            } while (true);
        }
        {
            switch (LA(1)) {
                case '.' :
                    {
                        match('.');
                        break;
                    }
                case '0' :
                case '1' :
                case '2' :
                case '3' :
                case '4' :
                case '5' :
                case '6' :
                case '7' :
                case '8' :
                case '9' :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }
        {
            int _cnt12 = 0;
            _loop12 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    if (_cnt12 >= 1) {
                        break _loop12;
                    } else {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
                }

                _cnt12++;
            } while (true);
        }

        if (!atBOL) {
            _ttype = PAC;
            if (log.isDebugEnabled())
                log.debug("PAC-Token");
        } else {
            if (log.isDebugEnabled())
                log.debug("XCHECK-Token");
        }
        atBOL = false;

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mPAC(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = PAC;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '+' :
                case '-' :
                    {
                        mSIGN(false);
                        break;
                    }
                case '0' :
                case '1' :
                case '2' :
                case '3' :
                case '4' :
                case '5' :
                case '6' :
                case '7' :
                case '8' :
                case '9' :
                    {
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }
        {
            int _cnt16 = 0;
            _loop16 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    if (_cnt16 >= 1) {
                        break _loop16;
                    } else {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
                }

                _cnt16++;
            } while (true);
        }

        if (log.isDebugEnabled())
            log.debug("PAC-Token");

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mPOSSQZ(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = POSSQZ;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '@' :
                    {
                        match('@');
                        break;
                    }
                case 'A' :
                case 'B' :
                case 'C' :
                case 'D' :
                case 'E' :
                case 'F' :
                case 'G' :
                case 'H' :
                case 'I' :
                    {
                        matchRange('A', 'I');
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }
        {
            _loop20 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    break _loop20;
                }

            } while (true);
        }

        if (log.isDebugEnabled())
            log.debug("POSSQZ-Token");

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mNEGSQZ(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = NEGSQZ;
        int _saveIndex;

        {
            matchRange('a', 'i');
        }
        {
            _loop24 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    break _loop24;
                }

            } while (true);
        }

        if (log.isDebugEnabled())
            log.debug("NEGSQZ-Token");

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mPOSDIF(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = POSDIF;
        int _saveIndex;

        {
            switch (LA(1)) {
                case '%' :
                    {
                        match('%');
                        break;
                    }
                case 'J' :
                case 'K' :
                case 'L' :
                case 'M' :
                case 'N' :
                case 'O' :
                case 'P' :
                case 'Q' :
                case 'R' :
                    {
                        matchRange('J', 'R');
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }
        {
            _loop28 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    break _loop28;
                }

            } while (true);
        }

        if (log.isDebugEnabled())
            log.debug("POSDIF-Token");

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mNEGDIF(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = NEGDIF;
        int _saveIndex;

        {
            matchRange('j', 'r');
        }
        {
            _loop32 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    break _loop32;
                }

            } while (true);
        }

        if (log.isDebugEnabled())
            log.debug("NEGDIF-Token");

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mDUP(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = DUP;
        int _saveIndex;

        {
            switch (LA(1)) {
                case 'S' :
                case 'T' :
                case 'U' :
                case 'V' :
                case 'W' :
                case 'X' :
                case 'Y' :
                case 'Z' :
                    {
                        matchRange('S', 'Z');
                        break;
                    }
                case 's' :
                    {
                        match('s');
                        break;
                    }
                default :
                    {
                        throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                    }
            }
        }
        {
            _loop36 : do {
                if (((LA(1) >= '0' && LA(1) <= '9'))) {
                    mDIGIT(false);
                } else {
                    break _loop36;
                }

            } while (true);
        }

        if (log.isDebugEnabled())
            log.debug("DUP-Token");

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mWS(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = WS;
        int _saveIndex;

        {
            int _cnt39 = 0;
            _loop39 : do {
                switch (LA(1)) {
                    case ' ' :
                        {
                            match(' ');
                            break;
                        }
                    case '\t' :
                        {
                            match('\t');
                            break;
                        }
                    default :
                        {
                            if (_cnt39 >= 1) {
                                break _loop39;
                            } else {
                                throw new NoViableAltForCharException((char) LA(1), getFilename(), getLine());
                            }
                        }
                }
                _cnt39++;
            } while (true);
        }
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    public final void mERROR(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = ERROR;
        int _saveIndex;

        match('?');
        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    protected final void mSL_COMMENT(boolean _createToken)
        throws RecognitionException, CharStreamException, TokenStreamException {
        int _ttype;
        Token _token = null;
        int _begin = text.length();
        _ttype = SL_COMMENT;
        int _saveIndex;

        match("$$");
        {
            _loop44 : do {
                if ((_tokenSet_3.member(LA(1)))) {
                    {
                        match(_tokenSet_3);
                    }
                } else {
                    break _loop44;
                }

            } while (true);
        }
        {
            mEOL(false);
        }

        _ttype = EOL;

        if (_createToken && _token == null && _ttype != Token.SKIP) {
            _token = makeToken(_ttype);
            _token.setText(new String(text.getBuffer(), _begin, text.length() - _begin));
        }
        _returnToken = _token;
    }

    private static final long _tokenSet_0_data_[] = { 288063250384289792L, 0L, 0L };
    public static final BitSet _tokenSet_0 = new BitSet(_tokenSet_0_data_);
    private static final long _tokenSet_1_data_[] = { 287992881640112128L, 0L, 0L };
    public static final BitSet _tokenSet_1 = new BitSet(_tokenSet_1_data_);
    private static final long _tokenSet_2_data_[] = { 288019269919178752L, 0L, 0L };
    public static final BitSet _tokenSet_2 = new BitSet(_tokenSet_2_data_);
    private static final long _tokenSet_3_data_[] = { -8935308576017088000L, 4503591171653631L, 0L, 0L };
    public static final BitSet _tokenSet_3 = new BitSet(_tokenSet_3_data_);

}
