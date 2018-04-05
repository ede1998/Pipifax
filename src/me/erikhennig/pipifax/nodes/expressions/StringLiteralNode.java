package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;

public class StringLiteralNode extends LiteralNode {
	public static final Types m_type = Types.STRING;
	private String m_value;
	
	public StringLiteralNode(String val)
	{
		m_value = val;
	}

}
