package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class StructComponentNode extends Node {
	
	private TypeNode m_type;
	private String m_name;
	public StructComponentNode(String name, TypeNode t) {
		m_name = name;
		m_type = t;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	public TypeNode getType() {
		return m_type;
	}
		
	public String getName()
	{
		return m_name;
	}
}
