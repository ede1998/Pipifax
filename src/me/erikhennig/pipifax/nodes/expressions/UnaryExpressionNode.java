package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

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
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public UnaryOperation getOperation()
	{
		return m_operation;
	}
}
