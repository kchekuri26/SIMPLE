
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parser loops through a list of tokens (generated by lexer) and
 * checks to see if the tokens make a valid assignment statement program
 */
public class Parser {

    private ArrayList<Token> tokens;
    private SymTab symTab = new SymTab();
    private static BytecodeInterpreter bytecodeInterpreter = new BytecodeInterpreter(10);
    private int indexAllot = 0;
    private int index;

    public Parser(String fileName) {
        Lexer lexer = new Lexer(fileName);
        System.out.println(lexer.buffer);
        tokens = lexer.getAllTokens();

    }

    /**
     * @return the token at the current index
     */
    Token nextToken() {
        Token t = tokens.get(index);
        index++;
        return t;
    }

    /**
     * when we've looked at a token to end an expression,
     * but want to put it back so parseAssignment can check it
     */
    void putTokenBack() {
        index--;
    }

    /**
     * parse an entire program
     *
     * @return true if valid program
     */
    public boolean parseProgram() {
        while (true) {
            if (parseAssignment()) {
                Token t = nextToken();
                if (t.getType() == Lexer.EOFTOKEN) {
                    return true;
                }
                putTokenBack();  // we jumped ahead
            } else {
                return false;
            }

        }
    }

    /**
     * parse a single assignment statement
     *
     * @return true if a valid assignment
     */
    public boolean parseAssignment() {
        Token idToken;
        idToken = parseIdentifier();
        if (idToken != null) {
            if (parseAssignOp()) {

                if (parseExpression()) {
                    bytecodeInterpreter.generate(BytecodeInterpreter.STORE, indexAllot);
                    indexAllot++;
                    return true;
                } else {
                    return false;
                }
            } else {
                printError("Expecting assignment operator");
                return false;
            }

        } else {
            printError("Expecting identifier");
            return false;
        }

    }

    /**
     * parse the left-hand side of an assignment, e.g., the "x" in "x=5+y"
     *
     * @return true if the current token is an id
     */
    public Token parseIdentifier() {

        Token t = nextToken();
        if (t.getType().equals(Lexer.IDTOKEN)) {
            symTab.add(t.getValue());
            return t;
        } else {
            return null;
        }
    }

    /**
     * parse the =
     *
     * @return true if current token is an =
     */
    public boolean parseAssignOp() {
        Token t = nextToken();
        return (t.getType().equals(Lexer.ASSMTTOKEN));
    }

    /**
     * parse the right hand side of an expression, the "y+5" in "x=y+5"
     *
     * @return true if the next tokens are a valid expression
     */
    public boolean parseExpression() {
        Token t = nextToken();
        if ((t.getType() == Lexer.INTTOKEN) || (t.getType() == Lexer.IDTOKEN)) {

            if (t.getType() == Lexer.IDTOKEN) {
                // better be in symtab already
                if (symTab.getAddress(t.getValue()) == -1) {
                    printError("variable undefined");
                    return false;
                } else {
                    bytecodeInterpreter.generate(BytecodeInterpreter.ADD, symTab.getAddress(t.getValue()));
                }
            }
            if (t.getType() == Lexer.INTTOKEN) {
                bytecodeInterpreter.generate(BytecodeInterpreter.ADDI, Integer.valueOf(t.getValue()));
            }
            while (true) {
                t = nextToken();
                if (t.getType() == Lexer.PLUSTOKEN || t.getType() == Lexer.MINUSTOKEN) {
                    t = nextToken();
                    if (!((t.getType() == Lexer.INTTOKEN) || (t.getType() == Lexer.IDTOKEN))) {
                        printError("expecting id or int");
                        return false;
                    }
                    else if (t.getType() == Lexer.IDTOKEN){
                        if (tokens.get(index-2).getType()==Lexer.PLUSTOKEN){
                            bytecodeInterpreter.generate(BytecodeInterpreter.ADD, symTab.getAddress(t.getValue()));
                        } else {
                            bytecodeInterpreter.generate(BytecodeInterpreter.MINUS, symTab.getAddress(t.getValue()));
                        }
                    }
                    else if (t.getType() == Lexer.INTTOKEN){
                        if (tokens.get(index-2).getType()==Lexer.PLUSTOKEN){
                            bytecodeInterpreter.generate(BytecodeInterpreter.ADDI, Integer.valueOf(t.getValue()));
                        } else {
                            bytecodeInterpreter.generate(BytecodeInterpreter.MINUSI, Integer.valueOf(t.getValue()));
                        }
                    }
                } else {
                    putTokenBack();
                    return true;
                }
            }
        } else {
            printError("expecting id or int");
            return false;
        }

    }

    /**
     * printError adds the current lineNum to an error to be printed
     */
    public void printError(String error) {
        putTokenBack();
        Token curToken = this.tokens.get(index);
        System.out.println("Error: " + error + " at line " + curToken.getLineNum());
    }

    /**
     * prints all the token and symbolTable.
     */
    @Override
    public String toString() {
        return this.tokens.toString() + "\n" + this.symTab.toString();
    }

    public static void main(String[] args) {
        Parser parser = new Parser("testOutOfBounds.txt");
        if (parser.parseProgram()) {
            System.out.println("Valid Program");
            System.out.println(parser.symTab);
            if (parser.symTab.getMap().size()>bytecodeInterpreter.getMemory().length){
                System.out.println("Error: Address out of range");
            }
            ArrayList<Integer> byteCode = bytecodeInterpreter.getBytecode();
            System.out.println("ByteCode: " + byteCode);
            bytecodeInterpreter.run();
            System.out.println("Memory: " + Arrays.toString(bytecodeInterpreter.getMemory()));

        } else {
            System.out.println("invalid Program");
        }

    }


///*
//                        LOOP
// */
//
//        String[] fileArray = {"test.txt", "test5.txt", "textExpectingId2.txt",
//                "testExpectingAssignOp.txt", "testExpectingIdOrInt2.txt", "testMultiplePlus.txt", "testWhiteSpace.txt"
//                , "testNotDefined.txt", "testSecondLineError.txt", "idNums.txt"};
//
//
//        for (String filename : fileArray) {
//            System.out.println(filename);
//            Parser parser = new Parser(filename);
//            //System.out.println(parser.tokens);
//            for (Token t : parser.tokens) {
//                System.out.println(t);
//            }
//
//
//            if (parser.parseProgram()) {
//                System.out.println("Valid Program");
//                System.out.println(parser.symTab);
//
//            } else {
//                System.out.println("invalid Program");
//            }
//            System.out.println();
//
//        }
//    }

}

