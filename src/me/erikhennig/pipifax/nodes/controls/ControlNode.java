package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;

public abstract class ControlNode extends Node
{
	protected ExpressionNode m_condition;
	protected BlockNode m_statements;

	public ControlNode(ExpressionNode cond, BlockNode bn)
	{
		m_condition = cond;
		m_statements = bn;
	}

	public boolean addStatement(Node n)
	{
		m_statements.addStatement(n);
		return true;
	}

	public ExpressionNode getCondition()
	{
		return m_condition;
	}

	public BlockNode getStatements()
	{
		return m_statements;
	}
}
