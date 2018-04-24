package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

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
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public String stringify()
	{
		switch (m_operation)
		{
		case ADDITION:
			return "+";
		case SUBTRACTION:
			return "-";
		case MULTIPLICATION:
			return "*";
		case DIVISION:
			return "/";
		case MODULO:
			return "%";
		case EQUALS:
			return "==";
		case NOTEQUALS:
			return "!=";
		case LESS:
			return "<";
		case LESSOREQUALS:
			return "<=";
		case GREATER:
			return ">";
		case GREATEROREQUALS:
			return ">=";
		case AND:
			return "&&";
		case OR:
			return "||";
		case STRINGCOMPARE:
			return "<=>";
		case CONCATENATION:
			return "...";
		}
		return "";
	}
}
