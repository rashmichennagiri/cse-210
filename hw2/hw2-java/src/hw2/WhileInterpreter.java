package hw2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import hw2.lexer.Lexer;
import hw2.lexer.Token;


/**
 * main class for WHILE interpreter
 * 
 * @author rashmichennagiri
 * @param args
 * 
 */
public class WhileInterpreter {
	
	
	static boolean hadError = false; 	// to specify if user input has error
	
	

	/**
	 * executes commands written in the specified file
	 * 
	 * @param path
	 * @throws IOException
	 * @throws WhileInterpreterException 
	 */
	private static void runFile(String path) throws IOException, WhileInterpreterException {
	    byte[] bytes = Files.readAllBytes(Paths.get(path));        
	    run(new String(bytes, Charset.defaultCharset()));    
	    
	    if(hadError)
	    	System.exit(65);
	  }                                                            

	
	/**
	 * provides prompt to enter code onto console
	 * 
	 * @throws IOException
	 * @throws WhileInterpreterException 
	 */
	private static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		for(;;) {
			hadError = false;
			System.out.print("\n>> ");
			try {
				run( reader.readLine() );
			} catch (WhileInterpreterException e) {
				e.printStackTrace();
			}
		}
		// cmd+C to exit
	}
	
	
	/**
	 * 
	 * @param userInput
	 * @throws WhileInterpreterException 
	 */
	private static void run(String userInput) throws WhileInterpreterException {
		Lexer lexer = new Lexer(userInput);
		List<Token> tokens = lexer.scanUserInputForTokens();
		
		for(Token t: tokens) {
			System.out.println(t);
		}
	}
	
	
	/**
	 * TODO throws declaration
	 * 
	 * @param args
	 * @throws IOException
	 * @throws WhileInterpreterException 
	 */
	public static void main(String[] args) throws IOException, WhileInterpreterException {
		
		if (args.length > 1) {                                   
		      System.out.println("Usage: while [scriptName]");            
		      System.exit(64);
		      
		    } else if (args.length == 1) {                           
		      runFile(args[0]);   
		      
		    } else {                                                 
		      runPrompt();                                           
		    }                                                        
		  }       
	
}
