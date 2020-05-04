package hw4.interpreter;

import java.util.ArrayList;
import java.util.List;

import hw4.WhileInterpreterException;
import hw4.lexer.Lexer;
import hw4.lexer.Token;
import hw4.parser.Node;
import hw4.parser.Parser;
import hw4.util.ASTPrinter;


/**
 * 
 * @author rashmichennagiri
 *
 */
public class SmallStepInterpreter {

	String atree;
	List<String> statements = new ArrayList<String>();


	/**
	 * 
	 * @param ast
	 * @throws WhileInterpreterException 
	 */
	public void interpretAST(Node ast) throws WhileInterpreterException {

		// 1. split AST into statements
		atree = new ASTPrinter().print(ast);
		System.out.println(atree );

		String[] arr = atree.split(";");
		for(String a : arr)
			statements.add(a);

		// 2. start executing statements:

		for ( String s : statements) {

			Lexer lexer = new Lexer(s);
			List<Token> tokens = lexer.scanUserInputForTokens();

			System.out.println("\n LIST OF TOKENS:");
			for (Token t : tokens) {
				System.out.println(t);
			}

			Parser parser = new Parser(tokens);
			Node ast2 = parser.parse();
			System.out.println( new ASTPrinter().print(ast2));


			Interpreter intepreter = new Interpreter();
			intepreter.interpret(ast2);
		}

	}
}
