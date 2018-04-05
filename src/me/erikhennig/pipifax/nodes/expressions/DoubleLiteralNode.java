package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.Types;


public class DoubleLiteralNode extends LiteralNode {
	
	private double m_value;
	
	public DoubleLiteralNode(double val)
	{
		m_value = val;
	}

	@Override
	public Types getType() {
		return Types.DOUBLE;
	}
}
