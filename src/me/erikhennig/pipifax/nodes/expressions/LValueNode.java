package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.VariableNode;

public class LValueNode extends ExpressionNode {
    private ArrayList<ExpressionNode> m_offsets;
    private VariableNode m_variable;
    private String m_name;

	public LValueNode(String name, ArrayList<ExpressionNode> offset) {
		m_name = name;
		m_offsets = offset;
	}

}
