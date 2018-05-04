package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassFunctionNode extends FunctionNode implements IVisibility
{
	private Visibility m_accessModifier;
	private ClassNode m_parent;

	public ClassFunctionNode(TypeNode retType, String name, BlockNode bn)
	{
		super(retType, name, bn);
	}

	public ClassFunctionNode(FunctionNode fn)
	{
		super(fn.getReturnVariable().getType(), fn.m_name, fn.m_statements);
		m_parameterList = fn.m_parameterList;
		setPosition(fn.getLine(), fn.getPositionInLine());
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	@Override
	public Visibility getVisibility()
	{
		return m_accessModifier;
	}

	@Override
	public void setVisibility(Visibility v)
	{
		m_accessModifier = v;
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
