package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.nodes.types.Types;
import me.erikhennig.pipifax.visitors.Visitor;

public class IntegerLiteralNode extends LiteralNode
{
	private int m_value;

	public IntegerLiteralNode(int val)
	{
		m_value = val;
		m_type = TypeNode.getInt();
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public int getValue()
	{
		return m_value;
	}
}
