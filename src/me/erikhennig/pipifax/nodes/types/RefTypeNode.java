package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;

public class RefTypeNode extends TypeNode
{

	private TypeNode m_type;

	public RefTypeNode(TypeNode tn)
	{
		m_type = tn;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public TypeNode getType()
	{
		return m_type;
	}

	@Override
	public boolean checkType(TypeNode tn)
	{
		return m_type.checkType(tn);
	}
}
