import java.util.HashMap;

/**
 A reference environment for variable storage.
 */
public class RefEnv {
  private HashMap<String, EvalResult> table;

  public RefEnv() {
    table = new HashMap<String, EvalResult>();
  }

  // store a variable
  public void setVariable(String name, EvalResult value) {
    table.put(name, value);
  }

  // retrieve a variable
  public EvalResult getVariable(String name) {
    return table.get(name);
  }
}