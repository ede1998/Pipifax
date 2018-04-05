package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;

public class IntegerLiteralNode extends LiteralNode {
	private int m_value;

	public IntegerLiteralNode(int val)
	{
		m_value = val;
	}

	@Override
	public Types getType() {
		return Types.INT;
	}
}
