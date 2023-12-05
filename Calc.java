import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class Calc
{
  // REPL (Read Evaluate Print Loop) for calc
  public static void repl(){
    Scanner scan = new Scanner(System.in);    
    RefEnv env = new RefEnv();

    while(true) {
      System.out.print("> ");
      System.out.flush();
      String line = scan.nextLine() + "\n";
      Lexer lex = new Lexer(new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8)));
      Parser parser = new Parser(lex);
      ParseTree program = parser.parse();
      EvalResult result = program.eval(env);

      if(result != null) {
        if(result.getType()==EvalType.REAL) {
          System.out.println(result.asReal());
        } else {
          System.out.println(result.asInteger());
        }
      }
    } 
  }

  public static void runFile(String fileName) {
    try {
      InputStream file = new FileInputStream(fileName);
      RefEnv env = new RefEnv();
      Lexer lex = new Lexer(file);
      Parser parser = new Parser(lex);
  
      ParseTree program = parser.parse();
      program.eval(env);
    } catch(FileNotFoundException ex) {
      System.err.println("File not found: " + fileName);
    }
    
  }

  
  public static void main(String [] args ) {
    // do a repl if args is empty
    if(args.length == 0) {
      repl();
    } else {
      runFile(args[0]);
    }
  }    
}