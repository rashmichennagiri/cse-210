package hw2.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author rashmichennagiri
 *
 */
public class ASTGenerator {

	/**
	 * 
	 * @param outputDirectory
	 * @param baseName
	 * @param types
	 * @throws IOException
	 */
	private static void defineAST(String outputDirectory, String baseName, List<String> types) throws IOException {

		String path = outputDirectory + "/" + baseName + ".java";
		PrintWriter pw = new PrintWriter(path, "UTF-8");

		pw.println("package hw2.parser;");
		pw.println();

		pw.println("import hw2.lexer.Token;");
		pw.println();

		pw.println("public abstract class " + baseName + "{");

		defineVisitor(pw, baseName, types);

		// BASE CLASS accept() METHOD
		pw.println();
		pw.println("	public abstract <R> R accept(Visitor<R> visitor);");
		pw.println();

		// AST classes
		for (String type : types) {
			String className = type.split(":")[0].trim();
			String fields = type.split(":")[1].trim();
			defineTypeSubClass(pw, baseName, className, fields);
		}


		pw.println("}");
		pw.close();

	}

	/**
	 * iterates through all of the subclasses and declares a visit method for each
	 * one
	 * 
	 * @param pw
	 * @param baseName
	 * @param types
	 */
	private static void defineVisitor(PrintWriter pw, String baseName, List<String> types) {
		pw.println("	public interface Visitor<R> {");

		for (String t : types) {
			String typeName = t.split(":")[0].trim();
			pw.println("	R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
		}

		pw.println("	}");
	}

	/**
	 * 
	 * @param pw
	 * @param baseName
	 * @param className
	 * @param fields
	 */
	private static void defineTypeSubClass(PrintWriter pw, String baseName, String className, String fields) {

		pw.println("	public static class " + className + " extends " + baseName + "{");

		// FIELDS
		pw.println();
		String[] fieldNames = fields.split(",");

		for (String f : fieldNames) {
			pw.println("	public final " + f + ";");
		}
		pw.println();

		// CONSTRUCTOR
		pw.println("	" + className + "(" + fields + "){");

		// store parameters in fields
		for (String f : fieldNames) {
			String var = f.split(" ")[1];
			pw.println("	this." + var + " = " + var + ";");
		}

		pw.println("	}");
		pw.println();

		// VISITOR PATTERN
		pw.println("	@Override");
		pw.println("	public <R> R accept(Visitor<R> visitor) {");
		pw.println("	return visitor.visit" + className + baseName + "(this);" );
		pw.println("	}");




		pw.println();

		pw.println("	}");

	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*
		 * if(args.length!=1) {
		 * System.err.println("Usage: generate_ast <output directory>"); System.exit(1);
		 * }
		 */

		String outputDirectory = "/Users/rashmichennagiri/Documents/2020_q3_spring/cse210a_PL/pl_homeworks/cse-210/hw2/hw2-java/src/hw2/parser";

		
		// NO SPACES!
		defineAST(outputDirectory, "Expression",
				Arrays.asList("Binary : Expression left,Token operator,Expression right",
						"Grouping : Expression expression", "Literal  : Object value",
						"Unary    : Token operator,Expression expr",
						"Variable: Token name,Expression initializer"));
		 


		defineAST(outputDirectory, "Statement",
				Arrays.asList("Expr : Expression ex",
						"Print : Expression ex",
						"Var : Token name,Expression initializer"));



	}
}
