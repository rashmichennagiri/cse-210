package hw2.parser;

import hw2.lexer.Token;

/**
 * 
 * @author rashmichennagiri
 *
 */
public abstract class Expression {

	
	/**
	 * 	Binary node
	 */
	static class Binary extends Expression{
		
		final Expression left;
		final Token operator;
		final Expression right;
		
		Binary(Expression left, Token operator, Expression right){
			this.left = left;
			this.operator = operator;
			this.right = right;
		}
	}
}
