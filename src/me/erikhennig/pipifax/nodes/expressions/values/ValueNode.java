package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;

public abstract class ValueNode extends ExpressionNode
{
	public abstract boolean isLValue();
}
