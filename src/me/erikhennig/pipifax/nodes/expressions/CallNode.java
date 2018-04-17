package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class CallNode extends ExpressionNode
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
	public void accept(Visitor v)
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
}
