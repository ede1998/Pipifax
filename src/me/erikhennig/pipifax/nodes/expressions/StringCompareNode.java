package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;

;

public class StringCompareNode extends ComparisonExpressionNode
{
	public StringCompareNode(ExpressionNode left, ExpressionNode right)
	{
		super(left, right);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
