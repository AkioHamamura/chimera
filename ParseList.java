public class ParseList extends NaryOp
{
  public EvalResult eval(RefEnv env)
  {
    return null;
  }


  public void print(int depth) {
    for(int i=0; i<getSize(); i++) {
      getChild(i).print(depth+1);
    }
  }
}