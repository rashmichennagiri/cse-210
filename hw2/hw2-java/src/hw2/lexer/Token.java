package hw2.lexer;

/**
 * represents a token with all of its details
 * 
 * @author rashmichennagiri
 *
 */
public class Token {

	final TokenType tokenType;		// type of the token
	final String lexeme;	// actual lexeme
	final Object literal;	// literal value of the token	
	final int line;			// line number of the token
	
	
	public Token(TokenType tt, String lexeme, Object literal, int line) {
		this.tokenType = tt;
		this.lexeme = lexeme;
		this.literal = literal;
		this.line = line;
	}


	@Override
	public String toString() {
		return "Token [tokenType=" + tokenType + ", lexeme=" + lexeme + ", literal=" + literal + ", line=" + line + "]";
	}
	
	
}
