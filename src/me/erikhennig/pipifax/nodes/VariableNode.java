package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class VariableNode extends NamedNode
{
	protected TypeNode m_type = null;
	private ExpressionNode m_initialValue = null;

	public VariableNode(String text, TypeNode tn, ExpressionNode init)
	{
		super(text);
		m_type = tn;
		m_initialValue = init;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public TypeNode getType()
	{
		return m_type;
	}

	public ExpressionNode getExpression()
	{
		return m_initialValue;
	}
}
