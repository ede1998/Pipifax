package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.TypeNode;

public abstract class ExpressionNode extends Node
{
	protected TypeNode m_type = null;

	public TypeNode getType()
	{
		return m_type;
	}

	public abstract boolean checkType();
}
