package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class IntegerLiteralNode extends LiteralNode
{
	private int m_value;

	public IntegerLiteralNode(int val)
	{
		m_value = val;
		m_type = TypeNode.getInt();
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public int getValue()
	{
		return m_value;
	}
}
