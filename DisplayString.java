import org.w3c.dom.ls.LSOutput;

public class DisplayString extends UnaryOp {

  public EvalResult eval(RefEnv env) {
    display(getChild().eval(env));
    return null;
  }

  private void display(EvalResult result) {

    if(result.getType() == EvalType.ARRAY) {
      display_array(result.asArray());
    } else if(result.asInteger() == 0){
      System.out.println();

    } else {
      System.out.print((char)result.asInteger());

    }
  }

  private void display_array(EvalResult[] array) {
    for(EvalResult r : array) {
      display(r);
    }
  }

  public void print(int depth) {
    getChild().print(depth + 1);
    System.out.printf("%" + (depth + 1) + "sdisplay\n", "");
  }
}
