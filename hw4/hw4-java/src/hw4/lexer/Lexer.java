package hw2.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private int lineNumber = 1; 	// line number of token

	private static final Map<String, TokenType> reservedKeywords;
	
	private final List<Token> tokens = new ArrayList<Token>();
	

	public Lexer(String input) {
		this.userInput = input;
	}
	
	static {
		
		reservedKeywords = new HashMap<>();
		
		reservedKeywords.put("skip", TokenType.SKIP);
		reservedKeywords.put("true", TokenType.TRUE);
		reservedKeywords.put("false", TokenType.FALSE);
		reservedKeywords.put("do", TokenType.DO);
		reservedKeywords.put("while", TokenType.WHILE);
		reservedKeywords.put("if", TokenType.IF);
		reservedKeywords.put("else", TokenType.ELSE);
		reservedKeywords.put("then", TokenType.THEN);
		reservedKeywords.put("var", TokenType.VAR);

		
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

		// public Token(TokenType tt, String lexeme, Object literal, int line) {
		tokens.add(new Token(TokenType.EOF, "", null, lineNumber));
		return tokens;
	}

	/**
	 * scans for a single token
	 * @throws WhileInterpreterException 
	 */
	private void scanToken() throws WhileInterpreterException {

		char c = updateCurrentCharacter();

		switch (c) {

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
			if (matchNextCharacter('/')) {
				// eat up comments
				while (getCurrentCharacter() != '\n' && !isAtEndOfUserInput())
					updateCurrentCharacter();
			} else
				addToken(TokenType.DIVIDE);
			break;

		case ';':
			addToken(TokenType.SEMICOLON);
			break;
		case ':':
			if (matchNextCharacter('='))
				addToken(TokenType.ASSIGNMENT);
			else 
				throw new WhileInterpreterException(lineNumber, "INVALID ASSIGNMENT OPERATOR");
			break;

		case '∧':
			addToken(TokenType.AND);
			break;
		case '∨':
			addToken(TokenType.OR);
			break;
		case '¬':
			addToken(TokenType.NEGATE);
			break;

		case '=':
			addToken(TokenType.EQUAL);
			break;
		case '<': // may be < or <=
			addToken(matchNextCharacter('=') ? TokenType.LESS_THAN_EQUAL : TokenType.LESS_THAN);
			break;

		case ' ': // ignore whitespaces...
		case '\r':
		case '\t':
			break;

		case '\n':
			lineNumber++;
			break;

		case '"': getStringToken();
		break;
		
		default:
			if( isDigit(c) )
				getNumberToken();
			else if( isAlphabet(c))
				getIdentifierToken();
			else
				throw new WhileInterpreterException(lineNumber, "INVALID CHARACTER!");
			// ˆ option i
			// ˇ shift option t
			// ¬ option l
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
	 * parses for string token
	 * @throws WhileInterpreterException 
	 */
	private void getStringToken() throws WhileInterpreterException {
		
		while( getCurrentCharacter() != '"' && !isAtEndOfUserInput() ) {
			updateCurrentCharacter();
		}
		
		if( isAtEndOfUserInput() )
			throw new WhileInterpreterException(lineNumber, "STRING NOT TERMINATED PROPERLY");
			
		//parse closing " character
		updateCurrentCharacter();
		
		//get actual string value:
		String stringValue = userInput.substring(startIndex+1, currentIndex-1);
		addToken(TokenType.STRING, stringValue);
		
	}
	
	
	/**
	 * checks if specified character is a digit
	 * 
	 * @param n
	 * @return
	 */
	private boolean isDigit(char n) {
		return (n>='0' && n<='9');
	}

	
	/**
	 * parses for a number token
	 */
	private void getNumberToken() {
		// TODO fractions
		while( isDigit(getCurrentCharacter()) )
			updateCurrentCharacter();
		
		addToken(TokenType.NUMBER, Integer.parseInt(userInput.substring(startIndex, currentIndex)));
	}
	
	
	/**
	 * checks if specified character is an alphabet
	 * @param a
	 * @return
	 */
	private boolean isAlphabet(char a) {
		return (a>='a' && a<='z') || (a>='A' && a<='Z') ;
	}
	
	
	/**
	 * parses for an identifier token
	 */
	private void getIdentifierToken() {
		while( isAlphabet(getCurrentCharacter()))
			updateCurrentCharacter();
		
		String var = userInput.substring(startIndex, currentIndex);
		
		if( reservedKeywords.get(var) == null)
			addToken(TokenType.IDENTIFIER, var);
		else
			;// TODO??
	}
	
	
	/**
	 * 
	 * @param tt
	 */
	private void addToken(TokenType tt) {
		// public Token(TokenType tt, String lexeme, Object literal, int line) {
		addToken(tt, null);
	}

	/**
	 * 
	 * @param tt
	 * @param literal
	 */
	private void addToken(TokenType tt, Object literal) {
		String tokenText = userInput.substring(startIndex, currentIndex);
		tokens.add(new Token(tt, tokenText, literal, lineNumber));
	}

	/**
	 * checks for end of user input
	 * 
	 * @return
	 */
	private boolean isAtEndOfUserInput() {
		return (currentIndex >= userInput.length());
	}

}
