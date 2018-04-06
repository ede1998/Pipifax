package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;

public class NegationNode extends UnaryExpressionNode {

	public NegationNode(ExpressionNode op) {
		super(op);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
