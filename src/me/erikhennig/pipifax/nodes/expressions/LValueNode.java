package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.TypeNode;
import me.erikhennig.pipifax.nodes.Types;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class LValueNode extends ExpressionNode
{
	private ArrayList<ExpressionNode> m_offsets;
	private VariableNode m_variable;
	private String m_name;

	public LValueNode(String name, ArrayList<ExpressionNode> offset)
	{
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

	@Override
	public boolean checkType()
	{
		int varDimensions = m_variable.getType().getDimensions().size();
		int lvalDimensions = m_offsets.size();
		if (lvalDimensions <= varDimensions)
		{
			m_type = new TypeNode(m_variable.getType(), varDimensions - lvalDimensions);
		} else
			return false;

		return checkArrayAccessType();
	}

	private boolean checkArrayAccessType()
	{
		for (ExpressionNode en : m_offsets)
		{
			if (!TypeNode.isSameType(en.getType(), Types.INT))
				return false;
		}
		return true;
	}
}
