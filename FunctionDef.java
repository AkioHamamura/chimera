public class FunctionDef extends BinaryOp implements Reference {
  private Lexeme tok;

  FunctionDef(Lexeme tok) {
    this.tok = tok;
  }

  public EvalResult eval(RefEnv env) {
    EvalResult val = new EvalResult();
    val.setValue(this);
    set(env, val);

    return null;
  }

  public String name() {
    return tok.str;
  }


  public void set(RefEnv env, EvalResult val) {
    env.setVariable(this.tok.str, val);
  }

  public void print(int depth) {
    getRight().print(depth+1);
    System.out.printf("%" + (depth + 1) + "s%s\n", "", tok.str);
    getLeft().print(depth+1);
    
  }
}