package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class DoubleTypeNode extends TypeNode
{
	private UnitNode m_unitNode;
	
	DoubleTypeNode()
	{
	};

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn)
	{
		return tn instanceof DoubleTypeNode;
	}

	public UnitNode getUnitNode()
	{
		return m_unitNode;
	}

	public void setUnitNode(UnitNode unitNode)
	{
		m_unitNode = unitNode;
	}
}
