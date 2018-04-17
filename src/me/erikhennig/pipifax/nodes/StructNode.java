package me.erikhennig.pipifax.nodes;

import java.util.LinkedHashMap;

import me.erikhennig.pipifax.visitors.Visitor;

public class StructNode extends NamedNode
{
	private LinkedHashMap<String, StructComponentNode> m_members = new LinkedHashMap<>();

	public StructNode(String name)
	{
		super(name);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public boolean add(String name, StructComponentNode t)
	{
		return m_members.put(name, t) != null;
	}
	
	public StructComponentNode find(String name)
	{
		return m_members.get(name);
	}
	
	public LinkedHashMap<String, StructComponentNode> getMembers()
	{
		return m_members;
	}
}
