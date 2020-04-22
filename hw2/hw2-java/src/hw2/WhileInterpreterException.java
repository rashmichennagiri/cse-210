package hw2;

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
	 * @param line
	 * @param where
	 * @param msg
	 */
	public WhileInterpreterException(int line, String msg) {
		this( " [LINE " + line + " ]  ERROR: " + msg);
    }
	

	
	
	/*

	public static void error(int line, String msg ) {
		reportError(line, "", msg);
	}
	
	private static void reportError(int line, String where, String message) {
		System.err.println(" [LINE " + line + " ]  ERROR AT: " + where + ": " + message);
		WhileInterpreter.hadError = true;  
	}
	 */

	
}
