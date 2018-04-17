package me.erikhennig.pipifax.visitors;

import java.util.ArrayList;
import java.util.Iterator;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ParameterNode;
import me.erikhennig.pipifax.nodes.StructComponentNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.values.ArrayAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.StructAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.VariableAccessNode;
import me.erikhennig.pipifax.nodes.types.ArrayTypeNode;
import me.erikhennig.pipifax.nodes.types.CustomTypeNode;
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

		final TypeNode returnType = n.getFunction().getReturnVariable().getType();

		n.setType(returnType);
		boolean isValidArgNumber = parameters.size() == arguments.size();
		boolean areValidArgs = true;

		Iterator<ExpressionNode> exp = arguments.iterator();
		Iterator<ParameterNode> param = parameters.iterator();
		if (isValidArgNumber)
			while (exp.hasNext())
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
			printErrorAndFail("Type Check Error: Conflicting types in Assignment.");
		if (!n.getDestination().isLValue())
			printErrorAndFail("Type Check Error: Assigned side is no lvalue");
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
	public void visit(StructAccessNode n)
	{
		super.visit(n);
		if (!(n.getBase().getType() instanceof CustomTypeNode))
			printErrorAndFail("Type Check Error: Base Type is no struct type");
		CustomTypeNode basetype = (CustomTypeNode) n.getBase().getType();

		StructComponentNode scn = basetype.getTypeDefinition().find(n.getName());
		if (scn == null)
			printErrorAndFail("Type Check Error: Struct " + basetype.getName() + " has no member " + n.getName());
		n.setComponent(scn);
		n.setType(scn.getType());
	}

	@Override
	public void visit(ArrayAccessNode n)
	{
		super.visit(n);
		final TypeNode predType = n.getBase().getType();
		if (predType instanceof ArrayTypeNode)
		{
			final ArrayTypeNode predArray = (ArrayTypeNode) predType;
			n.setType(predArray.getType());
		}
		else
		{
			printErrorAndFail("Type Check Error: No Array Access Node");
		}
	}

	@Override
	public void visit(VariableAccessNode n)
	{
		final TypeNode tn = n.getVariable().getType();
		n.setType((tn instanceof RefTypeNode) ? ((RefTypeNode) tn).getType() : tn);
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
