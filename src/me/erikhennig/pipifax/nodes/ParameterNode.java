package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class ParameterNode extends VariableNode
{
	public ParameterNode(String name, TypeNode tn)
	{
		super(name, tn, null);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
}
