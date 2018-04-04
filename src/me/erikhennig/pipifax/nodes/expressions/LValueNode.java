package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.VariableNode;

public class LValueNode extends UnaryExpressionNode {
    private ArrayList<Integer> m_offsets;

	public LValueNode(ExpressionNode op, ArrayList<Integer> offset) {
		super(op);
		m_offsets = offset;
	}

}
