package me.erikhennig.pipifax.nodes;

public class ParameterNode extends Node {
	private String m_name;
	private ParameterTypeNode m_type;
	
	
	public ParameterNode(String name, ParameterTypeNode ptn)
	{
		m_name = name;
		m_type = ptn;
	}
}
