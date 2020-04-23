package hw2.util;

import hw2.lexer.Token;
import hw2.lexer.TokenType;
import hw2.parser.Expression;
import hw2.parser.Expression.Binary;
import hw2.parser.Expression.Grouping;
import hw2.parser.Expression.Literal;
import hw2.parser.Expression.Unary;

/**
 * 
 * @author rashmichennagiri
 *
 */
public class ASTPrinter implements Expression.Visitor<String>{

	
	/**
	 * 
	 * @param expr
	 * @return
	 */
	public String print(Expression expr) {
		return expr.accept(this);
	}
	
	
	@Override
	public String visitBinaryExpression(Binary expr) {
		return addParams( expr.operator.lexeme, expr.left, expr.right);
	}


	@Override
	public String visitGroupingExpression(Grouping expr) {
		return addParams("group", expr.expression);
	}


	@Override
	public String visitLiteralExpression(Literal expr) {
		if(expr.value==null)
			return null;
		
		return expr.value.toString();
	}

	@Override
	public String visitUnaryExpression(Unary expr) {
		return addParams( expr.operator.lexeme, expr.expr);
	}
	
	
	

	private String addParams(String name, Expression...expressions) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("(").append(name);
		
		for(Expression ex : expressions) {
			sb.append(" ");
			sb.append( ex.accept(this));
		}
		
		sb.append(")");

		return sb.toString();
	}

	public static void main(String[] args) {    
		
		Expression expression = new Expression.Binary(  
				
	        new Unary( new Token(TokenType.MINUS, "-", null, 1), new Literal(123)),                        
	        new Token(TokenType.MULTIPLY, "*", null, 1),    
	        new Expression.Grouping( new Expression.Literal(45.67))
	        );

	    System.out.println(new ASTPrinter().print(expression));
	  }                                                        



}
