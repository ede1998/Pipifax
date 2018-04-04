package me.erikhennig.pipifax;

public class VarNode extends Node {

	private String m_name;
	private TypeNode m_type;
	public VarNode(String text, TypeNode tn) {
		m_name = text;
		m_type = tn;
	}
	

}
