package hw4;

import hw4.lexer.Token;

/**
 * 
 * @author rashmichennagiri
 *
 */
public class WhileInterpreterException extends Exception{

	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 * @param errorMessage
	 */
	public WhileInterpreterException(String errorMessage) {
        super(errorMessage);
		WhileInterpreter.hadError = true;  
    }
	
	
	/**
	 * 
	 * @param errorMessage
	 * @param err
	 */
	public WhileInterpreterException(String errorMessage, Throwable err) {
        super(errorMessage, err);
		WhileInterpreter.hadError = true;  
    }
	
	
	/**
	 * 
	 * @param token
	 * @param msg
	 */
	public WhileInterpreterException(Token token, String msg) {
			this( " ERROR AT " + token.lexeme + ": " + msg);
    }
		
}
