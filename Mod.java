public class Mod extends BinaryOp
{
  public EvalResult eval(RefEnv env) {
    // get the operands
    EvalResult l = getLeft().eval(env);
    EvalResult r = getRight().eval(env);
    EvalResult result = new EvalResult();

    result.setValue(l.asInteger() % r.asInteger());
    
    return result;
  }

  public void print(int depth)  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"smod\n", "");
    getLeft().print(depth+1);
  }
}