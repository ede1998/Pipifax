package me.erikhennig.pipifax.nodes.types;

public abstract class ArrayTypeNode extends TypeNode
{
	private TypeNode m_type;

	public ArrayTypeNode(TypeNode tn)
	{
		m_type = tn;
	}

	public TypeNode getType()
	{
		return m_type;
	}

	public int getDimensions()
	{
		if (m_type instanceof ArrayTypeNode)
			return ((ArrayTypeNode) m_type).getDimensions() + 1;
		else
			return 1;
	}
}
