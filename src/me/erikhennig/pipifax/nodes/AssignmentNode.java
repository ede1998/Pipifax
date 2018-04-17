package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.nodes.expressions.values.ValueNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class AssignmentNode extends Node
{
	private ExpressionNode m_src;
	private ValueNode m_dest;

	public AssignmentNode(ValueNode dest, ExpressionNode src)
	{
		m_dest = dest;
		m_src = src;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public ExpressionNode getSource()
	{
		return m_src;
	}

	public ValueNode getDestination()
	{
		return m_dest;
	}
}
