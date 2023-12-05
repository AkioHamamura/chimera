class Lexeme {
    public Token tok;
    public String str;
    public int line;
    public int col;

    public Lexeme() {

    }

    public Lexeme(Token tok, String str, int line, int col) {
        this.tok = tok;
        this.str = str;
        this.line = line;
        this.col = col;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(tok);
        s.append(" at line: ");
        s.append(line);
        s.append(" column: ");
        s.append(col);
        s.append(" \"");
        s.append(str);
        s.append("\"");
        return s.toString();
    }

    public static void main(String[] args) {
        Lexeme l = new Lexeme();

        l.tok = Token.PLUS;
        l.str = "+";
        l.line = 1;
        l.col = 2;
        System.out.println(l);
    }
}