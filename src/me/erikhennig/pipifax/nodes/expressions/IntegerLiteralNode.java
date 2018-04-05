package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;

public class IntegerLiteralNode extends LiteralNode {
	private int m_value;
	public static final Types m_type = Types.INT;

	public IntegerLiteralNode(int val)
	{
		m_value = val;
	}
}
