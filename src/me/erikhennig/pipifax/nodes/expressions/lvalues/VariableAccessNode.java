package me.erikhennig.pipifax.nodes.expressions.lvalues;

import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class VariableAccessNode extends LValueNode {

	private VariableNode m_variable;
	private String m_name;
	
	public VariableAccessNode(String name) {
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
	public void accept(Visitor v) {
		v.visit(this);
	}
}
