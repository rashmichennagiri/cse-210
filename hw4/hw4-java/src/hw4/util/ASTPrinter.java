package hw4.util;

import hw4.parser.Node;
import hw4.parser.Node.ArrayNode;
import hw4.parser.Node.AssignmentOperationNode;
import hw4.parser.Node.BinaryOperationNode;
import hw4.parser.Node.BooleanOperationNode;
import hw4.parser.Node.BooleanValueNode;
import hw4.parser.Node.ComparisonOperationNode;
import hw4.parser.Node.IfOperationNode;
import hw4.parser.Node.IntegerValueNode;
import hw4.parser.Node.NotOperationNode;
import hw4.parser.Node.SemiColonNode;
import hw4.parser.Node.SkipOperationNode;
import hw4.parser.Node.UnaryOperationNode;
import hw4.parser.Node.VariableNameNode;
import hw4.parser.Node.WhileOperationNode;

/**
 * 
 * @author rashmichennagiri
 *
 */
public class ASTPrinter implements Node.Visitor<String> {

	/**
	 * 
	 * @param expr
	 * @return
	 */
	public String print(Node expr) {
		return expr.accept(this);
	}

	@Override
	public String visitSemicolonNode(SemiColonNode expression) {
		//return "< " + expression.left.accept(this) +"> "+expression.operator.lexeme +" < "+ expression.right.accept(this) + " > ";
		return expression.left.accept(this) +" "+expression.operator.lexeme +" "+ expression.right.accept(this) + "";

		//return addParams(expression.left, expression.operator.lexeme, expression.right);

	}

	@Override
	public String visitBinaryOperationNode(BinaryOperationNode expression) {
		return addParams(expression.left, expression.operator.lexeme, expression.right);
	}

	@Override
	public String visitUnaryOperationNode(UnaryOperationNode expression) {
		return addParams(expression.operator.lexeme, expression.expr);
	}

	@Override
	public String visitAssignmentNode(AssignmentOperationNode expression) {
		return expression.variableName.accept(this) + " " + expression.operator.lexeme + " " + expression.value.accept(this);
	}

	@Override
	public String visitComparisonOperationNode(ComparisonOperationNode expression) {
		return addParams(expression.left, expression.operator.lexeme, expression.right);
	}

	@Override
	public String visitBooleanOperationNode(BooleanOperationNode expression) {
		return addParams(expression.left, expression.operator.lexeme, expression.right);
	}

	@Override
	public String visitNotOperationNode(NotOperationNode expression) {
		return addParams(expression.token.lexeme, expression.expr);
	}

	@Override
	public String visitIfOperationNode(IfOperationNode e) {
		// if (5<x) then { x := (2-2) } else { y := 9 }, {x → 9}
		return e.token.lexeme + " " + e.condition.accept(this) + " then { " +  e.ifTrueCommands.accept(this) + " } else { " + e.ifFalseCommands.accept(this) + " }" ;
	}

	@Override
	public String visitWhileOperationNode(WhileOperationNode e) {
		// while ¬(x<0) do { x := -1 }
		return e.token.lexeme + " "+ e.condition.accept(this) + " do { " +  e.whileTrueCommands.accept(this) + " } ";
	}

	@Override
	public String visitSkipOperationNode(SkipOperationNode expression) {
		return "skip";
	}

	@Override
	public String visitIntegerValueNode(IntegerValueNode expression) {
		return ""+expression.value;
	}

	@Override
	public String visitBooleanValueNode(BooleanValueNode expression) {
		return ""+expression.value;
	}

	@Override
	public String visitVariableNameNode(VariableNameNode expression) {
		return ""+expression.variableName;
	}
	
	@Override
	public String visitArrayNode(ArrayNode expression) {
		return ""+expression.arrValue;
	}

	/**
	 * 
	 * @param name
	 * @param expressions
	 * @return
	 */
	private String addParams(String name, Node... expressions) {

		StringBuilder sb = new StringBuilder();

		sb.append("(").append(name);

		for (Node ex : expressions) {
			sb.append(" ");
			sb.append(ex.accept(this));
		}

		sb.append(")");

		return sb.toString();
	}


	private String addParams(Node expression1, String name, Node expression2) {

		StringBuilder sb = new StringBuilder();

		sb.append("(");
		sb.append(expression1.accept(this));
		sb.append(" ").append(name);
		sb.append(" ");
		sb.append(expression2.accept(this));

		sb.append(")");

		return sb.toString();
	}

	private String addParams(String name, Node expression) {

		StringBuilder sb = new StringBuilder();

		sb.append("(").append(name);

		sb.append(" ");
		sb.append(expression.accept(this));

		sb.append(")");

		return sb.toString();
	}



	/*
	 * public static void main(String[] args) {
	 * 
	 * Node expression = new Node.Binary(
	 * 
	 * new Unary( new Token(TokenType.MINUS, "-", null, 1), new Literal(123)), new
	 * Token(TokenType.MULTIPLY, "*", null, 1), new Node.Grouping( new
	 * Node.Literal(45.67)) );
	 * 
	 * System.out.println(new ASTPrinter().print(expression)); }
	 * 
	 */

}
