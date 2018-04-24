package me.erikhennig.pipifax.nodes.expressions.values;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class CallNode extends ValueNode
{
	private String m_name;
	private FunctionNode m_function;
	private ArrayList<ExpressionNode> m_arguments = new ArrayList<>();

	public CallNode(String name)
	{
		m_name = name;
	}

	public void addArgument(ExpressionNode arg)
	{
		if (arg != null)
			m_arguments.add(arg);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public String getName()
	{
		return m_name;
	}

	public ArrayList<ExpressionNode> getArguments()
	{
		return m_arguments;
	}

	public FunctionNode getFunction()
	{
		return m_function;
	}

	public void setFunction(FunctionNode function)
	{
		m_function = function;
	}

	@Override
	public boolean isLValue()
	{
		return false;
	}
}
