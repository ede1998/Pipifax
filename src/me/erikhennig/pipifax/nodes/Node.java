package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;

public abstract class Node {
	public abstract void accept(Visitor v)
	{
		v.visit(this);
	}
}
