package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;

public class ParameterNode extends VariableNode
{
	private boolean m_isReference;
	private boolean m_isArrayOfUnknownSize;

	public ParameterNode(String name, TypeNode tn, boolean isRef, boolean isArray)
	{
		super(name, tn);
		m_isReference = isRef;
		m_isArrayOfUnknownSize = isArray;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
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
