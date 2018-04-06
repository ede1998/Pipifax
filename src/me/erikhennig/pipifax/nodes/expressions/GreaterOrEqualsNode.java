package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;

public class GreaterOrEqualsNode extends ComparisonExpressionNode {

	public GreaterOrEqualsNode(ExpressionNode left, ExpressionNode right) {
		super(left, right);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
