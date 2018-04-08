package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;

public class VariableNode extends Node
{
	private String m_name;
	protected TypeNode m_type;

	public VariableNode(String text, TypeNode tn)
	{
		m_name = text;
		m_type = tn;
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

	public TypeNode getType()
	{
		return m_type;
	}
}
