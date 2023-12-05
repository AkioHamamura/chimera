public class Variable implements ParseTree, Reference {
  private Lexeme tok;

  Variable(Lexeme tok) {
    this.tok = tok;
  }

  public EvalResult eval(RefEnv env) {
    return env.getVariable(this.tok.str);
  }

  public String name() {
    return tok.str;
  }


  public void set(RefEnv env, EvalResult val) {
    env.setVariable(this.tok.str, val);
  }

  public void print(int depth) {
    System.out.printf("%" + (depth + 1) + "s%s\n", "", tok.str);
  }
}