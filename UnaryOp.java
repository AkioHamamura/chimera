public abstract class UnaryOp implements ParseTree 
{
  private ParseTree child;

  // set the child
  public void setChild(ParseTree child) {
    this.child = child;
  }

  // get the child
  public ParseTree getChild() {
    return child;
  }
}