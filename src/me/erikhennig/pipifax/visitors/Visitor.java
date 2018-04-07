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

	public void visit(AdditionNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(AndNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(CallNode n)
	{
		n.getArguments().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(DivisionNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(DoubleLiteralNode n)
	{
	}

	public void visit(EqualsNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(GreaterNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(GreaterOrEqualsNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(IntegerLiteralNode n)
	{
	}

	public void visit(LessNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(LessOrEqualsNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(LValueNode n)
	{
		n.getOffsets().forEach((subnode) -> subnode.accept(this));
	}

	public void visit(MultiplicationNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(NegationNode n)
	{
		n.getOperand().accept(this);
	}

	public void visit(NotEqualsNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(NotNode n)
	{
		n.getOperand().accept(this);
	}

	public void visit(OrNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(StringCompareNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(StringLiteralNode n)
	{
	}

	public void visit(SubtractionNode n)
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}
}
