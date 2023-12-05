import java.io.IOException;
import java.io.InputStream;

/**
 * Lexer for the calc language
 */
public class Lexer {
  /// Source of the character stream
  private InputStream file;

  /// Current character being matched
  private char cur;

  /// Current Lexeme
  private Lexeme curLex;

  /// Line and column number of input
  private int line;
  private int col;
  private int startLine;
  private int startCol;
  private boolean eof;

  /// The lexeme that we are accumulating
  private StringBuilder curString;

  /// Construct a lexer for the given input stream
  public Lexer(InputStream file) {
    this.file = file;
    this.line = 1;
    this.col = 0;
    this.eof = false;
    read();
  }

  private void setToken(Token tok) {
    curLex = new Lexeme(tok, curString.toString(), startLine, startCol);
  }

  // skip characters we wish to ignore
  private void skip() {
    // skip all the blank characters
    while (cur != '\n' && (Character.isSpaceChar(cur)|| cur == '#')) {
      if(cur == '#') {
        skipToEOL();
      } else { 
        read();
      }
    }
  }

  // skip to the end of the line
  private void skipToEOL() {
    while (!eof && cur != '\n') {
      read();
    }
  }

  public Lexeme next() {
    // start the matching
    skip();
    curString = new StringBuilder();
    startLine = line;
    startCol = col;

    // handle eof
    if (eof) {
      setToken(Token.EOF);
      return curLex;
    }

    if (match_single())
      return curLex;
    else if (match_number())
      return curLex;
    else if (matchWord())
      return curLex;
    else if(matchFixed()) 
      return curLex;
    else
      consume();

    // invalid
    setToken(Token.INVALID);
    return curLex;
  }

  public Lexeme cur() {
    return curLex;
  }

  /// Match our single character tokens
  /// Return true on success, false on failure.
  public boolean match_single() {
    Token tok;

    switch (cur) {
      case '+':
        tok = Token.PLUS;
        break;
      case '-':
        tok = Token.MINUS;
        break;
      case '*':
        tok = Token.TIMES;
        break;
      case '/':
        tok = Token.DIVIDE;
        break;
      case '^':
        tok = Token.POW;
        break;
      case '(':
        tok = Token.LPAREN;
        break;
      case ')':
        tok = Token.RPAREN;
        break;
      case '\n':
        tok = Token.NEWLINE;
        break;
      case '=':
        tok = Token.EQUAL;
        break;
      case '[':
        tok = Token.LBRACKET;
        break;
      case ']':
        tok = Token.RBRACKET;
        break;
      case '.':
        tok = Token.DOT;
        break;
      default:
        tok = Token.INVALID;
    }

    // did not match
    if (tok == Token.INVALID) {
      return false;
    }

    // match
    consume();
    setToken(tok);
    return true;
  }

  /// Match an integer
  private void consumeInteger() {
    while (Character.isDigit(cur)) {
      consume();
    }
  }

  /// Match a Number
  private boolean match_number() {
    if (!Character.isDigit(cur))
      return false;

    // this is an integer
    Token tok = Token.INTLIT;
    consumeInteger();

    // check for a dot
    if (cur != '.') {
      setToken(tok);
      return true;
    }

    // it is a dot!
    consume();
    if (Character.isDigit(cur)) {
      tok = Token.REALLIT;
      consumeInteger();
    } else {
      tok = Token.INVALID;
    }
    setToken(tok);
    return true;
  }

  private boolean matchWord() {
    if (!Character.isAlphabetic(cur) && cur != '_')
      return false;

    // consume letters numbers and _
    while (Character.isDigit(cur) || Character.isAlphabetic(cur) || cur == '_') {
      consume();
    }

    // match against keywords
    String str = curString.toString();
    if (str.equals("MOD")) {
      setToken(Token.MOD);
    } else if (str.equals("displayString")) {
      setToken(Token.DISPLAYSTRING);
    } else if (str.equals("display")) {
      setToken(Token.DISPLAY);
    } else if(str.equals("input")) {
      setToken(Token.INPUT);
    } else if(str.equals("if")) {
      setToken(Token.IF);
    } else if(str.equals("end")) {
      setToken(Token.END);
    } else if(str.equals("while")) {
      setToken(Token.WHILE);
    } else if(str.equals("dimension")) {
      setToken(Token.DIMENSION);
    } else if(str.equals("record")) {
      setToken(Token.RECORD);
    } else if(str.equals("field")) {
      setToken(Token.FIELD);
    } else if(str.equals("fun")) {
      setToken(Token.FUN);
    } else {
      //if it's not a keyword, it's an ID
      setToken(Token.ID); 
    }

    return true;
  }


  /// Match a multi-character but fixed width
  /// token
  private boolean matchFixed() {
    if(cur == '<') {
      consume();
      setToken(Token.LT);

      // check for the second part
      if(cur == '=') {
        consume();
        setToken(Token.LTE);
      } else if(cur == '>') {
        consume();
        setToken(Token.NE);
      }
      return true;
    } else if(cur == '>') {
      consume();
      setToken(Token.GT);
      if(cur == '=') {
        consume();
        setToken(Token.GTE);
      }
      return true;
    }

    return false;
  }

  /// Read the next character
  private void read() {
    // handle newline
    if (cur == '\n') {
      line++;
      col = 0;
    }
    try {
      int input = file.read();
      cur = (char) input;
      col++;
      if (input == -1) {
        eof = true;
      }
    } catch (IOException ex) {
      // do nothing for now
      eof = true;
    }
  }

  /// Insert the current character into the curStr
  /// and advanced the lexer
  private void consume() {
    curString.append(cur);
    read();
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer(System.in);
    Lexeme tok;

    do {
      tok = lex.next();
      System.out.println(tok);
    } while (tok.tok != Token.EOF);
  }
}