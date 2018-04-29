package me.erikhennig.pipifax.visitors;

import java.util.Map.Entry;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.values.ArrayAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassDataAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassFunctionAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.StructAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.VariableAccessNode;
import me.erikhennig.pipifax.nodes.types.*;

public abstract class Visitor
{
	protected String getName()
	{
		return "defaultvisitor";
	}

	public void visit(AssignmentNode n) throws VisitorException
	{
		n.getDestination().accept(this);
		n.getSource().accept(this);
	}

	public void visit(IntTypeNode n) throws VisitorException
	{
	}

	public void visit(StringTypeNode n) throws VisitorException
	{
	}

	public void visit(DoubleTypeNode n) throws VisitorException
	{
		n.getUnitNode().accept(this);
	}

	public void visit(VoidTypeNode n) throws VisitorException
	{
	}

	public void visit(CustomTypeNode n) throws VisitorException
	{
	}

	public void visit(SizedArrayTypeNode n) throws VisitorException
	{
		n.getType().accept(this);
	}

	public void visit(UnsizedArrayTypeNode n) throws VisitorException
	{
		n.getType().accept(this);
	}

	public void visit(RefTypeNode n) throws VisitorException
	{
		n.getType().accept(this);
	}

	public void visit(FunctionNode n) throws VisitorException
	{
		if (n.getReturnVariable() != null)
			n.getReturnVariable().accept(this);
		for (ParameterNode entry : n.getParameterList())
			entry.accept(this);
		n.getStatements().accept(this);
	}

	public void visit(IfNode n) throws VisitorException
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
		n.getElseStatements().accept(this);
	}

	public void visit(ParameterNode n) throws VisitorException
	{
		visit((VariableNode) n);
	}

	public void visit(ProgramNode n) throws VisitorException
	{
		for (Node entry : n.getNodes())
			entry.accept(this);
	}

	public void visit(VariableNode n) throws VisitorException
	{
		n.getType().accept(this);
		if (n.getExpression() != null)
			n.getExpression().accept(this);
	}

	public void visit(WhileNode n) throws VisitorException
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
	}

	public void visit(DoWhileNode n) throws VisitorException
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
	}

	public void visit(ForNode n) throws VisitorException
	{
		if (n.getInitialAssignment() != null)
			n.getInitialAssignment().accept(this);
		n.getCondition().accept(this);
		if (n.getLoopedAssignment() != null)
			n.getLoopedAssignment().accept(this);
		n.getStatements().accept(this);
	}

	public void visit(SwitchNode n) throws VisitorException
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
		n.getDefaultStatements().accept(this);
	}

	public void visit(CaseNode n) throws VisitorException
	{
		n.getCondition().accept(this);
		n.getStatements().accept(this);
	}

	public void visit(BinaryExpressionNode n) throws VisitorException
	{
		n.getLeftSide().accept(this);
		n.getRightSide().accept(this);
	}

	public void visit(UnaryExpressionNode n) throws VisitorException
	{
		n.getOperand().accept(this);
	}

	public void visit(ClassCastNode n) throws VisitorException
	{
		visit((UnaryExpressionNode) n);
	}

	public void visit(CallNode n) throws VisitorException
	{
		for (ExpressionNode entry : n.getArguments())
			entry.accept(this);
	}

	public void visit(DoubleLiteralNode n) throws VisitorException
	{
		n.getUnit().accept(this);
	}

	public void visit(IntegerLiteralNode n) throws VisitorException
	{
	}

	public void visit(ArrayAccessNode n) throws VisitorException
	{
		n.getOffset().accept(this);
		n.getBase().accept(this);
	}

	public void visit(StructAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
	}

	public void visit(VariableAccessNode n) throws VisitorException
	{
	}

	public void visit(StringLiteralNode n) throws VisitorException
	{
	}

	public void visit(BlockNode n) throws VisitorException
	{
		for (Node entry : n.getStatements())
			entry.accept(this);
	}

	public void visit(StructNode n) throws VisitorException
	{
		for (Entry<String, StructComponentNode> entry : n.getMembers().entrySet())
			entry.getValue().accept(this);
	}

	public void visit(StructComponentNode n) throws VisitorException
	{
		n.getType().accept(this);
	}

	public void visit(ClassFunctionNode n) throws VisitorException
	{
		visit((FunctionNode) n);
	}

	public void visit(ClassFieldNode n) throws VisitorException
	{
		visit((VariableNode) n);
	}

	public void visit(ClassNode n) throws VisitorException
	{
		for (Entry<String, ClassFieldNode> entry : n.getMembers().entrySet())
			entry.getValue().accept(this);
		for (Entry<String, ClassFunctionNode> entry : n.getFunctions().entrySet())
			entry.getValue().accept(this);
	}

	public void visit(ClassDataAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
	}

	public void visit(ClassFunctionAccessNode n) throws VisitorException
	{
		n.getCall().accept(this);
		n.getBase().accept(this);
	}

	public void visit(UnitNode n) throws VisitorException
	{
	}

	public void visit(UnitDefinitionNode n) throws VisitorException
	{
			n.getUnit().accept(this);
	}
}
