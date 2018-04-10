package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;

public class SizedArrayTypeNode extends ArrayTypeNode
{

	private int m_size;

	public SizedArrayTypeNode(TypeNode tn, int size)
	{
		super(tn);
		m_size = size;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn)
	{
		if (tn instanceof SizedArrayTypeNode)
			return ((SizedArrayTypeNode) tn).m_size == m_size;
		else if (tn instanceof UnsizedArrayTypeNode)
			return true;
		return false;
	}

	public int getSize()
	{
		return m_size;
	}
}
