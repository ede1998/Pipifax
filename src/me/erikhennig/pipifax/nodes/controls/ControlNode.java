package me.erikhennig.pipifax.nodes.controls;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.TypeNode;
import me.erikhennig.pipifax.nodes.Types;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;

public abstract class ControlNode extends Node
{
	protected ExpressionNode m_condition;
	protected ArrayList<Node> m_statements = new ArrayList<>();

	public ControlNode(ExpressionNode cond)
	{
		m_condition = cond;
	}

	public boolean addStatement(Node n)
	{
		if (n != null)
			m_statements.add(n);
		return true;
	}

	public ExpressionNode getCondition()
	{
		return m_condition;
	}

	public ArrayList<Node> getStatements()
	{
		return m_statements;
	}

	public boolean checkType()
	{
		return TypeNode.isSameType(m_condition.getType(), Types.INT)
				|| TypeNode.isSameType(m_condition.getType(), Types.STRING);
	}
}
