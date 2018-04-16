package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.nodes.StructNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class CustomTypeNode extends TypeNode
{
	private StructNode m_typeDefinition = null;
	private String m_name;
	
	public CustomTypeNode(String name)
	{
		m_name = name;
	}
	
	@Override
	public boolean checkType(TypeNode tn)
	{
		if (tn instanceof CustomTypeNode)
		{
			return ((CustomTypeNode) tn).m_typeDefinition == m_typeDefinition;
		}
		return false;
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

	public StructNode getTypeDefinition()
	{
		return m_typeDefinition;
	}

	public void setTypeDefinition(StructNode typeDefinition)
	{
		m_typeDefinition = typeDefinition;
	}
	
	

}
