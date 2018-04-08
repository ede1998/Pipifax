package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.TypeNode;
import me.erikhennig.pipifax.nodes.Types;
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

	@Override
	public boolean checkType()
	{
		switch (m_operation)
		{
		case ADDITION:
		case SUBTRACTION:
		case MULTIPLICATION:
		case DIVISION:
		case AND:
		case OR:
			return checkArithmeticType();
		case EQUALS:
		case NOTEQUALS:
		case LESS:
		case LESSOREQUALS:
		case GREATER:
		case GREATEROREQUALS:
			return checkComparisonType();
		case STRINGCOMPARE:
			return checkStringCompareType();
		}
		return false;
	}

	private boolean checkStringCompareType()
	{
		boolean retVal = TypeNode.isSameType(m_leftSide.getType(), m_rightSide.getType());
		retVal &= TypeNode.isSameType(m_leftSide.getType(), new TypeNode(Types.STRING));
		if (retVal)
		{
			m_type = new TypeNode(Types.INT);
		}
		return retVal;
	}

	private boolean checkComparisonType()
	{
		boolean retVal = TypeNode.isSameType(m_leftSide.getType(), m_rightSide.getType());
		retVal &= TypeNode.isSameType(m_leftSide.getType(), new TypeNode(Types.DOUBLE))
				|| TypeNode.isSameType(m_leftSide.getType(), new TypeNode(Types.INT));
		if (retVal)
		{
			m_type = new TypeNode(Types.INT);
		}
		return retVal;
	}

	private boolean checkArithmeticType()
	{
		boolean retVal = TypeNode.isSameType(m_leftSide.getType(), m_rightSide.getType());
		retVal &= TypeNode.isSameType(m_leftSide.getType(), new TypeNode(Types.DOUBLE))
				|| TypeNode.isSameType(m_leftSide.getType(), new TypeNode(Types.INT));
		if (retVal)
		{
			m_type = m_leftSide.getType();
		}
		return retVal;
	}
}
