package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;
import me.erikhennig.pipifax.visitors.Visitor;


public class DoubleLiteralNode extends LiteralNode {
	
	private double m_value;
	
	public DoubleLiteralNode(double val)
	{
		m_value = val;
	}

	@Override
	public Types getType() {
		return Types.DOUBLE;
	}
	
	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public double getValue()
	{
		return m_value;
	}
}
