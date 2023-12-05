public abstract class BinaryOp implements ParseTree
{
  private ParseTree left;
  private ParseTree right;

  // set the left child
  public void setLeft(ParseTree left) {
    this.left = left;
  }

  // set the right child
  public void setRight(ParseTree right) {
    this.right = right;
  }

  // get the left child
  public ParseTree getLeft() {
    return left;
  }

  // get the right child
  public ParseTree getRight() {
    return right;
  }
}