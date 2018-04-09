package me.erikhennig.pipifax.nodes.controls;

import java.util.ArrayList;
import java.util.Iterator;

import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.TypeNode;
import me.erikhennig.pipifax.nodes.Types;
import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class SwitchNode extends ControlNode
{
	ArrayList<Node> m_defaultStatements = new ArrayList<>();

	public SwitchNode(ExpressionNode cond)
	{
		super(cond);
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
			m_statements.add(n);
			return true;
		}
		return false;
	}

	public boolean checkType()
	{
		boolean retVal = TypeNode.isSameType(m_condition.getType(), Types.INT)
				|| TypeNode.isSameType(m_condition.getType(), Types.STRING);
		for (Iterator<Node> iter = m_statements.iterator(); iter.hasNext();)
		{
			retVal &= TypeNode.isSameType(m_condition.getType(), ((CaseNode) iter.next()).getCondition().getType());
		}
		return retVal;
	}

	public void addDefaultStatement(Node n)
	{
		if (n != null)
			m_defaultStatements.add(n);
	}

	public ArrayList<Node> getDefaultStatements()
	{
		return m_defaultStatements;
	}

}