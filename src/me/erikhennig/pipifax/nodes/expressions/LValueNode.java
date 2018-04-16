package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class LValueNode extends ExpressionNode
{
	private VariableNode m_variable;
	private ArrayList<SubLValueNode> m_children;

	public LValueNode(ArrayList<SubLValueNode> children)
	{
		m_children = children;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public VariableNode getVariable()
	{
		return m_variable;
	}

	public void setVariable(VariableNode variable)
	{
		m_variable = variable;
	}

	public ArrayList<SubLValueNode> getChildren()
	{
		return m_children;
	}
}
