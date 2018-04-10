package me.erikhennig.pipifax.nodes.controls;

import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class SwitchNode extends ControlNode
{
	private BlockNode m_defaultStatements;

	public SwitchNode(ExpressionNode cond, BlockNode bn, BlockNode bn1)
	{
		super(cond, bn);
		m_defaultStatements = bn1;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	@Override
	public boolean addStatement(Node n)
	{
		if (n instanceof CaseNode)
		{
			m_statements.addStatement(n);
			return true;
		}
		return false;
	}

	public void addDefaultStatement(Node n)
	{
		m_defaultStatements.addStatement(n);
	}

	public BlockNode getDefaultStatements()
	{
		return m_defaultStatements;
	}

}