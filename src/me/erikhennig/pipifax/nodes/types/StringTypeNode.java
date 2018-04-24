package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class StringTypeNode extends TypeNode
{

	StringTypeNode()
	{

	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	@Override
	public boolean checkType(TypeNode tn)
	{
		return this == tn;
	}

}
