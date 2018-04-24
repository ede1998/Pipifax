package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassFunctionComponentNode extends Node
{

	private FunctionNode m_function;
	private Visibility m_accessModifier;
	private ClassNode m_parent;

	public ClassFunctionComponentNode(FunctionNode fn)
	{
		m_function = fn;
		fn.setParent(this);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public FunctionNode getFunction()
	{
		return m_function;
	}

	public String getName()
	{
		return m_function.getName();
	}

	public Visibility getAccessModifier()
	{
		return m_accessModifier;
	}

	public void setAccessModifier(Visibility accessModifier)
	{
		m_accessModifier = accessModifier;
	}

	public ClassNode getParent()
	{
		return m_parent;
	}

	void setParent(ClassNode parent)
	{
		m_parent = parent;
	}
}
