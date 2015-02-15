package com.caseybrooks.logicplayground.evaluator;

/**A parser which takes a String as input and produces an expression tree that
 * can be evaluated later
 *
 * GRAMMAR
 *
 * expression ::= unaryOperator operand | operand binaryOperator operand
 *
 * unaryOperator ::= NOT
 *
 * binaryOperator ::= AND | NAND | OR | NOR | XOR | XNOR
 *
 * operand ::= identifier | ( expression )
 *
 *
 */


public class Parser {
	Lexer.TokenStream ts;
	Node root;

	public Parser(String expression) {
		ts = new Lexer.TokenStream(expression);
	}

	public Node parse() {
		return expression();
	}

	public Node expression() {
		Node unaryOperator = unaryOperator();
		if(unaryOperator != null) {
			Node operand = operand();
			if(operand != null) {
				root = new Operator(unaryOperator.type, operand, null);
				return root;
			}
		}
		else {
			Node left = operand();
			if(left != null) {
				Node operator = binaryOperator();
				if(operator != null) {
					Node right = operand();
					if(right != null) {

						root = new Operator(operator.type, left, right);
						return root;
					}
				}
			}
		}

		return null;
	}

	public Node unaryOperator() {
		Lexer.Token token = ts.get();
		if(token != null) {
			if(token.tokenType == Lexer.Token.NOT) {
				return new Operator(Node.Type.NOT, null, null);
			}
			else {
				ts.unget(token);
			}
		}
		return null;
	}

	public Node binaryOperator() {
		Lexer.Token token = ts.get();
		if(token != null) {
			if(token.tokenType == Lexer.Token.AND) {
				return new Operator(Node.Type.AND, null, null);
			}
			else if(token.tokenType == Lexer.Token.NAND) {
				return new Operator(Node.Type.NAND, null, null);
			}
			else if(token.tokenType == Lexer.Token.OR) {
				return new Operator(Node.Type.OR, null, null);
			}
			else if(token.tokenType == Lexer.Token.NOR) {
				return new Operator(Node.Type.NOR, null, null);
			}
			else if(token.tokenType == Lexer.Token.XOR) {
				return new Operator(Node.Type.XOR, null, null);
			}
			else if(token.tokenType == Lexer.Token.XNOR) {
				return new Operator(Node.Type.XNOR, null, null);
			}
			else {
				ts.unget(token);
			}
		}
		return null;
	}

	public Node operand() {
		Lexer.Token token = ts.get();
		if(token != null) {
			if(token.tokenType == Lexer.Token.IDENTIFIER) {
				if(root != null) {
					Node node = root.find(token.value);
					if(node != null) {
						return node;
					}
					else {
						return new Identifier(token.value);
					}
				}
				else {
					return new Identifier(token.value);
				}
			}
			else if(token.tokenType == Lexer.Token.LPAR) {
				Node expr = expression();
				if(expr != null) {
					Lexer.Token rpar = ts.get();
					if(rpar.tokenType == Lexer.Token.RPAR) {
						return expr;
					}
				}
			}
			else {
				ts.unget(token);
			}
		}

		return null;
	}

}
