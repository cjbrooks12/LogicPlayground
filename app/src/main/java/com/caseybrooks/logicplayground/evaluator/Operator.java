package com.caseybrooks.logicplayground.evaluator;

import java.util.Set;
import java.util.TreeSet;

public class Operator extends Node {
	public Node left;
	public Node right;

	public Operator(Type type, Node left, Node right) {
		super("", type, false);

		if(left != null && right  != null) {
			this.left = left;
			this.right = right;
		}
		//force unary NOT operator to act only on the left subtree
		else if(left == null && right != null) {
			type = Type.NOT;
			this.left = right;
		}
		else if(left != null && right == null) {
			type = Type.NOT;
			this.left = left;
		}
	}

	public Node evaluate() {
		//type is a binary operator and both subtrees are not null
		if(!(type == Type.IDENTIFIER || type == Type.NOT) && (left != null && right != null)) {
			boolean a = left.evaluate().value;
			boolean b = right.evaluate().value;

			switch(type) {
			case AND:
				this.value = a && b;
				break;
			case NAND:
				this.value = !(a && b);
				break;
			case OR:
				this.value = (a || b);
				break;
			case NOR:
				this.value = !(a || b);
				break;
			case XOR:
				this.value = ((a && !b) || (!a && b));
				break;
			case XNOR:
				this.value = !((a && !b) || (!a && b));
				break;
			}
			return this;
		}
		//we have a unary (NOT) operator and a non-null left subtree
		else if((type == Type.NOT) && left != null) {
			if(left.getClass().equals(Identifier.class)) {
				this.name = type.name + " " + left.name;
			}
			else {
				this.name = type.name + " (" + left.name + ")";
			}

			this.value = !(left.evaluate().value);

			return this;
		}
		else if(type == Type.IDENTIFIER) {
			throw new ArithmeticException("Identifier incorrectly placed as an operator");
		}
		else {
			throw new ArithmeticException("Attempt to call binary operator [" + type.name + "] on node with at least one null child");
		}
	}

	@Override
	public boolean equals(Node o) {
		Operator other;
		try {
			other = (Operator) o;
			return this.left.equals(other.left) && this.right.equals(other.right);
		}
		catch(ClassCastException cce) {
			return false;
		}
	}

	@Override
	public Node find(String identifier) {
		if(left != null) {
			Node child = left.find(identifier);
			if(child != null) {
				return child;
			}
			else {
				if(right != null) {
					child = right.find(identifier);
					if(child != null) {
						return child;
					}
				}
			}
		}
		return null;
	}

	@Override
	public Set<Identifier> getAllIdentifiers() {
		TreeSet<Identifier> list = new TreeSet<>();
		if(left != null) list.addAll(left.getAllIdentifiers());
		if(right != null) list.addAll(right.getAllIdentifiers());
		return list;
	}

	@Override
	public String getName() {
		if(!(type == Type.IDENTIFIER || type == Type.NOT) && (left != null && right != null)) {
			if(left.getClass().equals(Identifier.class)) {
				this.name = left.getName() + " " + type.name + " " + right.getName();
			}
			else {
				this.name = "(" + left.getName() + ") " + type.name + " (" + right.getName() + ")";
			}

			return this.name;
		}
		else if((type == Type.NOT) && left != null) {
			if(left.getClass().equals(Identifier.class)) {
				this.name = type.name + " " + left.getName();
			}
			else {
				this.name = type.name + " (" + left.getName() + ")";
			}

			return this.name;
		}
		else {
			return null;
		}
	}
}
