package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class UnaryExpressionNode extends ExpressionNode
{
	private ExpressionNode m_operand;
	private UnaryOperation m_operation;

	public UnaryExpressionNode(ExpressionNode operand, UnaryOperation operation)
	{
		m_operand = operand;
		m_operation = operation;
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
	
	public String stringify()
	{
		switch (m_operation)
		{
		case INTCAST:
			return "(int)";
		case DOUBLECAST:
			return "(double)";
		case NEGATION:
			return "-";
		case NOT:
			return "!";
		case CLASSCAST:
			return "(CLASS)";
		}
		return "";
	}
}
