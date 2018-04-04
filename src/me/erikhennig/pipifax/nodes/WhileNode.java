package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;

public class WhileNode extends ControlNode {

	public WhileNode(ExpressionNode cond) {
		super(cond);
	}

}
