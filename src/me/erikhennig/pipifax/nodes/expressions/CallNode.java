package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.FunctionNode;

public class CallNode extends ExpressionNode {
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
}
