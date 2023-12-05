public class GreaterOrEqual extends BinaryOp {
  public EvalResult eval(RefEnv env) {
    // get the operands
    EvalResult l = getLeft().eval(env);
    EvalResult r = getRight().eval(env);
    EvalResult result = new EvalResult();

    if (l.getType() == EvalType.REAL || r.getType() == EvalType.REAL) {
      result.setValue(l.asReal() >= r.asReal());
    } else {
      result.setValue(l.asInteger() >= r.asInteger());
    }

    return result;
  }

  public void print(int depth) {
    getRight().print(depth + 1);
    System.out.printf("%" + (depth + 1) + "s>= (Comparision) \n", "");
    getLeft().print(depth + 1);
  }
}