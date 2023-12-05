public class Divide extends BinaryOp
{
  public EvalResult eval(RefEnv env) {
    // get the operands
    EvalResult l = getLeft().eval(env);
    EvalResult r = getRight().eval(env);
    EvalResult result = new EvalResult();
    
    if(l.getType() == EvalType.REAL || r.getType() == EvalType.REAL) {
      double x = l.asReal() / r.asReal();
      result.setValue(x);
    } else {
      int x = l.asInteger() / r.asInteger();
      result.setValue(x);
    }

    
    return result;
  }


  public void print(int depth)  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"s/\n", "");
    getLeft().print(depth+1);
  }
}