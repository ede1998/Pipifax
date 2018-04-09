package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class CaseNode extends ControlNode
{

	public CaseNode(ExpressionNode cond)
	{
		super(cond);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	/**
	 * not needed for this subclass
	 */
	public boolean checkType()
	{
		return true;
	}
}