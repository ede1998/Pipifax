package me.erikhennig.pipifax.visitors;

import java.util.ArrayList;
import java.util.Iterator;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.ClassFieldNode;
import me.erikhennig.pipifax.nodes.ClassFunctionNode;
import me.erikhennig.pipifax.nodes.ClassNode;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ParameterNode;
import me.erikhennig.pipifax.nodes.StructComponentNode;
import me.erikhennig.pipifax.nodes.StructNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.values.ArrayAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassDataAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassFunctionAccessNode;
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
	public void visit(BinaryExpressionNode n) throws VisitorException
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
			throw new VisitorException(this, n, "Binary expression " + n.stringify() + " doesn't accept those types");
	}

	@Override
	public void visit(CallNode n) throws VisitorException
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
			throw new VisitorException(this, n, "Invalid argument types for function call");
		if (!isValidArgNumber)
			throw new VisitorException(this, n, "Invalid number of arguments for function call");
	}

	@Override
	public void visit(AssignmentNode n) throws VisitorException
	{
		super.visit(n);
		if (!n.getSource().getType().checkType(n.getDestination().getType()))
			throw new VisitorException(this, n, "Conflicting types in assignment.");
		if (!n.getDestination().isLValue())
			throw new VisitorException(this, n, "Assigned side is no lvalue");
	}

	@Override
	public void visit(VariableNode n) throws VisitorException
	{
		super.visit(n);
		if (n.getExpression() != null)
			if (!n.getType().checkType(n.getExpression().getType()))
				throw new VisitorException(this, n, "Conflicting types in initial assignment to " + n.getName());
	}

	@Override
	public void visit(UnaryExpressionNode n) throws VisitorException
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
		default:
			throw new UnsupportedOperationException("Can't resolve this Unary Expression here.");
		}

		if (!retVal)
			throw new VisitorException(this, n, "Unary expression doesn't accept this type");
	}

	@Override
	public void visit(ClassCastNode n) throws VisitorException
	{
		n.getOperand().accept(this);
		TypeNode type = n.getOperand().getType();

		CustomTypeNode temp = new CustomTypeNode(n.getClassName());
		temp.setTypeDefinition(n.getClassNode());

		if (!temp.checkType(type))
			throw new VisitorException(this, n,
					"Cannot cast into " + n.getClassName() + " (Incompatible static types)");
		n.setType(temp);
	}

	@Override
	public void visit(StructAccessNode n) throws VisitorException
	{
		super.visit(n);
		if (!(n.getBase().getType() instanceof CustomTypeNode))
			throw new VisitorException(this, n, "Base type is no struct type");
		CustomTypeNode basetype = (CustomTypeNode) n.getBase().getType();

		StructComponentNode scn = ((StructNode) basetype.getTypeDefinition()).find(n.getName());
		if (scn == null)
			throw new VisitorException(this, n, "Struct " + basetype.getName() + " has no member " + n.getName());
		n.setComponent(scn);
		n.setType(scn.getType());
	}

	@Override
	public void visit(ClassDataAccessNode n) throws VisitorException
	{
		super.visit(n);
		if (!(n.getBase().getType() instanceof CustomTypeNode))
			throw new VisitorException(this, n, "Base type is no class type");
		CustomTypeNode basetype = (CustomTypeNode) n.getBase().getType();

		ClassFieldNode cdcn = ((ClassNode) basetype.getTypeDefinition()).findMember(n.getName());
		if (cdcn == null)
			throw new VisitorException(this, n, "Class " + basetype.getName() + " has no member " + n.getName());
		n.setComponent(cdcn);
		n.setType(cdcn.getType());
	}

	@Override
	public void visit(ClassFunctionAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);

		if (!(n.getBase().getType() instanceof CustomTypeNode))
			throw new VisitorException(this, n, "Base type is no class type");
		CustomTypeNode basetype = (CustomTypeNode) n.getBase().getType();

		ClassFunctionNode cfcn = ((ClassNode) basetype.getTypeDefinition()).findFunction(n.getName());
		if (cfcn == null)
			throw new VisitorException(this, n, "Class " + basetype.getName() + " has no member " + n.getName());

		// should maybe be done earlier in name resolution?
		n.getCall().setFunction(cfcn);
		n.setComponent(cfcn);
		n.setType(cfcn.getReturnVariable().getType());

		n.getCall().accept(this);
	}

	@Override
	public void visit(ArrayAccessNode n) throws VisitorException
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
			throw new VisitorException(this, n, "Not an array access node");
		}
	}

	@Override
	public void visit(VariableAccessNode n) throws VisitorException
	{
		final TypeNode tn = n.getVariable().getType();
		n.setType((tn instanceof RefTypeNode) ? ((RefTypeNode) tn).getType() : tn);
	}

	@Override
	public void visit(WhileNode n) throws VisitorException
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			throw new VisitorException(this, n, "While needs int type as condition");
	}

	@Override
	public void visit(DoWhileNode n) throws VisitorException
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			throw new VisitorException(this, n, "DoWhile needs int type as condition");
	}

	@Override
	public void visit(IfNode n) throws VisitorException
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			throw new VisitorException(this, n, "If needs int type as condition");
	}

	@Override
	public void visit(ForNode n) throws VisitorException
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			throw new VisitorException(this, n, "For needs int type as condition");
	}

	@Override
	public void visit(SwitchNode n) throws VisitorException
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
			throw new VisitorException(this, n, "Switch needs int or string type as condition");
		if (!areCaseTypesCorrect)
			throw new VisitorException(this, n, "Case type of case " + position + " differs from switch type");
	}
}
