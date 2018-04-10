package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;

public class VoidTypeNode extends TypeNode {
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn) {
		return false;
	}	
}
