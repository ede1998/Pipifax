package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class UnaryExpressionNode extends ExpressionNode
{
	private ExpressionNode m_operand;
	private UnaryOperation m_operation;

	public UnaryExpressionNode(ExpressionNode op, UnaryOperation negation)
	{
		m_operand = op;
		m_operation = negation;
	}

	public ExpressionNode getOperand()
	{
		return m_operand;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public UnaryOperation getOperation()
	{
		return m_operation;
	}
}
