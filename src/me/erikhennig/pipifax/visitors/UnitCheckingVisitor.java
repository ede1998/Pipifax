package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.expressions.BinaryExpressionNode;
import me.erikhennig.pipifax.nodes.expressions.UnaryExpressionNode;
import me.erikhennig.pipifax.nodes.types.DoubleTypeNode;
import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.nodes.types.UnitNode;

public class UnitCheckingVisitor extends Visitor
{
	@Override
	protected String getName()
	{
		return "unit checking";
	}

	@Override
	public void visit(BinaryExpressionNode n) throws VisitorException
	{
		super.visit(n);

		// check if this node has to be checked (is a double node)
		TypeNode lefttype = n.getLeftSide().getType();
		TypeNode righttype = n.getRightSide().getType();
		if (!lefttype.checkType(righttype) || !lefttype.checkType(TypeNode.getDouble()))
			return;

		UnitNode left = ((DoubleTypeNode) n.getLeftSide().getType()).getUnitNode();
		UnitNode right = ((DoubleTypeNode) n.getRightSide().getType()).getUnitNode();

		UnitNode tmp = new UnitNode(left);

		switch (n.getOperation())
		{
		case ADDITION:
		case SUBTRACTION:
			if (!left.check(right))
				throw new VisitorException(this, n, "Incompatible units in operation");
			((DoubleTypeNode) n.getType()).setUnitNode(left);
			break;
		case MULTIPLICATION:
			tmp.expand(right);
			((DoubleTypeNode) n.getType()).setUnitNode(tmp);
			break;
		case DIVISION:
			tmp.reduce(right);
			((DoubleTypeNode) n.getType()).setUnitNode(tmp);
			break;
		case MODULO:
			((DoubleTypeNode) n.getType()).setUnitNode(left);
			break;
		case EQUALS:
		case NOTEQUALS:
		case LESS:
		case LESSOREQUALS:
		case GREATER:
		case GREATEROREQUALS:
			if (!left.check(right))
				throw new VisitorException(this, n, "Incompatible units in comparison");
			break;
		default:
			return;
		}
	}

	@Override
	public void visit(UnaryExpressionNode n) throws VisitorException
	{
		super.visit(n);

		if (!n.getOperand().getType().checkType(TypeNode.getDouble()))
			return;
		UnitNode operandUnit = ((DoubleTypeNode) n.getOperand().getType()).getUnitNode();
		switch (n.getOperation())
		{
		case NEGATION:
			((DoubleTypeNode) n.getType()).setUnitNode(operandUnit);
			break;
		default:
			return;
		}
	}

	@Override
	public void visit(AssignmentNode n) throws VisitorException
	{
		super.visit(n);
		UnitNode left = ((DoubleTypeNode) n.getSource().getType()).getUnitNode();
		UnitNode right = ((DoubleTypeNode) n.getDestination().getType()).getUnitNode();
		if (!left.check(right))
			throw new VisitorException(this, n, "Incompatible units in assignment");
	}
}
