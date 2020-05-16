package hw4.parser;

import hw4.lexer.Token;

/**
 * definitions of all the various types of nodes in the AST
 * 
 * @author rashmichennagiri
 *
 */
public abstract class Node {

	public abstract <R> R accept(Visitor<R> visitor);

	public interface Visitor<R> {

		R visitSemicolonNode(SemiColonNode expression);

		R visitBinaryOperationNode(BinaryOperationNode expression);

		R visitUnaryOperationNode(UnaryOperationNode expression);

		R visitAssignmentNode(AssignmentOperationNode expression);

		R visitComparisonOperationNode(ComparisonOperationNode expression);

		R visitBooleanOperationNode(BooleanOperationNode expression);

		R visitNotOperationNode(NotOperationNode expression);

		R visitIfOperationNode(IfOperationNode expression);

		R visitWhileOperationNode(WhileOperationNode expression);

		R visitSkipOperationNode(SkipOperationNode expression);

		R visitIntegerValueNode(IntegerValueNode expression);

		R visitBooleanValueNode(BooleanValueNode expression);

		R visitVariableNameNode(VariableNameNode expression);

		R visitArrayNode(ArrayNode expression);
	}

	/**
	 * node for the ';' character
	 * 
	 * @author rashmichennagiri
	 */
	public static class SemiColonNode extends Node {

		public Node parent = null;
		public final Node left;
		public final Token operator; // ;
		public final Node right; // cterm

		SemiColonNode(Node left, Token operator, Node right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
			// this.parent = parent;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitSemicolonNode(this);
		}
	}

	/**
	 * node for the 'skip' operation
	 * 
	 * @author rashmichennagiri
	 */
	public static class SkipOperationNode extends Node {

		public final Token token;
		public final Object value;
		public Node parent = null;

		public SkipOperationNode(Token token, Object value) {
			this.token = token;
			this.value = value;
			// this.parent = parent;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitSkipOperationNode(this);
		}
	}

	/**
	 * assignment operator node
	 * 
	 * @author rashmichennagiri
	 *
	 */
	public static class AssignmentOperationNode extends Node {

		public Node parent = null;
		public final Node variableName;
		public final Token operator; // :=
		public final Node value;

		AssignmentOperationNode(Node variableName, Token op, Node value) {
			this.variableName = variableName;
			this.operator = op;
			this.value = value;
			// this.parent = parent;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitAssignmentNode(this);
		}
	}

	/**
	 * node for 'if' operation
	 * 
	 * @author rashmichennagiri
	 */
	public static class IfOperationNode extends Node {

		public Node parent = null;
		public final Token token;
		public final Node condition; // boolean op
		public final Node ifTrueCommands;
		public final Node ifFalseCommands;

		public IfOperationNode(Token token, Node condition, Node ifTrueCommands, Node ifFalseCommands) {
			this.token = token;
			this.condition = condition;
			this.ifTrueCommands = ifTrueCommands;
			this.ifFalseCommands = ifFalseCommands;
			// this.parent = parent;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitIfOperationNode(this);
		}
	}

	/**
	 * node for 'while' operation
	 * 
	 * @author rashmichennagiri
	 */
	public static class WhileOperationNode extends Node {

		public final Token token;
		public final Node condition; // boolean op
		public final Node whileTrueCommands;
		public final Node whileFalseCommands;
		public Node parent = null;

		public WhileOperationNode(Token token, Node condition, Node whileTrueCommands, Node whileFalseCommands) {
			this.token = token;
			this.condition = condition;
			this.whileTrueCommands = whileTrueCommands;
			this.whileFalseCommands = whileFalseCommands;
			// this.parent = parent;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitWhileOperationNode(this);
		}
	}

	/**
	 * binary operator node
	 * 
	 * @author rashmichennagiri
	 */
	public static class BinaryOperationNode extends Node {

		public final Node left; // integer node of type 'Number' / subtree of AST
		public final Token operator; // operation token: + - * /
		public final Node right; // integer node of type 'Number' / subtree of AST

		BinaryOperationNode(Node left, Token operator, Node right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitBinaryOperationNode(this);
		}
	}

	/**
	 * unary operator node
	 * 
	 * @author rashmichennagiri
	 */
	public static class UnaryOperationNode extends Node {

		public final Token operator; // unary operator: + or -
		public final Node expr; // expr = AST node

		UnaryOperationNode(Token operator, Node expr) {
			this.operator = operator;
			this.expr = expr;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitUnaryOperationNode(this);
		}
	}

	/**
	 * comparison operator node
	 * 
	 * @author rashmichennagiri
	 */
	public static class ComparisonOperationNode extends Node {

		public final Node left;
		public final Token operator; // = <
		public final Node right;

		ComparisonOperationNode(Node left, Token operator, Node right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitComparisonOperationNode(this);
		}
	}

	/**
	 * boolean operator node
	 * 
	 * @author rashmichennagiri
	 */
	public static class BooleanOperationNode extends Node {

		public final Node left;
		public final Token operator; // AND OR
		public final Node right;

		BooleanOperationNode(Node left, Token operator, Node right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitBooleanOperationNode(this);
		}
	}

	/**
	 * node for the 'not' operation
	 * 
	 * @author rashmichennagiri
	 */
	public static class NotOperationNode extends Node {

		public final Token token; // ¬
		public final Node expr;

		NotOperationNode(Token t, Node expr) {
			this.token = t;
			this.expr = expr;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitNotOperationNode(this);
		}
	}

	/**
	 * integer value node
	 * 
	 * @author rashmichennagiri
	 */
	public static class IntegerValueNode extends Node {

		public final Token token;
		public final int value;

		IntegerValueNode(Token t, int value) {
			this.token = t;
			this.value = value;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitIntegerValueNode(this);
		}
	}

	/**
	 * boolean value node
	 * 
	 * @author rashmichennagiri
	 */
	public static class BooleanValueNode extends Node {

		public final Token token;
		public final boolean value; // true false

		BooleanValueNode(Token t, boolean value) {
			this.token = t;
			this.value = value;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitBooleanValueNode(this);
		}
	}

	/**
	 * variable name node
	 * 
	 * @author rashmichennagiri
	 */
	public static class VariableNameNode extends Node {

		public final Token token;
		public final String variableName;

		VariableNameNode(Token t, String varName) {
			this.token = t;
			this.variableName = varName;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitVariableNameNode(this);
		}
	}

	/**
	 * array node
	 * 
	 * @author rashmichennagiri
	 */
	public static class ArrayNode extends Node {

		public final Token token;
		public final String arrValue;

		ArrayNode(Token t, String arr) {
			this.token = t;
			this.arrValue = arr;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitArrayNode(this);
		}
	}

	/**
	 * to delete command node from the given ast
	 * 
	 * @param command
	 * @param ast
	 * @return
	 */
	public Node deleteNode(Node command, Node ast) {

		return ast;
	}

}
