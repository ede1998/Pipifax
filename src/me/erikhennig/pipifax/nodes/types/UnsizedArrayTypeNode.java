package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;

public class UnsizedArrayTypeNode extends ArrayTypeNode {
	
	public UnsizedArrayTypeNode(TypeNode tn) {
		super(tn);
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn) {
		return tn instanceof UnsizedArrayTypeNode;
	}
}
