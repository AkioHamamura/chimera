import java.util.ArrayList;

public abstract class NaryOp implements ParseTree {
  protected ArrayList<ParseTree> children;

  public NaryOp() {
    children = new ArrayList<ParseTree>();
  }


  // add a child to the list
  public void addChild(ParseTree child) {
    children.add(child);
  }


  // retrieve a child from the list
  public ParseTree getChild(int index) {
    // protect against index errors 
    if(index < 0 || index >= children.size()) {
      return null;
    }

    return children.get(index);
  }

  // retrieve the size of the list
  public int getSize() {
    return children.size();
  }
  
}