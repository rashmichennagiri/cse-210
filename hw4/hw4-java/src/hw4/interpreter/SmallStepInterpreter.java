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
	
	List<String> results = new ArrayList<String>();


	int ssize = 0;
	int current = 0;
	int mode = 0;

	/**
	 * 
	 * @param ast
	 * @throws WhileInterpreterException
	 */
	public void interpretAST(Node ast) throws WhileInterpreterException {

		// 1. split AST into statements
		atree = new ASTPrinter().print(ast);
		System.out.println(atree);

		String[] arr = atree.split(";");
		for (String a : arr)
			statements.add(a);

		// 2. start executing statements:

		ssize = statements.size();

		while (current < ssize) {
			String s = getInputString(); 

			Lexer lexer = new Lexer(s);
			List<Token> tokens = lexer.scanUserInputForTokens();

			System.out.println("\n LIST OF TOKENS:");
			for (Token t : tokens) {
				System.out.println(t);
			}

			Parser parser = new Parser(tokens);
			Node ast2;
			try {
				ast2 = parser.parse();

			} catch (WhileInterpreterException e) {
				e.printStackTrace();
				System.out.println("taking next statement");
				mode = 1;
				continue;

			}

//			System.out.println(new ASTPrinter().print(ast2));

			Interpreter intepreter = new Interpreter();
			String result = intepreter.interpret(ast2);
			
			if(result==null)
				results.add(Storage.getCurrentState());
			else
				results.add( result );

			current++;

			System.out.println(results.get(results.size()-1));

		}
		
		System.out.println(results.get(results.size()-1));
	}
	

	/**
	 * 
	 * @return
	 */
	public String getInputString() {

		if (mode == 0) {
			return statements.get(current);

		} else if (mode == 1) {
			String a = statements.get(current);
			current++;
			String b = statements.get(current);
			mode = 0;
			return a + " ; " + b;
		}
		return null;
	}

}
