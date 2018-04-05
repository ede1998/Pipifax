package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;

public abstract class LiteralNode extends ExpressionNode {
	public abstract Types getType();
}
