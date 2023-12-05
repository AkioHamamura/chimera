public interface ParseTree
{
  public EvalResult eval(RefEnv env);    
  public void print(int depth);
}