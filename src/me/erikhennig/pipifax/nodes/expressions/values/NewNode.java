package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class NewNode extends CallNode
{

	public NewNode(String name)
	{
		super(name);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}
}
