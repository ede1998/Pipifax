package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class DoWhileNode extends ControlNode {

	public DoWhileNode(ExpressionNode cond, BlockNode bn) {
		super(cond, bn);
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
