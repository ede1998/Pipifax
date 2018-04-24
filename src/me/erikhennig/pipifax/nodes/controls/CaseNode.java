package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class CaseNode extends ControlNode
{

	public CaseNode(ExpressionNode cond, BlockNode bn)
	{
		super(cond, bn);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
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