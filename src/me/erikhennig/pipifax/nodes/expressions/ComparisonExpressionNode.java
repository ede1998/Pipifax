package me.erikhennig.pipifax.nodes.expressions;

public abstract class ComparisonExpressionNode extends BinaryExpressionNode {

	public ComparisonExpressionNode(ExpressionNode left, ExpressionNode right) {
		super(left, right);
	}

}
