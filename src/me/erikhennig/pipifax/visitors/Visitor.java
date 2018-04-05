package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;

public abstract class Visitor<T>
{
	public T visitAssignmentNode(AssignmentNode n)
	{
		return n.accept(this);
	}
}
