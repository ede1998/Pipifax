package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;

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

}
