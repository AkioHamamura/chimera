  public class Parser
{
  private Lexer lexer;    

  public Parser(Lexer lexer) {
    this.lexer = lexer;
  }

  public ParseTree parse() {
    this.lexer.next();  // initialize the lexer
    return parseProgram();
  }

  /**
   Attempt to match tok.
   If it matches, return the lexeme. If it does not match, return null.
   */
  private Lexeme match(Token tok) {
    Lexeme l = lexer.cur();
    if(l.tok == tok) {
      // match, advance lexer, return the match
      lexer.next();
      return l;
    } 

    // no match
    return null;
  }


  /**
   * Attempt to match a token. Returns lexeme on match, halts the program on failure.
   */
  private Lexeme mustBe(Token tok) {
    Lexeme l = match(tok);

    if(l == null) {
      System.out.println("Parse Error: " + lexer.cur().toString());
      System.exit(-1);
    }

    return l;
  }

  /**
   * Return true if one of the tokens in the list are
   * currently in the lexer
   */
  private boolean has(Token... tokens) {
    Lexeme l = lexer.cur();
    for(Token t : tokens) {
      if(l.tok == t) return true;
    }
    return false;
  }

  /**
  < Program >    ::= < Program > < Statement >
                   | < Statement >
   */
  private ParseTree parseProgram() {
    Program result = new Program();
    
    while(lexer.cur().tok != Token.EOF && lexer.cur().tok != Token.END) {
      ParseTree statement = parseStatement();
      if(statement != null) {
        result.addChild(statement);
      }
    }

    return result;
  }


  /**
  < Statement >  ::= ID < Statement' > NEWLINE
                     | < IO-Operation > NEWLINE
                     | < Array-Dim > NEWLINE
                     | < Branch > NEWLINE
                     | < LOOP > NEWLINE
                     | < Fun-Def > NEWLINE
                     | < Expression > NEWLINE
                     | NEWLINE
  */
  private ParseTree parseStatement() {
    ParseTree result = null;
    Lexeme tok;

    if(has(Token.ID)) {
        // Handle an ID statement / expression
        result = parseRef();
        result = parseStatement2(result);
    }else if(has(Token.LET)){
        //set the current token to be an ID
        mustBe(Token.ID);
        result = parseRef();
        result = parseStatement2(result);


    } else if(has(Token.INPUT, Token.DISPLAY)) {
        result = parseIOOperation();
    } else if(has(Token.INPUT, Token.DISPLAYSTRING)) {
        result = parseIOOperation();
    } else if(has(Token.DIMENSION)) {
      result = parseArrayDimension(); 
    } else if(has(Token.IF)) {
      result = parseBranch();
    } else if(has(Token.WHILE)) {
      result = parseLoop();
    //} else if(has(Token.FUN))
    //{
        //result = parseFunctionDef();
    }
    else if(!has(Token.NEWLINE)) {
      result = parseExpression();
    }
    
    if(match(Token.EOF) == null) {
      mustBe(Token.NEWLINE);
    }
    return result;
  }

  /**
  < Statement' >  ::= EQUAL < Expression >
                      | < Factor' > < Term' > < Expression' >
  */
  private ParseTree parseStatement2(ParseTree left) {
    // match an assignment
    if(match(Token.EQUAL) != null) {
      Assignment result = new Assignment();
      result.setLeft(left);
      result.setRight(parseExpression());
      return result;
    }

    // an expression beginning with an ID
    ParseTree result = parseFactor2(left);
    result = parseTerm2(result);
    result = parseExpression2(result);
    return result;
  }

  /**
  < Array-Dim >  ::= DIMENSION ID < Expression >
  */
  private ParseTree parseArrayDimension() {
    mustBe(Token.DIMENSION);
    ParseTree left = new Variable(mustBe(Token.ID));
    ParseTree right = parseExpression();
    ArrayDimension result = new ArrayDimension();
    result.setLeft(left);
    result.setRight(right);

    return result;
  }
  

  /**
   < Branch >     ::= IF LPAREN < Condition > RPAREN LBRACE NEWLINE < Program > END
   */
  private ParseTree parseBranch() {
    mustBe(Token.IF);
    mustBe(Token.LPAREN);
    ParseTree condition = parseCondition();
    mustBe(Token.RPAREN);
      //If the next token is a newline, then we need to skip it
    if(has(Token.NEWLINE)){
         mustBe(Token.NEWLINE);
    }
    mustBe(Token.LBRACE);
    mustBe(Token.NEWLINE);
    ParseTree program = parseProgram();
    mustBe(Token.END);

    
    Branch result = new Branch();
    result.setLeft(condition);
    result.setRight(program);
    return result;
  }

  /**
   < Loop >        ::= WHILE LPAREN < Condition > RPAREN LBRACE NEWLINE < Program > RBRACE
  */
  private ParseTree parseLoop() {
    mustBe(Token.WHILE);
    mustBe(Token.LPAREN);
    ParseTree condition = parseCondition();
    mustBe(Token.RPAREN);

    //If the next token is a newline, then we need to skip it
    if(has(Token.NEWLINE)){
        mustBe(Token.NEWLINE);
    }

    //mustBe(Token.NEWLINE);
    mustBe(Token.LBRACE);
    mustBe(Token.NEWLINE);
    ParseTree program = parseProgram();
    mustBe(Token.END);
    //mustBe(Token.RPAREN);

    Loop result = new Loop();
    result.setLeft(condition);
    result.setRight(program);

    return result;
  }


  /**
  < Function-Def >   ::= "fun" < Id > "(" < Param-List > ")" "\n" < Program > "end" "fun"
  */
  private ParseTree ParseFunctionDef() {
    mustBe(Token.FUN);
    Lexeme tok = mustBe(Token.ID);
    mustBe(Token.LPAREN);
    //ParseTree params = parseParamList();
    mustBe(Token.RPAREN);
    mustBe(Token.NEWLINE);
    ParseTree program = parseProgram();
    mustBe(Token.END);
    mustBe(Token.FUN);

    FunctionDef result = new FunctionDef(tok);
    //result.setLeft(params);
    result.setRight(program);
    return result;
  } 
  
  
 /**  
  < Param-List > ::= < NV-Param-List > | ""
  
  < NV-Param-List > ::= < NV-Param-List > "," < Id >
                        | < Id >
  */
 private ParseTree parseParamList() {
   ParseList result = new ParseList();

   // empty parameter list
   if(!has(Token.ID)) return result;

   //do {
   //  result.add(new Variable(mustBe(Token.ID)));
   //} while(match(Token.COMMA) != null);

   return result;
 }


  /**
   < Condition >  ::= < Expression > < Condition' >
   */
  private ParseTree parseCondition() {
    ParseTree left = parseExpression();
    return parseCondition2(left);
  }


  /**
   < Condition' > ::= EQUAL < Expression > 
   < Condition' > ::= GT < Expression >
                      | LT < Expression >
                      | LTE < Expression >
                      | GTE < Expression >
                      | EQUAL < Expression >
                      | NE < Expression >
   */
  private ParseTree parseCondition2(ParseTree left) {
    BinaryOp result;

    if(match(Token.GT) != null) {
      result = new Greater();
    } else if(match(Token.LT)!=null) {
      result = new Less(); 
    } else if(match(Token.LTE)!=null) {
      result = new LessOrEqual();
    } else if(match(Token.EQUAL) != null) {
      result = new Equal();
    } else if(mustBe(Token.NE) != null) {
      result = new NotEqual();
    } else {
      return null;
    }
    
    result.setLeft(left);
    result.setRight(parseExpression());
    
    return result;
  }



  
  
  /** 
  < IO-Operation > ::= DISPLAY LPAREN < Expression > RPAREN
                       | INPUT < Ref >
  */
  private ParseTree parseIOOperation() {
    if(match(Token.DISPLAY) != null) {
      Display result = new Display();
      mustBe(Token.LPAREN);
      result.setChild(parseExpression());
      mustBe(Token.RPAREN);
      return result;
    }
    else if (match(Token.DISPLAYSTRING) != null){
        mustBe(Token.LPAREN);
        DisplayString result = new DisplayString();
        result.setChild(parseExpression());
        mustBe(Token.RPAREN);
        return result;
    }

    mustBe(Token.INPUT);
    Input result = new Input();
    result.setChild(parseRef());
    return result;
  }

  /**
  < Term >       ::= < Factor > < Term' >
  */
  private ParseTree parseTerm() {
    ParseTree left = parseFactor();
    return parseTerm2(left);
  }
  
  /**
  < Term' >      ::= TIMES < Factor > < Term' >
                   | DIVIDE < Factor > < Term' >
                   | MOD < Factor > < Term' >
                   | ""
  */
 private ParseTree parseTerm2(ParseTree left) {
    if(match(Token.TIMES) != null) {
        Multiply result = new Multiply();
        result.setLeft(left);
        result.setRight(parseFactor());
        return parseTerm2(result);
    } else if(match(Token.DIVIDE) != null) {
        Divide result = new Divide();
        result.setLeft(left);
        result.setRight(parseTerm());
        return parseTerm2(result);
      
    } else if(match(Token.MOD) != null) {
        Mod result  = new Mod();
        result.setLeft(left);
        result.setRight(parseFactor());
        return parseTerm2(result);
    }
    
    return left; 
  }

  /** 
  < Number >     ::= INTLIT | REALLIT | < Ref >
  */
  private ParseTree parseNumber() {
    Lexeme tok = match(Token.INTLIT);
    if (tok != null) { 
      return new Literal(tok);
    }

    if(has(Token.ID)) {
        return parseRef();
    }

    tok = mustBe(Token.REALLIT);
    return new Literal(tok);
  }

    /* 
    < Ref >        ::= ID < Ref' >
    */
    ParseTree parseRef() {
        Lexeme tok = mustBe(Token.ID);

        return parseRef2(new Variable(tok));
    }


    /* 
    < Ref' >       ::= LBRACKET < Expression > RBRACKET < Ref' >
                       | DOT ID < Ref' >
                       | ""
    */
    ParseTree parseRef2(ParseTree left) {
        if(match(Token.LBRACKET) != null) {
            // array access
            ArrayAccess result = new ArrayAccess();
            result.setLeft(left);
            result.setRight(parseExpression());
            mustBe(Token.RBRACKET);
            return parseRef2(result);
        } else if(match(Token.DOT) != null) {
            // record access
            RecordAccess result = new RecordAccess();
            result.setLeft(left);
            result.setRight(new Variable(mustBe(Token.ID)));
            return parseRef2(result);
        } else if(match(Token.LPAREN) != null) {
          // function call
          FunctionCall result = new FunctionCall();
          result.setLeft(left);
          result.setRight(parseArgList());
          mustBe(Token.RPAREN);
          return parseRef2(result);
        }

        // null string
        return left;
    }

  
  /**
    < Exponent >   ::= < Number >
                   | MINUS < Exponent >
                   | LPAREN < Expression > RPAREN
  */

  private ParseTree parseExponent(){
    if(match(Token.MINUS) != null){
      Negate result = new Negate();
      result.setChild(parseExponent());
      return result;
    } else if(match(Token.LPAREN) != null){
      ParseTree result = parseExpression();
      mustBe(Token.RPAREN);
      return result;
    } else {
      return parseNumber();
    }
  }

// < Expression > ::= < Term > < Expression' >
  
 private ParseTree parseExpression() {
    ParseTree left = parseTerm();
    return parseExpression2(left);
 }
  
/**
< Expression' > ::= PLUS < Term > < Expression' >
                   | MINUS < Term > < Expression' >
                   | ""
 */
public ParseTree parseExpression2(ParseTree left){
  if (match(Token.PLUS) != null){
    Add result = new Add();
    result.setLeft(left);
    result.setRight(parseTerm());
    return parseExpression2(result);
  } else if (match(Token.MINUS) != null){
    Subtract result = new Subtract();
    result.setLeft(left);
    result.setRight(parseTerm());
    return parseExpression2(result);
  } 

  // ""
  return left;
}

    /*
    < Factor >     ::= < Exponent > < Factor' >

    */
    public ParseTree parseFactor(){
      ParseTree left = parseExponent();
      return parseFactor2(left);
    }


    /**
    < Factor' >    ::= POW < Exponent > < Factor' >
                      | ""
    */
    public ParseTree parseFactor2(ParseTree left){
      if (match(Token.POW) != null){
        Power result = new Power();
        result.setLeft(left);
        result.setRight(parseExponent());
        return parseFactor2(result);
      }

      return left;
    }
  
 /**  
  < Arg-List > ::= < NV-Arg-List > | ""
  
  < NV-Arg-List > ::= < NV-Arg-List > "," < Expression >
                        | < Expression >
  */
 private ParseTree parseArgList() {
   ParseList result = new ParseList();

   // empty parameter list
   if(!has(Token.ID)) return result;

   //do {
   //  result.add(parseExpression());
   //} while(match(Token.COMMA) != null);

   return result;
 }

  /** 
   * Test the parser
   */
  public static void main(String [] args) {
    Lexer lexer = new Lexer(System.in);
    Parser parser = new Parser(lexer);

    parser.parse().print(0);
  }
}
