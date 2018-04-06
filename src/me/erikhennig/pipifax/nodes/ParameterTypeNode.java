package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;

public class ParameterTypeNode extends Node
{
	private boolean m_isReference;
	private boolean m_isArrayOfUnknownSize;
	private TypeNode m_type;

	public ParameterTypeNode(TypeNode tn, boolean isRef, boolean isArray)
	{
		m_isReference = isRef;
		m_isArrayOfUnknownSize = isArray;
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

	public boolean isArrayOfUnknownSize()
	{
		return m_isArrayOfUnknownSize;
	}

	public boolean isReference()
	{
		return m_isReference;
	}

}
