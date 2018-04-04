package me.erikhennig.pipifax.nodes.expressions;

public abstract class UnaryExpressionNode extends ExpressionNode {

	protected ExpressionNode m_operand;
	
	public UnaryExpressionNode(ExpressionNode op) {
		m_operand = op;
	}

}
