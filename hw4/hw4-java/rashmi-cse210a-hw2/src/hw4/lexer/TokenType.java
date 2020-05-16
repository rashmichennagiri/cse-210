package hw4.lexer;

/**
 * enumerates all the possible tokens in the while language
 * 
 * @author rashmichennagiri
 *
 */
public enum TokenType {

	// SINGLE CHARACTER TOKENS:
	LEFT_PARENTHESIS, RIGHT_PARENTHESIS, // ( )
	LEFT_BRACE, RIGHT_BRACE,	// { }
	PLUS, MINUS, MULTIPLY, DIVIDE,
	NOT,
	AND, OR,
	SEMICOLON,
	EQUAL,
	LESS_THAN,
	
	
	// DOUBLE CHARACTER TOKENS:,
	ASSIGNMENT,
	
	// LITERALS:
	VARIABLE, NUMBER, BOOLEAN, ARRAY,
	
	
	//KEYWORDS:
	SKIP, 
	DO, WHILE, 
	IF, ELSE, THEN,
	
	EOF
}

