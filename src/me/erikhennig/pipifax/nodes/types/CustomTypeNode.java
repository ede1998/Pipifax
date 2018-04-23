package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.nodes.TypeDefinitionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class CustomTypeNode extends TypeNode
{
	private TypeDefinitionNode m_typeDefinition = null;
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

	public TypeDefinitionNode getTypeDefinition()
	{
		return m_typeDefinition;
	}

	public void setTypeDefinition(TypeDefinitionNode typeDefinition)
	{
		m_typeDefinition = typeDefinition;
	}

}
