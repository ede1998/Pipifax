package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassFunctionAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.VariableAccessNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class DeleteNode extends Node
{
	private static final String DESTRUCTOR_NAME = "destroy";
	private ClassFunctionAccessNode m_destructor;

	public DeleteNode(String instance)
	{
		m_destructor = new ClassFunctionAccessNode(new VariableAccessNode(instance), new CallNode(DESTRUCTOR_NAME));
	}

	public String getVariableName()
	{
		return ((VariableAccessNode) m_destructor.getBase()).getName();
	}

	public static String getDestructorName()
	{
		return DESTRUCTOR_NAME;
	}

	public ClassFunctionAccessNode getDestructorAccess()
	{
		return m_destructor;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}
}
