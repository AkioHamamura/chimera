public interface Reference {
  public String name();
  public void set(RefEnv env, EvalResult val); 
}