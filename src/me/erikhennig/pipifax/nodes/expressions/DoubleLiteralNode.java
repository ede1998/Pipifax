package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.DoubleTypeNode;
import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.nodes.types.UnitNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class DoubleLiteralNode extends LiteralNode
{
	private double m_value;
	private UnitNode m_unit;

	public DoubleLiteralNode(double val, UnitNode unit)
	{
		m_value = val;
		m_type = TypeNode.getDouble();
		((DoubleTypeNode) m_type).setUnitNode(unit);
		m_unit = unit;
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
	
	public UnitNode getUnit()
	{
		return m_unit;
	}
}
