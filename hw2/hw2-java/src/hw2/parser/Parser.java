package hw2.parser;

import java.util.ArrayList;
import java.util.List;

import hw2.WhileInterpreterException;
import hw2.lexer.Token;
import hw2.lexer.TokenType;

/**
 * 
 * 1. Given a valid sequence of tokens, produce corresponding syntax tree.
 * 2. Given an invalid sequence of tokens, detect and report errors.
 * 
 * each rule of the grammar becomes a method
 * 
 * @author rashmichennagiri
 *
 */
public class Parser {

	private final List<Token> tokens;	// list of tokens to be parsed
	private int currentTokenIndex = 0;	// pointer to current token

	public Parser(List<Token> tokens){
		this.tokens = tokens;
	}


	/**
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	public List<Statement> parse() throws WhileInterpreterException {
		List<Statement> stmts = new ArrayList<Statement>();

		while( !isLastToken() ) {
			stmts.add(declaration());
			stmts.add(statement());
		}
		return stmts;
	}


	private Statement statement() {
		try {
			return expressionStatement();

		} catch (WhileInterpreterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Statement expressionStatement() throws WhileInterpreterException {
		Expression ex = expression();
		consumeToken(TokenType.SEMICOLON, "Expect ';' after expression");
		return new Statement.Expr(ex);
	}


	private Statement declaration() {
		try {
			if(matchNextToken(TokenType.VAR))
				return variableDeclaration();

			return statement();
			
		}catch(Exception wie) {
			synchronize();
			return null;
		}
	}
	
	
	private Statement variableDeclaration() {
		try {
			Token name = consumeToken(TokenType.IDENTIFIER, "Expect variable name");
			
			Expression initializer = null;
			
			if( matchNextToken(TokenType.ASSIGNMENT)) {
				initializer = expression();
			}
			
			consumeToken(TokenType.SEMICOLON, "Expect ';' after variable declaration!");

			return new Statement.Var(name, initializer);
			
			
		} catch (WhileInterpreterException e) {
			e.printStackTrace();
		}
		return null;
		
	}


	/**
	 * expression -> equality
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression expression() throws WhileInterpreterException {
		return assignment();
	}
	
	
	/**
	 * 
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression assignment() throws WhileInterpreterException {
		Expression ex = equality();
		
		if( matchNextToken(TokenType.ASSIGNMENT)) {
			Token equals = getPreviousToken();
			Expression value = assignment();
			
			if( ex instanceof Expression.Variable ) {
				Token name = ((Expression.Variable)ex).name;
				return new Expression.Assign(name, value);
			}
			
			throw new WhileInterpreterException(equals, "Invalid assignment target");
		}
		return ex;
	}
	

	/**
	 * equality -> comparison (= comparison)*
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression equality() throws WhileInterpreterException {

		Expression ex = comparison();

		while( matchNextToken(TokenType.EQUAL)) {
			Token operator = getPreviousToken();
			Expression right = comparison();
			ex = new Expression.Binary(ex, operator, right);
		}

		return ex;
	}


	/**
	 * comparison -> addition ( (<|<=)addition )*
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression comparison() throws WhileInterpreterException {

		Expression ex = addition();

		while( matchNextToken(TokenType.LESS_THAN, TokenType.LESS_THAN_EQUAL)) {
			Token op = getPreviousToken();
			Expression right = addition();
			ex = new Expression.Binary(ex, op, right);
		}

		return ex;
	}


	/**
	 * addition -> multiplication ( (+|-)multiplication )*
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression addition() throws WhileInterpreterException {
		Expression ex = multiplication();

		while( matchNextToken(TokenType.PLUS, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expression right = multiplication();
			ex = new Expression.Binary(ex, op, right);
		}

		return ex;
	}


	/**
	 * multiplication -> unary ( (*|/)unary )*
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression multiplication() throws WhileInterpreterException {
		Expression ex = unary();

		while( matchNextToken(TokenType.MULTIPLY, TokenType.DIVIDE)) {
			Token op = getPreviousToken();
			Expression right = unary();
			ex = new Expression.Binary(ex, op, right);
		}

		return ex;
	}


	/**
	 * unary -> (+|-)unary ) | primary
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression unary() throws WhileInterpreterException {

		if( matchNextToken(TokenType.MINUS, TokenType.PLUS)) {
			Token op = getPreviousToken();
			Expression right = unary();
			return new Expression.Unary(op, right);
		}

		return primary();
	}


	/**
	 * primary -> NUMBER | STRING | "true" | "false" | "(" expression ")"
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Expression primary() throws WhileInterpreterException {

		if( matchNextToken(TokenType.TRUE))
			return new Expression.Literal(true);
		if( matchNextToken(TokenType.FALSE))
			return new Expression.Literal(false);

		if( matchNextToken(TokenType.STRING, TokenType.NUMBER))
			return new Expression.Literal( getPreviousToken().literal );

		if( matchNextToken(TokenType.LEFT_PARENTHESIS)) {
			Expression ex = expression();
			consumeToken( TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression");
			return new Expression.Grouping(ex);
		}
		
		//if( matchNextToken(TokenType.IDENTIFIER))
		//	return new Expression.Variable(getPreviousToken(),null );  //TODO

		throw new WhileInterpreterException(getCurrentToken(), "EXPRESSION EXPECTED");
	
	
	
	}


	/**
	 * 
	 * @param tokenTypes
	 * @return
	 */
	private boolean matchNextToken(TokenType...tokenTypes) {

		for(TokenType tt : tokenTypes) {
			if( checkCurrentTokenType(tt) ) {
				advanceToken();
				return true;
			}
		}
		return false;
	}


	private Token consumeToken(TokenType tt, String msg) throws WhileInterpreterException {

		if( checkCurrentTokenType(tt) )
			return advanceToken();

		throw new WhileInterpreterException(getCurrentToken(), msg);
	}


	/**
	 * checks for expected token type
	 * @param tt
	 * @return
	 */
	private boolean checkCurrentTokenType(TokenType tt) {
		if( isLastToken() )
			return false;
		return getCurrentToken().tokenType == tt;
	}


	/**
	 * gets next token in line
	 * @return
	 */
	private Token advanceToken() {
		if( !isLastToken() )
			currentTokenIndex++;
		return getPreviousToken();
	}


	/**
	 * peeks and returns current token value
	 * @return
	 */
	private Token getCurrentToken() {           
		return tokens.get(currentTokenIndex);    
	}                                


	/**
	 * returns previous token value
	 * @return
	 */
	private Token getPreviousToken() {       
		return tokens.get(currentTokenIndex - 1);
	} 


	/**
	 * checks if token is last token
	 * @return
	 */
	private boolean isLastToken() {      
		return getCurrentToken().tokenType == TokenType.EOF;     
	}


	/**
	 * error handling, to advance until next statement
	 */
	private void synchronize() {                 
		advanceToken();

		while (!isLastToken()) {                       
			if (getPreviousToken().tokenType == TokenType.SEMICOLON) 
				return;

			switch (getCurrentToken().tokenType) {                   
			case IF:                               
			case WHILE:                            
				return;                              
			}                                        

			advanceToken();                               
		}                                          
	}         

}
