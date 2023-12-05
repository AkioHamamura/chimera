public class Literal implements ParseTree
{
  private Lexeme literal;

  Literal(Lexeme literal) {
    this.literal = literal;
  }

  public EvalResult eval(RefEnv env)
  {
    EvalResult result = new EvalResult();

    if(literal.tok == Token.INTLIT) {
      result.setValue(Integer.parseInt(literal.str));
    } else {
      result.setValue(Double.parseDouble(literal.str));
    }
    return result;
  }

  

  public void print(int depth) {
    System.out.printf("%"+(depth+1)+"s%s\n", "", literal.str);
  }
}