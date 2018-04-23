package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.nodes.ClassFunctionComponentNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class ClassFunctionAccessNode extends ValueNode
{
	private ValueNode m_base;
	private ClassFunctionComponentNode m_component;
	private CallNode m_call;

	public ClassFunctionAccessNode(ValueNode base, CallNode cn)
	{
		m_base = base;
		m_call = cn;
	}

	public void setComponent(ClassFunctionComponentNode comp)
	{
		m_component = comp;
	}

	public ClassFunctionComponentNode getComponent()
	{
		return m_component;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public CallNode getCall()
	{
		return m_call;
	}

	public ValueNode getBase()
	{
		return m_base;
	}
	
	public String getName()
	{
		return m_call.getName();
	}

	@Override
	public boolean isLValue()
	{
		return m_base.isLValue();
	}
}
