package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.VariableNode;
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
			((DoubleTypeNode) n.getType()).setUnitNode(new UnitNode(left));
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
			((DoubleTypeNode) n.getType()).setUnitNode(new UnitNode(left));
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

	//TODO unit checking does not work with arrays or maybe only array parameter, check pls
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
			((DoubleTypeNode) n.getType()).setUnitNode(new UnitNode(operandUnit));
			break;
		default:
			return;
		}
	}

	@Override
	public void visit(VariableNode n) throws VisitorException {
		super.visit(n);
		if (!n.getType().checkType(TypeNode.getDouble()))
			return;
		if (n.getExpression() == null)
			return;
		UnitNode varunit = ((DoubleTypeNode) n.getType()).getUnitNode();
	    UnitNode assignunit = ((DoubleTypeNode) n.getExpression().getType()).getUnitNode();
	    
	    //Infer units if one has a unit and one has none
	    if (varunit.hasNoDimension() != assignunit.hasNoDimension())
	    {
	    	if (varunit.hasNoDimension())
	    		((DoubleTypeNode) n.getType()).setUnitNode(new UnitNode(assignunit));
		    else if (assignunit.hasNoDimension())
			    ((DoubleTypeNode) n.getExpression().getType()).setUnitNode(new UnitNode(varunit));
	    }
	    
	    if (!varunit.check(assignunit))
	    	throw new VisitorException(this, n, "Incompatible units in initial assignment");
	    if (varunit.getCoefficient() != assignunit.getCoefficient())
	    	throw new VisitorException(this, n, "Conflicting coefficients in initial assignment: " + 
	    			varunit.getCoefficient() + " ~ " + assignunit.getCoefficient());
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
