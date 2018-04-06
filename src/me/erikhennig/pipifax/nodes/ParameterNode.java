package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;

public class ParameterNode extends Node
{
	private String m_name;
	private ParameterTypeNode m_type;

	public ParameterNode(String name, ParameterTypeNode ptn)
	{
		m_name = name;
		m_type = ptn;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public String getName()
	{
		return m_name;
	}

	public ParameterTypeNode getType()
	{
		return m_type;
	}
}
