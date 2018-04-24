package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassFieldNode extends VariableNode implements IVisibility
{

	private ClassNode m_parent;
	private Visibility m_accessModifier;

	public ClassFieldNode(String text, TypeNode tn)
	{
		super(text, tn, null);
	}

	public ClassFieldNode(VariableNode vn)
	{
		super(vn.m_name, vn.getType(), vn.getExpression());
		vn.setPosition(vn.getLine(), vn.getPositionInLine());
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public TypeNode getType()
	{
		return m_type;
	}

	public String getName()
	{
		return m_name;
	}

	public ClassNode getParent()
	{
		return m_parent;
	}

	void setParent(ClassNode parent)
	{
		m_parent = parent;
	}

	@Override
	public void setVisibility(Visibility v)
	{
		m_accessModifier = v;
	}

	@Override
	public Visibility getVisibility()
	{
		return m_accessModifier;
	}
}
