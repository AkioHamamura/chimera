public class ArrayAccess extends BinaryOp implements Reference
{
  public EvalResult eval(RefEnv env)
  {
    EvalResult r = getLeft().eval(env);
    return r.asArray()[index(env)];
  }

  public String name()
  {
    String s = ((Reference) getLeft()).name()+"[]";
    return s;
  }

  
  public void set(RefEnv env, EvalResult val)
  {
    EvalResult r = getLeft().eval(env);
    r.asArray()[index(env)] = val;
  }

  public void print(int depth) 
  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"s[] (array index)\n", "");
    getLeft().print(depth+1);
  }

  
  private int index(RefEnv env) 
  {
    EvalResult result = getRight().eval(env);
    return result.asInteger();
  }
}
