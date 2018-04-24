package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.nodes.ClassFieldNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassDataAccessNode extends ValueNode
{
	private ValueNode m_base;
	private ClassFieldNode m_component;
	private String m_name;

	public ClassDataAccessNode(ValueNode base, String name)
	{
		m_base = base;
		m_name = name;
	}

	public void setComponent(ClassFieldNode comp)
	{
		m_component = comp;
	}

	public ClassFieldNode getComponent()
	{
		return m_component;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public ValueNode getBase()
	{
		return m_base;
	}

	public String getName()
	{
		return m_name;
	}

	@Override
	public boolean isLValue()
	{
		return m_base.isLValue();
	}
}
