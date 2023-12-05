public class Program extends NaryOp
{
  public EvalResult eval(RefEnv env)
  {
    EvalResult result = null;

    // execute each child
    for(int i=0; i<getSize(); i++) {
      result = getChild(i).eval(env);
    }
    
    return result;
  }


  public void print(int depth) {
    int mid = getSize()/2;

    for(int i=0; i<mid; i++) {
      getChild(i).print(depth+1);
    }

    System.out.printf("%"+(depth+1)+"sPROGRAM\n", "");
    
    for(int i=mid; i<getSize(); i++) {
      getChild(i).print(depth+1);
    }
  }
}