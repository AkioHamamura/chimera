import java.util.Scanner;

public class Input extends UnaryOp {
  public EvalResult eval(RefEnv env) {
    Scanner scan = new Scanner(System.in);
    
    Reference v = (Reference) getChild();
    System.out.print(v.name() + "=");
    double num = scan.nextDouble();

    EvalResult value = new EvalResult();
    if(num % 1 == 0) { 
      // integer input
      value.setValue((int)num);
    } else {
      //double input
      value.setValue(num);
    }
    v.set(env, value);
    return null;
  }

  public void print(int depth) {
    getChild().print(depth + 1);
    System.out.printf("%" + (depth + 1) + "sinput\n", "");
  }
}