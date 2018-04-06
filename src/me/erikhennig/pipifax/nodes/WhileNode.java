package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class WhileNode extends ControlNode {

	public WhileNode(ExpressionNode cond) {
		super(cond);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
