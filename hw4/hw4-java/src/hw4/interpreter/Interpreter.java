package hw4.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	

	public void interpret(Node n) {
		Object value = evaluateExpression(n);
		System.out.println( stringify(value));
	}

	private Object evaluateExpression(Node expr) {
		return expr.accept(this);
	}

	@Override
	public Object visitSemicolonNode(SemiColonNode expression) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public Object visitAssignmentNode(AssignmentOperationNode expression) {
		
		// add or update variable
		statesStore.defineVariable(expression.variableName.toString(), expression.value.accept(this));
		
		
		return null;
	}

	@Override
	public Object visitComparisonOperationNode(ComparisonOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBooleanOperationNode(BooleanOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNotOperationNode(NotOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIfOperationNode(IfOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitWhileOperationNode(WhileOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitSkipOperationNode(SkipOperationNode expression) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object visitBooleanValueNode(BooleanValueNode expression) {
		// TODO Auto-generated method stub
		return null;
	}



	// EVALUATE AEXP:
	@Override
	public Object visitIntegerValueNode(IntegerValueNode expression) {
		return expression.value;
	}
	
	@Override
	public Object visitVariableNameNode(VariableNameNode expression) {
		return statesStore.getVariableValue(expression.variableName);
	}
	
	@Override
	public Object visitBinaryOperationNode(BinaryOperationNode expr) {
		try {
			// TODO exception handling!

			Object left = evaluateExpression(expr.left);
			Object right = evaluateExpression(expr.right);

			switch( expr.operator.tokenType) {

			case PLUS:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left + (int)right;

			case MINUS:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left - (int)right;

			case MULTIPLY:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left * (int)right;

			case DIVIDE:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left / (int)right;

			default: throw new WhileInterpreterException("invalid binary operator!");

			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}

		return null;
	}


	@Override
	public Object visitUnaryOperationNode(UnaryOperationNode expression) {
		try {
			Object right = evaluateExpression(expression.expr);

			switch( expression.operator.tokenType) {
			case PLUS:
				checkForNumberOperands(expression.operator, right);
				return (int)right;

			case MINUS:
				checkForNumberOperands(expression.operator, right);
				return -(int)right;
				
			default: throw new WhileInterpreterException("invalid unary operator!");
			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println( e.getMessage() );
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
	private void checkForNumberOperands(Token operator, Object...objects) throws WhileInterpreterException {
		for( Object o: objects)
			if( ! (o instanceof Integer) )
				throw new WhileInterpreterException(operator, "RUNTIME ERROR: Operand must be a number!");
		return;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String stringify(Object obj) {
		return obj.toString();
	}

}
