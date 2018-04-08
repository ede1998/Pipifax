package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.*;

public abstract class Visitor
{
	public void visit(AssignmentNode n)
	{
		n.getDestination().accept(this);
		n.getSource().accept(this);
	}

	public void visit(TypeNode n)
	{
	}

	public void visit(FunctionNode n)
	{
		if (n.getReturnVariable() != null)
			n.getReturnVariable().accept(this);
		n.getParameterList().forEach((subnode) -> subnode.accept(this));
		n.getStatements().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(IfNode n)
	{
		n.getCondition().accept(this);
		n.getStatements().forEach((subnode) -> subnode.accept(this));
		n.getElseStatements().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(ParameterNode n)
	{
		n.getType().accept(this);
	}

	public void visit(ProgramNode n)
	{
		n.getNodes().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(VariableNode n)
	{
		n.getType().accept(this);
	}

	public void visit(WhileNode n)
	{
		n.getCondition().accept(this);
		n.getStatements().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(BinaryExpressionNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}
	
	public void visit(UnaryExpressionNode n)
	{
		n.getOperand().accept(this);
	}
	
	public void visit(CallNode n)
	{
		n.getArguments().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(DoubleLiteralNode n)
	{
	}

	public void visit(IntegerLiteralNode n)
	{
	}

	public void visit(LValueNode n)
	{
		n.getOffsets().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(StringLiteralNode n)
	{
	}
}
