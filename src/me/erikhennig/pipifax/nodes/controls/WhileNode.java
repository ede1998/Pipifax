package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class WhileNode extends ControlNode
{
	public WhileNode(ExpressionNode cond, BlockNode bn)
	{
		super(cond, bn);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}
}
