public class Branch extends BinaryOp
{
  public EvalResult eval(RefEnv env) {
    if(getLeft().eval(env).asBoolean()) { 
      getRight().eval(env);
    }
    
    return new EvalResult();
  }

  public void print(int depth)  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"sIF\n", "");
    getLeft().print(depth+1);
  }
}