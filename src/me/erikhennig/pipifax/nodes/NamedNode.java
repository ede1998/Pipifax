package me.erikhennig.pipifax.nodes;

public abstract class NamedNode extends Node
{
	protected String m_name;
	
	
	public NamedNode(String name)
	{
		m_name = name;
	}
	public String getName()
	{
		return m_name;
	}
}
