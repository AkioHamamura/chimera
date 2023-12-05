public class ParseTreeTest
{
   public static void main(String [] args) {
     Add tree = new Add();
     Literal left = new Literal(new Lexeme(Token.INTLIT, "2", 0,0));
     Literal right = new Literal(new Lexeme(Token.INTLIT, "3", 0,0));

     tree.setLeft(left);
     tree.setRight(right);
     tree.print(0);
   } 
}