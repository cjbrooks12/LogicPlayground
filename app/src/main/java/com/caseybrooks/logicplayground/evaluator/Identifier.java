package com.caseybrooks.logicplayground.evaluator;

import java.util.Set;
import java.util.TreeSet;

public class Identifier extends Node implements Comparable{
	public Identifier(String name) {
		super(name, Type.IDENTIFIER, false);
	}

	public Identifier(String name, boolean value) {
		super(name, Type.IDENTIFIER, value);
	}

	@Override
	public Node evaluate() {
		return this;
	}

	@Override
	public boolean equals(Node o) {
		Identifier other;
		try {
			other = (Identifier) o;
			return this.getName().equals(other.getName());
		}
		catch(ClassCastException cce) {
			return false;
		}
	}

	@Override
	public Node find(String identifier) {
		return (this.getName().equals(identifier)) ? this : null;
	}

	@Override
	public Set<Identifier> getAllIdentifiers() {
		TreeSet<Identifier> list = new TreeSet<>();
		list.add(this);
		return list;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int compareTo(Object another) {
		Identifier other = (Identifier) another;
		return this.name.compareTo(other.getName());
	}

	@Override
	public int getHeight() {
		return 1;
	}
}
