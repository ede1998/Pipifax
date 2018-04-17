package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class StructComponentNode extends NamedNode {
	
	private TypeNode m_type;

	public StructComponentNode(String name, TypeNode t) {
		super(name);
		m_type = t;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	public TypeNode getType() {
		return m_type;
	}
}
