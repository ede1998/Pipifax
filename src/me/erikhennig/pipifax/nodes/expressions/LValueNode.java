package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class LValueNode extends ExpressionNode {
    private ArrayList<ExpressionNode> m_offsets;
    private VariableNode m_variable;
    private String m_name;

	public LValueNode(String name, ArrayList<ExpressionNode> offset) {
		m_name = name;
		m_offsets = offset;
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

	public ArrayList<ExpressionNode> getOffsets()
	{
		return m_offsets;
	}

	public VariableNode getVariable()
	{
		return m_variable;
	}

	public void setVariable(VariableNode variable)
	{
		m_variable = variable;
	}
}
