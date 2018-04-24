package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class VariableAccessNode extends ValueNode
{

	private VariableNode m_variable;
	private String m_name;

	public VariableAccessNode(String name)
	{
		m_name = name;
	}

	public VariableNode getVariable()
	{
		return m_variable;
	}

	public void setVariable(VariableNode variable)
	{
		m_variable = variable;
	}

	public String getName()
	{
		return m_name;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	@Override
	public boolean isLValue()
	{
		return true;
	}
}
