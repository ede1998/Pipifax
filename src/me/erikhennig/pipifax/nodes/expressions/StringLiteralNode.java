package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class StringLiteralNode extends LiteralNode
{
	private String m_value;

	public StringLiteralNode(String val)
	{
		m_value = val;
		m_type = TypeNode.getString();
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public String getValue()
	{
		return m_value;
	}
}
