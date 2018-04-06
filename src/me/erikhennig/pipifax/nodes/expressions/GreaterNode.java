package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;

public class GreaterNode extends ComparisonExpressionNode {

	public GreaterNode(ExpressionNode left, ExpressionNode right) {
		super(left, right);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
