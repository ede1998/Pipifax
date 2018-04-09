package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class IfNode extends ControlNode {
    private ArrayList<Node> m_statements1 = new ArrayList<>();
	public IfNode(ExpressionNode cond) {
		super(cond);
	}
	
	public void addElseStatement(Node n)
	{
		if (n != null)
			m_statements1.add(n);
	}
	
	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}
	
	public ArrayList<Node> getElseStatements()
	{
		return m_statements1;
	}
}
