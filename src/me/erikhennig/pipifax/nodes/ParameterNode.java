package me.erikhennig.pipifax.nodes;

public class ParameterNode extends Node {
	private String m_name;
	private boolean m_isReference;
	private TypeNode m_type;
	
	public ParameterNode(String name, boolean isRef, TypeNode t)
	{
		m_name = name;
		m_isReference = isRef;
		m_type = t;
	}
}
