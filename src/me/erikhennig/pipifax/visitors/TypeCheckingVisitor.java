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
import me.erikhennig.pipifax.nodes.types.StringTypeNode;
import me.erikhennig.pipifax.nodes.types.TypeNode;

public class TypeCheckingVisitor extends Visitor
{
	@Override
	protected String getName()
	{
		return "type checking";
	}

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
			printErrorAndFail(n, "Binary expression " + n.getOperationAsString() + " doesn't accept those types");
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
			printErrorAndFail(n, "Invalid argument types for function call");
		if (!isValidArgNumber)
			printErrorAndFail(n, "Invalid number of arguments for function call");
	}

	@Override
	public void visit(AssignmentNode n)
	{
		super.visit(n);
		if (!n.getSource().getType().checkType(n.getDestination().getType()))
			printErrorAndFail(n, "Conflicting types in assignment.");
		if (!n.getDestination().isLValue())
			printErrorAndFail(n, "Assigned side is no lvalue");
	}

	@Override
	public void visit(VariableNode n)
	{
		super.visit(n);
		if (n.getExpression() != null)
			if (!n.getType().checkType(n.getExpression().getType()))
				printErrorAndFail(n, "Conflicting types in initial assignment to " + n.getName());
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
			printErrorAndFail(n, "Unary expression doesn't accept this type");
	}

	@Override
	public void visit(StructAccessNode n)
	{
		super.visit(n);
		if (!(n.getBase().getType() instanceof CustomTypeNode))
			printErrorAndFail(n, "Base type is no struct type");
		CustomTypeNode basetype = (CustomTypeNode) n.getBase().getType();

		StructComponentNode scn = basetype.getTypeDefinition().find(n.getName());
		if (scn == null)
			printErrorAndFail(n, "Struct " + basetype.getName() + " has no member " + n.getName());
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
		else if (predType instanceof StringTypeNode)
		{
			n.setType(TypeNode.getString());
		}
		else
		{
			printErrorAndFail(n, "Not an array access node");
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
			printErrorAndFail(n, "While needs int type as condition");
	}
	
	@Override
	public void visit(DoWhileNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			printErrorAndFail(n, "DoWhile needs int type as condition");
	}

	@Override
	public void visit(IfNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			printErrorAndFail(n, "If needs int type as condition");
	}

	@Override
	public void visit(ForNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			printErrorAndFail(n, "For needs int type as condition");
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
			printErrorAndFail(n, "Switch needs int or string type as condition");
		if (!areCaseTypesCorrect)
			printErrorAndFail(n, "Case type of case " + position + " differs from switch type");
	}
}
