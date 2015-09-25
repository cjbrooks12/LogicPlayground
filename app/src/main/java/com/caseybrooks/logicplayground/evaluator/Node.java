package com.caseybrooks.logicplayground.evaluator;

import java.util.Set;

public abstract class Node {
	public enum Type {
		AND("AND"),
		NAND("NAND"),
		OR("OR"),
		NOR("NOR"),
		XOR("XOR"),
		XNOR("XNOR"),
		NOT("NOT"),
		IDENTIFIER("");

		public String name;

		Type(String name) {
			this.name = name;
		}
	}

	protected String name;
	public Type type;
	public boolean value;
	public int level;

	public float x;
	public float y;
	public float radius;

	public Node(String name, Type type, boolean value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	/** Evaluates the expression tree at this node downward */
	public abstract Node evaluate();

	/** determine if this node is equal to another by seeing if its children are equal */
	public abstract boolean equals(Node other);

	/** If it exists, find the Node in this tree with the given identifier */
	public abstract Node find(String identifier);

	public abstract Set<Identifier> getAllIdentifiers();
	public abstract String getName();

	/** The height of the entire tree from this Node downard */
	public abstract int getHeight();
}
