/**
 * class Token packages the type and value of each token in a string of text
 * and also keeps track of the line number where the token appeared.
 */
public class Token {
    private String value;
    private String type;
    private int lineNum=1;

    public Token (String t, String v , int lineNum) {
       this.value=v;
       this.type=t;
       this.lineNum=lineNum;
    }

    /**
     *
     * @return type of the token
     */
	public String getType() {
	    return type;
    }

    /**
     *
     * @return value of the token
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @return line number of the token
     */
    public int getLineNum() {
        return lineNum;
    }

    /**
     * Prints the type, value, line number of a token.
     * @return
     */
    @Override
	public String toString() {

        return (String.format("%-10s%-8sline%s", type, value,lineNum));
    }
}