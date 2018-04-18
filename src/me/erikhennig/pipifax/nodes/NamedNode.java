package me.erikhennig.pipifax.nodes;

public abstract class NamedNode extends Node
{
	protected String m_name;
	private boolean m_isExported;
	
	public NamedNode(String name)
	{
		m_name = name;
	}
	
	public String getName()
	{
		return m_name;
	}
	public void setExported(boolean export)
	{
		m_isExported = export;
	}
	public boolean isExported()
	{
		return m_isExported;
	}
}
