package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;

public class DoubleTypeNode extends TypeNode {
	
	DoubleTypeNode() {
	};
   
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn) {
		return tn == this;
	}
}
