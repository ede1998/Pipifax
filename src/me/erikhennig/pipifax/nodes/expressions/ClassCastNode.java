package me.erikhennig.pipifax.nodes.expressions;

import me.erikhennig.pipifax.nodes.ClassNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassCastNode extends UnaryExpressionNode
{
	private ClassNode m_classNode;
	private String m_className;
	public ClassCastNode(ExpressionNode operand, String name)
	{
		super(operand, UnaryOperation.CLASSCAST);
		m_className = name;
	}
	
	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public String getClassName()
	{
		return m_className;
	}

	public ClassNode getClassNode()
	{
		return m_classNode;
	}

	public void setClassNode(ClassNode classNode)
	{
		m_classNode = classNode;
	}
	
	@Override
	public String stringify()
	{
		return "(" + m_className + ")";
	}
}
