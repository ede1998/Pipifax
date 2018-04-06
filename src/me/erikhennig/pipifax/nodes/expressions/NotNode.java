package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;

public class NotNode extends UnaryExpressionNode {

	public NotNode(ExpressionNode op) {
		super(op);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
