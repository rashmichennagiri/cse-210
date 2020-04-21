package hw2;

/**
 * main class for while interpreter
 * 
 * @param args
 */
public class WhileInterpreter {

	

	
	public static void main(String[] args) {
		if (args.length > 1) {                                   
		      System.out.println("Usage: jlox [script]");            
		      System.exit(64); 
		    } else if (args.length == 1) {                           
		      runFile(args[0]);                                      
		    } else {                                                 
		      runPrompt();                                           
		    }                                                        
		  }       
	}
}
