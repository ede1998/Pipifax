package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.nodes.expressions.ExpressionNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class ForNode extends ControlNode
{
	private AssignmentNode m_initialAssignment;
	private AssignmentNode m_loopedAssignment;

	public ForNode(AssignmentNode initialAssign, ExpressionNode cond, AssignmentNode loopedAssign)
	{
		super(cond);
		m_initialAssignment = initialAssign;
		m_loopedAssignment = loopedAssign;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public AssignmentNode getInitialAssignment()
	{
		return m_initialAssignment;
	}
	
	public AssignmentNode getLoopedAssignment()
	{
		return m_loopedAssignment;
	}
}
