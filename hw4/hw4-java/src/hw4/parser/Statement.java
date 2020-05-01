package hw2.parser;

import hw2.lexer.Token;

public abstract class Statement{
	public interface Visitor<R> {
		R visitExprStatement(Expr statement);
		R visitPrintStatement(Print statement);
		R visitVarStatement(Var statement);
	}

	public abstract <R> R accept(Visitor<R> visitor);

	public static class Expr extends Statement{

		public final Expression ex;

		Expr(Expression ex){
			this.ex = ex;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitExprStatement(this);
		}

	}
	public static class Print extends Statement{

		public final Expression ex;

		Print(Expression ex){
			this.ex = ex;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitPrintStatement(this);
		}

	}
	public static class Var extends Statement{

		public final Token name;
		public final Expression initializer;

		Var(Token name,Expression initializer){
			this.name = name;
			this.initializer = initializer;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			return visitor.visitVarStatement(this);
		}

	}
}
