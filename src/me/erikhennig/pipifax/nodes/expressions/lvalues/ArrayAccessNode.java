package me.erikhennig.pipifax.nodes.expressions.lvalues;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class ArrayAccessNode extends LValueNode {
	private ExpressionNode m_offset;
	private LValueNode m_base;
	
	public ArrayAccessNode(LValueNode base, ExpressionNode offset) {
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
	
	public LValueNode getBase()
	{
		return m_base;
	}
	
}
