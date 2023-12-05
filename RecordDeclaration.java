public class RecordDeclaration extends NaryOp {
  private Lexeme id;
  
  public RecordDeclaration(Lexeme id) {
    this.id = id;
  }

  public Lexeme getId() {
    return id;
  }

  public EvalResult eval(RefEnv env) {
    EvalResult value = new EvalResult();
    value.setValue(this);
    
    env.setVariable(id.str, value);
    return null;
  }

  public void print(int depth) {
    System.out.printf("%"+(depth+1)+"sRECORD %s\n", "", id.str);
    
    for(int i=0; i<getSize(); i++) {
      getChild(i).print(depth+1);
    }
  }
}
