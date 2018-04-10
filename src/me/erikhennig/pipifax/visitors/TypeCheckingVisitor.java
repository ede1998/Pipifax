package me.erikhennig.pipifax.visitors;

import java.util.ArrayList;
import java.util.Iterator;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ParameterNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.types.ArrayTypeNode;
import me.erikhennig.pipifax.nodes.types.RefTypeNode;
import me.erikhennig.pipifax.nodes.types.TypeNode;

public class TypeCheckingVisitor extends Visitor
{
	@Override
	public void visit(BinaryExpressionNode n)
	{
		super.visit(n);

		TypeNode lefttype = n.getLeftSide().getType();
		TypeNode righttype = n.getRightSide().getType();

		boolean retVal = lefttype.checkType(righttype);

		switch (n.getOperation())
		{
		case ADDITION:
		case SUBTRACTION:
		case MULTIPLICATION:
		case DIVISION:
			retVal &= lefttype.checkType(TypeNode.getDouble()) || lefttype.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(lefttype);
			break;
		case MODULO:
		case AND:
		case OR:
			retVal &= lefttype.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(lefttype);
			break;
		case EQUALS:
		case NOTEQUALS:
		case LESS:
		case LESSOREQUALS:
		case GREATER:
		case GREATEROREQUALS:
			retVal &= lefttype.checkType(TypeNode.getDouble()) || lefttype.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(TypeNode.getInt());
			break;
		case STRINGCOMPARE:
			retVal &= lefttype.checkType(TypeNode.getString());
			if (retVal)
				n.setType(TypeNode.getInt());
			break;
		case CONCATENATION:
			retVal &= lefttype.checkType(TypeNode.getString());
			if (retVal)
				n.setType(TypeNode.getString());
			break;
		}

		if (!retVal)
			printErrorAndFail(
					"Type Check Error: Binary Expression " + n.getOperationAsString() + " can't take those types");
	}

	@Override
	public void visit(CallNode n)
	{
		super.visit(n);

		FunctionNode func = n.getFunction();
		ArrayList<ParameterNode> parameters = func.getParameterList();
		ArrayList<ExpressionNode> arguments = n.getArguments();

		n.setType(func.getReturnVariable().getType());

		boolean isValidArgNumber = parameters.size() == arguments.size();
		boolean areValidArgs = true;

		Iterator<ExpressionNode> exp = arguments.iterator();
		Iterator<ParameterNode> param = parameters.iterator();
		if (isValidArgNumber)
			for (; exp.hasNext();)
			{
				areValidArgs &= param.next().getType().checkType(exp.next().getType());
			}

		if (!areValidArgs)
			printErrorAndFail("Type Check Error: Invalid argument types for function call");
		if (!isValidArgNumber)
			printErrorAndFail("Type Check Error: Invalid number of arguments for function call");
	}

	@Override
	public void visit(AssignmentNode n)
	{
		super.visit(n);
		if (!n.getSource().getType().checkType(n.getDestination().getType()))
			printErrorAndFail("Type Check Error: Conflicting types in Assignment to " + n.getDestination().getName());
	}

	@Override
	public void visit(VariableNode n)
	{
		super.visit(n);
		if (n.getExpression() != null)
			if (!n.getType().checkType(n.getExpression().getType()))
				printErrorAndFail("Type Check Error: Conflicting types in initial Assignment to " + n.getName());
	}

	@Override
	public void visit(UnaryExpressionNode n)
	{
		super.visit(n);

		boolean retVal = false;
		TypeNode type = n.getOperand().getType();

		switch (n.getOperation())
		{
		case INTCAST:
			retVal = type.checkType(TypeNode.getInt()) || type.checkType(TypeNode.getDouble());
			if (retVal)
				n.setType(TypeNode.getInt());
			break;
		case DOUBLECAST:
			retVal = type.checkType(TypeNode.getInt()) || type.checkType(TypeNode.getDouble());
			if (retVal)
				n.setType(TypeNode.getDouble());
			break;
		case NEGATION:
			retVal = type.checkType(TypeNode.getInt()) || type.checkType(TypeNode.getDouble());
			if (retVal)
				n.setType(type);
			break;
		case NOT:
			retVal = type.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(TypeNode.getInt());
			break;
		}

		if (!retVal)
			printErrorAndFail("Type Check Error: Unary Expression can't take this type");
	}

	@Override
	public void visit(LValueNode n)
	{
		super.visit(n);

		boolean enoughDimensions = true;
		boolean onlyIntsInOffsets = true;
		TypeNode vtn = n.getVariable().getType();

		TypeNode potentialtype = (vtn instanceof RefTypeNode) ? ((RefTypeNode) vtn).getType() : vtn;
		for (ExpressionNode en : n.getOffsets())
		{
			if (!en.getType().checkType(TypeNode.getInt()))
			{
				onlyIntsInOffsets = false;
				break;
			}
			if (potentialtype instanceof ArrayTypeNode)
				potentialtype = ((ArrayTypeNode) potentialtype).getType();
			else
			{
				enoughDimensions = false;
				break;
			}
		}
		/*
		 * //necessary if Pointertypes are introduced or sth if (vtn instanceof
		 * RefTypeNode) { potentialtype = new RefTypeNode(potentialtype); }
		 */
		if (enoughDimensions && onlyIntsInOffsets)
			n.setType(potentialtype);

		if (!enoughDimensions)
			printErrorAndFail("Type Check Error: LValue-ArrayAccess requests more dimensions than variable offers");
		if (!onlyIntsInOffsets)
			printErrorAndFail("Type Check Error: LValue offsets has non integer types");
	}

	@Override
	public void visit(WhileNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			printErrorAndFail("Type Check Error: While Node needs int type");
	}

	@Override
	public void visit(IfNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			printErrorAndFail("Type Check Error: If Node needs int type");
	}

	@Override
	public void visit(ForNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			printErrorAndFail("Type Check Error: For Node needs int type");
	}

	@Override
	public void visit(SwitchNode n)
	{
		super.visit(n);

		TypeNode type = n.getCondition().getType();
		ArrayList<Node> nodes = n.getStatements().getStatements();
		boolean retVal = type.checkType(TypeNode.getInt()) || type.checkType(TypeNode.getString());
		boolean areCaseTypesCorrect = true;

		int position = 0;
		for (Iterator<Node> iter = nodes.iterator(); iter.hasNext(); position++)
		{
			TypeNode casecondtype = ((CaseNode) iter.next()).getCondition().getType();
			areCaseTypesCorrect &= type.checkType(casecondtype);
		}

		if (!retVal)
			printErrorAndFail("Type Check Error: Switch Node needs int or string type");
		if (!areCaseTypesCorrect)
			printErrorAndFail("Type Check Error: Case Type of case " + position + " differs from switch type");
	}
}
