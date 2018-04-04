package me.erikhennig.pipifax.nodes.expressions;

import java.util.ArrayList;

public class CallNode extends ExpressionNode {
	private ArrayList<ExpressionNode> m_arguments = new ArrayList<>();
	
	public CallNode()
	{
		
	}
	
	public void addArgument(ExpressionNode arg)
	{
		m_arguments.add(arg);
	}
}
