package me.erikhennig.pipifax.nodes.expressions;

public abstract class BinaryExpressionNode extends ExpressionNode
{
	private ExpressionNode m_leftSide;
	private ExpressionNode m_rightSide;

	public BinaryExpressionNode(ExpressionNode left, ExpressionNode right)
	{
		m_leftSide = left;
		m_rightSide = right;
	}

	public ExpressionNode getLeftSide()
	{
		return m_leftSide;
	}

	public ExpressionNode getRightSide()
	{
		return m_rightSide;
	}

}
