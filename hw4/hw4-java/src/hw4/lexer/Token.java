package hw4.lexer;

/**
 * represents a token with all of its details
 * 
 * @author rashmichennagiri
 *
 */
public class Token {

	public final TokenType tokenType;		// type of the token
	public final String lexeme;				// string value of token
	public final Object literal;			// actual token value
	
	
	public Token(TokenType tt, String lexeme, Object literal) {
		this.tokenType = tt;
		this.lexeme = lexeme;
		this.literal = literal;
	}


	@Override
	public String toString() {
		return "Token [tokenType=" + tokenType + ", lexeme=" + lexeme + ", literal=" + literal + "]";
	}
	
	
}
