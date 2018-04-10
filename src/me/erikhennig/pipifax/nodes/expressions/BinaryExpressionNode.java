package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class BinaryExpressionNode extends ExpressionNode
{
	private ExpressionNode m_leftSide;
	private ExpressionNode m_rightSide;
	private BinaryOperation m_operation;

	public BinaryExpressionNode(ExpressionNode left, ExpressionNode right, BinaryOperation bop)
	{
		m_leftSide = left;
		m_rightSide = right;
		m_operation = bop;
	}

	public ExpressionNode getLeftSide()
	{
		return m_leftSide;
	}

	public ExpressionNode getRightSide()
	{
		return m_rightSide;
	}

	public BinaryOperation getOperation()
	{
		return m_operation;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
