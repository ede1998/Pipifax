package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class DoubleLiteralNode extends LiteralNode
{
	private double m_value;

	public DoubleLiteralNode(double val)
	{
		m_value = val;
		m_type = TypeNode.getDouble();
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public double getValue()
	{
		return m_value;
	}
}
