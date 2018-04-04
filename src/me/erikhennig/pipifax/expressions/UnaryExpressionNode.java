package me.erikhennig.pipifax.expressions;

public class UnaryExpressionNode extends ExpressionNode {

	protected ExpressionNode m_operand;
	
	public UnaryExpressionNode(ExpressionNode op) {
		m_operand = op;
	}

}
