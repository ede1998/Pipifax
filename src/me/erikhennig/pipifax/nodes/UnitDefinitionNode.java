package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.UnitNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class UnitDefinitionNode extends NamedNode
{
	private UnitNode m_unit;
	
	public UnitDefinitionNode(String name, UnitNode unit)
	{
		super(name);
		m_unit = unit;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public UnitNode getUnit()
	{
		return m_unit;
	}
}
