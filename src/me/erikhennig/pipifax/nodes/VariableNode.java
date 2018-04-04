package me.erikhennig.pipifax.nodes;

public class VariableNode extends Node {

	private String m_name;
	private TypeNode m_type;
	public VariableNode(String text, TypeNode tn) {
		m_name = text;
		m_type = tn;
	}

}
