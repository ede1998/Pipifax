package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.visitors.Visitor;

public class SubLValueNode extends ExpressionNode
{
	private ExpressionNode m_parent;
	private ArrayList<ExpressionNode> m_offsets;
	private String m_name;
	private SubLValueNode m_predecessor = null;

	public SubLValueNode(String name, ArrayList<ExpressionNode> offset)
	{
		m_name = name;
		m_offsets = offset;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public ExpressionNode getParent()
	{
		return m_parent;
	}

	public void setParent(LValueNode parent)
	{
		m_parent = parent;
	}

	public void setParent(SubLValueNode parent)
	{
		m_parent = parent;
	}

	public void setParent(CallNode parent)
	{
		m_parent = parent;
	}

	public ArrayList<ExpressionNode> getOffsets()
	{
		return m_offsets;
	}

	public String getName()
	{
		return m_name;
	}

	public SubLValueNode getPredecessor()
	{
		return m_predecessor;
	}

	public void setPredecessor(SubLValueNode pred)
	{
		m_predecessor = pred;
	}
}
