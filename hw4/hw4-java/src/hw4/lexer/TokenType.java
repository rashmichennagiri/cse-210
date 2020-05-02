package hw4.lexer;

/**
 * enumerates all the possible tokens in the while language
 * 
 * @author rashmichennagiri
 *
 */
public enum TokenType {

	// SINGLE CHARACTER TOKENS:
	LEFT_PARENTHESIS, RIGHT_PARENTHESIS,
	LEFT_BRACE, RIGHT_BRACE,
	//COMMA, DOT, 
	PLUS, MINUS, MULTIPLY, DIVIDE,
	NEGATE,
	AND, OR,
	SEMICOLON,
//	EQUAL,
	LESS_THAN,
	
	
	// DOUBLE CHARACTER TOKENS:,
	ASSIGNMENT,
	
	// LITERALS:
	VARIABLE, NUMBER,
	
	
	//KEYWORDS:
	SKIP, 
	TRUE, FALSE,
	DO, WHILE, 
	IF, ELSE, THEN,
	
	EOF
}

