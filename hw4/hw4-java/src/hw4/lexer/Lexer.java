package hw4.lexer;

import java.util.ArrayList;
import java.util.List;

import hw4.WhileInterpreterException;

/**
 * To turn the input of characters into a stream of tokens
 * 
 * @author rashmichennagiri
 *
 */
public class Lexer {

	private final String userInput; // user input string of characters

	private int startIndex = 0; 	// starting character index of token
	private int currentIndex = 0; 	// current character index in lexeme

	private final List<Token> tokens = new ArrayList<Token>();

	public Lexer(String input) {
		this.userInput = input;
	}

	/**
	 * scans user input for a list of tokens
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	public List<Token> scanUserInputForTokens() throws WhileInterpreterException {

		while (!isAtEndOfUserInput()) {
			startIndex = currentIndex;
			scanToken();
		}

		// public Token(TokenType tt, String lexeme, Object literal) {
		tokens.add(new Token(TokenType.EOF, "", null));
		return tokens;
	}

	/**
	 * scans for a single token
	 * 
	 * @throws WhileInterpreterException
	 */
	private void scanToken() throws WhileInterpreterException {

		char c = updateCurrentCharacter();

		switch (c) {

		case ' ': // ignore whitespaces...
		case '\r':
		case '\t':
			break;

		// brackets
		case '(':
			addToken(TokenType.LEFT_PARENTHESIS);
			break;
		case ')':
			addToken(TokenType.RIGHT_PARENTHESIS);
			break;
		case '{':
			addToken(TokenType.LEFT_BRACE);
			break;
		case '}':
			addToken(TokenType.RIGHT_BRACE);
			break;

		// arithmetic operators
		case '+':
			addToken(TokenType.PLUS);
			break;
		case '-':
			addToken(TokenType.MINUS);
			break;
		case '*':
			addToken(TokenType.MULTIPLY);
			break;
		case '/':
			addToken(TokenType.DIVIDE);
			break;

		// special characters
		case ';':
			addToken(TokenType.SEMICOLON);
			break;

		case ':':
			if (matchNextCharacter('='))
				addToken(TokenType.ASSIGNMENT);
			else
				throw new WhileInterpreterException("INVALID ASSIGNMENT OPERATOR");
			break;

		// boolean operators
		case '∧':
			addToken(TokenType.AND);
			break;
		case '∨':
			addToken(TokenType.OR);
			break;
		case '¬': // ¬ option l
			addToken(TokenType.NOT);
			break;
			
		// comparison operators
		case '<':
			addToken(TokenType.LESS_THAN);
			break;
		case '=':
			addToken(TokenType.EQUAL);
			break;

		default:
			if (isDigit(c))
				getNumberToken();
			else if (isAlphaNumeric(c)) // either keyword or variable
				getIdentifierToken();

			else
				throw new WhileInterpreterException("INVALID CHARACTER!");
			// ˆ option i
			// ˇ shift option t

		}
	}

	/**
	 * advances the current character pointer
	 * 
	 * @return
	 */
	private char updateCurrentCharacter() {
		currentIndex++;
		return userInput.charAt(currentIndex - 1);
	}

	/**
	 * checks for end of user input
	 * 
	 * @return
	 */
	private boolean isAtEndOfUserInput() {
		return (currentIndex >= userInput.length());
	}

	/**
	 * 
	 * @param expectedChar
	 * @return
	 */
	private boolean matchNextCharacter(char expectedChar) {

		if (isAtEndOfUserInput())
			return false;
		if (userInput.charAt(currentIndex) != expectedChar)
			return false;

		currentIndex++;
		return true;
	}

	/**
	 * returns current character
	 * 
	 * @return
	 */
	private char getCurrentCharacter() {
		if (isAtEndOfUserInput())
			return '\0';

		return userInput.charAt(currentIndex);
	}

	/**
	 * checks if specified character is a digit
	 * 
	 * @param n
	 * @return
	 */
	private boolean isDigit(char n) {
		return (n >= '0' && n <= '9');
	}

	/**
	 * parses for a number token
	 */
	private void getNumberToken() {
		while (isDigit(getCurrentCharacter()))
			updateCurrentCharacter();

		addToken(TokenType.NUMBER, Integer.parseInt(userInput.substring(startIndex, currentIndex)));
	}

	/**
	 * checks if specified character is an alphabet
	 * 
	 * @param a
	 * @return
	 */
	private boolean isAlphaNumeric(char a) {
		return (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z') || (a >= '0' && a <= '9');
	}

	/**
	 * parses for an identifier token (keyword/variable)
	 */
	private void getIdentifierToken() {
		while (isAlphaNumeric(getCurrentCharacter()))
			updateCurrentCharacter();

		String val = userInput.substring(startIndex, currentIndex);

		switch (val) {

		case "skip":
			addToken(TokenType.SKIP);
			break;

		case "true":
			addToken(TokenType.BOOLEAN, true);
			break;

		case "false":
			addToken(TokenType.BOOLEAN, false);
			break;

		case "while":
			addToken(TokenType.WHILE);
			break;

		case "do":
			addToken(TokenType.DO);
			break;

		case "if":
			addToken(TokenType.IF);
			break;

		case "then":
			addToken(TokenType.THEN);
			break;

		case "else":
			addToken(TokenType.ELSE);
			break;

		default:
			addToken(TokenType.VARIABLE, val);
		}
	}

	/**
	 * 
	 * @param tt
	 */
	private void addToken(TokenType tt) {
		addToken(tt, null);
	}

	/**
	 * 
	 * @param tt
	 * @param literal
	 */
	private void addToken(TokenType tt, Object literal) {
		String tokenText = userInput.substring(startIndex, currentIndex);
		// public Token(TokenType tt, String lexeme, Object literal) {
		tokens.add(new Token(tt, tokenText, literal));
	}

}
