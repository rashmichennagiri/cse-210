package hw2.parser;

import java.util.List;

import hw2.lexer.Token;


/**
 * 
 * @author rashmichennagiri
 *
 */
abstract class Expression {
	
	
	/**
	 * node for actual string/number/variable
	 */
	static class Literal extends Expression {

		final Object value;

		Literal(Object value) {
			this.value = value;
		}

	}

	
	/**
	 * node for unary operation
	 */
	static class Unary extends Expression {

		final Token operator;
		final Expression expr;

		Unary(Token operator, Expression expr) {
			this.operator = operator;
			this.expr = expr;
		}

	}
	
	
	/**
	 * binary operation node
	 */
	static class Binary extends Expression {

		final Expression left;
		final Token operator;
		final Expression right;

		Binary(Expression left, Token operator, Expression right) {
			this.left = left;
			this.operator = operator;
			this.right = right;
		}

	}

	
	/**
	 * node for nested expressions
	 */
	static class Grouping extends Expression {

		final Expression expression;

		Grouping(Expression expression) {
			this.expression = expression;
		}

	}

}
