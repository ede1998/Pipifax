package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassDataComponentNode extends Node
{

	private ClassNode m_parent;
	private TypeNode m_type;
	private String m_name;
	private Visibility m_accessModifier;
	private VariableNode m_variable;

	public ClassDataComponentNode(String name, TypeNode t)
	{
		m_name = name;
		m_type = t;
		m_variable = new VariableNode(m_name, m_type, null);
		m_variable.setParent(this);
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

	public Visibility getAccessModifier()
	{
		return m_accessModifier;
	}

	public void setAccessModifier(Visibility accessModifier)
	{
		m_accessModifier = accessModifier;
	}

	public VariableNode getVariable()
	{
		return m_variable;
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
