package hw4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import hw2.interpreter.Interpreter;
import hw2.lexer.Lexer;
import hw2.lexer.Token;
import hw2.parser.Expression;
import hw2.parser.Parser;
import hw2.parser.Statement;
import hw2.util.ASTPrinter;


/**
 * main class for WHILE interpreter
 * 
 * @author rashmichennagiri
 * @param args
 * 
 */
public class WhileInterpreter {


	static boolean hadError = false; 	// to specify if user input has error
	public static boolean hadRuntimeError = false; 	// to specify if user input has error



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
	    if (hadRuntimeError) 
	    	System.exit(70);            

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
		String userInput = "@";

		for(;;) {
			hadError = false;
			System.out.print("\n>> ");
			try {
				userInput = reader.readLine();
				if(userInput.equals("@"))
						break;
				else
					run( userInput );
			} catch (WhileInterpreterException e) {
				e.printStackTrace();
			}
		}
		// @ to exit
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
		
		
		Parser parser = new Parser(tokens);
		//Expression expr 
		List<Statement> s = parser.parse();

		if(hadError)
			return;

		//System.out.println( new ASTPrinter().print(expr));
		
		Interpreter intepreter = new Interpreter();
		intepreter.interpret(s);
		
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
