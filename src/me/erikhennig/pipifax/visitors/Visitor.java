package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.types.*;

public abstract class Visitor
{
	private boolean m_success = true;

	public boolean wasSuccessful()
	{
		return m_success;
	}
	
	protected void printErrorAndFail(String errstr)
	{
		System.err.println(errstr);
		m_success = false;
	}

	public void visit(AssignmentNode n)
	{
		n.getDestination().accept(this);
		n.getSource().accept(this);
	}

	public void visit(IntTypeNode n)
	{
	}

	public void visit(StringTypeNode n)
	{
	}

	public void visit(DoubleTypeNode n)
	{
	}

	public void visit(VoidTypeNode n)
	{
	}

	public void visit(SizedArrayTypeNode n)
	{
		n.getType().accept(this);
	}

	public void visit(UnsizedArrayTypeNode n)
	{
		n.getType().accept(this);
	}

	public void visit(RefTypeNode n)
	{
		n.getType().accept(this);
	}

	public void visit(FunctionNode n)
	{
		if (n.getReturnVariable() != null)
			n.getReturnVariable().accept(this);
		n.getParameterList().forEach((subnode) -> subnode.accept(this));
		n.getStatements().accept(this);
	}

	public void visit(IfNode n)
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
		n.getElseStatements().accept(this);
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
		if (n.getExpression() != null)
			n.getExpression().accept(this);
	}

	public void visit(WhileNode n)
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
	}

	public void visit(ForNode n)
	{
		if (n.getInitialAssignment() != null)
			n.getInitialAssignment().accept(this);
		n.getCondition().accept(this);
		if (n.getLoopedAssignment() != null)
			n.getLoopedAssignment().accept(this);
		n.getStatements().accept(this);
	}

	public void visit(SwitchNode n)
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
		n.getDefaultStatements().accept(this);
	}

	public void visit(CaseNode n)
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
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

	public void visit(BlockNode n)
	{
		n.getStatements().forEach((subnode) -> subnode.accept(this));
	}
}
