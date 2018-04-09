package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.ControlNode;
import me.erikhennig.pipifax.nodes.expressions.*;

public class TypeCheckingVisitor extends Visitor
{
	@Override
	public void visit(BinaryExpressionNode n)
	{
		super.visit(n);
		if (!n.checkType())
			System.err.println("Type Check Error: Binary Expression can't take those types");
	}

	@Override
	public void visit(CallNode n)
	{
		super.visit(n);
		if (!n.checkType())
			System.err.println("Type Check Error: Invalid arguments for function call");
	}

	@Override
	public void visit(AssignmentNode n)
	{
		super.visit(n);
		if (!n.checkType())
			System.err.println("Type Check Error: Conflicting types in Assignment");
	}

	@Override
	public void visit(UnaryExpressionNode n)
	{
		super.visit(n);
		if (!n.checkType())
			System.err.println("Type Check Error: Unary Expression can't take this type");
	}

	@Override
	public void visit(LValueNode n)
	{
		super.visit(n);
		if (!n.checkType())
			System.err.println("Type Check Error: LValue type error");

	}

	@Override
	public void visit(ControlNode n)
	{
		super.visit(n);
		if (!n.checkType())
			System.err.println("Type Check Error: Control Node needs int type");
	}
}
