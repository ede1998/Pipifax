package me.erikhennig.pipifax.nodes.expressions;

public abstract class UnaryExpressionNode extends ExpressionNode
{

	private ExpressionNode m_operand;

	public UnaryExpressionNode(ExpressionNode op)
	{
		m_operand = op;
	}

	public ExpressionNode getOperand()
	{
		return m_operand;
	}
}
