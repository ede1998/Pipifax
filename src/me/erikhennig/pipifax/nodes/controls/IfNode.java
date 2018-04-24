package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class IfNode extends ControlNode
{
	private BlockNode m_statements1;

	public IfNode(ExpressionNode cond, BlockNode bn, BlockNode bn1)
	{
		super(cond, bn);
		m_statements1 = bn1;
	}

	public void addElseStatement(Node n)
	{
		m_statements1.addStatement(n);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public BlockNode getElseStatements()
	{
		return m_statements1;
	}
}
