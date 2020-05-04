package hw4.interpreter;

import hw4.WhileInterpreter;
import hw4.WhileInterpreterException;
import hw4.lexer.Token;
import hw4.parser.Node;
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
 * To execute the parsed AST
 * 
 * @author rashmichennagiri
 *
 */
public class Interpreter implements Node.Visitor<Object> {

	private Storage statesStore = new Storage();

	/**
	 * 
	 * @param n
	 */
	public void interpret(Node n) {
		Object value = evaluateExpression(n);
		System.out.println(stringify(value));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String stringify(Object obj) {
		return obj.toString();
	}

	/**
	 * 
	 * @param expr
	 * @return
	 */
	private Object evaluateExpression(Node expr) {
		return expr.accept(this);
	}






	// ###############################################################################
	// EVALUATE A-EXP:
	// ###############################################################################

	@Override
	public Object visitIntegerValueNode(IntegerValueNode expression) {
		return expression.value;
	}

	@Override
	public Object visitVariableNameNode(VariableNameNode expression) {
		//return statesStore.getVariableValue(expression.variableName);
		return expression.variableName;
	}

	@Override
	public Object visitBinaryOperationNode(BinaryOperationNode expr) {
		try {
			// TODO exception handling!

			Object left = evaluateExpression(expr.left);
			Object right = evaluateExpression(expr.right);

			switch (expr.operator.tokenType) {

			case PLUS:
				checkForNumberOperands(expr.operator, left, right);
				return (int) left + (int) right;

			case MINUS:
				checkForNumberOperands(expr.operator, left, right);
				return (int) left - (int) right;

			case MULTIPLY:
				checkForNumberOperands(expr.operator, left, right);
				return (int) left * (int) right;

			case DIVIDE:
				checkForNumberOperands(expr.operator, left, right);
				return (int) left / (int) right;

			default:
				throw new WhileInterpreterException("invalid binary operator!");

			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object visitUnaryOperationNode(UnaryOperationNode expression) {
		try {
			Object right = evaluateExpression(expression.expr);

			switch (expression.operator.tokenType) {
			case PLUS:
				checkForNumberOperands(expression.operator, right);
				return (int) right;

			case MINUS:
				checkForNumberOperands(expression.operator, right);
				return -(int) right;

			default:
				throw new WhileInterpreterException("invalid unary operator!");
			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		// unreachable
		return null;
	}

	/**
	 * 
	 * @param operator
	 * @param objects
	 * @throws WhileInterpreterException
	 */
	private void checkForNumberOperands(Token operator, Object... objects) throws WhileInterpreterException {
		for (Object o : objects)
			if (!(o instanceof Integer))
				throw new WhileInterpreterException(operator, "RUNTIME ERROR: Operand must be a number!");
		return;
	}


	// ###############################################################################
	// EVALUATE B-EXP:
	// ###############################################################################

	@Override
	public Object visitBooleanValueNode(BooleanValueNode expression) {
		return expression.value; // returns true/false
	}

	@Override
	public Object visitComparisonOperationNode(ComparisonOperationNode expr) {
		try {
			// TODO exception handling!

			Object left = evaluateExpression(expr.left);
			Object right = evaluateExpression(expr.right);

			switch (expr.operator.tokenType) {

			case EQUAL:
				checkForNumberOperands(expr.operator, left, right);
				return (left == right);

			case LESS_THAN:
				if( !(left instanceof Integer))
					left = statesStore.getVariableValue(left.toString());
				if( !(right instanceof Integer))
					right = statesStore.getVariableValue(right.toString());

				return ((int) left < (int) right);

			default:
				throw new WhileInterpreterException("invalid comparison operator!");

			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}


	private boolean isDigit(char n) {
		return (n >= '0' && n <= '9');
	}


	@Override
	public Object visitBooleanOperationNode(BooleanOperationNode expr) {
		try {
			// TODO exception handling!

			Object left = evaluateExpression(expr.left);
			Object right = evaluateExpression(expr.right);

			switch (expr.operator.tokenType) {

			case AND:
				checkForBooleanOperands(expr.operator, left, right);
				return (Boolean.parseBoolean((String) left) && Boolean.parseBoolean((String) right));

			case OR:
				checkForBooleanOperands(expr.operator, left, right);
				return (Boolean.parseBoolean((String) left) || Boolean.parseBoolean((String) right));

			default:
				throw new WhileInterpreterException("invalid comparison operator!");

			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object visitNotOperationNode(NotOperationNode expr) {
		try {
			Object right = evaluateExpression(expr.expr);
			checkForBooleanOperands(expr.token, right);
			return (!Boolean.parseBoolean((String) right));

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param operator
	 * @param objects
	 * @throws WhileInterpreterException
	 */
	private void checkForBooleanOperands(Token operator, Object... objects) throws WhileInterpreterException {
		for (Object o : objects)
			if (!(o instanceof Boolean))
				throw new WhileInterpreterException(operator, "RUNTIME ERROR: Operand must be a boolean!");
		return;
	}

	// ###############################################################################
	// EVALUATE C-EXP:
	// for each command node
	//		1. execute command
	// 		2. remove step -> if skip: ( delete node from AST) else replace current node with skip
	//		3. print remaining AST
	//		4. print current states
	//
	// ###############################################################################

	@Override
	public Object visitSkipOperationNode(SkipOperationNode expression) {
		// 1. nothing to execute
		// 2. remove current node from tree
		return null;
	}

	@Override
	public Object visitAssignmentNode(AssignmentOperationNode expression) {

		//		1. execute command: add or update variable
		String varName = "" + expression.variableName.accept(this);
		int value = Integer.parseInt("" + expression.value.accept(this));
		statesStore.defineVariable(varName, value );


		// 		2. remove step -> replace current node with skip

		//		3. print remaining AST
		//		4. print current states

		return statesStore.getCurrentState();
	}


	@Override
	public Object visitSemicolonNode(SemiColonNode expression) {
		// breaks into steps
		return null;
	}

	@Override
	public Object visitIfOperationNode(IfOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitWhileOperationNode(WhileOperationNode expression) {

		if( (boolean) expression.condition.accept(this)) {
			return expression.whileTrueCommands.accept(this);
		}
		else {
			return 0;
			//return expression.whileFalseCommands.accept(this);
		}
		//return null;
	}

}
