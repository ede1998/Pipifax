package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ParameterNode extends VariableNode
{
	public ParameterNode(String name, TypeNode tn)
	{
		super(name, tn, null);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}
}
