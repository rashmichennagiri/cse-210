package hw2.interpreter;

import java.util.List;

import hw2.WhileInterpreter;
import hw2.WhileInterpreterException;
import hw2.lexer.Token;
import hw2.parser.Expression;
import hw2.parser.Expression.Binary;
import hw2.parser.Expression.Grouping;
import hw2.parser.Expression.Literal;
import hw2.parser.Expression.Unary;
import hw2.parser.Expression.Variable;
import hw2.parser.Statement;
import hw2.parser.Statement.Expr;
import hw2.parser.Statement.Print;
import hw2.parser.Statement.Var;

/**
 * 
 * @author rashmichennagiri
 *
 */
public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Void> {


	/*
	 * public void interpret(Expression expr) { Object value =
	 * evaluateExpression(expr); System.out.println( stringify(value) ); }
	 */
	
	/**
	 * 
	 * @param stmts
	 */
	public void interpret(List<Statement> stmts) {
		
		for( Statement s: stmts) {
			executeStatement(s);
		}
	}
	

	@Override
	public Object visitBinaryExpression(Binary expr) {
		// TODO exception handling!
		try {
			Object left = evaluateExpression(expr.left);
			Object right = evaluateExpression(expr.right);

			switch( expr.operator.tokenType ) {
			case MINUS:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left - (int)right;
				
			case PLUS:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left + (int) right;	
				/* if (left instanceof String && right instanceof String) {
          		return (String)left + (String)right; } 
          		throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");  */
			
			case MULTIPLY:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left * (int) right;	
				
			case DIVIDE:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left / (int) right;	

			case LESS_THAN:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left < (int)right;
				
			case LESS_THAN_EQUAL:
				checkForNumberOperands(expr.operator, left, right);
				return (int)left <= (int)right;

			case EQUAL: return left.equals(right);

			}

		} catch (WhileInterpreterException e) {
			WhileInterpreter.hadRuntimeError = true;
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Object visitGroupingExpression(Grouping expr) {
		return evaluateExpression(expr.expression);
	}

	@Override
	public Object visitLiteralExpression(Literal expr) {
		return expr.value;
	}

	@Override
	public Object visitUnaryExpression(Unary expr) {
		Object right = evaluateExpression(expr.expr);

		switch( expr.operator.tokenType ) {
		case MINUS:
			// TODO
			try {
				checkForNumberOperand(expr.operator, right);
			} catch (WhileInterpreterException e) {
				WhileInterpreter.hadRuntimeError = true;
				System.out.println( e.getMessage() );
				e.printStackTrace();
			}
			return -(int)right;
		case PLUS:
			return (int) right;
			//case NEGATE: return !isTruthy(right); 

		}
		// unreachable
		return null;
	}
	
	@Override
	public Object visitVariableExpression(Variable expression) {
		return null;
	}



	/**
	 * 
	 * @param ex
	 * @return
	 */
	private Object evaluateExpression(Expression ex) {
		return ex.accept(this);
	}
	
	
	private Object executeStatement(Statement s) {
		return s.accept(this);
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
	 * @param operator
	 * @param right
	 * @throws WhileInterpreterException 
	 */
	private void checkForNumberOperand(Token operator, Object operand) throws WhileInterpreterException {
		if(operand instanceof Integer)
			return;
		throw new WhileInterpreterException(operator, "RUNTIME ERROR: Operand must be a number!");
	}


	/**
	 * 
	 * @param operator
	 * @param left
	 * @param right
	 * @throws WhileInterpreterException
	 */
	private void checkForNumberOperands(Token operator, Object left, Object right) throws WhileInterpreterException {
		if(left instanceof Integer && right instanceof Integer)
			return;
		throw new WhileInterpreterException(operator, "RUNTIME ERROR: Operand must be a number!");
	}


	private boolean isTruthy(Object object) {               
		if (object == null) return false;                     
		if (object instanceof Boolean) return (boolean)object;
		return true;                                          
	}



	@Override
	public Void visitExprStatement(Expr st) {
		Object value = evaluateExpression(st.ex);
		System.out.println( stringify(value) );
		return null;
	}


	@Override
	public Void visitVarStatement(Var st) {
		return null;
	}


	@Override
	public Void visitPrintStatement(Print st) {
		return null;
	}

}