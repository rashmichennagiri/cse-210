package hw4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import hw4.lexer.Lexer;
import hw4.lexer.Token;

/**
 * main class for WHILE interpreter
 * 
 * @author rashmichennagiri
 * @param args
 * 
 */
public class WhileInterpreter {

	static boolean hadError = false; // to specify if user input has error
	public static boolean hadRuntimeError = false; // to specify if user input has error

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

		for (;;) {
			hadError = false;
			System.out.print("\n>> ");
			try {
				userInput = reader.readLine();
				if (userInput.equals("@"))
					break;
				else
					run(userInput);
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

		for (Token t : tokens) {
			System.out.println(t);
		}

		// Parser parser = new Parser(tokens);
		// Expression expr
		// List<Statement> s = parser.parse();

		if (hadError)
			return;

		// System.out.println( new ASTPrinter().print(expr));

		// Interpreter intepreter = new Interpreter();
		// intepreter.interpret(s);

	}

	/**
	 * TODO throws declaration
	 * 
	 * @param args
	 * @throws IOException
	 * @throws WhileInterpreterException
	 */
	public static void main(String[] args) throws IOException, WhileInterpreterException {

		runPrompt();
	}

}
