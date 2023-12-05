public class FunctionCall extends BinaryOp
{
  public EvalResult FunctionCall(RefEnv env) {
    //EvalResult function = ((Reference) getLeft()).eval(env);
    
    // TODO: Parameter Binding and Scope Handling
    
    //return function.eval(env);
    return null;
  }

  @Override
  public EvalResult eval(RefEnv env) {
    // TODO Auto-generated method stub
    return null;
  }

  public void print(int depth)  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"s Function Call\n", "");
    getLeft().print(depth+1);
  }
}