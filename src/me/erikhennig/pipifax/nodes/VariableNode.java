package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class VariableNode extends Node
{
	private String m_name;
	protected TypeNode m_type = null;
	private ExpressionNode m_initialValue = null;

	public VariableNode(String text, TypeNode tn, ExpressionNode init)
	{
		m_name = text;
		m_type = tn;
		m_initialValue = init;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public String getName()
	{
		return m_name;
	}

	public TypeNode getType()
	{
		return m_type;
	}
	
	public ExpressionNode getExpression()
	{
		return m_initialValue;
	}
}
