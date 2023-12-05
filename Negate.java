public class Negate extends UnaryOp {
  public EvalResult eval(RefEnv env) {
    return null;
  }

  public void print(int depth) {
    getChild().print(depth + 1);
    System.out.printf("%" + (depth + 1) + "-s\n", "");
    
  }
}