package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;
import me.erikhennig.pipifax.visitors.Visitor;

public class StringLiteralNode extends LiteralNode {
	private String m_value;
	
	public StringLiteralNode(String val)
	{
		m_value = val;
	}

	@Override
	public Types getType() {
		return Types.STRING;
	}
	
	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public String getValue()
	{
		return m_value;
	}

}
