public class Loop extends BinaryOp
{
  public EvalResult eval(RefEnv env) {
    while(getLeft().eval(env).asBoolean()) { 
      getRight().eval(env);
    }

    return new EvalResult();
  }

  public void print(int depth)  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"sWHILE\n", "");
    getLeft().print(depth+1);
  }
}