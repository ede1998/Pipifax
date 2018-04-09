package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.TypeNode;
import me.erikhennig.pipifax.nodes.Types;
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

	@Override
	public boolean checkType()
	{
		switch (m_operation)
		{
		case INTCAST:
			return checkIntCastType();
		case DOUBLECAST:
			return checkDoubleCastType();
		case NEGATION:
			return checkNegationType();
		case NOT:
			return checkNotType();
		}
		return false;
	}

	private boolean checkIntCastType()
	{
		boolean retVal = TypeNode.isSameType(m_operand.getType(), Types.INT)
				|| TypeNode.isSameType(m_operand.getType(), Types.DOUBLE);
		if (retVal)
		{
			m_type = new TypeNode(Types.INT);
		}
		return retVal;
	}

	private boolean checkDoubleCastType()
	{
		boolean retVal = TypeNode.isSameType(m_operand.getType(), Types.INT)
				|| TypeNode.isSameType(m_operand.getType(), Types.DOUBLE);
		if (retVal)
		{
			m_type = new TypeNode(Types.DOUBLE);
		}
		return retVal;
	}

	private boolean checkNegationType()
	{
		boolean retVal = TypeNode.isSameType(m_operand.getType(), Types.INT)
				|| TypeNode.isSameType(m_operand.getType(), Types.DOUBLE);
		if (retVal)
		{
			m_type = m_operand.getType();
		}
		return retVal;
	}

	private boolean checkNotType()
	{
		boolean retVal = TypeNode.isSameType(m_operand.getType(), Types.INT);
		if (retVal)
		{
			m_type = new TypeNode(Types.INT);
		}
		return retVal;
	}
}
