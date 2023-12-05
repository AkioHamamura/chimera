public class ArrayDimension extends BinaryOp
{
  public EvalResult eval(RefEnv env)
  {
    //create the array
    int n = getRight().eval(env).asInteger();
    EvalResult [] array = new EvalResult[n];
    for(int i=0; i<n; i++) {
      array[i] = new EvalResult();
      array[i].setValue(0);
    }
    EvalResult value = new EvalResult();
    value.setValue(array);

    //store the array
    Reference var = (Reference) getLeft();
    var.set(env, value);
    return null;
  }

  public void print(int depth) 
  {
    getRight().print(depth+1);
    System.out.printf("%"+(depth+1)+"sDIMENSION\n", "");
    getLeft().print(depth+1);
  }
}
