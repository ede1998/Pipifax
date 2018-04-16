package me.erikhennig.pipifax.nodes;

import java.util.Collection;
import java.util.Hashtable;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class StructNode extends NamedNode
{
	private Hashtable<String, TypeNode> m_members = new Hashtable<>();

	public StructNode(String name)
	{
		super(name);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public boolean add(String name, TypeNode t)
	{
		return m_members.put(name, t) != null;
	}

	public TypeNode getType(String name)
	{
		TypeNode retVal = m_members.get(name);
		return (retVal != null) ? retVal : TypeNode.getVoid();
	}

	public Collection<TypeNode> getTypeMembers()
	{
		return m_members.values();
	}

	public Hashtable<String, TypeNode> getMembers()
	{
		return m_members;
	}
}
