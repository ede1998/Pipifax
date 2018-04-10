package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;

public class IntTypeNode extends TypeNode {
	
	IntTypeNode() {
	}
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn) {
		return tn == this;
	}
}
