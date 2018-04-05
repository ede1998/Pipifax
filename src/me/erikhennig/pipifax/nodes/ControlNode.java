package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;

public class ControlNode extends Node {
	protected ExpressionNode m_condition;
	protected ArrayList<Node> m_statements = new ArrayList<>();
	
	public ControlNode(ExpressionNode cond)
	{
		m_condition = cond;
	}
	
	public void addStatement(Node n)
	{
		if (n != null)
			m_statements.add(n);
	}
}
