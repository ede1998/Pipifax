package me.erikhennig.pipifax.nodes;

public class ParameterTypeNode extends Node
{
	private boolean m_isReference;
	private boolean m_isArrayOfUnknownSize;
	private TypeNode m_type;
	
	public ParameterTypeNode(TypeNode tn, boolean isRef, boolean isArray)
	{
		m_isReference = isRef;
		m_isArrayOfUnknownSize = isArray;
		m_type = tn;
	}
}
