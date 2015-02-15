package com.caseybrooks.logicplayground.evaluator;

import java.util.LinkedList;
import java.util.Stack;

public class Lexer {
	public static class Token {
		public static final int IDENTIFIER = 0;
		public static final int AND = 1;
		public static final int OR = 2;
		public static final int NAND = 3;
		public static final int NOR = 4;
		public static final int XOR = 5;
		public static final int XNOR = 6;
		public static final int NOT = 7;
		public static final int LPAR = 8;
		public static final int RPAR = 9;

		public int tokenType;
		public String value;

		Token(int tokenType) {
			this.tokenType = tokenType;
		}

		Token(int tokenType, String value) {
			this.tokenType = tokenType;
			this.value = value;
		}
	}

	public static class TokenStream {
		LinkedList<Character> chars;
		Stack<Token> ungetTokens;

		public TokenStream(String expression) {
			String toParse = expression.replaceAll("\\s+", " ");
			chars = new LinkedList<Character>();
			for(int i = 0; i < toParse.length(); i++) {
				chars.add(toParse.charAt(i));
			}
			ungetTokens = new Stack<Token>();
		}

		public Token get() {
			try {
				if (ungetTokens.size() > 0) {
					return ungetTokens.pop();
				}
				else if (chars.size() > 0) {
					char ch = chars.removeFirst();
					String s;

					switch (ch) {
					case ' ': return get();
					case '*': return new Token(Token.AND);
					case '+': return new Token(Token.OR);
					case ')': return new Token(Token.RPAR);
					case '(': return new Token(Token.LPAR);
					default:
						s = "";
						s += ch;
						while (chars.size() > 0 && chars.getFirst() != null &&
									   Character.isLetter(chars.getFirst())) {
							s += chars.removeFirst();
						}

						if (s.equalsIgnoreCase("and")) return new Token(Token.AND);
						else if (s.equalsIgnoreCase("nand")) return new Token(Token.NAND);
						else if (s.equalsIgnoreCase("or")) return new Token(Token.OR);
						else if (s.equalsIgnoreCase("NOR")) return new Token(Token.NOR);
						else if (s.equalsIgnoreCase("xor")) return new Token(Token.XOR);
						else if (s.equalsIgnoreCase("xnor")) return new Token(Token.XNOR);
						else if (s.equalsIgnoreCase("not")) return new Token(Token.NOT);
						else {
							return new Token(Token.IDENTIFIER, s);
						}
					}
				} else {
					return null;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public void unget(Token token) {
			ungetTokens.push(token);
		}

		@Override
		public String toString() {
			String s = "";
			for(int i = 0; i < chars.size(); i++) {
				s += chars.get(i);
			}
			return s;
		}
	}
}
