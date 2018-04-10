package me.erikhennig.pipifax.nodes.types;

public abstract class ArrayTypeNode extends TypeNode {
	private TypeNode m_type;
	
	public ArrayTypeNode(TypeNode tn)	
	{
		m_type = tn;
	}
	
	public TypeNode getType()
	{
		return m_type;
	}
}
