package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;

public class IfNode extends ControlNode {
    private ArrayList<Node> m_statements1 = new ArrayList<>();
	public IfNode(ExpressionNode cond) {
		super(cond);
	}
	
	public void addElseStatements(Node n)
	{
		m_statements1.add(n);
	}
}
