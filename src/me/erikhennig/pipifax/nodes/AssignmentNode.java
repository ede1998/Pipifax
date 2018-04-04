package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.nodes.expressions.LValueNode;

public class AssignmentNode extends Node {
	private ExpressionNode m_src;
	private LValueNode m_dest;
	
	public AssignmentNode(LValueNode dest, ExpressionNode src) {
		m_dest = dest;
		m_src = src;
	}

}
