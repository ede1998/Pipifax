package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public abstract class ControlNode extends Node {
	private ExpressionNode m_condition;
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

	public ExpressionNode getCondition()
	{
		return m_condition;
	}
	
	public ArrayList<Node> getStatements()
	{
		return m_statements;
	}
}
