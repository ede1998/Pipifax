package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class ArrayAccessNode extends ValueNode {
	private ExpressionNode m_offset;
	private ValueNode m_base;
	
	public ArrayAccessNode(ValueNode base, ExpressionNode offset) {
		m_base = base;
		m_offset = offset;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
	
	public ExpressionNode getOffset()
	{
		return m_offset;
	}
	
	public ValueNode getBase()
	{
		return m_base;
	}

	@Override
	public boolean isLValue()
	{
		return m_base.isLValue();
	}
	
}
