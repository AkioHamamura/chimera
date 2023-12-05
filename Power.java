public class Power extends BinaryOp
{
  public EvalResult eval(RefEnv env) {
    // get the operands
    double l = getLeft().eval(env).asReal();
    double r = getRight().eval(env).asReal();
    
    double x = Math.pow(l, r);
    EvalResult result = new EvalResult();
    result.setValue(x);
    
    return result;
  }

  public void print(int depth)  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"s^\n", "");
    getLeft().print(depth+1);
  }
}