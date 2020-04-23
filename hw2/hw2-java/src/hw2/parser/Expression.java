package hw2.parser;

import hw2.lexer.Token;

public abstract class Expression{
	public interface Visitor<R> {
	R visitBinaryExpression(Binary expression);
	R visitGroupingExpression(Grouping expression);
	R visitLiteralExpression(Literal expression);
	R visitUnaryExpression(Unary expression);
	R visitVariableExpression(Variable expression);
	}

	public abstract <R> R accept(Visitor<R> visitor);

	public static class Binary extends Expression{

	public final Expression left;
	public final Token operator;
	public final Expression right;

	Binary(Expression left,Token operator,Expression right){
	this.left = left;
	this.operator = operator;
	this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
	return visitor.visitBinaryExpression(this);
	}

	}
	public static class Grouping extends Expression{

	public final Expression expression;

	Grouping(Expression expression){
	this.expression = expression;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
	return visitor.visitGroupingExpression(this);
	}

	}
	public static class Literal extends Expression{

	public final Object value;

	Literal(Object value){
	this.value = value;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
	return visitor.visitLiteralExpression(this);
	}

	}
	public static class Unary extends Expression{

	public final Token operator;
	public final Expression expr;

	Unary(Token operator,Expression expr){
	this.operator = operator;
	this.expr = expr;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
	return visitor.visitUnaryExpression(this);
	}

	}
	public static class Variable extends Expression{

	public final Token name;
	public final Expression initializer;

	Variable(Token name,Expression initializer){
	this.name = name;
	this.initializer = initializer;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
	return visitor.visitVariableExpression(this);
	}

	}
}
