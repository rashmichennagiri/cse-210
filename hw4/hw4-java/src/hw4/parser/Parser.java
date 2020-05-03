package hw4.parser;

import java.util.List;

import hw4.lexer.Token;
import hw4.lexer.TokenType;
import hw4.WhileInterpreterException;

/**
 * 
 * 1. Given a valid sequence of tokens, produce corresponding abstract syntax tree.
 * 2. Given an invalid sequence of tokens, detect and report errors.
 * 
 * each rule of the grammar becomes a method
 * 
 * @author rashmichennagiri
 * 
 * 
 *         # WHILE Grammar:
 * 
 *         cexpr : cterm (SEMICOLON cterm)*
 * 
 *         cterm: bexpr (ASSIGN bexpr)*
 * 
 *         bexpr: bterm ((AND|OR) bterm)*
 * 
 *         bterm : aexpr ((EQUAL|LESSTHAN) aexpr)*
 * 
 *         aexpr : aterm ((PLUS | MINUS) aterm)*
 * 
 *         aterm : unary ((MUL | DIV) unary)*
 * 
 *         unary : (PLUS|MINUS) unary | primary
 * 
 *         primary : INTEGER | BOOLEAN | VARIABLE-NAME | NOT (bexpr) | NOT
 *         BOOLEAN | ARRAY | "(" bexpr ")" | "{" cexpr "}" | if condition=bexpr
 *         then iftrue=cexpr else iffalse = cexpr | while condition=bexpr do
 *         {whiletrue = cexpr} | while condition=bexpr do whiletrue=cterm | skip
 *
 */
public class Parser {

