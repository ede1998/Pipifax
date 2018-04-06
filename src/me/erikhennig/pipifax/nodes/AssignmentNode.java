package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.nodes.expressions.LValueNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class AssignmentNode extends Node {
	private ExpressionNode m_src;
	private LValueNode m_dest;
	
	public AssignmentNode(LValueNode dest, ExpressionNode src) {
		m_dest = dest;
		m_src = src;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
	
	public ExpressionNode getSource()
	{
		return m_src;
	}
	
	public LValueNode getDestination()
	{
		return m_dest;
	}

}
