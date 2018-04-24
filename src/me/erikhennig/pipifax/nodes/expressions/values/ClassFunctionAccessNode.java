package me.erikhennig.pipifax.nodes.expressions.values;

import me.erikhennig.pipifax.nodes.ClassFunctionNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassFunctionAccessNode extends ValueNode
{
	private ValueNode m_base;
	private ClassFunctionNode m_component;
	private CallNode m_call;

	public ClassFunctionAccessNode(ValueNode base, CallNode cn)
	{
		m_base = base;
		m_call = cn;
	}

	public void setComponent(ClassFunctionNode comp)
	{
		m_component = comp;
	}

	public ClassFunctionNode getComponent()
	{
		return m_component;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
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