	private final List<Token> tokens; // list of tokens to be parsed
	private int currentTokenIndex = 0; // pointer to current token

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	public Node parse() throws WhileInterpreterException {
		//while (!isLastToken()) {

		Node ast = commandExpression();
		if(isLastToken())
			return ast;
		else
			throw new WhileInterpreterException("something wrong! :(");
	}

	
	/**
	 * cexpr : cterm (SEMICOLON cterm)*
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node commandExpression() throws WhileInterpreterException {

		Node left = commandTerm();

		while (matchNextToken(TokenType.SEMICOLON)) {
			Token operator = getPreviousToken(); // ;
			Node right = commandTerm();
			left = new Node.AssignmentOperationNode(left, operator, right);
		}

		return left;
	}
	
	
	/**
	 * cterm: bexpr (ASSIGN bexpr)*
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node commandTerm() throws WhileInterpreterException {

		Node left = booleanExpression();

		while (matchNextToken(TokenType.ASSIGNMENT)) {
			Token operator = getPreviousToken(); // :=
			Node right = booleanExpression();
			left = new Node.AssignmentOperationNode(left, operator, right);
		}

		return left;
	}

	

	/**
	 * bexpr: bterm ((AND|OR) bterm)*
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node booleanExpression() throws WhileInterpreterException {

		Node left = booleanTerm();

		while (matchNextToken(TokenType.AND, TokenType.OR)) {
			Token op = getPreviousToken();
			Node right = booleanTerm();
			left = new Node.BooleanOperationNode(left, op, right);
		}

		return left;
	}
	
	
	/**
	 * bterm : aexpr ((EQUAL|LESSTHAN) aexpr)*
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node booleanTerm() throws WhileInterpreterException {

		Node left = arithmeticExpression();

		while (matchNextToken(TokenType.LESS_THAN, TokenType.EQUAL)) {
			Token op = getPreviousToken();
			Node right = arithmeticExpression();
			left = new Node.ComparisonOperationNode(left, op, right);
		}

		return left;
	}

	/**
	 * aexpr   : aterm ((PLUS | MINUS) aterm)*
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node arithmeticExpression() throws WhileInterpreterException {
		Node ex = arithmeticTerm();

		while (matchNextToken(TokenType.PLUS, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Node right = arithmeticTerm();
			ex = new Node.BinaryOperationNode(ex, op, right);
		}

		return ex;
	}

	/**
	 * aterm -> unary ( (*|/)aterm )*
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node arithmeticTerm() throws WhileInterpreterException {
		Node ex = unary();

		while (matchNextToken(TokenType.MULTIPLY, TokenType.DIVIDE)) {
			Token op = getPreviousToken();
			Node right = unary();
			ex = new Node.BinaryOperationNode(ex, op, right);
		}
		return ex;
	}

	/**
	 * unary -> (+|-)unary ) | primary
	 * 
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Node unary() throws WhileInterpreterException {

		if (matchNextToken(TokenType.MINUS, TokenType.PLUS)) {
			Token op = getPreviousToken();
			Node right = unary();
			return new Node.UnaryOperationNode(op, right);
		}
		return primary();
	}

	/**
	 * primary -> BOOLEAN(true/false) | NUMBER | variableName
	 * 				| "(" bexpr ")" 
	 * 				| "{" cexpr "}" 
	 * 				| NOT (bexpr) | NOT BOOLEAN
	 * 				| if condition=bexpr then iftrue=cexpr else iffalse = cexpr 
	 * 				| while condition=bexpr do {whiletrue = cexpr}
	 * 				| while condition=bexpr do whiletrue=cterm
	 * 				| skip
        TODO: | ARRAY
	 * @return
	 * @throws WhileInterpreterException 
	 */
	private Node primary() throws WhileInterpreterException {

		if( matchNextToken(TokenType.BOOLEAN))
			return new Node.BooleanValueNode(getPreviousToken(), Boolean.parseBoolean(getPreviousToken().lexeme));

		if( matchNextToken(TokenType.NUMBER))
			return new Node.IntegerValueNode(getPreviousToken(), Integer.parseInt(getPreviousToken().lexeme));

		if( matchNextToken(TokenType.VARIABLE))
			return new Node.VariableNameNode(getPreviousToken(), getPreviousToken().lexeme);

		if( matchNextToken(TokenType.LEFT_PARENTHESIS)) {
			Node ex = booleanExpression();
			consumeToken( TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression");
			return  ex; // new Node.Grouping(ex);
		}

		if( matchNextToken(TokenType.LEFT_BRACE)) {
			Node ex = commandExpression(); 
			consumeToken( TokenType.RIGHT_BRACE, "Expect '}' after commands");
			return  ex; // new Node.Grouping(ex);
		}

		if( matchNextToken(TokenType.IF)) {
			Token ifToken = getPreviousToken();
			// if condition=bexpr then iftrue=cexpr else iffalse = cexpr 
			Node condition = booleanExpression();
			Node ifTrue = null, ifFalse = null;
			if( matchNextToken(TokenType.THEN))
				ifTrue = commandExpression();
			if( matchNextToken(TokenType.ELSE))
				ifFalse = commandExpression();
			return new Node.IfOperationNode(ifToken, condition, ifTrue, ifFalse);
		}

		if( matchNextToken(TokenType.WHILE)) {
			Token whileToken = getPreviousToken();
			// while condition=bexpr do {whiletrue = cexpr}
			// while condition=bexpr do whiletrue=cterm
			Node condition = booleanExpression();
			Node whileTrue = null;
			if( matchNextToken(TokenType.DO)) {
				if( matchNextToken( TokenType.LEFT_BRACE)) {
					whileTrue = commandExpression();
					consumeToken( TokenType.RIGHT_BRACE, "Expect '}' after command");
				}
				else
					whileTrue = commandTerm();
			}
			Node whileFalse = new Node.SkipOperationNode( new Token(TokenType.SKIP, "skip", null), "skip");
			return new Node.WhileOperationNode(whileToken, condition, whileTrue, whileFalse);
		}

		if( matchNextToken(TokenType.NOT)) {
			// NOT (bexpr) | NOT BOOLEAN
			Token notToken = getPreviousToken();

			Node ex;
			if( matchNextToken(TokenType.LEFT_PARENTHESIS)) {
				ex = booleanExpression();
				consumeToken( TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression in NOT");
			}
			else if( matchNextToken(TokenType.BOOLEAN))
				ex = new Node.BooleanValueNode(getPreviousToken(), Boolean.parseBoolean(getPreviousToken().lexeme));
			else
				throw new WhileInterpreterException(getCurrentToken(), "INVALID 'not' SYNTAX");


			return new Node.NotOperationNode(notToken, ex);
		}
		
		if( matchNextToken(TokenType.SKIP))
			return new Node.SkipOperationNode(getPreviousToken(), getPreviousToken().lexeme);


		throw new WhileInterpreterException(getCurrentToken(), "INVALID EXPRESSION");



	}

	/**
	 * matches for any one of the specified token types
	 * 
	 * @param tokenTypes
	 * @return
	 */
	private boolean matchNextToken(TokenType... tokenTypes) {

		for (TokenType tt : tokenTypes) {
			if (checkCurrentTokenType(tt)) {
				advanceToken();
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param tt
	 * @param msg
	 * @return
	 * @throws WhileInterpreterException
	 */
	private Token consumeToken(TokenType tt, String msg) throws WhileInterpreterException {

		if (checkCurrentTokenType(tt))
			return advanceToken();

		throw new WhileInterpreterException(getCurrentToken(), msg);
	}

	/**
	 * checks for expected token type
	 * 
	 * @param tt
	 * @return
	 */
	private boolean checkCurrentTokenType(TokenType tt) {
		if (isLastToken())
			return false;
		return getCurrentToken().tokenType == tt;
	}

	/**
	 * gets next token in line
	 * 
	 * @return
	 */
	private Token advanceToken() {
		if (!isLastToken())
			currentTokenIndex++;
		return getPreviousToken();
	}

	/**
	 * peeks and returns current token value
	 * 
	 * @return
	 */
	private Token getCurrentToken() {
		return tokens.get(currentTokenIndex);
	}

	/**
	 * returns previous token value
	 * 
	 * @return
	 */
	private Token getPreviousToken() {
		return tokens.get(currentTokenIndex - 1);
	}

	/**
	 * checks if token is last token
	 * 
	 * @return
	 */
	private boolean isLastToken() {
		return getCurrentToken().tokenType == TokenType.EOF;
	}

}
